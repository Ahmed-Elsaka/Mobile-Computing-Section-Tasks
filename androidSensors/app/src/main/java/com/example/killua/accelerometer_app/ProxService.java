package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.File;

/**
 * Created by killua on 5/7/18.
 */

public class ProxService extends Service implements SensorEventListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //variables :
    private SensorManager sensorManager=null;
    private Sensor proximity = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


       // Toast.makeText(this, "iam on StartCommand", Toast.LENGTH_LONG).show();
        // create sensor manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer using sensor manager
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //create register sensor to lisen to this serve and speacially accelerometer
        sensorManager.registerListener(this,proximity,sensorManager.SENSOR_DELAY_NORMAL);

        WriteInMemory.Save("proximitySensor", "distance",this);



        return START_STICKY;//you have to stop the serve with your self
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float distance = sensorEvent.values[0];
        WriteInMemory.Save("proximitySensor", Float.toString(distance),this);
      //  Toast.makeText(this, Float.toString(distance), Toast.LENGTH_LONG).show();
        sendBroadcastMessage(distance);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {
       // Toast.makeText(this, "iam on onSensorChanged", Toast.LENGTH_LONG).show();
        sensorManager.unregisterListener(this);
        sensorManager = null;
        super.onDestroy();
        stopSelf();

    }

    public static final String
            ACTION_PROX_BROADCAST = ProxService.class.getName() + "ProxService",
            X_VALUE = "x_value";


    private void sendBroadcastMessage(float x) {

        Intent intent = new Intent(ACTION_PROX_BROADCAST);
        intent.putExtra(X_VALUE, x);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
