package berufsschulefreising.de.sqliteprojekt;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import static berufsschulefreising.de.sqliteprojekt.R.layout.personendaten;


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
        // -----------------------------------------------

        // ************* funktioniert nicht ***************************
         // ctv = (CheckedTextView) findViewById(R.id.listItemMultiChoiceWrapper);


        final CheckedTextView  ctv = (CheckedTextView) findViewById(R.id.text1);
        /*  checkedTextView1 =(CheckedTextView)findViewById(R.id.checked_textview1);
        // Toast.makeText(this, "CheckedTextView" + checkedTextView, Toast.LENGTH_LONG).show(); -*/
         // Log.e(ctv.toString(),"error1");
      /* ctv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();
            }
        });
             ctv.setOnClickListener(this);*/

        // ----------------------------

        // ------- Contextuel ActionBar initialisieren -------------
        this.initializeContextualActionBar();
        //--------------------------------------------------------




    }

    @Override
    protected void onPause() {
        super.onPause();
        dbmgr.close();
        Toast.makeText(this, "Datenbank geschlossen", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqldb = dbmgr.getReadableDatabase();
        //  Cursor tabellenCursor = sqldb.rawQuery(DatenbankManager.KLASSEN_SELECT_RAW, null);
        Toast.makeText(this, "Datenbank geöffnet", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v)
    {
   // ---------------------------------
       /* switch(v.getId()) {
            case R.id.listItemMultiChoiceWrapper: {
                Toast.makeText(this, "Checkbox angeklickt", Toast.LENGTH_LONG).show();
                if (ctv.isChecked())
                    ctv.setChecked(false);
                else {
                    ctv.setChecked(true);
                }
                break;
            }
        }*/
        // ---------------------------------


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


    public void showAllEntries() {
        // Vorbereitung Ausgabe Datenbankinhalt mit SimpleCursorAdapter
        Toast.makeText(this, "Cursor für SimpleCursorAdapter erzeugen", Toast.LENGTH_LONG).show();
        // Ist der Parameter 3 null, werden alle Datensätze der Tabelle zurückgeliefert:
        Cursor cursor = sqldb.query(DatenbankManager.DATABASE_TABLE, dbmgr.columns, null, null, null, null, null);
        Toast.makeText(this, "Test: DB-Zeilenzahl abfragen: " + cursor.getCount(), Toast.LENGTH_LONG).show();
        // SimpleCursorAdapter:
        // Parameter 1: App-Contex
        //  Parameter 2: resource-ID des Tabellenlayout
        //  Parameter 2: resource-ID des Tabellenlayout
        // Parameter 4:  String-Array der Spalten
        // Parameter 5: Array  Layouts der Spalten
         int[] to = new int[]{R.id.textView_id, R.id.textView_nachname, R.id.textView_vorname, R.id.textView_geburtstag};
          SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, personendaten, cursor,
                dbmgr.columns, to, 0);
        adapter.getItemId(R.id.text1);
         // int[] to = new int[]{R.id.textView_id, R.id.textView_nachname, R.id.textView_vorname, R.id.textView_geburtstag};
       // Vogella http://www.vogella.com/tutorials/AndroidListView/article.html

        // Jede Reihe besitzt aufgrund dieses Layout eine textview und eine Checkbox / Komatineni Android 4 170
       //  SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, cursor,
          //       dbmgr.columns, to, 0);

        MyListView personendaten = (MyListView) findViewById(R.id.listview_personendaten);
        personendaten.setAdapter(adapter);

        // personendaten.setAdapter(adapter);
        // Erweiterung:
        personendaten.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        }
        // -----------------------------------
          // Komatineni Android 4 169
       // http://abhiandroid.com/ui/checkedtextview



    // =================================================================================================
    // Contextual ActionBar
    //  --------------------------------------------------------------------------------------------------
    private void initializeContextualActionBar() {
        Toast.makeText(this, "Initializ CAB", Toast.LENGTH_LONG).show();

        final MyListView personenListView = (MyListView) findViewById(R.id.listview_personendaten);
        personenListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    //=============================
        //  setContentView(R.layout.personendaten);
        /*TextView v1=  (TextView) personenListView.findViewById(R.id.editText_geburtstag);
        Log.e(v1.toString(),"error1");*/
        // ==================================
        personenListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Toast.makeText(getBaseContext(), "onItemCheckedStateChanged", Toast.LENGTH_LONG).show();

               // ------------------------------------------------------------------------------------------------------
                //Diese Zeilen zeigen dass die Elemente tatsächlich gecheckt werden, dies ist jedoch nicht sichtbar
                // http://stackoverflow.com/questions/5612600/listview-with-choice-mode-multiple-using-checkedtext-in-a-custom-view
                Toast.makeText(getBaseContext(), "Zahl der gecheckten Elemente " + personenListView.getCheckedItemCount(), Toast.LENGTH_LONG).show();

                final long[] checkedIds = personenListView.getCheckedItemIds();
                for ( int i = 0 ; i < personenListView.getCheckedItemCount() ; i++ ) {
                    Log.d("check", "id checked: " + checkedIds[i]);  }

                // ------------------------------------------------------------------------------------------------------
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menue_cab, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    // hier gibt es bisher nur ein "item"
                    case R.id.cab_delete:
                       // Toast.makeText(getBaseContext(), "Element delete ausgewählt ", Toast.LENGTH_LONG).show();
                        SparseBooleanArray touchedPersonPositions = personenListView.getCheckedItemPositions();
                        // iterieren über alle Elemente
                        Toast.makeText(getBaseContext(), "touchedPositions", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < touchedPersonPositions.size(); i++) {
                            boolean isChecked = touchedPersonPositions.valueAt(i);

                            if (isChecked) {
                                int postitionInListView = touchedPersonPositions.keyAt(i);
                               /* Shopping shopping = (Shopping) personenListView.getItemAtPosition(postitionInListView);
                                Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + shopping.toString());
                                dbmgr.deleteShopping(shopping);*/
                            }
                        }
                        showAllEntries();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        // Contextual ActionBAr
        // ---------------------------------------------------------------------------------------------------------------
// http://kb4dev.com/tutorial/android-listview/android-listview-with-checkbox
        // http://stackoverflow.com/questions/25414334/disable-checkbox-in-a-simple-list-view-multi-choice
        // http://www.technotalkative.com/contextual-action-bar-cab-android/
        // http://stackoverflow.com/questions/35021271/is-there-any-built-in-item-checked-listener-on-android-listview
      // https://developer.android.com/guide/topics/ui/menus.html#CAB
        // Achtung:
        // http://stackoverflow.com/questions/8369640/listview-setitemchecked-only-works-with-standard-arrayadapter-does-not-work-w
    }
}
