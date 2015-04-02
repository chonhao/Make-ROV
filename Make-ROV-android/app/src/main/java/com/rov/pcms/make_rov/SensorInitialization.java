package com.rov.pcms.make_rov;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class SensorInitialization extends ActionBarActivity {
//------------UI class init values-----------------------------------------------
    private final int SENSOR_AMOUNT = 4;
    private Spinner UIsensorInitSpinner;
    private Spinner UIsensorTypeSpinner;
    private EditText UIsensorName, UIsensorUnit, UIsensorFormula;

//-------------Shared preferences init values------------------------------------
    private SharedPreferences sharedPreferences;
    private SharedPreferences[] sharedPreferencesSensor = new SharedPreferences[5];
    public final String SENSOR_PREFERENCE_TAG = "sensor-preference-tag";
    public final String SENSOR_TYPE_TAG = "sensor-type-tag";
    public final String SENSOR_UNIT_TAG = "sensor-unit-tag";
    public final String SENSOR_FORMULA_TAG = "sensor-formula-tag";
//-------------other values------------------------------------
    public String[] SENSOR_SELECTION;


    public String[] SENSOR_TYPE ={
            "Select One...",
            "Temperature Sensor", "Resistance Sensor",
            "Other",
    };
//-------------Current selection---------------------------------
    private String currentName, currentUnit, currentFormula;
    private int currentType = 0;
    private int currentSelected = 0;
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

        sharedPreferences = getSharedPreferences(BasicInformationActivity.ROV_BASIC_INFORMATION,MODE_PRIVATE);
        int tempSensorNum = Integer.parseInt(sharedPreferences.getString(BasicInformationActivity.SENSOR_NUM,"0"));
        SENSOR_SELECTION = new String[tempSensorNum];
        if(tempSensorNum ==0){finish();}
        //Early Exit

        UIsensorInitSpinner = (Spinner)findViewById(R.id.sensorInitSpinner);
        ArrayAdapter<String> sensorArrayAdapter = new ArrayAdapter<String>(SensorInitialization.this,
                android.R.layout.simple_spinner_item,SENSOR_SELECTION);
        sensorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UIsensorInitSpinner.setAdapter(sensorArrayAdapter);

        UIsensorTypeSpinner = (Spinner)findViewById(R.id.sensorTypeSpinner);
        ArrayAdapter<String> sensorTypeArrayAdapter = new ArrayAdapter<String>(SensorInitialization.this,
                android.R.layout.simple_spinner_item,SENSOR_TYPE);
        sensorTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UIsensorTypeSpinner.setAdapter(sensorTypeArrayAdapter);

        UIsensorName = (EditText)findViewById(R.id.sensorNameEditText);
        UIsensorUnit = (EditText)findViewById(R.id.sensorUnitEditText);
        UIsensorFormula = (EditText)findViewById(R.id.sensorFormulaEditText);
//------------------Shared Preference Init---------------------------------------

        for(int i=0;i<tempSensorNum;i++){
            SENSOR_SELECTION[i] = "Sensor "+(i+1)+" OUTLET";
            sharedPreferencesSensor[i] = getSharedPreferences(SENSOR_SELECTION[i],MODE_PRIVATE);
        }


        UIsensorInitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelected = position;
                currentName = sharedPreferencesSensor[position].getString(SENSOR_SELECTION[position],"");
                UIsensorName.setText(currentName);

                currentType = Integer.parseInt(
                        sharedPreferencesSensor[position].getString(SENSOR_SELECTION[position]+SENSOR_TYPE_TAG,"0"));
                UIsensorTypeSpinner.setSelection(currentType);

                currentUnit = sharedPreferencesSensor[position].getString(SENSOR_SELECTION[position]+SENSOR_UNIT_TAG,"");
                UIsensorUnit.setText(currentUnit);

                currentFormula = sharedPreferencesSensor[position].getString(SENSOR_SELECTION[position]+SENSOR_FORMULA_TAG,"");
                UIsensorFormula.setText(currentFormula);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//------------------components init----------------------------------------------
//------------------Additional setup---------------------------------------------
//------------------Component listeners------------------------------------------
        UIsensorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentName = UIsensorName.getText().toString();
                SharedPreferences.Editor editor = sharedPreferencesSensor[currentSelected].edit();
                editor.putString(SENSOR_SELECTION[currentSelected],currentName);
                editor.apply();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        UIsensorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentType = position;
                SharedPreferences.Editor editor = sharedPreferencesSensor[currentSelected].edit();
                editor.putString(SENSOR_SELECTION[currentSelected]+SENSOR_TYPE_TAG,Integer.toString(currentType));
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        UIsensorUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentUnit = UIsensorUnit.getText().toString();
                SharedPreferences.Editor editor = sharedPreferencesSensor[currentSelected].edit();
                editor.putString(SENSOR_SELECTION[currentSelected]+SENSOR_UNIT_TAG,currentUnit);
                editor.apply();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        UIsensorFormula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentFormula = UIsensorFormula.getText().toString();
                SharedPreferences.Editor editor = sharedPreferencesSensor[currentSelected].edit();
                editor.putString(SENSOR_SELECTION[currentSelected]+SENSOR_FORMULA_TAG,currentFormula);
                editor.apply();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
