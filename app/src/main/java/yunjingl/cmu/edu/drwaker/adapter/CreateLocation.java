package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yunjing on 4/22/16.
 */
public interface CreateLocation {
    public void createLocation(LatLng latlng, String tag);
    public int getAvailableID();
    public void setContext(Context con);
}
