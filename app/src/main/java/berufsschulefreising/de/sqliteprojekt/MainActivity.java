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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    DatenbankManager dbmgr;
    SQLiteDatabase sqldb;
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


}
