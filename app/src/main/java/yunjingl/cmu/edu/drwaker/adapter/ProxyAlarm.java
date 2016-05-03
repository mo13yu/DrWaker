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
 * This is a proxy contains all the methods of populating and manipulating alarms.
 */
public abstract class ProxyAlarm {
    private static LinkedHashMap<Integer, Alarm> alarms = new LinkedHashMap<Integer, Alarm>();//keeps all the alarms in database

    private static HashMap<Integer,String> mathProblemAndAnswer=new HashMap<Integer,String>();//keeps all the math problems read from remote server database.
    private Context context;//context helps to connect database
    private static AlarmDatabaseConnector alarmDatabaseConnector;//connector to connect database.

    private String math;//math problems get from remote server database


    public LinkedHashMap<Integer, Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(LinkedHashMap<Integer, Alarm> alarms) {
        ProxyAlarm.alarms = alarms;
    }

    /**
     * initialize all alarms in database, and populate them to the alarms linked hash map.
     */
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

    /**
     * get all math problems from remote server database by socket and populate them to the mathProblemAndAnswer hash map.
     */
    public void initializeMathsSet() {

            //TODO: Insert Server's IP
            SocketClient socketClient = new SocketClient("128.237.207.109", 8904);
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
    }

    /**
     * create a new alarm and add it to the linked hash map as well as to the database.
     * @param hour hour of the alarm
     * @param minute minute of the alarm
     * @param locationtag location tag
     * @param locationswitch whether the location is switched on.
     * @param wake_up_method the wake up method
     * @param tag tag of the alarm
     * @param tone which tone to use
     */
    public void createAlarm(int hour, int minute, String locationtag, boolean locationswitch, String wake_up_method,
                            String tag, String tone) {

        Alarm newalarm = new Alarm();
        newalarm.setHour(hour);
        newalarm.setMinute(minute);
        newalarm.setWake_up_method(wake_up_method);
        newalarm.setTag(tag);
        newalarm.setTone(tone);

        if(wake_up_method.equals("Math Calculation")) {
            Log.d("in proxyalarm, math:", String.valueOf(mathProblemAndAnswer.get(1)));
            int random=new Random().nextInt(mathProblemAndAnswer.size())+1;
            while(!mathProblemAndAnswer.keySet().contains(random)){
                random=new Random().nextInt(mathProblemAndAnswer.size());
            }

            String[] qa=mathProblemAndAnswer.get(random).split(" ");
            newalarm.setMath(random,qa[0],qa[1]);
        }

        // Get next available alarm ID
        Set<Integer> keys = alarms.keySet();
        int alarmid;
        if (keys.isEmpty()) {
            alarmid = 1;
        } else {
            alarmid = Collections.max(keys) + 1;
        }
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

    /**
     * add an alarm to the database.
     * @param newalarm
     */
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


        }
        catch (DatabaseException e) {
            e.fix(e.getErrNo());
        }
    }

    /**
     * update a current alarm with new settings.
     * @param alarmid
     * @param hour
     * @param minute
     * @param locationtag
     * @param locationswitch
     * @param wake_up_method
     * @param tag
     * @param tone
     */
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

    /**
     * delete a current alarm.
     * @param alarmid
     */
    public void deleteAlarm(int alarmid) {
        alarms.remove(alarmid);

        delateFromDB(alarmid);
    }

    /**
     * update a current alarm with new alarm object.
     * @param newalarm
     */
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
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete an existing alarm from database.
     * @param alarmid
     */
    public void delateFromDB(int alarmid) {
        try {
            alarmDatabaseConnector.deleteAlarm(alarmid);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the number of total alarms
     * @return
     */
    public int getNumberOfAlarms() {
        return alarms.size();
    }

    /**
     * get the hour feild of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public int getHour(int alarmno) {
        return alarms.get(alarmno).getHour();
    }
    /**
     * get the minute feild of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public int getMinute(int alarmno) {
        return alarms.get(alarmno).getMinute();
    }
    /**
     * get the tone feild of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getTone(int alarmno) {
        return alarms.get(alarmno).getTone();
    }
    /**
     * get the wake_up_method feild of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getWakUpMethod(int alarmno) {
        return alarms.get(alarmno).getWake_up_method();
    }

    /**
     * get the key set of the alarms linked hash map
     * @return
     */
    public Set<Integer> getIdSet() {
        return alarms.keySet();
    }
    /**
     * get the latitude feild of location of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getLatitude(int alarmno) {
        return Double.toString(alarms.get(alarmno).getLocation().getLatitude());
    }

    /**
     * get the tag feild of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getTag(int alarmno) {
        return alarms.get(alarmno).getTag();
    }
    /**
     * get the question feild of math object of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getMathQuestion(int alarmno) {
        return alarms.get(alarmno).getMathQuestion();
    }
    /**
     * get the answer feild of math object of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getMathAnswer(int alarmno) {
        return alarms.get(alarmno).getMathAnswer();
    }
    /**
     * get the longitude feild of location of a certain alarm
     * @param alarmno alarm id
     * @return
     */
    public String getLongitude(int alarmno) {
        return Double.toString(alarms.get(alarmno).getLocation().getLongitude());
    }
    /**
     * see if user switch on the location detection function
     * @param alarmno alarm id
     * @return
     */
    public boolean isLocationSwitchOn(int alarmno) {
        return alarms.get(alarmno).isLoc_switch();
    }

    /**
     * set the context and connect to database
     * @param con
     */
    public void setContext(Context con) {
        context = con;
        alarmDatabaseConnector = new AlarmDatabaseConnector(context,mathProblemAndAnswer);
    }

    public void printAll() {
        Iterator iterator = alarms.keySet().iterator();
        while (iterator.hasNext()) {
            alarms.get(iterator.next()).print();
        }
    }

}
