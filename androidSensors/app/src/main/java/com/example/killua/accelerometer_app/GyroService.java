package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;

/**
 * Created by killua on 5/6/18.
 */

public class GyroService extends Service implements SensorEventListener {
    //declare vars
    private SensorManager mSensorManager = null;
    private Sensor mGyrosensor = null;



    long lastUpdate = System.currentTimeMillis();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyrosensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mGyrosensor, SensorManager.SENSOR_DELAY_NORMAL);

        WriteInMemory.Save("Gyroscope", "Pitch,Roll,Yaw,Magnitude",this);


        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long timestamp = sensorEvent.timestamp;
        float dt = timestamp - lastUpdate;
        lastUpdate = timestamp;

        float pitch = sensorEvent.values[0];
        float roll = sensorEvent.values[1];
        float yaw = sensorEvent.values[2];

        /*
        float roraX = sensorEvent.values[0];        //rate of rotation around x
        float roraY = sensorEvent.values[1];         //rate of rotation around y
        float roraZ = sensorEvent.values[2];          //rate of rotation around z


        pitch = pitch + roraX*dt;
        roll = roll + roraY*dt;
        yaw = yaw + roraZ*dt;

        */


        long timestmp = sensorEvent.timestamp;
        float Magnitude = (float) Math.sqrt((double) (pitch * pitch + roll * roll + yaw * yaw));

        sendBroadcastMessage(pitch,roll, yaw);

        if ((System.currentTimeMillis() - lastUpdate)>100) {              //intervals to log readings
            lastUpdate = System.currentTimeMillis();
            String result= String.format(" %f,  %f, %f, %f", pitch, roll, yaw, Magnitude);
            WriteInMemory.Save("Gyroscope",result,this);

        }





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        mSensorManager = null;
        super.onDestroy();
        stopSelf();
    }

    public static final String
            ACTION_GYRO_BROADCAST = GyroService.class.getName() + "GyroService",
            PITCH_VALUE = "x_value",
            ROLL_VALUE = "y_value",
            YAW_VALUE ="z_value";

    private void sendBroadcastMessage(float x, float y,float z) {

        Intent intent = new Intent(ACTION_GYRO_BROADCAST);
        intent.putExtra(PITCH_VALUE, x);
        intent.putExtra(ROLL_VALUE, y);
        intent.putExtra(YAW_VALUE, z);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
