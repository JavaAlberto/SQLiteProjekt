package berufsschulefreising.de.sqliteprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Albrecht on 14.10.15.
 */
public class DatenbankManager extends SQLiteOpenHelper
{

    private static final int DATENBANK_VERSION = 1;
    private static final String DATENBANK_NAME = "formularDatenbank.db";
    private static final String DATABASE_TABLE = "personenDaten";
    public static final String
            KLASSEN_SELECT_RAW = "SELECT * FROM " + DatenbankManager.DATABASE_TABLE ;
    private SQLiteDatabase sqldb;
    private Context activity;
    private String[] columns = {"nachname","vorname","geburtsdatum"};


    public DatenbankManager (Context activity)
    {
        super(activity,DATENBANK_NAME,null,
                DATENBANK_VERSION);
        sqldb = getWritableDatabase();
        this.activity = activity;
    }
    @Override
    public void onCreate(SQLiteDatabase sqldb)
    {
       // Toast.makeText(activity, "Tabelle erzeugen", Toast.LENGTH_LONG).show();
        String sql="CREATE TABLE "+ DATABASE_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nachname VARCHAR(20) NOT NULL," + "vorname VARCHAR(20) NOT NULL,"
                + "geburtsdatum DATE);";
        sqldb.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqldb, int oldVersion, int newVersion)
    {
    }

    @Override
    public void close()
    {
     sqldb.close();
    }

    public String ausgabe () {
        // sqldb = getReadableDatabase();
        // Verwendung von query
        Cursor result = sqldb.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String mInhalt = "";
        if (result == null) {
            Toast.makeText(activity, "Tabelle leer", Toast.LENGTH_LONG).show();
        } else {
            while (result.moveToNext()) {
                mInhalt += result.getString(0) + " " + result.getString(1) + " " + result.getString(2) + "\n";
            }
        }
        sqldb.close();
        return mInhalt;
    }

    public String ausgabe1 () {
        // sqldb = getReadableDatabase();
        // Verwendung von rawQuery
        Cursor result = sqldb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        String mInhalt = "";
        if (result == null) {
         Toast.makeText(activity, "Tabelle leer", Toast.LENGTH_LONG).show();
        }
        else
        {
        while (result.moveToNext()) {
            mInhalt += result.getString(1) + " " + result.getString(2) + " " + result.getString(3) + "\n";
        }
        }
        sqldb.close();
        return mInhalt;
    }




    // Datensatz in Datenbank schreiben:

    public long insertRecord(String nachname1,String vorname1,SimpleDateFormat df1)
    {
        ContentValues cv =new ContentValues();
        cv.put("nachname", "Meier");
        cv.put("vorname", "Paul");
        // SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal =  new GregorianCalendar(1958,2-1,10);
        df1.setCalendar(cal);
        cv.put("geburtsdatum", df1.format(cal.getTime()));
        long rowId = sqldb.insert(DATABASE_TABLE, null, cv);
        return rowId;
    }
    }
