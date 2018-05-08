package com.example.killua.task1_calculator_using_intent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCalculator(View view){
        // this function will execute when click on GoToCalculator Button
        Intent intent = new Intent(this,OpenCalcActivity.class);
        startActivity(intent);

    }
    public void goToLink(View view){

        goToUrl("http://www.google.com");

    }
    public void goToUrl(String url){
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

    }
    public void writeToFile(){
        //File path = context.getExternalFilesDir(null);


    }
}
