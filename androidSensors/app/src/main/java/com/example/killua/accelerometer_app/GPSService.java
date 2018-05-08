package com.example.killua.accelerometer_app;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by killua on 5/7/18.
 */

public class GPSService extends Service implements LocationListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "you need permissions from the user", Toast.LENGTH_LONG).show();
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }


        WriteInMemory.Save("GPS","timestamp,lat,lon,alt",this);

        return START_STICKY;//you have to stop the serve with your self

    }
    @Override
    public void onLocationChanged(Location location) {

        float lat = (float) location.getLatitude();
        float lon = (float) location.getLongitude();
        float alt = (float) location.getAltitude();

        sendBroadcastMessage(lat,lon,alt);

        WriteInMemory.Save("GPS",location.getTime()+","+lat+","+lon+","+alt,this);
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static final String
            ACTION_GPS_BROADCAST = AccService.class.getName() + "GPSService",
            X_VALUE = "x_value",
            Y_VALUE = "y_value",
            Z_VALUE ="z_value";


    private void sendBroadcastMessage(float x, float y,float z) {

        Intent intent = new Intent(ACTION_GPS_BROADCAST);
        intent.putExtra(X_VALUE, x);
        intent.putExtra(Y_VALUE, y);
        intent.putExtra(Z_VALUE, z);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        locationManager = null;
        super.onDestroy();
    }
}
