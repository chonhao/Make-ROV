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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BasicInformationActivity extends ActionBarActivity {
//-----------fixed values----------------------------------------

    final private int MAX_SENSOR_NUM = 4;
    final private int MAX_OUTPUT_NUM = 8;
    final private String MOTOR_NUM = "motor-num";
    final private String SENSOR_NUM = "sensor-num";
    final private String PAYLOAD_NUM = "payload-num";
    //-------------Shared preferences init values-------------------------------------
    public  static final String ROV_BASIC_INFORMATION = "rov-basic-information";
    private static final String ROV_NAME = "rov-name";
    public  static final String FIRST_TIME_SETUP = "first-time-setup";
//------------UI class init values-----------------------------------------------
    public static String[] navBarChoices;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private android.support.v7.app.ActionBarDrawerToggle drawerListener;
    MyAdapter myAdapter;

    private static Button motorMinus;
    private static Button motorPlus;
    private static Button sensorMinus;
    private static Button sensorPlus;
    private static Button payloadMinus;
    private static Button payloadPlus;
    private static TextView motorNumTextView;
    private static TextView sensorNumTextView;
    private static TextView payloadNumTextView;

    private EditText rovNameEditText;

    private static int motorNum = 0;
    private static int sensorNum = 0;
    private static int payloadNum = 0;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);



        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        motorMinus = (Button)findViewById(R.id.motorMinus);
        motorPlus = (Button)findViewById(R.id.motorPlus);
        sensorMinus = (Button)findViewById(R.id.sensorMinus);
        sensorPlus = (Button)findViewById(R.id.sensorPlus);
        payloadMinus = (Button)findViewById(R.id.payloadMinus);
        payloadPlus = (Button)findViewById(R.id.payloadPlus);
        motorNumTextView = (TextView)findViewById(R.id.motorNumTextView);
        sensorNumTextView = (TextView)findViewById(R.id.sensorNumTextView);
        payloadNumTextView = (TextView)findViewById(R.id.payloadNumTextView);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        myAdapter=new MyAdapter(this);
        drawerList.setAdapter(myAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BasicInformationActivity.this, Long.toString(id), Toast.LENGTH_SHORT).show();
                switch ((int)id) {
                    case 1:
                         startActivity(new Intent(BasicInformationActivity.this, MultiMotorAllocationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(BasicInformationActivity.this, SensorInitialization.class));
                        break;
                }
                drawerLayout.closeDrawer(drawerList);
            }
        });
        drawerListener = new android.support.v7.app.ActionBarDrawerToggle(this,drawerLayout,null,0,0){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerListener);
//------------------components init-------------------------------------
        rovNameEditText = (EditText)findViewById(R.id.rovNameEditText);

//------------------UI setup completed----------------------------------
//------------------Additional setup------------------------------------
        sharedPreferences = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
        if(!sharedPreferences.getString(FIRST_TIME_SETUP,"true").equals("false")) //debug
            startActivity(new Intent(BasicInformationActivity.this,setup_BasicInformationActivity.class));

        //TODO: make a dialog when sd card is not mounting
        //TODO: open the drawer automatically if the user haven't learn the usage of drawer
        SharedPreferences rovName = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
        rovNameEditText.setText(rovName.getString(ROV_NAME,""));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//---------------Other init-------------------------------------
        sharedPreferences = getSharedPreferences(ROV_BASIC_INFORMATION,MODE_PRIVATE);
        motorNumTextView.setText(sharedPreferences.getString(MOTOR_NUM, "0"));
        motorNum = Integer.parseInt(sharedPreferences.getString(MOTOR_NUM, "0"));
        sensorNumTextView.setText(sharedPreferences.getString(SENSOR_NUM, "0"));
        sensorNum = Integer.parseInt(sharedPreferences.getString(SENSOR_NUM, "0"));
        payloadNumTextView.setText(sharedPreferences.getString(PAYLOAD_NUM, "0"));
        payloadNum = Integer.parseInt(sharedPreferences.getString(PAYLOAD_NUM, "0"));
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
        motorMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(motorNum>0)
                    motorNum--;
                updateAllNumText();
            }
        });
        motorPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(motorNum+payloadNum<MAX_OUTPUT_NUM)
                    motorNum++;
                else
                    showMaxToast("motorAndPayload");
                updateAllNumText();
            }
        });
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
                if(motorNum+payloadNum<MAX_OUTPUT_NUM)
                    payloadNum++;
                else
                    showMaxToast("motorAndPayload");
                updateAllNumText();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();

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

        if(drawerListener.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        drawerListener.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        String[] features;
        public MyAdapter(Context context){
            this.context = context;
            features = context.getResources().getStringArray(R.array.features_array);
        }

        @Override
        public int getCount() {
            return features.length;
        }

        @Override
        public Object getItem(int position) {
            return features[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.custom_nav_drawer,parent,false);

            }else{
                row = convertView;
            }
            TextView motorTextField = (TextView) row.findViewById(R.id.motorTextView);
            motorTextField.setText(features[position]);
            return row;
        }
    }

    private void updateAllNumText(){
        motorNumTextView.setText(Integer.toString(motorNum));
        sensorNumTextView.setText(Integer.toString(sensorNum));
        payloadNumTextView.setText(Integer.toString(payloadNum));


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOTOR_NUM,Integer.toString(motorNum));
        editor.putString(SENSOR_NUM,Integer.toString(sensorNum));
        editor.putString(PAYLOAD_NUM,Integer.toString(payloadNum));
        editor.apply();
    }

    private void showMaxToast(String id){
        if(id.equals("motorAndPayload"))
            Toast.makeText(this, "The sum of motors and payload tools should not be more than " + Integer.toString(MAX_OUTPUT_NUM) +
                    " because they share the same connection port.", Toast.LENGTH_LONG).show();
        if(id.equals("sensor"))
            Toast.makeText(this,"Numbers of sensors should not be more than "+Integer.toString(MAX_SENSOR_NUM)+
                    " because sensor ports are limited.",Toast.LENGTH_LONG).show();

    }
}
