package com.example.killua.task1_calculator_using_intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OpenCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_calc);
    }


    public void addTwoNumbers(View view){
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        int number1 = Integer.parseInt(editText1.getText().toString());
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        int number2 = Integer.parseInt(editText2.getText().toString());
        int result = number1 + number2 ;
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText("Result :" + result);


    }
}
