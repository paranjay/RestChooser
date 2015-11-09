package edu.osu.restchooser;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alcoa on 11/9/2015.
 */
public class GPSLocation extends Activity {

    private LatLng gpsLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new myLocationListener();
        checkSelfPermission(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    class myLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            if (location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                gpsLocation = new LatLng(latitude, longitude);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }



}
