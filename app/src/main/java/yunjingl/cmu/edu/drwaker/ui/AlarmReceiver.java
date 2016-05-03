package yunjingl.cmu.edu.drwaker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * A receiver that will catch the Intent and process the information to sound the alarm accordingly
 */
public class AlarmReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private Intent intent;
    protected GoogleApiClient mGoogleApiClient;
    boolean loc_switch;
    String temp;
    String ringtone;
    String method;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.intent = intent;

        // Get alarm information
        loc_switch = intent.getExtras().getBoolean("loc_switch");
        temp = intent.getExtras().getString("extra");
        method = intent.getExtras().getString("wake_up_method");
        ringtone = intent.getExtras().getString("ring_tone");

        // If location service is disabled, ring no matter where the user is
        if (!loc_switch) {
            // Create new Intent and start service to sound the alarm
            Intent ring_intent = new Intent(context, RingtonePlayingService.class);
            ring_intent.putExtra("extra", temp);
            ring_intent.putExtra("ring_tone", ringtone);
            context.startService(ring_intent);

            // Create new Intent to stop the alarm after user has completed the task
            Intent stopintent = new Intent();
            // Math Calculation
            if (method.equals("math")) {
                String question = intent.getExtras().getString("question");
                String answer = intent.getExtras().getString("answer");
                stopintent = new Intent(context, MathActivity.class);
                stopintent.putExtra("question", question);
                stopintent.putExtra("answer", answer);
            }
            // Facial Recognition
            else if (method.equals("facial")) {
                stopintent = new Intent(context, SelfieActivity.class);
            }
            stopintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(stopintent);
        }

        // If location service is enabled, setup GoogleApiClient so can get user's last known location
        else {
            // Build a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
            if (mGoogleApiClient == null) {
                Log.d("nearLocation", "Build GoogleAiClient");
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            // Connect to Google Play services location API
            mGoogleApiClient.connect();

            // onConnected() will be called and set last known location to mLastLocation inside the function
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("GoogleApiClient", "onConnected()");

        Location mLastLocation = null;

        // Get last known location
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Disconnect from Google Play services location API
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        // Compare user location with alarm location to determine if alarm should be sound
        if (loc_switch) {
            // Get alarm location
            String la = intent.getExtras().getString("loc_la");
            String lo = intent.getExtras().getString("loc_lo");
            Location alarmLocation = new Location("alarm");
            alarmLocation.setLatitude(Double.parseDouble(la));
            alarmLocation.setLongitude(Double.parseDouble(lo));

            // Calculate distance between the two
            boolean result;
            if (mLastLocation != null) {
                float distance = mLastLocation.distanceTo(alarmLocation);
                Log.e("CompareLocation", "alarmLocation: (" + String.valueOf(alarmLocation.getLatitude()) + ", " + String.valueOf(alarmLocation.getLongitude()) + ")");
                Log.e("CompareLocation", "mLastLocation: (" + String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()) + ")");
                Log.e("CompareLocation", "Distance: " + String.valueOf(distance) + "m");

                // Determine result based on distance in meters
                if (distance < 600) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = true;
            }

            // Sound the alarm if user is near alarm location
            if (result) {
                // Create new Intent and start service to sound the alarm
                Intent ring_intent = new Intent(context, RingtonePlayingService.class);
                ring_intent.putExtra("extra", temp);
                ring_intent.putExtra("ring_tone", ringtone);
                context.startService(ring_intent);

                // Create new Intent to stop the alarm after user has completed the task
                Intent stopintent = new Intent();
                // Math Calculation
                if (method.equals("math")) {
                    String question = intent.getExtras().getString("question");
                    String answer = intent.getExtras().getString("answer");
                    stopintent = new Intent(context, MathActivity.class);
                    stopintent.putExtra("question", question);
                    stopintent.putExtra("answer", answer);
                }
                // Facial Recognition
                else if (method.equals("facial")) {
                    stopintent = new Intent(context, SelfieActivity.class);
                }
                stopintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(stopintent);
            } else {
                Log.e("CompareLocation", "Disable alarm due to location service");
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GoogleApiClient", "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GoogleApiClient", "oConnectionFailed: Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
