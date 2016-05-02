package yunjingl.cmu.edu.drwaker.entities;

/**
 * Created by yunjing on 4/13/16.
 */
public class Alarm {
    private int alarmid;
    private int hour;
    private int minute;
    private Math math;
    private Location location;
    private String wake_up_method;
    private boolean loc_switch;
    private String tag;
    private String tone;
    //private String alarm_switch;

    public Alarm(int alarmid,int hour,int minute){
        this.alarmid = alarmid;
        this.hour=hour;
        this.minute=minute;
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
<<<<<<< HEAD
    public String getMathQuestion(){
        return math.getQuestion();
    }
    public String getMathAnswer(){
        return math.getAnswer();
=======
    public void setMath(int id,String question,String answer) {
        Math newMath=new Math();
        newMath.setMathid(id);
        newMath.setQuestion(question);
        newMath.setAnswer(answer);
>>>>>>> origin/master
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

    public int getLocationID(){
        return location.getLocid();
    }

    public boolean isLoc_switch() {
        return loc_switch;
    }

    public void setLoc_switch(boolean loc_switch) {
        this.loc_switch = loc_switch;
    }

    public boolean hasLocation(){
        if(location==null){
            return false;
        }else{
            return true;
        }
    }

    public Alarm(){
        super();
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
