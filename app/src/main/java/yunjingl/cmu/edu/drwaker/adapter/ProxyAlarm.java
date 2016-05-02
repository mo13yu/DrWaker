package yunjingl.cmu.edu.drwaker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

import yunjingl.cmu.edu.drwaker.database.AlarmDatabaseConnector;
import yunjingl.cmu.edu.drwaker.entities.Alarm;
import yunjingl.cmu.edu.drwaker.exception.DatabaseException;
import yunjingl.cmu.edu.drwaker.ws.local.SocketClient;

/**
 * Created by yunjing on 4/22/16.
 */
public abstract class ProxyAlarm {
    private static LinkedHashMap<Integer, Alarm> alarms = new LinkedHashMap<Integer, Alarm>();

    private static HashMap<Integer,String> mathProblemAndAnswer=new HashMap<Integer,String>();
    private Context context;
    private static AlarmDatabaseConnector alarmDatabaseConnector;

    private String math;


    public LinkedHashMap<Integer, Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(LinkedHashMap<Integer, Alarm> alarms) {
        ProxyAlarm.alarms = alarms;
    }

    public void initializeAlarms() {
        try {
            if(mathProblemAndAnswer.isEmpty()){
                initializeMathsSet();
            }
            alarms = alarmDatabaseConnector.getAllAlarm();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void initializeMathsSet() {

            SocketClient socketClient = new SocketClient("10.0.2.2", 8902);
            socketClient.start();

            while (socketClient.isAlive()) {
                continue;
            }
            math = socketClient.getMath();
            Log.d("initialize alarm test", math);
            String[] qa = math.split(" ");
            for(int i=0;i<qa.length;i=i+3){
                mathProblemAndAnswer.put(Integer.parseInt(qa[i]),qa[i+1]+" "+qa[i+2]);
            }
            //newalarm.setMath(Integer.parseInt(qa[0]), qa[1], qa[2]);
            //alarms = alarmDatabaseConnector.getAllAlarm();

    }

    public void createAlarm(int hour, int minute, String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone) {

        //Log.d("proxy:location tag", "is null???");
        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);


        if(wake_up_method.equals("Math Calculation")) {
//            SocketClient socketClient = new SocketClient("10.0.2.2", 8896);
//            socketClient.start();
//
//            while (socketClient.isAlive()) {
//                continue;
//            }
//            math = socketClient.getMath();
//            Log.d("alarm test", math);
//            String[] qa = math.split(" ");
            Log.d("in proxyalarm, math:", String.valueOf(mathProblemAndAnswer.get(1)));
            int random=new Random().nextInt(mathProblemAndAnswer.size())+1;
            while(!mathProblemAndAnswer.keySet().contains(random)){
                random=new Random().nextInt(mathProblemAndAnswer.size());
            }

            String[] qa=mathProblemAndAnswer.get(random).split(" ");
            newalarm.setMath(random,qa[0],qa[1]);
            //newalarm.setMath(1, qa[1], qa[2]);
        }

//        int max=0;
//        Iterator iterator=alarms.keySet().iterator();
//        while(iterator.hasNext()){
//            int key=(int)iterator.next();
//
//        }
//        */

        // Get next available alarm ID
        Set<Integer> keys = alarms.keySet();
        int alarmid;
        if (keys.isEmpty()) {
            alarmid = 1;
        } else {
            alarmid = Collections.max(keys) + 1;
        }
        //int alarmid = Collections.max(keys)+1;
        newalarm.setAlarmid(alarmid);
        newalarm.setLoc_switch(locationswitch);
        if(locationswitch){
            newalarm.setLocation(new SetLocation().getLocation(locationtag));
        }

        newalarm.setLoc_switch(locationswitch);

        Log.e("ProxyAlarm check", "alarm id" + alarmid);
        alarms.put(alarmid, newalarm);

        addToDB(newalarm);
    }

    public void addToDB(Alarm newalarm) {
        try {
            if(!newalarm.hasLocation()&&!newalarm.hasMath()){
                alarmDatabaseConnector.insertAlarm(newalarm.getAlarmid(), newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(),"0", 0);
            }else if(!newalarm.hasLocation()){
                alarmDatabaseConnector.insertAlarm(newalarm.getAlarmid(), newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(),"0",newalarm.getMathID());
            }else if(!newalarm.hasMath()){
                alarmDatabaseConnector.insertAlarm(newalarm.getAlarmid(), newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(), newalarm.getLocationTag(),0);
            }else{
                alarmDatabaseConnector.insertAlarm(newalarm.getAlarmid(), newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(), newalarm.getLocationTag(), newalarm.getMathID());
            }


        }              //ToDo: newalarm.getMathID(), need add mathID,on/off,locationID
        catch (DatabaseException e) {
            e.fix(e.getErrNo());
        }
    }

    public void updateAlarm(int alarmid, int hour, int minute, String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone) {
        alarms.remove(alarmid);
        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);
        newalarm.setAlarmid(alarmid);
        newalarm.setLoc_switch(locationswitch);

        if(locationswitch){
            newalarm.setLocation(new SetLocation().getLocation(locationtag));
        }
        if(wake_up_method.equals("Math Calculation")) {
            String[] qa=mathProblemAndAnswer.get(1).split(" ");
            newalarm.setMath(1,qa[0],qa[1]);
        }
        alarms.put(alarmid, newalarm);

        updateToDB(newalarm);
    }

