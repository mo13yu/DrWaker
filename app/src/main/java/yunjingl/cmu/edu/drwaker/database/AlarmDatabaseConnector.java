package yunjingl.cmu.edu.drwaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yunjingl.cmu.edu.drwaker.exception.DatabaseException;

/**
 * Created by yapeng on 4/30/2016.
 */
public class AlarmDatabaseConnector {
    private static final String TABLE_NAME="AlarmDatabase";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        //constructor
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }//end constructor

        //override method

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create the query first
            String createQuery="CREATE TABLE "+TABLE_NAME+
                    " (Id TEXT, Hour TEXT, Minute TEXT, Wakeupmethod TEXT, Tag TEXT, Tune TEXT,Status TEXT, LocID TEXT, MathID TEXT);";//
            db.execSQL(createQuery);
//            System.out.println(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    //end class DatabaseOpenHelper

    public AlarmDatabaseConnector(Context context)
    {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context,TABLE_NAME, null, 1);


    } // end DatabaseConnector constructor

    //open database connection
    public void open() throws DatabaseException
    {
        //open a database for writing
        database=databaseOpenHelper.getWritableDatabase();
        if (database==null){
            throw new DatabaseException(1);
        }
    }

    //close database connection
    public void close(){
        if(database!=null){
            database.close();
        }
    }

    //insert a new Alarm into database
    public void insertAlarm(int id, int hour,int minute,String method,String tag ,String tune ,
                            boolean status,int locID,int mathID) throws DatabaseException {

        ContentValues newAlarm=new ContentValues();
        newAlarm.put("Id",id);
        newAlarm.put("Hour",String.valueOf(hour));
        newAlarm.put("Minute", String.valueOf(minute));
        newAlarm.put("Wakeupmethod", method);
        newAlarm.put("Tag", tag);
        newAlarm.put("Tune", tune);
        newAlarm.put("Status",status);
        newAlarm.put("LocID", String.valueOf(locID));
        newAlarm.put("MathID", String.valueOf(mathID));


        open();//open the database
        database.insert(TABLE_NAME, null, newAlarm);
        close();

    }
    //end the code insert Alarm

    //update a Alarm which is already in the database
    public void updateAlarm(int id,int hour,int minute,String method,String tag ,String tune ,
                            boolean status, int locID,int mathID) throws DatabaseException {
        ContentValues editAlarm=new ContentValues();
        editAlarm.put("Hour",hour);
        editAlarm.put("Minute",minute);
        editAlarm.put("Wakeupmethod",method);
        editAlarm.put("Tag",tag);
        editAlarm.put("Tune",tune);
        editAlarm.put("Status",status);
        editAlarm.put("LocID",locID);
        editAlarm.put("MathID",mathID);

        open();//open the database
        database.update(TABLE_NAME, editAlarm,"Id=" + id, null);
        close();

    }
    //end the code update Alarm

    //return a cursor with all Alarm information
    public Cursor getAllAlarm(){
//        return database.query(TABLE_NAME,null,null,null,null,null,"PurchasedPrice");
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    //return a curser for a specific Alarm
    public Cursor getOneAlarm(int id){
        return database.query(TABLE_NAME,null,"Id="+id,null,null,null,null);
    }

    //delete a specific Alarm
    public void deleteAlarm(int id) throws DatabaseException {

        open();
        database.delete(TABLE_NAME,"Id="+id,null);
        close();

    }
    //end code for delete Alarm
}
