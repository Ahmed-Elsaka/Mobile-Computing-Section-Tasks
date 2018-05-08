package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by killua on 5/7/18.
 */

public class RotationVectorService extends Service implements SensorEventListener {





   // private Sensor rotationvector = null;
    private Sensor accelerometer = null,compass=null,rotationvector =null,magnotemoter =null;

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private long lastUpdate = System.currentTimeMillis();;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }
    private Sensor mCompass = null;
    private Sensor mAccelerometer = null;
    private Sensor mMagnotemoter = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnotemoter = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, magnotemoter, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        WriteInMemory.Save("RotationAngel", "theta0,theta1,theta2",this);



        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if((System.currentTimeMillis() - lastUpdate) > 100){
            lastUpdate = System.currentTimeMillis();

            switch (sensorEvent.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                            0, accelerometerReading.length);
                    //Toast.makeText(this, "iam sensor changed", Toast.LENGTH_LONG).show();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                            0, magnetometerReading.length);
                    // Toast.makeText(this, "iam sensor changed 120", Toast.LENGTH_LONG).show();
                    break;
            }
            updateOrientationAngles(sensorEvent);
        }

    }

    public void updateOrientationAngles(SensorEvent sensorEvent) {

        // Update rotation matrix, which is needed to update orientation angles.
        sensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
        // "mRotationMatrix" now has up-to-date information.

        sensorManager.getOrientation(rotationMatrix, orientationAngles);

        for(int i = 0; i < 3; i++) {
            orientationAngles[i] = (float)(Math.toDegrees(orientationAngles[i]));
        }

        String result = Float.toString(orientationAngles[0]) +" , " +
                Float.toString(orientationAngles[1]) +" , " +
                Float.toString(orientationAngles[2]);



        sendBroadcastMessage(orientationAngles[0],orientationAngles[1],orientationAngles[2]);
         WriteInMemory.Save("RotationAngel",result,this);
    }


    public static final String
            ACTION_RV_BROADCAST = RotationVectorService.class.getName() + "RotationVectorService",
            X_VALUE = "x_value",
            Y_VALUE = "y_value",
            Z_VALUE ="z_value";


    private void sendBroadcastMessage(float x, float y,float z) {

        Intent intent = new Intent(ACTION_RV_BROADCAST);
        intent.putExtra(X_VALUE, x);
        intent.putExtra(Y_VALUE, y);
        intent.putExtra(Z_VALUE, z);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }




    @Override
    public void onDestroy() {
      //  Toast.makeText(this, "iam on onSensorChanged", Toast.LENGTH_LONG).show();
        sensorManager.unregisterListener(this);
        sensorManager = null;
        super.onDestroy();
        stopSelf();

    }



}
