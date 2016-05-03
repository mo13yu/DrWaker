package yunjingl.cmu.edu.drwaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;

import yunjingl.cmu.edu.drwaker.entities.Location;
import yunjingl.cmu.edu.drwaker.exception.CusException;
import yunjingl.cmu.edu.drwaker.exception.DatabaseException;

/**
 * This is to connect to location database and performs direct CRUD to data.
 */
public class LocationDatabaseConnector {
    private static final String TABLE_NAME = "LocationDatabaseTesting9";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;
    /**
     * connect to database and create new table
     */
    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createQuery = "CREATE TABLE " + TABLE_NAME +
                    "(Id TEXT, Latitude TEXT, Longitude TEXT, Tag TEXT);";
            db.execSQL(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public LocationDatabaseConnector(Context context) {
        databaseOpenHelper =
                new DatabaseOpenHelper(context, TABLE_NAME, null, 1);


    }

    public void open() throws DatabaseException {
        database = databaseOpenHelper.getWritableDatabase();
        if (database == null) {
            throw new DatabaseException(1);
        }
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    /**
     * insert a new Location into database
     * @param id
     * @param latitude
     * @param longitude
     * @param tag
     * @throws DatabaseException
     */
    public void insertLocation(int id, double latitude, double longitude, String tag) throws DatabaseException {
        ContentValues newLocation = new ContentValues();
        newLocation.put("Id", String.valueOf(id));
        newLocation.put("Latitude", String.valueOf(latitude));
        newLocation.put("Longitude", String.valueOf(longitude));
        newLocation.put("Tag", tag);

        open();//open the database
        database.insert(TABLE_NAME, null, newLocation);
        close();

    }

    /**
     * update a Location which is already in the database
     * @param id
     * @param latitude
     * @param longitude
     * @param tag
     * @throws DatabaseException
     */
    public void updateLocation(int id, double latitude, double longitude, String tag) throws DatabaseException {
        ContentValues editLocation = new ContentValues();
        editLocation.put("Latitude", String.valueOf(latitude));
        editLocation.put("Longitude", String.valueOf(longitude));
        editLocation.put("Tag", tag);

        open();//open the database
        database.update(TABLE_NAME, editLocation, "Id=" + String.valueOf(id), null);
        close();
    }

    /**
     * return all locations
     * @return
     * @throws DatabaseException
     */
    public LinkedHashMap<String, Location> getAllLocation() throws DatabaseException {
        LinkedHashMap<String, Location> locations = new LinkedHashMap<String, Location>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        open(); // open the database
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex("Id");
            int latitudeIndex = cursor.getColumnIndex("Latitude");
            int longitudeIndex = cursor.getColumnIndex("Longitude");
            int tagIndex = cursor.getColumnIndex("Tag");
            int counter = 1;
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.valueOf(cursor.getString(idIndex));
                    double latitude = Double.valueOf(cursor.getString(latitudeIndex));
                    double longitude = Double.valueOf(cursor.getString(longitudeIndex));
                    String tag = cursor.getString(tagIndex);
                    LatLng latLng = new LatLng(latitude, longitude);
                    Location location = new Location(id, latLng, tag);
                    locations.put(tag, location);
                    counter++;

                } while (cursor.moveToNext());
            }
        }
        close();

        return locations;
    }

    /**
     * get a certain location
     * @param id
     * @return
     * @throws DatabaseException
     */
    public Location getOneLocation(int id) throws DatabaseException {
        open();
        Location location = new Location();
        Cursor cursor = database.query(TABLE_NAME, null, "Id=" + String.valueOf(id), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex("Id");
            int latitudeIndex = cursor.getColumnIndex("Latitude");
            int longitudeIndex = cursor.getColumnIndex("Longitude");
            int tagIndex = cursor.getColumnIndex("Tag");
            cursor.moveToFirst();
            int locid = Integer.valueOf(cursor.getString(idIndex));
            double latitude = Double.valueOf(cursor.getString(latitudeIndex));
            double longitude = Double.valueOf(cursor.getString(longitudeIndex));
            String tag = cursor.getString(tagIndex);
            LatLng latLng = new LatLng(latitude, longitude);
            location = new Location(id, latLng, tag);
        } else {
            new CusException("No such location exist");
        }
        close();
        return location;
    }

    /**
     * delete a certain location
     * @param id
     * @throws DatabaseException
     */
    public void deleteLocation(int id) throws DatabaseException {

        open();
        database.delete(TABLE_NAME, "Id=" + String.valueOf(id), null);
        close();

    }
}
