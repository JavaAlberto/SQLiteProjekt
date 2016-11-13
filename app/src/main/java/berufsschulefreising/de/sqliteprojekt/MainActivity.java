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


    Button user;
    Cursor cursor;
    DatenbankManager dbmgr;
    SQLiteDatabase sqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbmgr = new DatenbankManager(this);
        user = (Button) findViewById(R.id.button_add_user);
        user.setOnClickListener(this);
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
       //  Cursor tabellenCursor = sqldb.rawQuery(DatenbankManager.KLASSEN_SELECT_RAW, null);
        Toast.makeText(this, "Datenbank geöffnet",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        String datum = "";
        // Datensatz einlesen:
        EditText nachname = (EditText) findViewById(R.id.editText_nachname);
        String nachnameString = nachname.getText().toString();
        EditText vorname = (EditText) findViewById(R.id.editText_vorname);
        String vornameString = vorname.getText().toString();
        // Fehlermeldung GUI
        if (TextUtils.isEmpty(nachnameString)) {
            nachname.setError(getString(R.string.editText_errorMessage));
        }
        if (TextUtils.isEmpty(vornameString)) {
            vorname.setError(getString(R.string.editText_errorMessage));
        }
        Toast.makeText(this, "Vorname und Nachname: " + vornameString + " " + nachnameString, Toast.LENGTH_LONG).show();
        EditText geburtstag = (EditText) findViewById(R.id.editText_geburtstag);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = geburtstag.getText().toString(); // EditText to check
            java.util.Date parsedDate = dateFormat.parse(date);
            // Eingabefelder leeren:
            nachname.setText("");
            vorname.setText("");
            geburtstag.setText("");
            // Eingabedatensatz beseitigen:
            long rowID = dbmgr.insertRecord(nachnameString, vornameString, date);

            this.showAllEntries();

        } catch (java.text.ParseException e) {
            Toast.makeText(this, "Datum: falsche Eingabe" + "(parse error)", Toast.LENGTH_LONG).show();
        }
    }
        public void showAllEntries()
    {
        // Vorbereitung Ausgabe Datenbankinhalt mit SimpleCursorAdapter
        Toast.makeText(this, "Cursor für SimpleCursorAdapter erzeugen",Toast.LENGTH_LONG).show();
        // Ist der Parameter 3 null, werden alle Datensätze der Tabelle zurückgeliefert:
        Cursor cursor = sqldb.query(DatenbankManager.DATABASE_TABLE, dbmgr.columns, null, null, null, null, null);
        Toast.makeText(this, "Test: DB-Zeilenzahl abfragen: " + cursor.getCount(),Toast.LENGTH_LONG).show();
        // SimpleCursorAdapter:
        // Parameter 1: App-Contex
        //  Parameter 2: resource-ID des Tabellenlayout
        //  Parameter 2: resource-ID des Tabellenlayout
        // Parameter 4:  String-Array der Spalten
        // Parameter 5: Array  Layouts der Spalten
        int[] to = new int[]{R.id.textView_id, R.id.textView_nachname, R.id.textView_vorname,R.id.textView_geburtstag};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.personendaten,cursor,
                dbmgr.columns,to,0);
        ListView personendaten = (ListView) findViewById(R.id.listview_personendaten);
        personendaten.setAdapter(adapter);
    }




}
