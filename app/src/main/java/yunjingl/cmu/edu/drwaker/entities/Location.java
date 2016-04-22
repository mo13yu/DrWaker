package yunjingl.cmu.edu.drwaker.entities;

/**
 * Created by yunjing on 4/13/16.
 */
public class Location {
    private int locid;
    private String latitude;
    private String longitude;

    public Location(int locid, String latitude, String longitude) {
        this.locid = locid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocid() {
        return locid;
    }

    public void setLocid(int locid) {
        this.locid = locid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
