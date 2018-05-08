package com.example.killua.accelerometer_app;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
            "/ElsakaSensors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create dir
        File dir = new File(path);
            dir.mkdir();

           // WriteInMemory.Save("khaled","ahmed elsaka",this);




    }

    // to to acc tab
    public void  onClick_acc(View view){
        Intent intent = new Intent(this , AccelerometerActivity.class);
        startActivity(intent);

    }
    // to to Gyro tab
    public void  onClick_gyro(View view){
        Intent intent = new Intent(this , GyroActivity.class);
        startActivity(intent);

    }

    public void RVSonClick(View view) {

        Intent intent = new Intent(this, RotationVectorActivity.class);
        startActivity(intent);
    }

    public void POSonClick(View view) {

        Intent intent = new Intent(this, ProxActivity.class);
        startActivity(intent);
    }



    public void LightonClick(View view) {

        Intent intent = new Intent(this, LightActivity.class);
        startActivity(intent);
    }

    public void BaroonClick(View view) {

        Intent intent = new Intent(this, BaroActivity.class);
        startActivity(intent);
    }

    public void GPSonClick(View view) {

        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }



}
