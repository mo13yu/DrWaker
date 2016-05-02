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
public class LocationDatabaseConnector {
    private static final String TABLE_NAME="LocationDatabase";
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
                    " Id TEXT, "+
                    " Latitude TEXT, Longitude TEXT, Tag TEXT);";//
            db.execSQL(createQuery);
//            System.out.println(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    //end class DatabaseOpenHelper

    public LocationDatabaseConnector(Context context)
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

    //insert a new Location into database
    public void insertLocation(int id, double latitude,double longitude,String tag) throws DatabaseException {

        ContentValues newLocation=new ContentValues();
        newLocation.put("Id",String.valueOf(id));
        newLocation.put("Latitude",String.valueOf(latitude));
        newLocation.put("Longitude", String.valueOf(longitude));
        newLocation.put("Tag", tag);



        open();//open the database
        database.insert(TABLE_NAME, null, newLocation);
        close();

    }
    //end the code insert Location

    //update a Location which is already in the database
    public void updateLocation(int id,double latitude,double longitude,String tag) throws DatabaseException {
        ContentValues editLocation=new ContentValues();
        editLocation.put("Latitude",String.valueOf(latitude));
        editLocation.put("Longitude", String.valueOf(longitude));
        editLocation.put("Tag", tag);


        open();//open the database
        database.update(TABLE_NAME, editLocation,"Id=" + id, null);
        close();

    }
    //end the code update Location

    //return a cursor with all Location information
    public Cursor getAllLocation(){
//        return database.query(TABLE_NAME,null,null,null,null,null,"PurchasedPrice");
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    //return a curser for a specific Location
    public Cursor getOneLocation(int id){
        return database.query(TABLE_NAME,null,"Id="+id,null,null,null,null);
    }

    //delete a specific Location
    public void deleteLocation(int id) throws DatabaseException {

        open();
        database.delete(TABLE_NAME,"Id="+id,null);
        close();

    }
    //end code for delete Location
}
