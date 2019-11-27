package com.abhijeet.gps_location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
public class MainActivity extends AppCompatActivity {
    private GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPSTracker(MainActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //p3
            builder.include(new LatLng(18.511563, 73.923075));
            builder.include(new LatLng(18.511481, 73.92403));
            builder.include(new LatLng(18.511115, 73.924245));
            builder.include(new LatLng(18.510967, 73.923059));
            //p4
          /*  builder.include(new LatLng(18.512046, 73.923129));
            builder.include(new LatLng(18.512046, 73.923633));
            builder.include(new LatLng(18.511593, 73.923612));
            builder.include(new LatLng(18.511634, 73.922995));*/
            LatLngBounds bound = builder.build();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            if (bound.contains(new LatLng(latitude, longitude))) {
                Toast.makeText(this,
                        "Location is Inside Campus",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Location is Outside Campus",
                        Toast.LENGTH_LONG).show();
            }
            // \n is for new line
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
