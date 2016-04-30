package yunjingl.cmu.edu.drwaker.adapter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

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
    public static void createAlarm(int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone){

        Alarm newalarm=new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        int alarmid= Collections.max(alarms.keySet())+1;
        newalarm.setAlarmid(alarmid);
        newalarm.setLocation(SetLocation.read(locationtag));
        newalarm.setLoc_switch(locationswitch);
        newalarm.setMath(ReadMath.read());
        alarms.put(alarmid,newalarm);

        addToDB(newalarm);

    }
    public static void updateAlarm(int alarmid,int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone){
        alarms.remove(alarmid);
        Alarm newalarm=new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        newalarm.setAlarmid(alarmid);
        newalarm.setLocation(SetLocation.read(locationtag));
        newalarm.setLoc_switch(locationswitch);
        newalarm.setMath(ReadMath.read());
        alarms.put(alarmid, newalarm);

        updateToDB(newalarm);
    }
    public static void deleteAlarm(int alarmid){
        alarms.remove(alarmid);

        delateFromDB(alarmid);
    }

    public static void addToDB(Alarm newalarm){

    }
    public static void updateToDB(Alarm newalarm){

    }
    public static void delateFromDB(int alarmid){

    }
    public static int getNumberOfAlarms(){
        return alarms.size();
    }
    public static int getHour(int alarmno){
        return alarms.get(alarmno).getHour();
    }
    public static int getMinute(int alarmno){
        return alarms.get(alarmno).getMinute();
    }
    public static String getTone(int alarmno){
        return alarms.get(alarmno).getTone();
    }
    public static String getWakUpMethod(int alarmno){
        return alarms.get(alarmno).getWake_up_method();
    }
    public static Set<Integer> getIdSet(){
        return alarms.keySet();
    }

    public static String getLatitude(int alarmno){
        return alarms.get(alarmno).getLocation().getLatitude();
    }

    public static String getLongitude(int alarmno){
        return alarms.get(alarmno).getLocation().getLongitude();
    }

    public static boolean isLocationSwitchOn(int alarmno){
        return alarms.get(alarmno).isLoc_switch();
    }

}
