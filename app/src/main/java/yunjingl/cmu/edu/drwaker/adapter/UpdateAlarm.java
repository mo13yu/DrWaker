package yunjingl.cmu.edu.drwaker.adapter;

/**
 * This is an interface to update an existing alarm
 */
public interface UpdateAlarm {
    public void updateAlarm(int alarmid,int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone);
}
