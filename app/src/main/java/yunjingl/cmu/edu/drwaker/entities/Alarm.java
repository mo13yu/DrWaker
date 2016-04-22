package yunjingl.cmu.edu.drwaker.entities;

/**
 * Created by yunjing on 4/13/16.
 */
public class Alarm {
    private int alarmid;
    private Math math;
    private Location location;
    private String wake_up_method;
    private String loc_switch;
    private String tag;
    private String tone;
    private String alarm_switch;

    public Alarm(int alarmid, Math math, Location location, String wake_up_method, String loc_switch,
                 String tag, String tone, String alarm_switch) {
        this.alarmid = alarmid;
        this.math = math;
        this.location = location;
        this.wake_up_method = wake_up_method;
        this.loc_switch = loc_switch;
        this.tag = tag;
        this.tone = tone;
        this.alarm_switch = alarm_switch;
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
