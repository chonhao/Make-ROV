package com.rov.pcms.make_rov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


public class setup_BasicInformationActivity extends ActionBarActivity {
    //------------UI class init values-----------------------------------------------
    private static FloatingActionButton nextFab;
    private EditText rovNameEditText;
    //-------------Shared preferences init values-------------------------------------
    private static final String ROV_BASIC_INFORMATION = "rov-basic-information";
    private static final String ROV_NAME = "rov-name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__basic_information);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
//------------------default SharedPreferences---------------------------
        SharedPreferences defaultSharedPreferences = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
        SharedPreferences.Editor defaultSharedPreferencesEditor = defaultSharedPreferences.edit();
        // first page
        defaultSharedPreferencesEditor.putString(ROV_NAME,"Make-ROV").apply();
        // second page
        final String MOTOR_NUM = "motor-num";
        final String SENSOR_NUM = "sensor-num";
        final String PAYLOAD_NUM = "payload-num";
        defaultSharedPreferencesEditor.putString(MOTOR_NUM,"6").apply();
        defaultSharedPreferencesEditor.putString(SENSOR_NUM,"2").apply();
        defaultSharedPreferencesEditor.putString(PAYLOAD_NUM,"0").apply();
        // third page--nothing to set
        // forth page
        SharedPreferences[] sharedPreferencesSensor = new SharedPreferences[5];
        SharedPreferences.Editor[] sharedPreferenceSensorEditor = new SharedPreferences.Editor[5];
        for(int i=0;i<2;i++){
            sharedPreferencesSensor[i] = getSharedPreferences("Sensor "+(i+1)+" OUTLET",MODE_PRIVATE);
            sharedPreferenceSensorEditor[i] = sharedPreferencesSensor[i].edit();
        }
        sharedPreferenceSensorEditor[0].putString("Sensor "+(1)+" OUTLET","Temp Sensor").apply();
        sharedPreferenceSensorEditor[0].putString("Sensor "+(1)+" OUTLET"
                +setup_SensorInitialization.SENSOR_TYPE_TAG,Integer.toString(1)).apply();
        sharedPreferenceSensorEditor[0].putString("Sensor "+(1)+" OUTLET"
                +setup_SensorInitialization.SENSOR_UNIT_TAG,"C").apply();

        sharedPreferenceSensorEditor[1].putString("Sensor "+(2)+" OUTLET","Resistance Sensor").apply();
        sharedPreferenceSensorEditor[1].putString("Sensor "+(2)+" OUTLET"
                +setup_SensorInitialization.SENSOR_TYPE_TAG,Integer.toString(3)).apply();
        sharedPreferenceSensorEditor[1].putString("Sensor "+(2)+" OUTLET"
                +setup_SensorInitialization.SENSOR_UNIT_TAG,"Omega").apply();

//------------------components init-------------------------------------
        rovNameEditText = (EditText)findViewById(R.id.setup_rovNameEditText);
        nextFab = (FloatingActionButton)findViewById(R.id.nextFab);
//------------------UI setup completed----------------------------------
//------------------Additional setup------------------------------------
        //TODO: make a dialog when sd card is not mounting
        //TODO: open the drawer automatically if the user haven't learn the usage of drawer
        SharedPreferences rovName = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
        rovNameEditText.setText(rovName.getString(ROV_NAME,""));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//------------------Component listeners---------------------------------
        rovNameEditText.addTextChangedListener(new TextWatcher() {
            //when the rov edit text changes, save the data to shared preference
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences rovName = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
                SharedPreferences.Editor editor = rovName.edit();
                editor.putString(ROV_NAME,rovNameEditText.getText().toString()).apply();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(setup_BasicInformationActivity.this,setup_BasicInformationActivity_2.class));
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic_information, menu);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        moveTaskToBack(false);
    }


}
