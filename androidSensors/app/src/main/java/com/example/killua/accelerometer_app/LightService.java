package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by killua on 5/7/18.
 */

public class LightService extends Service implements SensorEventListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private SensorManager sensorManager=null;
    private Sensor light = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // create sensor manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer using sensor manager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //create register sensor to lisen to this serve and speacially accelerometer
        sensorManager.registerListener(this,light,sensorManager.SENSOR_DELAY_NORMAL);


        WriteInMemory.Save("Light", "timestamp,Value",this);



        return START_STICKY;//you have to stop the serve with your self

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float LightValue = sensorEvent.values[0];
        sendBroadcastMessage(LightValue);
        WriteInMemory.Save("Light", Long.toString(sensorEvent.timestamp)+","+Float.toString(LightValue),this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        sensorManager = null;
        super.onDestroy();
        //SystemClock.sleep(1000);
        stopSelf();
    }

    public static final String
            ACTION_LIGHT_BROADCAST = AccService.class.getName() + "LightService",
            X_VALUE = "x_value";


    private void sendBroadcastMessage(float x) {

        Intent intent = new Intent(ACTION_LIGHT_BROADCAST);
        intent.putExtra(X_VALUE, x);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
