package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccelerometerActivity extends AppCompatActivity {

    Button save,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        save = (Button)findViewById(R.id.btnSave);
        stop = (Button)findViewById(R.id.btnStop);

        final TextView xtextview = (TextView)findViewById(R.id.xtxtview);
        final TextView ytextview = (TextView)findViewById(R.id.ytxtview);
        final TextView ztextview = (TextView)findViewById(R.id.ztxtview);



        //listner to broad cast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        float X = intent.getFloatExtra(AccService.X_VALUE, 0);
                        float Y = intent.getFloatExtra(AccService.Y_VALUE, 0);
                        float Z = intent.getFloatExtra(AccService.Z_VALUE, 0);
                        xtextview.setText( X +"" );
                        ytextview.setText( Y +"" );
                        ztextview.setText( Z +"" );
                    }
                }, new IntentFilter(AccService.ACTION_ACC_BROADCAST)
        );
    }



    public void saveOnclick(View view){

        //create an intent
        Intent SaveService = new Intent(getApplicationContext(),AccService.class);
        Toast.makeText(this, "iam in saveOnclicButtonAction", Toast.LENGTH_LONG).show();
        //start service
        this.startService(SaveService);
        //disable save button
        save.setEnabled(false);
        stop.setEnabled(true);



    }

    public void stopOnclick(View view){
        //create an intent
        this.stopService(new Intent(getApplicationContext(),AccService.class));
        //change buttons state
        save.setEnabled(true);
        stop.setEnabled(false);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(getApplicationContext(),AccService.class));

    }

}
