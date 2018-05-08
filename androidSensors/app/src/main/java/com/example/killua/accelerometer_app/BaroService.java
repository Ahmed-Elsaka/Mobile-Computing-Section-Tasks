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

public class BaroService extends Service implements SensorEventListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private SensorManager sensorManager=null;
    private Sensor parometer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // create sensor manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer using sensor manager
        parometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        //create register sensor to lisen to this serve and speacially accelerometer
        sensorManager.registerListener(this,parometer,sensorManager.SENSOR_DELAY_NORMAL);

        WriteInMemory.Save("parometer", "timestamp,Value",this);



        return START_STICKY;//you have to stop the serve with your self

    }

    public float lastP = 0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float pressure =   sensorEvent.values[0];
        if( pressure-lastP >0.1){
            lastP = pressure;
            String result = Long.toString(sensorEvent.timestamp);
            WriteInMemory.Save("parometer",result+","+pressure,this);
            sendBroadcastMessage(pressure);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "iam on onSensorChanged", Toast.LENGTH_LONG).show();
        sensorManager.unregisterListener(this);
        sensorManager = null;
        super.onDestroy();
        stopSelf();

    }

    public static final String
            ACTION_BARO_BROADCAST = BaroService.class.getName() + "BaroService",
            X_VALUE = "x_value";


    private void sendBroadcastMessage(float x) {

        Intent intent = new Intent(ACTION_BARO_BROADCAST);
        intent.putExtra(X_VALUE, x);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
