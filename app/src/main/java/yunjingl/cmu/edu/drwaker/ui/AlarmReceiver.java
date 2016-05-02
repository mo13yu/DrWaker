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
 * Created by yunjing on 4/22/16.
 */
public class AlarmReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private Intent intent;
    protected GoogleApiClient mGoogleApiClient;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.intent = intent;

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

        // Get info from Intent
        String temp = intent.getExtras().getString("extra");
        String method = intent.getExtras().getString("wake_up_method");
        String ringtone = intent.getExtras().getString("ring_tone");
        boolean loc_switch = intent.getExtras().getBoolean("loc_switch");
        Log.e("location switch", String.valueOf(loc_switch));

        // If alarm's location service is enabled,
        // need to compare user location with alarm location to determine if alarm should be sounded
        if (loc_switch) {
            // Get alarm location
            //TODO: might have empty location, will have error trying to read latlng values
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
                Intent ring_intent = new Intent(context, RingtonePlayingService.class);
                ring_intent.putExtra("extra", temp);
                ring_intent.putExtra("ring_tone", ringtone);
                context.startService(ring_intent);

                Intent stopintent = new Intent();
                // Math Calculation
                if (method.equals("math")) {
                    String question = intent.getExtras().getString("question");
                    String answer = intent.getExtras().getString("answer");
                    stopintent = new Intent(context, MathActivity.class);
                    stopintent.putExtra("question", question);
                    stopintent.putExtra("answer", answer);
                }
                // Facial Recognization
                else if (method.equals("facial")) {
                    stopintent = new Intent(context, SelfieActivity.class);
                }
                stopintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(stopintent);
            } else {
                Log.e("CompareLocation", "Disable alarm due to location service");
            }
        }

        // If alarm's location service is disabled, sound the alarm no matter what
        else {
            Intent ring_intent = new Intent(context, RingtonePlayingService.class);
            ring_intent.putExtra("extra", temp);
            ring_intent.putExtra("ring_tone", ringtone);
            //ring_intent.putExtra("loc_switch", loc_switch);
            context.startService(ring_intent);

            Intent stopintent = new Intent();
            // Math Calculation
            if (method.equals("math")) {
                String question = intent.getExtras().getString("question");
                String answer = intent.getExtras().getString("answer");
                stopintent = new Intent(context, MathActivity.class);
                stopintent.putExtra("question", question);
                stopintent.putExtra("answer", answer);
            }
            // Facial Recognization
            else if (method.equals("facial")) {
                stopintent = new Intent(context, SelfieActivity.class);
            }
            stopintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(stopintent);
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
