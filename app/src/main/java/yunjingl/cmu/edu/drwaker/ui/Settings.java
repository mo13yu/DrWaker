package yunjingl.cmu.edu.drwaker.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.entities.AlarmReceiver;
import yunjingl.cmu.edu.drwaker.entities.ClockTime;

public class Settings extends AppCompatActivity {
    private List<String> list = new ArrayList<String>();
    private ClockTime clockTime;
    private TextView myTextView;
    private PendingIntent pendingIntent;
    AlarmManager alarmManager;
    //private static MainActivity inst;
    private String tunes;
    private Context context;
//    private Spinner hour;
//    private Spinner minute;
//    private Spinner ampm;
//    private Spinner tune;
    private ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.context = this;
        clockTime=new ClockTime();
        Spinner hour = (Spinner)findViewById(R.id.hour);
        Spinner minute = (Spinner)findViewById(R.id.minute);
        Spinner ampm = (Spinner)findViewById(R.id.ampm);
        Spinner tune = (Spinner)findViewById(R.id.tune);
        hour.setAdapter(ArrayAdapter.createFromResource(this,R.array.hour, android.R.layout.simple_spinner_item));
        minute.setAdapter(ArrayAdapter.createFromResource(this,R.array.minute, android.R.layout.simple_spinner_item));
        ampm.setAdapter(ArrayAdapter.createFromResource(this,R.array.ampm, android.R.layout.simple_spinner_item));
        tune.setAdapter(ArrayAdapter.createFromResource(this, R.array.tune, android.R.layout.simple_spinner_item));

        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                String temp = myview.getText().toString();
                if(temp!=null){
                    if (temp.startsWith("0")) {
                        temp = temp.substring(1, temp.length());
                    }
                    clockTime.setHour(Integer.parseInt(temp));
                }

                //Toast.makeText(getBaseContext(),inthour,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                String temp = myview.getText().toString();
                if(temp!=null) {
                    if (temp.startsWith("0")) {
                        temp = temp.substring(1, temp.length());
                    }
                }
                clockTime.setMinute(Integer.parseInt(temp));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ampm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if(myview!=null)
                clockTime.setAmpm(myview.getText().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if(myview!=null)
                tunes = myview.getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button newloc=(Button)findViewById(R.id.newlocbutton);
        newloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(), SetLocation.class);
                startActivity(intent);
            }
        });


        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        Button done=(Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                //Calendar calendar=setAlarm(clockTime);
                int hour;
                int minute=clockTime.getMinute();
                String ampm=clockTime.getAmpm();
                if(ampm.equals("PM")){
                    hour=clockTime.getHour()+12;
                }else{
                    hour=clockTime.getHour();
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.e("clicked", "ye");
//                Intent alarm_intent=new Intent(getApplicationContext(), AlarmReceiver.class);
//                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                Log.e("set alarm manager", "ye");

 //               setAlarm(clockTime,tunes);
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                //Toast.makeText(getBaseContext(),inthour+""+intminute+""+ampms,Toast.LENGTH_SHORT).show();
            }
        });
        Button delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public Calendar setAlarm(ClockTime clockTime){
        final int hour;
        final int minute=clockTime.getMinute();
        final String ampm=clockTime.getAmpm();
        if(ampm.equals("PM")){
            hour=clockTime.getHour()+12;
        }else{
            hour=clockTime.getHour();
        }
        Calendar calendar=Calendar.getInstance();
        Toast.makeText(getBaseContext(), hour + "" + minute + "" + ampm, Toast.LENGTH_SHORT).show();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

//    public void setAlarm(ClockTime clockTime,String tunes){
//        final int hour;
//        final int minute=clockTime.getMinute();
//        final String ampm=clockTime.getAmpm();
//        if(ampm.equals("PM")){
//            hour=clockTime.getHour()+12;
//        }else{
//            hour=clockTime.getHour();
//        }
//        final Intent alarm_intent=new Intent(MainActivity.this, AlarmReceiver.class);
//        final AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
//        final Calendar calendar=Calendar.getInstance();
//        Toast.makeText(getBaseContext(), hour + "" + minute + "" + ampm, Toast.LENGTH_SHORT).show();
//        calendar.set(Calendar.HOUR_OF_DAY,hour);
//        calendar.set(Calendar.MINUTE, minute);
//
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(getBaseContext(),0,alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//    }






}
