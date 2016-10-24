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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity implements View.OnClickListener {

    DatenbankManager dbmgr;
    SQLiteDatabase sqldb;
    Button user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbmgr = new DatenbankManager(this);
        // Datensatz einfügen:
        Toast.makeText(this,"Datensatz einfügen", Toast.LENGTH_LONG).show();
        long rowID =  dbmgr.insertRecord();
        Toast.makeText(this,"row ID" + rowID, Toast.LENGTH_LONG).show();
        // Datenbank  aus lesen:
        Toast.makeText(this,"Aufruf Ausgabemethode", Toast.LENGTH_LONG).show();
        String tabelleninhalt = dbmgr.ausgabe();
        Toast.makeText(this, tabelleninhalt, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "GitTest", Toast.LENGTH_LONG).show();
        user = (Button)findViewById(R.id.button_add_user);
        user.setOnClickListener(this);

        //=========================================

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
        EditText nachname = (EditText) findViewById(R.id.editText_nachname);
        String nachnameString = nachname.getText().toString();
        EditText vorname = (EditText) findViewById(R.id.editText_vorname);
        String vornameString = nachname.getText().toString();
        if(TextUtils.isEmpty(nachnameString) || TextUtils.isEmpty(vornameString)) {
            nachname.setError(getString(R.string.editText_errorMessage));
        }
        Toast.makeText(this, "Vorname und Nachname: " + vornameString + " " + nachnameString ,Toast.LENGTH_LONG).show();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
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
        }


    }
}
