package yunjingl.cmu.edu.drwaker.database;

import java.util.ArrayList;

import yunjingl.cmu.edu.drwaker.entities.Alarm;
import yunjingl.cmu.edu.drwaker.entities.ClockTime;

/**
 * Created by yunjing on 4/13/16.
 */
public class Read {
    public ClockTime readCalender(int id){

        return new ClockTime();
    }
    public Alarm readAlarm(int id){
        return new Alarm();
    }
    public ArrayList<ClockTime> readAllCalender(int id){
        return new ArrayList<ClockTime>();
    }
    public ArrayList<Alarm> readAllAlarm(){
        return new ArrayList<Alarm>();
    }
}
