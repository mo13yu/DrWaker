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

    public LinkedHashMap<Integer, Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(LinkedHashMap<Integer, Alarm> alarms) {
        ProxyAlarm.alarms = alarms;
    }

    public void initializeAlarms(){
        alarms = ReadAlarm.readAll();
    }

    public void createAlarm(int hour, int minute, int locationid, String wake_up_method, String tag, String tone) {

    }

    public void createAlarm(int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone){
        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        int alarmid = Collections.max(alarms.keySet())+1;
        newalarm.setAlarmid(alarmid);
        newalarm.setLocation(new SetLocation().getLocation(locationtag));
        newalarm.setLoc_switch(locationswitch);
        //TODO: newalarm.setMath(ReadMath.read());
        alarms.put(alarmid,newalarm);

        addToDB(newalarm);
    }

    public void updateAlarm(int alarmid, int hour, int minute, int locationid, String wake_up_method, String tag, String tone) {

    }

    public void updateAlarm(int alarmid,int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone){
        alarms.remove(alarmid);
        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        newalarm.setAlarmid(alarmid);
        newalarm.setLocation(new SetLocation().getLocation(locationtag));
        newalarm.setLoc_switch(locationswitch);
        //TODO: newalarm.setMath(ReadMath.read());
        alarms.put(alarmid, newalarm);

        updateToDB(newalarm);
    }

    public void deleteAlarm(int alarmid){
        alarms.remove(alarmid);

        delateFromDB(alarmid);
    }

    public void addToDB(Alarm newalarm){

    }
    public void updateToDB(Alarm newalarm){

    }

    public void delateFromDB(int alarmid){

    }
    public int getNumberOfAlarms(){
        return alarms.size();
    }
    public int getHour(int alarmno){
        return alarms.get(alarmno).getHour();
    }
    public int getMinute(int alarmno){
        return alarms.get(alarmno).getMinute();
    }
    public String getTone(int alarmno){
        return alarms.get(alarmno).getTone();
    }
    public String getWakUpMethod(int alarmno){
        return alarms.get(alarmno).getWake_up_method();
    }
    public Set<Integer> getIdSet(){
        return alarms.keySet();
    }

    public String getLatitude(int alarmno){
        return alarms.get(alarmno).getLocation().getLatitude();
    }

    public String getLongitude(int alarmno){
        return alarms.get(alarmno).getLocation().getLongitude();
    }

    public boolean isLocationSwitchOn(int alarmno){
        return alarms.get(alarmno).isLoc_switch();
    }

}
