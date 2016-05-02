package yunjingl.cmu.edu.drwaker.adapter;


import android.content.Context;

/**
 * This is an interface to create a new alarm
 */
public interface CreateAlarm {
    public void createAlarm(int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone);
    public void setContext(Context con);
}
