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

public class LightActivity extends AppCompatActivity {


    Button save,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);


        save = (Button)findViewById(R.id.btnSave);
        stop = (Button)findViewById(R.id.btnStop);

        final TextView xtextview = (TextView)findViewById(R.id.xtxtview);




        //listner to broad cast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        float X = intent.getFloatExtra(LightService.X_VALUE, 0);

                        xtextview.setText( X +"" );
                        if(X > 80){
                            getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                        }else if(X > 70){
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                        }else if(X > 60){
                            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                        }else if(X > 50){
                            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                        }
                        else if(X > 30){
                            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                        }else if(X > 20){
                            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                        }else if(X > 10){
                            getWindow().getDecorView().setBackgroundColor(Color.CYAN);
                        }

                    }
                }, new IntentFilter(LightService.ACTION_LIGHT_BROADCAST)
        );
    }


    public void saveOnclick(View view){

        //create an intent
        Intent SaveService = new Intent(getApplicationContext(),LightService.class);
        //Toast.makeText(this, "iam in saveOnclicButtonAction", Toast.LENGTH_LONG).show();
        //start service
        this.startService(SaveService);
        //disable save button
        save.setEnabled(false);
        stop.setEnabled(true);



    }

    public void stopOnclick(View view){
        //create an intent
        this.stopService(new Intent(getApplicationContext(),LightService.class));
        //change buttons state
        save.setEnabled(true);
        stop.setEnabled(false);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(getApplicationContext(),LightService.class));

    }

}
