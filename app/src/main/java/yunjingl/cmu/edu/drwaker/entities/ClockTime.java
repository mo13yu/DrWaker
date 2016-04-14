package yunjingl.cmu.edu.drwaker.entities;

/**
 * holds the time of clock setted
 */
public class ClockTime {

    private int hour;
    private int minute;
    private String ampm;

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

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }
}
