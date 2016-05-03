package yunjingl.cmu.edu.drwaker.adapter;

import java.util.ArrayList;

import yunjingl.cmu.edu.drwaker.entities.Location;

/**
 *  This is an interface to get all locations as well as a single location
 */
public interface GetLocation {
    public ArrayList<String> getAllLocations();
    public Location getLocation(String tag);

    // functions for debugging purpose
    public void printAllLocations();
}
