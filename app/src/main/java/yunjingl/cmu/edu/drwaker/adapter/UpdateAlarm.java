package yunjingl.cmu.edu.drwaker.adapter;

/**
 * Created by yunjing on 4/22/16.
 */
public interface UpdateAlarm {
    public void updateAlarm(int alarmid,int hour,int minute,int locationid, String wake_up_method,
                            String tag, String tone);
}
