package com.example.killua.accelerometer_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GyroActivity extends AppCompatActivity {

    Button save,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro);

        save = (Button)findViewById(R.id.btnSave);
        stop = (Button)findViewById(R.id.btnStop);

        final TextView pitchtextview = (TextView)findViewById(R.id.ptxtview);
        final TextView rolltextview = (TextView)findViewById(R.id.rtxtview);
        final TextView yawtextview = (TextView)findViewById(R.id.ytxtview);

        //listner to broad cast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        float pitch = intent.getFloatExtra(GyroService.PITCH_VALUE, 0);
                        float roll = intent.getFloatExtra(GyroService.ROLL_VALUE, 0);
                        float yaw = intent.getFloatExtra(GyroService.YAW_VALUE, 0);
                        pitchtextview.setText( pitch +"" );
                        rolltextview.setText( roll +"" );
                        yawtextview.setText( yaw +"" );

                        if(yaw < 0.5 && yaw > -0.5){
                            getWindow().getDecorView().setBackgroundColor(Color.CYAN);
                        }
                        else if(yaw > 0.5f) { // anticlockwise
                            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                        } else if(yaw < -0.5f) { // clockwise
                            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                        }
                    }
                }, new IntentFilter(GyroService.ACTION_GYRO_BROADCAST)
        );
    }



    public void saveOnclick(View view){
        //create an intent
        Intent gyroService = new Intent(getApplicationContext(),GyroService.class);
        //start service
        this.startService(gyroService);
        //disable save button
        save.setEnabled(false);
        stop.setEnabled(true);

    }

    public void stopOnclick(View view){
        //create an intent
        this.stopService(new Intent(getApplicationContext(),GyroService.class));
        //change buttons state
        save.setEnabled(true);
        stop.setEnabled(false);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(getApplicationContext(),GyroService.class));

    }


}
