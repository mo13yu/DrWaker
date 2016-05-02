package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;
import android.database.Cursor;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import yunjingl.cmu.edu.drwaker.database.AlarmDatabaseConnector;
import yunjingl.cmu.edu.drwaker.entities.Alarm;
import yunjingl.cmu.edu.drwaker.exception.DatabaseException;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyAlarm {
    private static LinkedHashMap<Integer,Alarm> alarms=new LinkedHashMap<Integer,Alarm>();

    private Context context;
    private static  AlarmDatabaseConnector alarmDatabaseConnector;


    public LinkedHashMap<Integer, Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(LinkedHashMap<Integer, Alarm> alarms) {
        ProxyAlarm.alarms = alarms;
    }

    public void initializeAlarms(){
        //ToDo:alarms = ReadAlarm.readAll();
    }

    public void createAlarm(int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone){
        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
//        int max=0;
//        Iterator iterator=alarms.keySet().iterator();
//        while(iterator.hasNext()){
//            int key=(int)iterator.next();
//
//        }
        Set<Integer> keys=alarms.keySet();
        int alarmid=0;
        if(keys.isEmpty()){
            alarmid=1;
        }else{
            alarmid = Collections.max(keys)+1;
        }
        //int alarmid = Collections.max(keys)+1;
        newalarm.setAlarmid(alarmid);
        newalarm.setLocation(new SetLocation().getLocation(locationtag));
        newalarm.setLoc_switch(locationswitch);
        //TODO: newalarm.setMath(ReadMath.read());
        alarms.put(alarmid,newalarm);

        addToDB(newalarm);
    }

    public void addToDB(Alarm newalarm){
//        try{
//            alarmDatabaseConnector.insertAlarm(newalarm.getHour(),
//                    newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
//                    newalarm.isLoc_switch(), 1, 1);}              //ToDo: newalarm.getMathID(), newalarm.getLocationID(), need add mathID,on/off,locationID
//        catch(DatabaseException e){
//            e.fix(e.getErrNo());
//        }
    }

    public void updateAlarm(int alarmid,int hour,int minute,String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone) {
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


    public void updateToDB(Alarm newalarm){
//        int id = newalarm.getAlarmid();
//        try {
//            alarmDatabaseConnector.updateAlarm(id, newalarm.getHour(),
//                    newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
//                    newalarm.isLoc_switch(), newalarm.getMathID(),newalarm.getLocationID());       //TODO:need add mathID,on/off,locationID
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }
    }

    public void delateFromDB(int alarmid){
        try {
            alarmDatabaseConnector.deleteAlarm(alarmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
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
        return Double.toString(alarms.get(alarmno).getLocation().getLatitude());
    }

    public String getLongitude(int alarmno){
        return Double.toString(alarms.get(alarmno).getLocation().getLongitude());
    }

    public boolean isLocationSwitchOn(int alarmno){
        return alarms.get(alarmno).isLoc_switch();
    }

    public void setContext(Context con){
        context=con;
        alarmDatabaseConnector=new AlarmDatabaseConnector(context);
    }

    public LinkedHashMap<Integer,Alarm> allAlarm(){
        LinkedHashMap<Integer,Alarm> data= new LinkedHashMap<>();
        try {
            alarmDatabaseConnector.open();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        Cursor cursor=alarmDatabaseConnector.getAllAlarm();
        int idIndex= cursor.getColumnIndex("id");
        int hourIndex=cursor.getColumnIndex("Hour");
        int minuteIndex=cursor.getColumnIndex("Minute");
        int wakeupmethodIndex=cursor.getColumnIndex("Wakeupmethod");
        int tagIndex=cursor.getColumnIndex("Tag");
        int tuneIndex=cursor.getColumnIndex("Tune");
        int statusIndex=cursor.getColumnIndex("Status");
        cursor.moveToFirst();
        int id=Integer.valueOf(cursor.getString(idIndex));
        int hour=Integer.valueOf(cursor.getString(hourIndex));
        int minute=Integer.valueOf(cursor.getString(minuteIndex));
        String wakeupmethod=cursor.getString(wakeupmethodIndex);
        String tag=cursor.getString(tagIndex);
        String tune=cursor.getString(tuneIndex);
        Boolean status=Boolean.valueOf(cursor.getString(statusIndex));        //Status is boolean
        int counter=0;

        while(!cursor.isLast()){
            Alarm alarm=new Alarm(id,hour,minute);
            alarm.setWake_up_method(wakeupmethod);
            alarm.setTag(tag);
            alarm.setTone(tune);
            alarm.setLoc_switch(status);
            //need math and location
            data.put(counter,alarm);
            counter++;
            cursor.moveToNext();
        }
        return data;
    }

    public Alarm readAlarm(int id){
        try {
            alarmDatabaseConnector.open();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        Cursor cursor=alarmDatabaseConnector.getOneAlarm(id);
        int idIndex= cursor.getColumnIndex("id");
        int hourIndex=cursor.getColumnIndex("Hour");
        int minuteIndex=cursor.getColumnIndex("Minute");
        int wakeupmethodIndex=cursor.getColumnIndex("Wakeupmethod");
        int tagIndex=cursor.getColumnIndex("Tag");
        int tuneIndex=cursor.getColumnIndex("Tune");
        int statusIndex=cursor.getColumnIndex("Status");
        cursor.moveToFirst();
        int alarmid=Integer.valueOf(cursor.getString(idIndex));
        int hour=Integer.valueOf(cursor.getString(hourIndex));
        int minute=Integer.valueOf(cursor.getString(minuteIndex));
        String wakeupmethod=cursor.getString(wakeupmethodIndex);
        String tag=cursor.getString(tagIndex);
        String tune=cursor.getString(tuneIndex);
        Boolean status=Boolean.valueOf(cursor.getString(statusIndex));
        Alarm alarm=new Alarm(id,hour,minute);
        alarm.setWake_up_method(wakeupmethod);
        alarm.setTag(tag);
        alarm.setTone(tune);
        alarm.setLoc_switch(status);
        //need math and location

        return alarm;
    }
}
