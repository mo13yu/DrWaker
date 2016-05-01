package yunjingl.cmu.edu.drwaker.ui;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.adapter.SetLocation;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    protected static final String TAG = "LocationActivity";
    protected static final String LOCATION_REQUESTED_KEY = "location-request-pending";
    protected static final String LOCATION_LAT_KEY = "location-lat";
    protected static final String LOCATION_LNG_KEY = "location-lng";

    private String newAddress;
    private String newTag;
    protected boolean mLocationRequested;
    protected LatLng mLocationOutput;
    private LocationResultReceiver mResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Set up "OK" button
        Button findloc = (Button)findViewById(R.id.button_findloc);
        findloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                // get user input address and tag
                newAddress = ((EditText) findViewById(R.id.text_findloc)).getText().toString();
                newTag = ((EditText) findViewById(R.id.text_loctag)).getText().toString();
                Log.i(TAG, "User Input: " + newAddress);

                // translate address into LatLng
                fetchLocationButtonHandler();
            }
        });

        // Set up "Save" button
        Button saveloc = (Button)findViewById(R.id.button_saveloc);
        saveloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                SetLocation locations = new SetLocation();
                if (mLocationOutput!=null) {
                    String tag = ((EditText) findViewById(R.id.text_loctag)).getText().toString();
                    //TODO: maybe change it to custom exception handling?
                    if (tag.isEmpty()) {
                        tag = "Location " + Integer.toString(locations.getAvailableID());
                    }
                    locations.createLocation(mLocationOutput, tag);
                }
                //Intent intent = new Intent(getApplicationContext(), Settings.class);
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        // Set up Google Maps
        if (findViewById(R.id.map_frame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment for map
            SupportMapFragment mapFragment = new SupportMapFragment();
            // Add new fragment to the FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.map_frame, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }

        // Build a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Set up LocationResultReceiver
        // Set defaults, then update using values stored in the Bundle
        mResultReceiver = new LocationResultReceiver(new Handler());
        mLocationRequested = false;
        mLocationOutput = null;
        updateValuesFromBundle(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("GoogleMap", "Map Ready");
    }


    /**
     * GoogleApiClient
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        /*
        //TODO: this was for testing get user current location
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                String mLatitudeText = String.valueOf(mLastLocation.getLatitude());
                String mLongitudeText = String.valueOf(mLastLocation.getLongitude());
                System.out.printf("Last Location: (%s, %s)\n", mLatitudeText, mLongitudeText);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        */
        // For fetching location
        if (newAddress != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No Geocoder Available", Toast.LENGTH_LONG).show();
                return;
            }
            // If the user pressed "OK" before GoogleApiClient was able to connect, need to start the intent service here
            // since fetchLocationButtonHandler() wouldn't start intent service until GooglApiClient connects
            if (mLocationRequested) {
                startIntentService();
            }
        }
    }


    /*  FetchLocationIntentService  */

    /**
     * Start the service to fetch location if GoogleApiClient is connected
     */
    public void fetchLocationButtonHandler() {
        if (mGoogleApiClient.isConnected() && newAddress != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, set mLocationRequested to "true"
        // So when GoogleApiClient connects, onConnected() will start the intent service
        mLocationRequested = true;
    }

    /**
     * Start the intent service for fetching location based on an address
     */
    protected void startIntentService() {
        Intent intent = new Intent(getApplicationContext(), FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, newAddress);
        startService(intent);
    }

    /**
     * Receiver for data sent from FetchLocationIntentService.
     */
    public class LocationResultReceiver extends ResultReceiver {

        public LocationResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchLocationIntentService
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String message = resultData.getString(Constants.RESULT_MESSAGE_KEY);
            Log.i(TAG, "RECEIVED: " + message);

            // Get location LatLng from result
            if (resultCode == Constants.SUCCESS_RESULT) {
                mLocationOutput = new LatLng(resultData.getDouble(Constants.RESULT_LAT_DATA_KEY),
                        resultData.getDouble(Constants.RESULT_LNG_DATA_KEY));
                System.out.printf("RESULT: (%f, %f)\n", mLocationOutput.latitude, mLocationOutput.longitude);

                System.out.printf("New Address: %s\n", newAddress);
                System.out.printf("New LatLng: (%f, %f)\n", mLocationOutput.latitude, mLocationOutput.longitude);
                System.out.printf("New Tag: %s\n", newTag);
                mMap.addMarker(new MarkerOptions().position(mLocationOutput).title(newTag));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocationOutput));
                //TODO: zoom in
            }

            // Reset
            mLocationRequested = false;
        }
    }

    /**
     * Update fields based on data stored in the bundle
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the location was previously requested
            if (savedInstanceState.keySet().contains(LOCATION_REQUESTED_KEY)) {
                mLocationRequested = savedInstanceState.getBoolean(LOCATION_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location was previously found and stored in the Bundle
            if (savedInstanceState.keySet().contains(LOCATION_LAT_KEY) && savedInstanceState.keySet().contains(LOCATION_LNG_KEY)) {
                mLocationOutput = new LatLng(savedInstanceState.getDouble(LOCATION_LAT_KEY),
                                                savedInstanceState.getDouble(LOCATION_LNG_KEY));
                System.out.printf("updateValueFromBundle - New Location: (%f, %f)\n", mLocationOutput);
            }
        }
    }

    /**
     * Save dynamic instance states in LocationActivity into the given Bundle
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the location has been requested
        savedInstanceState.putBoolean(LOCATION_REQUESTED_KEY, mLocationRequested);

        // Save the location
        savedInstanceState.putDouble(LOCATION_LAT_KEY, mLocationOutput.latitude);
        savedInstanceState.putDouble(LOCATION_LNG_KEY, mLocationOutput.latitude);
        super.onSaveInstanceState(savedInstanceState);
    }

}
