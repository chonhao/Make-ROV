package com.rov.pcms.make_rov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;


public class setup_BasicInformationActivity_2 extends ActionBarActivity {
//--------------Fixed Values----------------------------------------
    final private int MAX_SENSOR_NUM = 2;
    final private int MAX_OUTPUT_NUM = 4;
    final private String ROV_BASIC_INFORMATION = "rov-basic-information";
//    final private String MOTOR_NUM = "motor-num";
    final private String SENSOR_NUM = "sensor-num";
    final private String PAYLOAD_NUM = "payload-num";
//--------------UI components values----------------------------------
    private static FloatingActionButton previousFab;
    private static FloatingActionButton nextFab;

//    private static Button motorMinus;
//    private static Button motorPlus;
    private static Button sensorMinus;
    private static Button sensorPlus;
    private static Button payloadMinus;
    private static Button payloadPlus;
//    private static TextView motorNumTextView;
    private static TextView sensorNumTextView;
    private static TextView payloadNumTextView;
//------------------Other Values-----------------------------------
//    private static int motorNum = 0;
    private static int sensorNum = 0;
    private static int payloadNum = 0;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__basic_information_activity_2);
//----------------UI init------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        setTitle("Basic Information");
        nextFab = (FloatingActionButton)findViewById(R.id.nextFab);
        previousFab = (FloatingActionButton)findViewById(R.id.previousFab);

//        motorMinus = (Button)findViewById(R.id.motorMinus);
//        motorPlus = (Button)findViewById(R.id.motorPlus);
        sensorMinus = (Button)findViewById(R.id.sensorMinus);
        sensorPlus = (Button)findViewById(R.id.sensorPlus);
        payloadMinus = (Button)findViewById(R.id.payloadMinus);
        payloadPlus = (Button)findViewById(R.id.payloadPlus);
//        motorNumTextView = (TextView)findViewById(R.id.motorNumTextView);
        sensorNumTextView = (TextView)findViewById(R.id.sensorNumTextView);
        payloadNumTextView = (TextView)findViewById(R.id.payloadNumTextView);
//---------------Other init-------------------------------------
        sharedPreferences = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
//        motorNumTextView.setText(sharedPreferences.getString(MOTOR_NUM, "0"));
        sensorNumTextView.setText(sharedPreferences.getString(SENSOR_NUM, "0"));
        payloadNumTextView.setText(sharedPreferences.getString(PAYLOAD_NUM, "0"));
//----------------Components listeners------------------------------
        previousFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right_close,R.anim.right_to_left_close);
            }
        });
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(setup_BasicInformationActivity_2.this,setup_SensorInitialization.class));
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });
        //====================motor==================================
//        motorMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(motorNum>0)
//                    motorNum--;
//                updateAllNumText();
//            }
//        });
//        motorPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(motorNum+payloadNum<MAX_OUTPUT_NUM)
//                    motorNum++;
//                else
//                    showMaxToast("motorAndPayload");
//                updateAllNumText();
//            }
//        });
        //===================sensor===================================
        sensorMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensorNum>0)
                    sensorNum--;

                updateAllNumText();
            }
        });
        sensorPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensorNum<MAX_SENSOR_NUM)
                    sensorNum++;
                else
                    showMaxToast("sensor");
                updateAllNumText();
            }
        });
        //====================payloads============================
        payloadMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payloadNum>0)
                    payloadNum--;
                updateAllNumText();
            }
        });
        payloadPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0/*motorNum*/+payloadNum<MAX_OUTPUT_NUM)
                    payloadNum++;
                else
                    showMaxToast("motorAndPayload");
                updateAllNumText();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right_close,R.anim.right_to_left_close);
        moveTaskToBack(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup__basic_information_activity_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAllNumText(){
//        motorNumTextView.setText(Integer.toString(motorNum));
        sensorNumTextView.setText(Integer.toString(sensorNum));
        payloadNumTextView.setText(Integer.toString(payloadNum));


        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(MOTOR_NUM,Integer.toString(motorNum));
        editor.putString(SENSOR_NUM,Integer.toString(sensorNum));
        editor.putString(PAYLOAD_NUM,Integer.toString(payloadNum));
        editor.apply();
    }

    private void showMaxToast(String id){
        if(id.equals("motorAndPayload"))
            Toast.makeText(this,"The sum of motors and payload tools should not be more than "+Integer.toString(MAX_OUTPUT_NUM)+
                    " because they share the same connection port.",Toast.LENGTH_LONG).show();
        if(id.equals("sensor"))
            Toast.makeText(this,"Numbers of sensors should not be more than "+Integer.toString(MAX_SENSOR_NUM)+
                    " because sensor ports are limited.",Toast.LENGTH_LONG).show();

    }
}
