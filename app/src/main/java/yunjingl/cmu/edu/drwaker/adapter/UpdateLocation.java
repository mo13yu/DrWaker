package yunjingl.cmu.edu.drwaker.adapter;

import com.google.android.gms.maps.model.LatLng;

/**
 * This is an interface to update an existing location
 */
public interface UpdateLocation {
    public void updateLocation(int locid, LatLng latlng, String tag);
}
