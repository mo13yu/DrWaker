package yunjingl.cmu.edu.drwaker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.adapter.SetAlarm;
import yunjingl.cmu.edu.drwaker.adapter.SetLocation;

/**
 * set all info when add a new alarm or edit an alarm
 */
public class Settings extends AppCompatActivity {

    MediaPlayer ring;
    String ringtone;
    int inputhour;
    int inputminute;
    String wake_up_method;
    String tag;
    String locationtag;
    boolean locationswitch;
    SetAlarm setAlarm = new SetAlarm();

    @Override
    protected void onResume() {
        super.onResume();
        Spinner location = (Spinner) findViewById(R.id.location);
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

        final String method = (getIntent().getExtras()).getString("create_or_update");
        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.wake_up);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                wake_up_method = radioButton.getText().toString();
                tag = ((EditText) findViewById(R.id.tag)).getText().toString();

                Switch loc_switch = (Switch) findViewById(R.id.locswitch);
                locationswitch = loc_switch.isChecked();
                if (locationtag == null && locationswitch) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setTitle("Attention");
                    builder.setMessage("You need to add a new location");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
                    builder.show();
                }else{
                    if (method.equals("create")) {
                        setAlarm.createAlarm(inputhour, inputminute, locationtag, locationswitch, wake_up_method, tag, ringtone);
                    } else if (method.equals("update")) {
                        int alarmid = Integer.parseInt((getIntent().getExtras()).getString("alarmid"));
                        setAlarm.updateAlarm(alarmid, inputhour, inputminute, locationtag, locationswitch, wake_up_method, tag, ringtone);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

/**
 * hour, minute, ring tone drop down list.
 */
        Spinner hour = (Spinner) findViewById(R.id.hour);
        Spinner minute = (Spinner) findViewById(R.id.minute);
        hour.setAdapter(ArrayAdapter.createFromResource(this, R.array.hour, android.R.layout.simple_spinner_item));
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
        Spinner tune = (Spinner) findViewById(R.id.tune);
        tune.setAdapter(ArrayAdapter.createFromResource(this, R.array.tune, android.R.layout.simple_spinner_item));
        tune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myview = (TextView) view;
                if (myview != null) {
                    ringtone = myview.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner location = (Spinner) findViewById(R.id.location);
        SetLocation setLocation = new SetLocation();
        setLocation.initializeLocations();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, setLocation.getAllLocations());
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
        Button testring = (Button) findViewById(R.id.testbutton);
        testring.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {

                if (ringtone.equals("Kiss the rain")) {
                    ring = MediaPlayer.create(getApplicationContext(), R.raw.kiss_the_rain);
                    ring.start();
                } else if (ringtone.equals("River that flows in you")) {
                    ring = MediaPlayer.create(getApplicationContext(), R.raw.river);
                    ring.start();
                } else if (ringtone.equals("I just wanna run")) {
                    ring = MediaPlayer.create(getApplicationContext(), R.raw.i_just_wanna_run);
                    ring.start();
                }

            }
        });
        Button stoptest = (Button) findViewById(R.id.stoptest);
        stoptest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                ring.stop();

            }
        });

        Button newloc = (Button) findViewById(R.id.newlocbutton);
        newloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        });
    }

}
