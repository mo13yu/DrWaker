package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import yunjingl.cmu.edu.drwaker.database.LocationDatabaseConnector;
import yunjingl.cmu.edu.drwaker.entities.Location;
import yunjingl.cmu.edu.drwaker.exception.DatabaseException;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected static final String TAG = "ProxyLocation";

    private static LinkedHashMap<String,Location> locations = new LinkedHashMap<String,Location>();

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

    private static Context context;
    private static LocationDatabaseConnector locationDatabaseConnector;

    private android.location.Location alarmLocation = new android.location.Location("alarm");


    /* CreateLocation */
    public void createLocation(LatLng latlng, String tag) {
        // create new location
        Location newLocation = new Location(getAvailableID(), latlng, tag);
        // add new location to LinkedHashMap
        locations.put(newLocation.getTag(), newLocation);
        // add new location to database
        //TODO: add new location to database

        printAllLocations();
    }

    public int getAvailableID() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            ids.add((locations.get(itr.next())).getLocid());
        }
        if (ids.isEmpty()) {
            return 1;
        }
        else {
            return Collections.max(ids) + 1;
        }
    }


    /* DeleteLocation */
    //TODO: test deleteLocation
    public void deleteLocation(String tag) {
        int id = locations.get(tag).getLocid();
        // delete location from LinkedHashMap
        locations.remove(tag);
        // delete location from database
        //TODO: remove location from database

        printAllLocations();
    }


    /* GetLocation */
    public ArrayList<String> getAllLocations() {
        ArrayList<String> tags = new ArrayList<String>();
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            tags.add(locations.get(itr.next()).getTag());
        }
        return tags;
    }

    public Location getLocation(String tag) {
        if(locations.containsKey(tag)){
            return locations.get(tag);
        }else{
            //throw CusException("")
        }
        return locations.get(tag);
    }

    //TODO: test nearLocation
    public boolean nearLocation(String lat, String lng) {
        // get alarm location
        //android.location.Location alarmLocation = new android.location.Location("");
        alarmLocation.setLatitude(Double.parseDouble(lat));
        alarmLocation.setLongitude(Double.parseDouble(lng));
        Log.i(TAG, "Input Location: (" + String.valueOf(alarmLocation.getLatitude()) + ", " + String.valueOf(alarmLocation.getLongitude()) + ")");

        // Build a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Connect to Google Play services location API
        mGoogleApiClient.connect();

        // onConnected() will be called and set last known location to mLastLocation inside the function

        /*
        // Disconnect from Google Play services location API
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        */

        return false;
    }


    /* InitializeLocation */
    //TODO: test initializeLocations
    public void initializeLocations() {
        // get locations from database
        //TODO: get all stored locations from database, loop through them and add all to LinkedHashMap
    }


    /* UpdateLocation */
    //TODO: test updateLocation
    public void updateLocation(int locid, LatLng latlng, String tag) {
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            String currentTag = locations.get(itr.next()).getTag();
            if(locations.get(currentTag).getLocid()==locid) {
                //TODO: update the values in the database
                locations.get(currentTag).setLatlng(latlng);
                locations.get(currentTag).setTag(tag);
            }
        }
    }


    /**
     * GoogleApiClient
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient: onConnectionSuspended");
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient: oConnectionFailed: Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient: onConnected()");

        // Get last known location
        try {
            Log.i(TAG, "Trying to get last known location");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                String mLatitudeText = String.valueOf(mLastLocation.getLatitude());
                String mLongitudeText = String.valueOf(mLastLocation.getLongitude());
                Log.i(TAG, "Last Location: (" + mLatitudeText + ", " + mLongitudeText + ")");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "mLastLocation: (" + String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()) + ")");
        float distance = mLastLocation.distanceTo(alarmLocation);
        Log.i(TAG, "Distance: " + String.valueOf(distance) + "m");

        // Disconnect from Google Play services location API
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * DEBUGGING PURPOSE
     */
    public void printAllLocations() {
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            locations.get(itr.next()).print();
        }
    }


    /**
     * Database
     */
    public void setContext(Context con) {
        context=con;
        locationDatabaseConnector=new LocationDatabaseConnector(context);
    }

    public void addToDB(Location newLoc){
        try{
            locationDatabaseConnector.insertLocation(newLoc.getLocid(),newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getTag());}              //TODO:need add mathID,on/off,locationID
        catch(DatabaseException e){
            e.fix(e.getErrNo());
        }
    }

    public void updateToDB(Location newLoc){
        int id = newLoc.getLocid();
        try {
            locationDatabaseConnector.updateLocation(id, newLoc.getLatitude(),
                    newLoc.getLongitude(), newLoc.getTag());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void delateFromDB(int alarmid){
        try {
            locationDatabaseConnector.deleteLocation(alarmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<String,Location> allLoc(){
        LinkedHashMap<String,Location> data= new LinkedHashMap<>();
        try {
            locationDatabaseConnector.open();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        Cursor cursor=locationDatabaseConnector.getAllLocation();
        int idIndex= cursor.getColumnIndex("id");
        int latitudeIndex=cursor.getColumnIndex("Latitude");
        int longitudeIndex=cursor.getColumnIndex("Longitude");
        int tagIndex=cursor.getColumnIndex("Tag");
        cursor.moveToFirst();
        int id=Integer.valueOf(cursor.getString(idIndex));
        double latitude=Double.valueOf(cursor.getString(latitudeIndex));
        double longitude=Integer.valueOf(cursor.getString(longitudeIndex));
        String tag=cursor.getString(tagIndex);
        int counter=0;

        while(!cursor.isLast()){
            LatLng latLng=new LatLng(latitude,longitude);
            Location alarm=new Location(id,latLng,tag);
            //need math and location
            data.put(tag,alarm);
            counter++;
            cursor.moveToNext();
        }
        return data;
    }

    public Location readLocation(int id){
        try {
            locationDatabaseConnector.open();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        Cursor cursor=locationDatabaseConnector.getOneLocation(id);
        int idIndex= cursor.getColumnIndex("id");
        int latitudeIndex=cursor.getColumnIndex("Latitude");
        int longitudeIndex=cursor.getColumnIndex("Longitude");
        int tagIndex=cursor.getColumnIndex("Tag");
        cursor.moveToFirst();
        int locid=Integer.valueOf(cursor.getString(idIndex));
        double latitude=Double.valueOf(cursor.getString(latitudeIndex));
        double longitude=Integer.valueOf(cursor.getString(longitudeIndex));
        String tag=cursor.getString(tagIndex);
        LatLng latLng=new LatLng(latitude,longitude);
        Location location=new Location(id,latLng,tag);
        return location;
    }

}
