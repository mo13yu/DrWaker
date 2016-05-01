package yunjingl.cmu.edu.drwaker.adapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import yunjingl.cmu.edu.drwaker.entities.Location;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyLocation {
    private static LinkedHashMap<String,Location> locations = new LinkedHashMap<String,Location>();


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
    public void deleteLocation(String tag) {
        int id = locations.get(tag).getLocid();
        // delete location from LinkedHashMap
        locations.remove(tag);
        // delete location from database
        //TODO: remove location from database
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
        return locations.get(tag);
    }

    public boolean nearLocation(Location location) {
        //TODO: check if user is in the area
        return false;
    }


    /* InitializeLocation */
    public void initializeLocations() {
        // get locations from database
        //TODO: get all stored locations from database, loop through them and add all to LinkedHashMap
    }


    /* UpdateLocation */
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


    /* DEBUGGING */
    public void printAllLocations() {
        Set<String> st = locations.keySet();
        Iterator<String> itr = st.iterator();
        while (itr.hasNext()) {
            locations.get(itr.next()).print();
        }
    }
}
