package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * This is an interface to create a new location
 */
public interface CreateLocation {
    public void createLocation(LatLng latlng, String tag);
    public int getAvailableID();
    public void setContext(Context con);
}
