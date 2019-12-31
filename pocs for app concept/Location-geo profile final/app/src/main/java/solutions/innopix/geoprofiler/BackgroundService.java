package solutions.innopix.geoprofiler;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
public class BackgroundService extends Service {
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private StringBuilder stringBuilder;
    private boolean isGPS = false;

    static void getLocation() {
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        LatLngBounds.Builder builder;
                        builder = new LatLngBounds.Builder();
                        //p3
                        builder.include(new LatLng(18.5339174, 73.8277577));
                        builder.include(new LatLng(18.5339147, 73.8277522));
                        builder.include(new LatLng(18.5339144, 73.8277514));
                        builder.include(new LatLng(18.5339145, 73.827751));
                        builder.include(new LatLng(18.5339186, 73.8277517));
                        builder.include(new LatLng(18.5339209, 73.8277563));
                        builder.include(new LatLng(18.5339203, 73.8277586));
                        builder.include(new LatLng(18.5339174, 73.8277576));
                        builder.include(new LatLng(18.5339157, 73.8277657));
                        builder.include(new LatLng(18.5339174, 73.8277577));

                            /*73.8277844, 18.5338721
                            73.8277800, 18.5338747
                            73.8277700, 18.5338735
                            73.8277716, 18.5338677
                            73.8277770, 18.5338594
                            73.8277864, 18.5338670
                            73.8277844, 18.5338721*/
                       // LatLngBounds bound = builder.build();
                        LatLngBounds bound =toBounds(new LatLng(18.5338727, 73.8277765),0.001);
                        stringBuilder.append(wayLatitude);
                        stringBuilder.append("-");
                        stringBuilder.append(wayLongitude);
                        stringBuilder.append("\n\n" + bound.contains(new LatLng(wayLatitude, wayLongitude)) + "\n\n");
                        Toast.makeText(BackgroundService.this, wayLatitude + "," + wayLongitude + "" + bound.contains(new LatLng(wayLatitude, wayLongitude)), Toast.LENGTH_SHORT).show();
                        AudioManager myAudioManager;
                        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        if (bound.contains(new LatLng(wayLatitude, wayLongitude))) {
                            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        } else {
                            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        if (!isGPS) {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        stringBuilder = new StringBuilder();
        getLocation();
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
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}