    public void deleteAlarm(int alarmid) {
        alarms.remove(alarmid);

        delateFromDB(alarmid);
    }


    public void updateToDB(Alarm newalarm) {
        int id = newalarm.getAlarmid();
        try {
            if(!newalarm.hasLocation()&&!newalarm.hasMath()){
                alarmDatabaseConnector.updateAlarm(id, newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(), "0", 0);
            }else if(!newalarm.hasLocation()){
                alarmDatabaseConnector.updateAlarm(id, newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(),"0", newalarm.getMathID());
            }else if(!newalarm.hasMath()){
                alarmDatabaseConnector.updateAlarm(id, newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(), newalarm.getLocationTag(),0);
            }else{
                alarmDatabaseConnector.updateAlarm(id, newalarm.getHour(),
                        newalarm.getMinute(), newalarm.getWake_up_method(), newalarm.getTag(), newalarm.getTone(),
                        newalarm.isLoc_switch(), newalarm.getLocationTag(),newalarm.getMathID());
            }

            //TODO:need add mathID,on/off,locationID
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void delateFromDB(int alarmid) {
        try {
            alarmDatabaseConnector.deleteAlarm(alarmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfAlarms() {
        return alarms.size();
    }

    public int getHour(int alarmno) {
        return alarms.get(alarmno).getHour();
    }

    public int getMinute(int alarmno) {
        return alarms.get(alarmno).getMinute();
    }

    public String getTone(int alarmno) {
        return alarms.get(alarmno).getTone();
    }

    public String getWakUpMethod(int alarmno) {
        return alarms.get(alarmno).getWake_up_method();
    }

    public Set<Integer> getIdSet() {
        return alarms.keySet();
    }

    public String getLatitude(int alarmno) {
        return Double.toString(alarms.get(alarmno).getLocation().getLatitude());
    }

    public String getTag(int alarmno) {
        return alarms.get(alarmno).getTag();
    }

    public String getMathQuestion(int alarmno) {
        return alarms.get(alarmno).getMathQuestion();
    }

    public String getMathAnswer(int alarmno) {
        return alarms.get(alarmno).getMathAnswer();
    }

    public String getLongitude(int alarmno) {
        return Double.toString(alarms.get(alarmno).getLocation().getLongitude());
    }

    public boolean isLocationSwitchOn(int alarmno) {
        return alarms.get(alarmno).isLoc_switch();
    }

    public void setContext(Context con) {
        context = con;
        alarmDatabaseConnector = new AlarmDatabaseConnector(context,mathProblemAndAnswer);
    }


    public boolean hasLocation(int alarmid) {
        Alarm thisalarm = alarms.get(alarmid);
        return thisalarm.hasLocation();
    }


    //    public Alarm readAlarm(int id){
//        try {
//            alarmDatabaseConnector.open();
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }
//
//        Cursor cursor=alarmDatabaseConnector.getOneAlarm(id);
//        int idIndex= cursor.getColumnIndex("Id");
//        int hourIndex=cursor.getColumnIndex("Hour");
//        int minuteIndex=cursor.getColumnIndex("Minute");
//        int wakeupmethodIndex=cursor.getColumnIndex("Wakeupmethod");
//        int tagIndex=cursor.getColumnIndex("Tag");
//        int tuneIndex=cursor.getColumnIndex("Tune");
//        int statusIndex=cursor.getColumnIndex("Status");
//        cursor.moveToFirst();
//        int alarmid=Integer.valueOf(cursor.getString(idIndex));
//        int hour=Integer.valueOf(cursor.getString(hourIndex));
//        int minute=Integer.valueOf(cursor.getString(minuteIndex));
//        String wakeupmethod=cursor.getString(wakeupmethodIndex);
//        String tag=cursor.getString(tagIndex);
//        String tune=cursor.getString(tuneIndex);
//        Boolean status=Boolean.valueOf(cursor.getString(statusIndex));
//        Alarm alarm=new Alarm(id,hour,minute);
//        alarm.setWake_up_method(wakeupmethod);
//        alarm.setTag(tag);
//        alarm.setTone(tune);
//        alarm.setLoc_switch(status);
//        //need math and location
//
//        return alarm;
//    }

    public void printAll() {
        Iterator iterator = alarms.keySet().iterator();
        while (iterator.hasNext()) {
            alarms.get(iterator.next()).print();
            //Alarm next = alarms.get(iterator.next());
            //result += next.getHour() + ":" + next.getMinute() + "     ";
        }
    }

//    public String getIP() {
//        String strAddress = "";
//        try {
//            InetAddress local = InetAddress.getLocalHost();
//            byte[] b = local.getAddress();
//
//            for (int i = 0; i < b.length; i++) {
//                strAddress += ((int) 255 & b[i]) + ".";
//            }
//            strAddress = strAddress.substring(0, strAddress.length() - 1);
//
//        } catch (UnknownHostException e) {
//        }
//        return strAddress;
//    }
}
