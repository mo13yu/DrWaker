package yunjingl.cmu.edu.drwaker.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.adapter.SetAlarm;
import yunjingl.cmu.edu.drwaker.adapter.SetLocation;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    private AlarmManager mgrAlarm;
    ArrayList<PendingIntent> pendingIntents;
    ArrayList<Calendar> calendars;

    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                intent.putExtra("create_or_update", "create");
                startActivity(intent);
            }
        });

        container = (LinearLayout) findViewById(R.id.container);

        initializeAlarms();
    }

    public void initializeAlarms() {
        new SetLocation().setContext(MainActivity.this);
        new SetLocation().initializeLocations();
        new SetAlarm().setContext(MainActivity.this);
        new SetAlarm().initializeAlarms();

        calendars = new ArrayList<Calendar>();
        Iterator<Integer> itr = new SetAlarm().getIdSet().iterator();
        while (itr.hasNext()) {

            final int thisid = itr.next();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour_sys = calendar.get(Calendar.HOUR_OF_DAY);
            int minute_sys = calendar.get(Calendar.MINUTE);
            int hour = new SetAlarm().getHour(thisid);
            int minute = new SetAlarm().getMinute(thisid);
            if (hour < hour_sys || (hour == hour_sys && minute < minute_sys)) {
                calendar.set(Calendar.DAY_OF_MONTH, day + 1);
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, day);
            }
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            calendars.add(calendar);

            String alarm = "";
            if (hour < 10) {
                alarm += "0" + hour;
            } else {
                alarm += hour;
            }
            alarm += " : ";
            if (minute < 10) {
                alarm += "0" + minute;
            } else {
                alarm += minute;
            }

            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.row, null);
            TextView textOut = (TextView) addView.findViewById(R.id.textout);
            textOut.setText(alarm);
            textOut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) addView.getParent()).removeView(addView);
                    new SetAlarm().deleteAlarm(thisid);
                }
            });
            Button buttonEdit = (Button) addView.findViewById(R.id.edit);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    intent.putExtra("create_or_update", "update");
                    intent.putExtra("alarmid", thisid + "");
                    startActivity(intent);
                }
            });
            container.addView(addView);
        }


        mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntents = new ArrayList<PendingIntent>();
        final Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        itr = new SetAlarm().getIdSet().iterator();
        int i = 0;
        while (itr.hasNext()) {
            int thisid = itr.next();
            myIntent.putExtra("extra", "on");
            String wake_up = new SetAlarm().getWakUpMethod(thisid);
            if (wake_up.equals("Math Calculation")) {
                myIntent.putExtra("wake_up_method", "math");
                myIntent.putExtra("question", new SetAlarm().getMathQuestion(thisid));
                myIntent.putExtra("answer", new SetAlarm().getMathAnswer(thisid));
            } else if (wake_up.equals("Facial Recognization")) {
                myIntent.putExtra("wake_up_method", "facial");
            }
            myIntent.putExtra("ring_tone", new SetAlarm().getTone(thisid));
            myIntent.putExtra("loc_switch", new SetAlarm().isLocationSwitchOn(thisid));
            if (new SetAlarm().isLocationSwitchOn(thisid)) {
                myIntent.putExtra("loc_la", new SetAlarm().getLatitude(thisid));
                myIntent.putExtra("loc_lo", new SetAlarm().getLongitude(thisid));
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, thisid, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                    calendars.get(i).getTimeInMillis(),
                    pendingIntent);

            pendingIntents.add(pendingIntent);
            i++;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
