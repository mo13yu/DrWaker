package yunjingl.cmu.edu.drwaker.entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Location object
 */
public class Location {
    private int locid;
    private LatLng latlng;
    private String tag;

    /* Default Constructor */
    public Location() {
        locid = 0;
        latlng = null;
        tag = null;
    }

    /* Constructor */
    public Location(int locid, LatLng latlng, String tag) {
        this.locid = locid;
        this.latlng = latlng;
        this.tag = tag;
    }


    /**
     * Getters
     */
    public int getLocid() {
        return locid;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public Double getLatitude() {
        return latlng.latitude;
    }

    public Double getLongitude() {
        return latlng.longitude;
    }

    public String getTag() {
        return tag;
    }


    /**
     * Setters
     */
    public void setLocid(int locid) {
        this.locid = locid;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    /* print - print all information of given location */
    public void print() {
        System.out.printf("ID: %d\nTag: %s\nLatLng: (%f, %f)\n", locid, tag, latlng.latitude, latlng.longitude);
    }

}
