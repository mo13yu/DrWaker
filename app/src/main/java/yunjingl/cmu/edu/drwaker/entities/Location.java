package yunjingl.cmu.edu.drwaker.entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yunjing on 4/13/16.
 */
public class Location {
    private int locid;
    //private String latitude;
    //private String longitude;
    private LatLng latlng;
    private String tag;

    public Location() {
        locid = 0;
        latlng = null;
        tag = null;
    }

    public Location(int locid, LatLng latlng, String tag) {
        this.locid = locid;
        this.latlng = latlng;
        this.tag = tag;
    }

    public int getLocid() {
        return locid;
    }

    public void setLocid(int locid) {
        this.locid = locid;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getLatitude() {
        return latlng.latitude;
    }

    public Double getLongitude() {
        return latlng.longitude;
    }

    // Print information of Location
    public void print() {
        System.out.printf("ID: %d\nTag: %s\nLatLng: (%f, %f)\n", locid, tag, latlng.latitude, latlng.longitude);
    }
}
