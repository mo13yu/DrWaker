package yunjingl.cmu.edu.drwaker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import yunjingl.cmu.edu.drwaker.R;

public class SetLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Set up "OK" button
        Button findloc =(Button)findViewById(R.id.button_findloc);
        findloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Log.d("map", ((EditText)findViewById(R.id.text_findloc)).getText().toString());
            }
        });

        // Set up "Save" button
        Button saveloc  =(Button)findViewById(R.id.button_saveloc);
        saveloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
