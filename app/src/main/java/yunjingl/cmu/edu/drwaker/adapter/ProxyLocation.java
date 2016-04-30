package yunjingl.cmu.edu.drwaker.adapter;

import java.util.LinkedHashMap;

import yunjingl.cmu.edu.drwaker.entities.Location;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyLocation {
    private static LinkedHashMap<Integer,Location> locations=new LinkedHashMap<Integer,Location>();

    public static LinkedHashMap<Integer, Location> getLocations() {
        return locations;
    }

    public static void setLocations(LinkedHashMap<Integer, Location> locations) {
        ProxyLocation.locations = locations;
    }
}
