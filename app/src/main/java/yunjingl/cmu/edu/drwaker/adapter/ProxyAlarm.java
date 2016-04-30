package yunjingl.cmu.edu.drwaker.adapter;

import java.util.LinkedHashMap;

import yunjingl.cmu.edu.drwaker.database.ReadAlarm;
import yunjingl.cmu.edu.drwaker.entities.Alarm;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyAlarm {
    private static LinkedHashMap<Integer,Alarm> alarms=new LinkedHashMap<Integer,Alarm>();

    public static LinkedHashMap<Integer, Alarm> getAlarms() {
        return alarms;
    }

    public static void setAlarms(LinkedHashMap<Integer, Alarm> alarms) {
        ProxyAlarm.alarms = alarms;
    }
    public static void initializeAlarms(){
        alarms= ReadAlarm.readAll();

    }
    public static void createAlarm(int hour,int minute,int locationid, String wake_up_method,
                            String tag, String tone){

        Alarm newalarm=new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        newalarm.setAlarmid(alarms.size()+1);
        newalarm.setLocation(ReadLocation.read(locationid));
        newalarm.setMath(ReadMath.read());
    }
    public static void updateAlarm(int alarmid,int hour,int minute,int locationid, String wake_up_method,
                            String tag, String tone){

    }
    public static void deleteAlarm(int alarmid){

    }

}
