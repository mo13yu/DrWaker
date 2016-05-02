package yunjingl.cmu.edu.drwaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedHashMap;

import yunjingl.cmu.edu.drwaker.adapter.SetLocation;
import yunjingl.cmu.edu.drwaker.entities.Alarm;
import yunjingl.cmu.edu.drwaker.exception.CusException;
import yunjingl.cmu.edu.drwaker.exception.DatabaseException;

/**
 * Created by yapeng on 4/30/2016.
 */
public class AlarmDatabaseConnector {
    private static final String TABLE_NAME = "AlarmDatabaseTesting1";
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
            String createQuery = "CREATE TABLE " + TABLE_NAME +
                    " (Id TEXT, Hour TEXT, Minute TEXT, Wakeupmethod TEXT, Tag TEXT, Tune TEXT,Status TEXT, LocTag TEXT, MathID TEXT);";//
            db.execSQL(createQuery);
//            System.out.println(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    //end class DatabaseOpenHelper

    public AlarmDatabaseConnector(Context context) {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, TABLE_NAME, null, 1);


    } // end DatabaseConnector constructor

    //open database connection
    public void open() throws DatabaseException {
        //open a database for writing
        database = databaseOpenHelper.getWritableDatabase();
        if (database == null) {
            throw new DatabaseException(1);
        }
    }

    //close database connection
    public void close() {
        if (database != null) {
            database.close();
        }
    }

    //insert a new Alarm into database
    public void insertAlarm(int id, int hour, int minute, String method, String tag, String tune,
                            boolean status, String locTag, int mathID) throws DatabaseException {
        //Log.e("connector check", "id"+id+"inputhour" + String.valueOf(hour) + "inputminute" +String.valueOf(minute)+"tag"+tag);
        ContentValues newAlarm = new ContentValues();
        newAlarm.put("Id", String.valueOf(id));
        newAlarm.put("Hour", String.valueOf(hour));
        newAlarm.put("Minute", String.valueOf(minute));
        newAlarm.put("Wakeupmethod", method);
        newAlarm.put("Tag", tag);
        newAlarm.put("Tune", tune);
        newAlarm.put("Status", String.valueOf(status));
        newAlarm.put("LocTag", locTag);
        newAlarm.put("MathID", String.valueOf(mathID));


        open();//open the database
        database.insert(TABLE_NAME, null, newAlarm);
        close();

    }
    //end the code insert Alarm

    //update a Alarm which is already in the database
    public void updateAlarm(int id, int hour, int minute, String method, String tag, String tune,
                            boolean status, String locTag, int mathID) throws DatabaseException {
        ContentValues editAlarm = new ContentValues();
        editAlarm.put("Hour", String.valueOf(hour));
        editAlarm.put("Minute", String.valueOf(minute));
        editAlarm.put("Wakeupmethod", method);
        editAlarm.put("Tag", tag);
        editAlarm.put("Tune", tune);
        editAlarm.put("Status", String.valueOf(status));
        editAlarm.put("LocTag", locTag);
        editAlarm.put("MathID",String.valueOf(mathID));

        open();//open the database
        database.update(TABLE_NAME, editAlarm, "Id=" + String.valueOf(id), null);
        close();

    }
    //end the code update Alarm


    //return a LinkedHashMap with all Alarm information
    public LinkedHashMap<Integer,Alarm> getAllAlarm() throws DatabaseException {
        LinkedHashMap<Integer,Alarm> alarms= new LinkedHashMap<Integer,Alarm>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        open(); // open the database
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex("Id");
            int hourIndex = cursor.getColumnIndex("Hour");
            int minuteIndex = cursor.getColumnIndex("Minute");
            int wakeupmethodIndex = cursor.getColumnIndex("Wakeupmethod");
            int tagIndex = cursor.getColumnIndex("Tag");
            int tuneIndex = cursor.getColumnIndex("Tune");
            int statusIndex = cursor.getColumnIndex("Status");
            int locIndex = cursor.getColumnIndex("LocTag");
            int counter = 1;
            if (cursor.moveToFirst()) {
                do {
                    Alarm newalarm = new Alarm();
                    int id = Integer.valueOf(cursor.getString(idIndex));
                    int hour = Integer.valueOf(cursor.getString(hourIndex));
                    int minute = Integer.valueOf(cursor.getString(minuteIndex));
                    String wakeupmethod = cursor.getString(wakeupmethodIndex);
                    String tag = cursor.getString(tagIndex);
                    String tune = cursor.getString(tuneIndex);
                    Boolean status = Boolean.valueOf(cursor.getString(statusIndex));
                    String locTag = cursor.getString(locIndex);
                    newalarm.setAlarmid(id);
                    newalarm.setHour(hour);
                    newalarm.setMinute(minute);
                    newalarm.setWake_up_method(wakeupmethod);
                    newalarm.setTag(tag);
                    newalarm.setTone(tune);
                    newalarm.setLoc_switch(status);
                    newalarm.setLocation(new SetLocation().getLocation(locTag));
                    alarms.put(counter, newalarm);
                    counter++;
                } while (cursor.moveToNext());
            }
        }
        close();

        return alarms;
//        return database.query(TABLE_NAME,null,null,null,null,null,"PurchasedPrice");
        //      return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    //return a curser for a specific Alarm
    public Alarm getOneAlarm(int id) throws DatabaseException {

        open();
        Alarm alarm = new Alarm();
        Cursor cursor = database.query(TABLE_NAME, null, "Id=" + id, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex("Id");
            int hourIndex = cursor.getColumnIndex("Hour");
            int minuteIndex = cursor.getColumnIndex("Minute");
            int wakeupmethodIndex = cursor.getColumnIndex("Wakeupmethod");
            int tagIndex = cursor.getColumnIndex("Tag");
            int tuneIndex = cursor.getColumnIndex("Tune");
            int statusIndex = cursor.getColumnIndex("Status");
            int locIndex = cursor.getColumnIndex("LocTag");
            cursor.moveToFirst();
            int alarmid = Integer.valueOf(cursor.getString(idIndex));
            int hour = Integer.valueOf(cursor.getString(hourIndex));
            int minute = Integer.valueOf(cursor.getString(minuteIndex));
            String wakeupmethod = cursor.getString(wakeupmethodIndex);
            String tag = cursor.getString(tagIndex);
            String tune = cursor.getString(tuneIndex);
            Boolean status = Boolean.valueOf(cursor.getString(statusIndex));
            String locTag = cursor.getString(locIndex);
            alarm = new Alarm(id, hour, minute);
            alarm.setWake_up_method(wakeupmethod);
            alarm.setTag(tag);
            alarm.setTone(tune);
            alarm.setLoc_switch(status);
            alarm.setLocation(new SetLocation().getLocation(locTag));
        } else {
            new CusException("No such alarm exist");
        }
        close();
        return alarm;


    }

    //delete a specific Alarm
    public void deleteAlarm(int id) throws DatabaseException {

        open();
        database.delete(TABLE_NAME, "Id=" + String.valueOf(id), null);
        close();

    }
    //end code for delete Alarm
}
