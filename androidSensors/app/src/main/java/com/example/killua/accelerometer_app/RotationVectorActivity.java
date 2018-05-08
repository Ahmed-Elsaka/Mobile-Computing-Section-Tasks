package com.example.killua.accelerometer_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RotationVectorActivity extends AppCompatActivity {


    Button save,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_vector);


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
                        float X = intent.getFloatExtra(RotationVectorService.X_VALUE, 0);
                        float Y = intent.getFloatExtra(RotationVectorService.Y_VALUE, 0);
                        float Z = intent.getFloatExtra(RotationVectorService.Z_VALUE, 0);
                        xtextview.setText( X +"" );
                        ytextview.setText( Y +"" );
                        ztextview.setText( Z +"" );

                        if(Z > 45) {
                            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                        } else if(Z < -45) {
                            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                        } else if(Math.abs(Z) < 10) {
                            getWindow().getDecorView().setBackgroundColor(Color.CYAN);
                        }
                    }
                }, new IntentFilter(RotationVectorService.ACTION_RV_BROADCAST)
        );

    }


    public void saveOnclick(View view){

        //create an intent
        Intent SaveService = new Intent(getApplicationContext(),RotationVectorService.class);
        //Toast.makeText(this, "iam in saveOnclicButtonAction", Toast.LENGTH_LONG).show();
        //start service
        this.startService(SaveService);
        //disable save button
        save.setEnabled(false);
        stop.setEnabled(true);



    }

    public void stopOnclick(View view){
        //create an intent
        this.stopService(new Intent(getApplicationContext(),RotationVectorService.class));
        //change buttons state
        save.setEnabled(true);
        stop.setEnabled(false);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(getApplicationContext(),RotationVectorService.class));

    }







}
