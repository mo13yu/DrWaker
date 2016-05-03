package yunjingl.cmu.edu.drwaker.entities;

import android.util.Log;

/**
 * Alarm object
 */
public class Alarm {
    private int alarmid;//alarm id
    private int hour;
    private int minute;
    private Math math;//math problem to do the math detection when alarm rings
    private Location location;//Location object to do the location detection
    private String wake_up_method;//method user wants to be woke up
    private boolean loc_switch;//whether user enable location detection function.
    private String tag;//tag of the alarm
    private String tone;//tone of the alarm

    public Alarm(){
        super();
    }

    public Alarm(int alarmid,int hour,int minute){
        this.alarmid = alarmid;
        this.hour=hour;
        this.minute=minute;
    }

    public Alarm(int alarmid, int hour,int minute,Math math, Location location, String wake_up_method, boolean loc_switch,
                 String tag, String tone, String alarm_switch) {
        this.alarmid = alarmid;
        this.hour=hour;
        this.minute=minute;
        this.math = math;
        this.location = location;
        this.wake_up_method = wake_up_method;
        this.loc_switch = loc_switch;
        this.tag = tag;
        this.tone = tone;
        //this.alarm_switch = alarm_switch;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(int alarmid) {
        this.alarmid = alarmid;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getWake_up_method() {
        return wake_up_method;
    }

    public void setWake_up_method(String wake_up_method) {
        this.wake_up_method = wake_up_method;
    }

    public Math getMath() {
        return math;
    }

    public void setMath(Math math) {
        this.math = math;
    }

    public void setMath(int id,String question,String answer) {
        Math newMath=new Math();
        newMath.setMathid(id);
        newMath.setQuestion(question);
        newMath.setAnswer(answer);
        math=newMath;
    }

    public String getMathQuestion(){
        return math.getQuestion();
    }

    public String getMathAnswer() {
        return math.getAnswer();
    }

    public int getMathID(){
        return math.mathid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationTag(){
        return location.getTag();
    }

    public boolean hasLocation(){
        if(location==null){
            return false;
        }else{
            return true;
        }
    }

    public boolean hasMath(){
        if(math==null){
            return false;
        }else{
            return true;
        }
    }

    public boolean isLoc_switch() {
        return loc_switch;
    }

    public void setLoc_switch(boolean loc_switch) {
        this.loc_switch = loc_switch;
    }


    // Print information of Alarm
    public void print() {
        System.out.printf("ID: %d\nTag: %s\nHour: %d\nMinute: %d\nMethod: %s\n", alarmid, tag, hour, minute, wake_up_method);
        if(loc_switch) {
            System.out.printf("Location ON: %s (%f, %f)\n", location.getTag(), location.getLatitude(), location.getLongitude());
        }
        //System.out.printf("Math Question: %s\n", math.getQuestion());
    }


    /**
     * Math problem to perform math detection when alarm rings.
     */
    protected class Math {
        private int mathid;
        private String question;
        private String answer;

        public int getMathid() {
            return mathid;
        }

        public void setMathid(int mathid) {
            this.mathid = mathid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        protected Math(String question, String answer) {
            super();
            this.question = question;
            this.answer = answer;
        }

        protected Math() {
            super();
        }

    }
}
