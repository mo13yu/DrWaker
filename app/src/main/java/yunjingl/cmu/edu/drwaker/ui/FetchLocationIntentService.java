package yunjingl.cmu.edu.drwaker.ui;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * IntentService that will receiver the request of looking up LatLng from an address and send back the response
 */
public class FetchLocationIntentService extends IntentService {
    private static final String TAG = "FetchLocationIS";
    protected ResultReceiver mReceiver;


    public FetchLocationIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }


    /**
     * Handle intent and translate address to LatLng
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        // Get receiver
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get user input address
        String strAddress = intent.getStringExtra(Constants.LOCATION_DATA_EXTRA);

        // Make sure that the location data was really sent over through an extra
        // If it wasn't,send back an error message
        if (strAddress == null) {
            errorMessage = "No address was provided";
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }

        // Translate address into LatLng
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 5);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "The service is not available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid address values
            errorMessage = "Invalid address used";
            Log.e(TAG, errorMessage + ". " + "Address = " + strAddress, illegalArgumentException);
        }

        // Handle cases where no location was found
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No location found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            Log.i(TAG, "Location found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT, "SUCCESS", address.getLatitude(), address.getLongitude());
        }
    }

    /**
     * Send a resultCode and LatLng to the receiver
     */
    private void deliverResultToReceiver(int resultCode, String message, double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_MESSAGE_KEY, message);
        bundle.putDouble(Constants.RESULT_LAT_DATA_KEY, lat);
        bundle.putDouble(Constants.RESULT_LNG_DATA_KEY, lng);
        mReceiver.send(resultCode, bundle);
    }

    /**
     * Send a resultCode and message to the receiver
     */
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_MESSAGE_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
