package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;
import android.util.Log;

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
 * This is a proxy contains all the methods of populating and manipulating locations.
 */
public abstract class ProxyLocation {

    private static LinkedHashMap<String, Location> locations = new LinkedHashMap<String, Location>();

    private static Context context;
    private static LocationDatabaseConnector locationDatabaseConnector;


    /**
     * CreateLocation Interface
     */
    /* createLocation */
    public void createLocation(LatLng latlng, String tag) {
        Location newLocation = new Location(getAvailableID(), latlng, tag);
        locations.put(newLocation.getTag(), newLocation);
        addToDB(newLocation);
    }

    /* getAvailableID - return the next available ID for the new location */
    public int getAvailableID() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            ids.add((locations.get(itr.next())).getLocid());
        }
        if (ids.isEmpty()) {
            return 1;
        } else {
            return Collections.max(ids) + 1;
        }
    }


    /**
     * DeleteLocation Interface
     */
    /* DeleteLocation */
    public void deleteLocation(String tag) {
        int id = locations.get(tag).getLocid();
        locations.remove(tag);
        delateFromDB(id);
    }


    /**
     * GetLocation Interface
     */
    /* getAllLocations - return ArrayList<String> of all location tags */
    public ArrayList<String> getAllLocations() {
        ArrayList<String> tags = new ArrayList<String>();
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            tags.add(locations.get(itr.next()).getTag());
        }
        return tags;
    }

    /* getLocation - return Location based on given location tag */
    public Location getLocation(String tag) {
        if (locations.containsKey(tag)) {
            return locations.get(tag);
        } else {

        }
        return locations.get(tag);
    }


    /**
     * InitializeLocation Interface
     */
    /* intializeLocations - get all locations in database and put them in the static LinkedHashMap */
    public void initializeLocations() {
        try {
            locations = locationDatabaseConnector.getAllLocation();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


    /* UpdateLocation */
    /* updateLocation */
    public void updateLocation(int locid, LatLng latlng, String tag) {
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            String currentTag = locations.get(itr.next()).getTag();
            Location currentLoc = locations.get(currentTag);
            if (currentLoc.getLocid() == locid) {
                currentLoc.setLatlng(latlng);
                currentLoc.setTag(tag);
                locations.put(currentTag, currentLoc);
                updateToDB(currentLoc);

            }
        }
    }


    /**
     * DEBUGGING PURPOSE
     */
    /* printAllLocations - go through all locations in LinkedHashMap and print out information */
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
    /* setContext */
    public void setContext(Context con) {
        context = con;
        locationDatabaseConnector = new LocationDatabaseConnector(context);
    }

    /* addToDB - add a location into local database */
    public void addToDB(Location newLoc) {
        try {
            locationDatabaseConnector.insertLocation(newLoc.getLocid(), newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getTag());
        } catch (DatabaseException e) {
            e.fix(e.getErrNo());
        }
    }

    /* updateToDB - update a location in the database */
    public void updateToDB(Location newLoc) {
        int id = newLoc.getLocid();
        try {
            locationDatabaseConnector.updateLocation(id, newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getTag());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /* deleteFromDB - delete a location from database */
    public void delateFromDB(int locmid) {
        try {
            locationDatabaseConnector.deleteLocation(locmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


}
