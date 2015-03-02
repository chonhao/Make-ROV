package com.rov.pcms.make_rov;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SensorInitialization extends ActionBarActivity {
//------------UI class init values-----------------------------------------------
    private final int SENSOR_AMOUNT = 4;
    private Button[] sensorBtn = new Button[SENSOR_AMOUNT];
//-------------Shared preferences init values------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_initialization);
//-----------------UI init-------------------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.SensorInit_act_primaryColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//------------------components init----------------------------------------------
        sensorBtn[0] = (Button) findViewById(R.id.sensor1Btn);
        sensorBtn[1] = (Button) findViewById(R.id.sensor2Btn);
        sensorBtn[2] = (Button) findViewById(R.id.sensor3Btn);
        sensorBtn[3] = (Button) findViewById(R.id.sensor4Btn);
//------------------Additional setup---------------------------------------------
//------------------Component listeners------------------------------------------
        for(int i=0;i<SENSOR_AMOUNT;i++){
            final int tempi = i;
            sensorBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sensor_initialization, menu);
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
}
