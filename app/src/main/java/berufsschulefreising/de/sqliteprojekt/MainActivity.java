package berufsschulefreising.de.sqliteprojekt;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {


    Cursor cursor;
    DatenbankManager dbmgr;
    SQLiteDatabase sqldb;
    Button user;
    String [] userListeArray = {
            "Müller", "Maier",  "Schmidt", "Huber", "Pfleiderer", "Eiermann", "Fischer"} ;
    List <String> userList = new ArrayList<String> (Arrays.asList(userListeArray));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbmgr = new DatenbankManager(this);
       /*
        // Datensatz einfügen:
        Toast.makeText(this,"Datensatz einfügen", Toast.LENGTH_LONG).show();
        long rowID =  dbmgr.insertRecord();
        Toast.makeText(this,"row ID" + rowID, Toast.LENGTH_LONG).show();
        // Datenbank  aus lesen:
        Toast.makeText(this,"Aufruf Ausgabemethode", Toast.LENGTH_LONG).show();
        String tabelleninhalt = dbmgr.ausgabe();
        Toast.makeText(this, tabelleninhalt, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "GitTest", Toast.LENGTH_LONG).show();
        */
        user = (Button) findViewById(R.id.button_add_user);
        user.setOnClickListener(this);

        //==========Datenbankinhalt auslesen ============
        // Auslesen und Darstellung der Inhalte der Datenbank mit Hilfe des SimpleCursorAdapter
        // https://developer.android.com/reference/android/widget/SimpleCursorAdapter.html#SimpleCursorAdapter(android.content.Context,%20int,%20android.database.Cursor,%20java.lang.String[],%20int[],%20int)
        String anzeigeSpalten[] = {"_id", "Nachname", "Vorname", "Geburtsdatum"};

        // R.layout.list_item   >  ID der XML-Layout Datei
        // int: resource identifier of a layout file that defines the views for this list item.
        // The layout file should include at least those named views defined in "to"
        //  R.id.list_item_textview   > ID der TextView
        initializeContentLoader();
    }
        private void initializeContentLoader()
        {
            // http://www.appstoremarketresearch.com/articles/android-tutorial-loader-simple-cursor-adapter/
            // array of database column names:
            String [] columns = new String[]  {"_id", "nachname", "vorname", "geburtsdatum"};
        // array of views to display database values
            int[] viewIds = new int[] {R.id.list_item_textview, R.id.editText_nachname, R.id.editText_vorname, R.id.editText_geburtstag};
            // CursorAdapter to load data from the Cursor into the ListView
           // SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,null,
            //        columns,viewIds,0);
            // tie the CursorAdapter to the ListView
            // https://www.bignerdranch.com/blog/understanding-androids-layoutinflater-inflate/
           // LayoutInflater (which coverts an XML layout file into corresponding ViewGroups and Widgets
            //http://www.programmierenlernenhq.de/tutorial-android-listview-verwenden/
           /* LayoutInflater inflater = this.getLayoutInflater();
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            ListView userListView = (ListView) rootView.findViewById(R.id.listview_personendaten);
            userListView.setAdapter(adapter);  */

               // ============================================================
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(
                this,                             // Die aktuelle Umgebung (diese Activity)
                R.layout.list_item,                         // ID der XML-Layout Datei (mit TextView)
                R.id.list_item_textview,                    // ID der TextView in der XML-Datei
                userList);                              // Beispieldaten in einer ArrayList
            Toast.makeText(this,"Adapter: "+ adapter.getCount(), Toast.LENGTH_LONG).show();
            //=================== =============================================
        // Erweiterung des Layout
            // obtains th LayoutInflater from the given Context
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        // Parameter 1: Ressourcen-ID des Layouts das aufgebaut werden soll
            // Parameter 2: ViewGroup: Eltern das Layouts
            // Paramter 3: Einn Wert der angiebt, ob das aufgebaute Layout zur ViewGroup beigefügt werden soll
            // Hier bedeutet false dass das System bereits das aufgebaute Layout in den container einsetzt
            // true würde eine redundante Viewgroup im endgültige Layout erstellen
            // Google-Book: Android Apps erstellen
            View rootView = inflater.inflate(R.layout.activity_main,null);

           // Bezug auf die ListView in der activity_main.xml
            // Suchen der ListView aus der root-View:
            ListView userListView = (ListView) rootView.findViewById(R.id.listview_personendaten);
             userListView.setAdapter(adapter);
        //http://khajanpndey.blogspot.de/2012/12/android-layoutinflater-tutorial.html
        // =============================================================================
        }
    @Override
    protected void onPause()
    {
        super.onPause();
        dbmgr.close();
        Toast.makeText(this, "Datenbank geschlossen", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sqldb = dbmgr.getReadableDatabase();
        Cursor tabellenCursor = sqldb.rawQuery(DatenbankManager.KLASSEN_SELECT_RAW, null);
        Toast.makeText(this, "Datenbank geöffnet",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v)
    {
       // Datensatz einlesen:
        EditText nachname = (EditText) findViewById(R.id.editText_nachname);
        String nachnameString = nachname.getText().toString();
        EditText vorname = (EditText) findViewById(R.id.editText_vorname);
        String vornameString = nachname.getText().toString();
        // Fehlermeldung GUI
        if(TextUtils.isEmpty(nachnameString))  {
            nachname.setError(getString(R.string.editText_errorMessage));
        }
        if(TextUtils.isEmpty(vornameString)) {
            vorname.setError(getString(R.string.editText_errorMessage));
        }
        Toast.makeText(this, "Vorname und Nachname: " + vornameString + " " + nachnameString ,Toast.LENGTH_LONG).show();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        EditText geburtstag = (EditText) findViewById(R.id.editText_geburtstag);
        try {
            date = df.parse(geburtstag.getText().toString());
            String myStringDate = date.getDay() + "-" + date.getMonth() + "-" + date.getYear();
            Toast.makeText(this, "Eingelesenes Datum: " + myStringDate ,Toast.LENGTH_LONG).show();
        }
        catch (ParseException e)
        { e.printStackTrace();
            Toast.makeText(this, "Fehler beim Parsen des Datums",Toast.LENGTH_LONG).show();
            geburtstag.setError(getString(R.string.editText_errorMessage));
        }
       // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        long rowID =  dbmgr.insertRecord(nachnameString,vornameString, df);
      // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        // Versuch SimpleCursorAdapter
        // Ist der Suchstring 3 null, werden alle Datensätze der Tabelle zurückgeliefert
        Toast.makeText(this, "Cursor bauen",Toast.LENGTH_LONG).show();
        Cursor cursor = sqldb.query(DatenbankManager.DATABASE_TABLE,
                dbmgr.columns, null, null, null, null, null);
        Toast.makeText(this, "DB-Zeilenzahl " + cursor.getCount(),Toast.LENGTH_LONG).show();
        // Parameter 4:  String-Array der Spalten
        // Parameter 5: Array  Layouts der Spalten
        int[] to = new int[]{R.id.textView1, R.id.textView2, R.id.textView3,R.id.textView4};
        //
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.personendaten,cursor,
                        dbmgr.columns,to,0);
        ListView personendaten = (ListView) findViewById(R.id.listview_personendaten);
        personendaten.setAdapter(adapter);
          // http://www.programcreek.com/java-api-examples/android.widget.SimpleCursorAdapter
        // https://www.youtube.com/watch?v=DCtSw4pUj5s
              }

   /* public List  getAllShoppingMemos() {
        List  shoppingList = new ArrayList<>();
        // Ist der Suchstring 3 null, werden alle Datensätze der Tabelle zurückgeliefert
        Cursor cursor = sqldb.query(DatenbankManager.DATABASE_TABLE,
                dbmgr.columns, null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            // Objekt aus Datensatz bilden über Methodenaufruf
            shopping = cursorToShopping(cursor);
            // Objekt der Liste hinzufügen
            shoppingList.add(shopping);
            Log.d(LOG_TAG, "ID: " + shopping.getId() + ", Inhalt: " + shopping.toString());
            cursor.moveToNext();
        }
        // Wichtig : Schließen des Cursors !!
        cursor.close();
        // Liste mit allen Shopping-Objekten wird zurückgegeben
        return shoppingList;
    }*/
}
