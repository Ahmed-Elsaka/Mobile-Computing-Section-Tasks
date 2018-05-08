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
import android.widget.Toast;

/**
 * Created by killua on 4/28/18.
 */

public class AccService extends Service implements SensorEventListener {

    //variables :
    private SensorManager sensorManager=null;
    private Sensor accelerometer = null;
    //x,y,z

    private float gravity[] =new float[3],linear_acceleration[] = new float[3];
    private float magnituteOfOldAcc = 0 ;
    private long lastUpdate = System.currentTimeMillis();





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "iam on StartCommand", Toast.LENGTH_LONG).show();
        //inital values
        magnituteOfOldAcc = 0 ;
        lastUpdate = System.currentTimeMillis();
        // create sensor manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer using sensor manager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //create register sensor to lisen to this serve and speacially accelerometer
        sensorManager.registerListener(this,accelerometer,sensorManager.SENSOR_DELAY_NORMAL);

        WriteInMemory.Save("Acceleromter", "X,Y,Z",this);



        return START_STICKY;//you have to stop the serve with your self

    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

         float alpha = (float)0.8;


        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

        linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
        linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
        linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

        sendBroadcastMessage(linear_acceleration[0],linear_acceleration[1],linear_acceleration[2]);

        // view the values in activity :


        //get the magnitute of current acceleration
        float magnituteOfCurrentAcc = (float) Math.sqrt((double) ((linear_acceleration[0]*linear_acceleration[0])
                                                                  +(linear_acceleration[1]* linear_acceleration[1])
                                                                    +(linear_acceleration[2]* linear_acceleration[2])));
        //get the difference in magnitude
        float differenceInMagnitute = magnituteOfCurrentAcc - magnituteOfOldAcc;
        magnituteOfOldAcc = magnituteOfCurrentAcc;


        //get the difference in time



        float differentTime  = System.currentTimeMillis() - lastUpdate;



        if(Math.abs(differenceInMagnitute)>1.5 && differentTime >150){
            lastUpdate = System.currentTimeMillis();
            String result = Float.toString(linear_acceleration[0]) +" , " +
                    Float.toString(linear_acceleration[1]) +" , " +
                    Float.toString(linear_acceleration[2]);


            Toast.makeText(this, "X= "+Float.toString(linear_acceleration[0]) +" | "+
                    "Y= "+Float.toString(linear_acceleration[1] )+ " | "+
                    "Z= "+Float.toString(linear_acceleration[2]), Toast.LENGTH_LONG).show();
            //here the result will be in the form x -- y -- z
            WriteInMemory.Save("Acceleromter",result,this);

        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //nothing will happen

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "iam on onSensorChanged", Toast.LENGTH_LONG).show();
        sensorManager.unregisterListener(this);
        sensorManager = null;
        super.onDestroy();
        stopSelf();

    }

    public static final String
            ACTION_ACC_BROADCAST = AccService.class.getName() + "AccService",
            X_VALUE = "x_value",
            Y_VALUE = "y_value",
            Z_VALUE ="z_value";


    private void sendBroadcastMessage(float x, float y,float z) {

            Intent intent = new Intent(ACTION_ACC_BROADCAST);
            intent.putExtra(X_VALUE, x);
            intent.putExtra(Y_VALUE, y);
            intent.putExtra(Z_VALUE, z);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
