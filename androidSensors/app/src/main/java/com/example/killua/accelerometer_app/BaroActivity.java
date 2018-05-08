package com.example.killua.accelerometer_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BaroActivity extends AppCompatActivity {

    Button save,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baro);

        save = (Button)findViewById(R.id.btnSave);
        stop = (Button)findViewById(R.id.btnStop);

        final TextView xtextview = (TextView)findViewById(R.id.xtxtview);




        //listner to broad cast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        float X = intent.getFloatExtra(BaroService.X_VALUE, 0);
                        xtextview.setText( X +"" );

                    }
                }, new IntentFilter(BaroService.ACTION_BARO_BROADCAST)
        );
    }



    public void saveOnclick(View view){

        //create an intent
        Intent SaveService = new Intent(getApplicationContext(),BaroService.class);
        //start service
        this.startService(SaveService);
        //disable save button
        save.setEnabled(false);
        stop.setEnabled(true);



    }

    public void stopOnclick(View view){
        //create an intent
        this.stopService(new Intent(getApplicationContext(),BaroService.class));
        //change buttons state
        save.setEnabled(true);
        stop.setEnabled(false);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(getApplicationContext(),BaroService.class));

    }


}
