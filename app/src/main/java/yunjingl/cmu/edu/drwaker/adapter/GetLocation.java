package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;

import java.util.ArrayList;

import yunjingl.cmu.edu.drwaker.entities.Location;

/**
 * Created by yunjing on 4/22/16.
 */
public interface GetLocation {
    public ArrayList<String> getAllLocations();
    public Location getLocation(String tag);

    //TODO: a function takes alarm's location and return boolean if the location is near current location
    public boolean nearLocation(String lat, String lng);

    // functions for debugging purpose
    public void printAllLocations();
}
