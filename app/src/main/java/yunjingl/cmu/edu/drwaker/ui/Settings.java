package yunjingl.cmu.edu.drwaker.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.adapter.SetAlarm;
import yunjingl.cmu.edu.drwaker.adapter.SetLocation;
import yunjingl.cmu.edu.drwaker.entities.Alarm;
//import yunjingl.cmu.edu.drwaker.entities.Alarm;

public class Settings extends AppCompatActivity {

    MediaPlayer ring;
    String ringtone;
    int inputhour;
    int inputminute;
    //int locationid;
    String wake_up_method;
    String tag;
    String locationtag;
    boolean locationswitch;
    SetAlarm setAlarm=new SetAlarm();

    @Override
    protected void onResume() {
        super.onResume();
        Spinner location = (Spinner)findViewById(R.id.location);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new SetLocation().getAllLocations());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        location.setAdapter(dataAdapter);
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if (myview != null) {
                    locationtag = myview.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        new SetAlarm().setContext(this);
        new SetLocation().setContext(this);

        final String method=(getIntent().getExtras()).getString("create_or_update");
        //final Alarm newAlarm=getAlarm();
        Button done=(Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                RadioGroup radioGroup=(RadioGroup) findViewById(R.id.wake_up);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                wake_up_method=radioButton.getText().toString();
                tag=((EditText) findViewById(R.id.tag)).getText().toString();

                Switch loc_switch=(Switch) findViewById(R.id.locswitch);
                locationswitch=loc_switch.isChecked();
//                loc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        locationswitch=isChecked;
//                        Log.e("switch",String.valueOf(isChecked));
//                    }
//                });
                if(method.equals("create")){
                    setAlarm.createAlarm(inputhour, inputminute, locationtag, locationswitch, wake_up_method, tag, ringtone);
                }else if(method.equals("update")){
                    int alarmid=Integer.parseInt((getIntent().getExtras()).getString("alarmid"));
                    setAlarm.updateAlarm(alarmid, inputhour, inputminute, locationtag, locationswitch, wake_up_method, tag, ringtone);
                }
                //Alarm newAlarm = getAlarm();
                //SetAlarm.getAlarms().put(newAlarm.getAlarmid(), newAlarm);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        Spinner hour = (Spinner)findViewById(R.id.hour);
        Spinner minute = (Spinner)findViewById(R.id.minute);
        hour.setAdapter(ArrayAdapter.createFromResource(this,R.array.hour, android.R.layout.simple_spinner_item));
        minute.setAdapter(ArrayAdapter.createFromResource(this, R.array.minute, android.R.layout.simple_spinner_item));
        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                String temp = myview.getText().toString();
                if (temp != null) {
                    if (temp.startsWith("0")) {
                        temp = temp.substring(1, temp.length());
                    }
                    inputhour = Integer.parseInt(temp);
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
                if (temp != null) {
                    if (temp.startsWith("0")) {
                        temp = temp.substring(1, temp.length());
                    }
                }
                inputminute = Integer.parseInt(temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner tune = (Spinner)findViewById(R.id.tune);
        tune.setAdapter(ArrayAdapter.createFromResource(this, R.array.tune, android.R.layout.simple_spinner_item));
        tune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if (myview != null) {
                    ringtone= myview.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //ToDo:populate again
        Spinner location = (Spinner)findViewById(R.id.location);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new SetLocation().getAllLocations());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        location.setAdapter(dataAdapter);
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if (myview != null) {
                    locationtag = myview.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button testring=(Button)findViewById(R.id.testbutton);
        testring.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {

                if(ringtone.equals("Kiss the rain")){
                    ring=MediaPlayer.create(getApplicationContext(), R.raw.kiss_the_rain);
                    ring.start();
                }else if(ringtone.equals("River that flows in you")){
                    ring=MediaPlayer.create(getApplicationContext(), R.raw.river);
                    ring.start();
                }else if(ringtone.equals("I just wanna run")){
                    ring=MediaPlayer.create(getApplicationContext(), R.raw.i_just_wanna_run);
                    ring.start();
                }

            }
        });
        Button stoptest=(Button)findViewById(R.id.stoptest);
        stoptest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                ring.stop();

            }
        });

        Button newloc=(Button)findViewById(R.id.newlocbutton);
        newloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        });
//
//
//        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        final Calendar calendar = Calendar.getInstance();
//        Button done=(Button)findViewById(R.id.done);
//        done.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View viewParam) {
//                //Calendar calendar=setAlarm(clockTime);
//                int hour;
//                int minute=clockTime.getMinute();
//                String ampm=clockTime.getAmpm();
//                if(ampm.equals("PM")){
//                    hour=clockTime.getHour()+12;
//                }else{
//                    hour=clockTime.getHour();
//                }
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                Log.e("clicked", "ye");
////                Intent alarm_intent=new Intent(getApplicationContext(), AlarmReceiver.class);
////                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
////                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
////                Log.e("set alarm manager", "ye");
//
// //               setAlarm(clockTime,tunes);
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                startActivity(intent);
//                //Toast.makeText(getBaseContext(),inthour+""+intminute+""+ampms,Toast.LENGTH_SHORT).show();
//            }
//        });
//        Button delete=(Button)findViewById(R.id.delete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View viewParam) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//    public Alarm getAlarm(){
//        final Alarm resultAlarm=new Alarm();
//        resultAlarm.setHour(inputhour);
//        resultAlarm.setMinute(inputminute);
//        resultAlarm.setTone(ringtone);
//
//        RadioGroup radioGroup=(RadioGroup) findViewById(R.id.wake_up);
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//        RadioButton radioButton = (RadioButton) findViewById(selectedId);
//        String wake_up=radioButton.getText().toString();
//        Log.e("wake up set", wake_up);
//        resultAlarm.setWake_up_method(wake_up);
//
//        int id=SetAlarm.getAlarms().size()+1;
//        resultAlarm.setAlarmid(id);
//
//        String tag=((EditText) findViewById(R.id.tag)).getText().toString();
//        resultAlarm.setTag(tag);
//        return resultAlarm;
//    }

}
