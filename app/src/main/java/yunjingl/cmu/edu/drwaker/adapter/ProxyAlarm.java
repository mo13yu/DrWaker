package yunjingl.cmu.edu.drwaker.adapter;

import java.util.LinkedHashMap;

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
}
