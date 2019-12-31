package solutions.innopix.geoprofiler;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.coders.location.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class MainActivity extends AppCompatActivity {


    private android.widget.Button btnContinueLocation;

    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnContinueLocation = findViewById(R.id.btnContinueLocation);

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        btnContinueLocation.setOnClickListener(v -> {
            if (!isGPS) {
                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                return;
            }

            final RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION).subscribe((new Consumer() {
                public void accept(Object accept) {
                    accept((Boolean) accept);
                }

                final void accept(Boolean granted) {
                    if (granted) {
                        Intent intent = new Intent(getApplicationContext(),  AlarmReceiver.class);
                        // Create a PendingIntent to be triggered when the alarm goes off
                        final PendingIntent pIntent = PendingIntent.getBroadcast(MainActivity.this, AlarmReceiver.REQUEST_CODE,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        // Setup periodic alarm every every half hour from this point onwards
                        long firstMillis = System.currentTimeMillis(); // alarm is set right away
                        AlarmManager alarm = (AlarmManager)  getSystemService(Context.ALARM_SERVICE);
                        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
                        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
                        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                               10*1000, pIntent);
                        Intent background = new Intent(MainActivity.this, BackgroundService.class);

                        startService(background);
                    } else {
                        Toast.makeText(MainActivity.this, "not allowed", Toast.LENGTH_LONG).show();
                    }
                }
            }));


        });
    }
    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }
}