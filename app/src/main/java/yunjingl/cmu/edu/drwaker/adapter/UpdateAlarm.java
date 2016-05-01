package yunjingl.cmu.edu.drwaker.adapter;

/**
 * Created by yunjing on 4/22/16.
 */
public interface UpdateAlarm {
    public void updateAlarm(int alarmid,int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone);
}
