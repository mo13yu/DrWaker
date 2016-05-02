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
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyLocation {

    private static LinkedHashMap<String,Location> locations = new LinkedHashMap<String,Location>();

    private static Context context;
    private static LocationDatabaseConnector locationDatabaseConnector;



    /* CreateLocation */
    public void createLocation(LatLng latlng, String tag) {
        Location newLocation = new Location(getAvailableID(), latlng, tag);
        locations.put(newLocation.getTag(), newLocation);
        addToDB(newLocation);

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
        locations.remove(tag);
        delateFromDB(id);

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
        if (locations.containsKey(tag)) {
            return locations.get(tag);
        } else {
            //TODO: throw CusException("")
        }
        return locations.get(tag);
    }


    /* InitializeLocation */
    public void initializeLocations() {
        try {
            locations=locationDatabaseConnector.getAllLocation();
            printAllLocations();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


    /* UpdateLocation */
    //TODO: test updateLocation
    public void updateLocation(int locid, LatLng latlng, String tag) {
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            String currentTag = locations.get(itr.next()).getTag();
            Location currentLoc=locations.get(currentTag);
            if(currentLoc.getLocid()==locid) {
                currentLoc.setLatlng(latlng);
                currentLoc.setTag(tag);
                locations.put(currentTag,currentLoc);
                updateToDB(currentLoc);

            }
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
            locationDatabaseConnector.insertLocation(newLoc.getLocid(),newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getTag());
        }
        catch(DatabaseException e){
            e.fix(e.getErrNo());
        }
    }

    public void updateToDB(Location newLoc){
        int id = newLoc.getLocid();
        try {
            locationDatabaseConnector.updateLocation(id, newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getTag());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void delateFromDB(int locmid){
        try {
            locationDatabaseConnector.deleteLocation(locmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


}
