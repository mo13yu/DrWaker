package yunjingl.cmu.edu.drwaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yunjing on 4/13/16.
 */
public class Create {
    private static final String DATABASE_NAME = "DrWaker";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper
    public SQLiteDatabase getDatabase(){
        return database;
    }
    public Create(Context context)
    {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }
    public void open() throws SQLException
    {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    }
    public void close()
    {
        if (database != null)
            database.close(); // close the database connection
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // query to create a new table named contacts
            String createQuery = "CREATE TABLE Calender" +
                    "(Cal_id integer primary key autoincrement," +
                    "Year integer, Month integer, Day integer," +
                    "Hour integer,Minute integer);";

            db.execSQL(createQuery); // execute the query
            String createQuery2 = "CREATE TABLE Alarm" +
                    "(Al_id integer primary key autoincrement," +
                    "Tone TEXT," +
                    "Wake_up TEXT," +
                    "Tag TEXT," +
                    "Repeat TEXT," +
                    "Alarm_switch TEXT," +
                    "Loc_switch TEXT," +
                    "FOREIGN KEY (Cal_id) REFERENCES Calender(CAT_ID)," +
                    "FOREIGN KEY (Loc_id) REFERENCES Location(Loc_ID));";
            db.execSQL(createQuery2); // execute the query
            String createQuery3 = "CREATE TABLE Location" +
                    "(Loc_id integer primary key autoincrement," +
                    "Longitude TEXT," +
                    "Latitude TEXT);";
            db.execSQL(createQuery3); // execute the query
            String createQuery4 = "CREATE TABLE Math" +
                    "(Math_id integer primary key autoincrement," +
                    "Question TEXT," +
                    "Answer TEXT);";
            db.execSQL(createQuery3); // execute the query
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        }
    }
}
