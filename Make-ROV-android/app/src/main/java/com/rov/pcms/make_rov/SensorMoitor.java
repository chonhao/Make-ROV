package com.rov.pcms.make_rov;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLEngine;


public class SensorMoitor extends ActionBarActivity {
    //---------------ui--------------------------------
    public static String[] navBarChoices;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private android.support.v7.app.ActionBarDrawerToggle drawerListener;
    MyAdapter myAdapter;
    private static ChartGraph[] sensorChart = {
            new ChartGraph(),new ChartGraph(),
            new ChartGraph(),new ChartGraph(),
    };


    //------------shared prefrence-------------
    public SharedPreferences sharedPreferences;

    //------------Bluetooth Components-----------------------------------
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice lastDevice;
    BluetoothThread thread;
    String[] itemString;
    List<String> itemList = new ArrayList<>();
    private Set<BluetoothDevice> paired;

    //------------Packet Components--------------------------------------
    private InputStream inStream;
    private static final Character SENSOR_HEADER = '*';
    private static final Character SENSOR_SERIAL_TYPE = '6';
    private static int currentReadMode = 0;
    private static int currentCount = 1;
    private static int tempValue = 0;
    private static int[] sensorValue = new int[5];
    boolean stopWorker = false;
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_moitor);
//-----------------UI init--------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sensor Monitor");
//-------------check if it is first run--------------------------
        sharedPreferences = getSharedPreferences(BasicInformationActivity.ROV_BASIC_INFORMATION, MODE_PRIVATE);
        if (!sharedPreferences.getString(BasicInformationActivity.FIRST_TIME_SETUP, "true").equals("false")) //debug
            startActivity(new Intent(SensorMoitor.this, setup_BasicInformationActivity.class));
//-------------------INIT Nav Drawer---------------------------
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        myAdapter = new MyAdapter(this);
        drawerList.setAdapter(myAdapter);
        LayoutInflater inflater = getLayoutInflater();
        //add a picture in the Nav drawer as header
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.headerview, drawerList, false);
        drawerList.addHeaderView(header, null, false);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BasicInformationActivity.this, Long.toString(id), Toast.LENGTH_SHORT).show();
                switch ((int) id) {
                    case 1:
                        startActivity(new Intent(SensorMoitor.this, BasicInformationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(SensorMoitor.this, MultiMotorAllocationActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(SensorMoitor.this, SensorInitialization.class));
                        break;
                }
                drawerLayout.closeDrawer(drawerList);
            }
        });
        drawerListener = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, null, 0, 0) {
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
//---------------custom action starts-----------------------------
        sensorChart[1].lineChart = (LineChart) findViewById(R.id.sensor1chart);
        sensorChart[1].chartInit();

        sensorChart[2].lineChart = (LineChart) findViewById(R.id.sensor2chart);
        sensorChart[2].chartInit();

        sensorChart[3].lineChart = (LineChart) findViewById(R.id.sensor3chart);
        sensorChart[3].chartInit();
//-------------bluetooth--------------------------------------
        Bluetoothinit();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        moveTaskToBack(false);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();

    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // ........
            for (int i = 0; i <= 20; i++) {
                sensorChart[1].addEntry((float) (Math.random() * 10));
                sensorChart[2].addEntry((float) (Math.random() * 10));
                sensorChart[3].addEntry((float) (Math.random() * 10));
            }
            sensorChart[1].lineChart.animateX(1000);
            sensorChart[2].lineChart.animateX(1000);
            sensorChart[3].lineChart.animateX(1000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor_moitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
            // ........
//            for (int i = 0; i <= 20; i++) {
//                sensorChart[1].addEntry((float) (Math.random() * 10));
//                sensorChart[2].addEntry((float) (Math.random() * 10));
//                sensorChart[3].addEntry((float) (Math.random() * 10));
//            }
//            sensorChart[1].lineChart.animateX(1000);
//            sensorChart[2].lineChart.animateX(1000);
//            sensorChart[3].lineChart.animateX(1000);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan) {
            requestEnableBluetooth();
            return true;
        }
        if (drawerListener.onOptionsItemSelected(item)) {
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

        public MyAdapter(Context context) {
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
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.custom_nav_drawer, parent, false);

            } else {
                row = convertView;
            }
            TextView motorTextField = (TextView) row.findViewById(R.id.motorTextView);
            motorTextField.setText(features[position]);
            return row;
        }
    }

    //Bluetooth Components

    private void Bluetoothinit() {
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) //no bluetooth support
        return;

    if (!mBluetoothAdapter.isEnabled()) { //request to open bluetooth
        new MaterialDialog.Builder(this)
                .title("Please turn bluetooth ON")
                .content("Bluetooth is required in order to receive signals from your ROV." +
                        "")
                .positiveText("OK")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (!mBluetoothAdapter.isEnabled())
                            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                    }
                })
                .show();

    }


//        thread.AddMessage("connected");
}
    // Start a new Thread to monitor Received bluetooth data
    public void beginListenForData(){
    try{
        Thread.sleep(1000);
    }catch (InterruptedException e){}
    try {
        inStream = thread.getMmSocket().getInputStream();
    } catch (IOException e){ e.printStackTrace();
    }

    Thread workerThread = new Thread(new Runnable(){
        public void run(){
            while(!Thread.currentThread().isInterrupted() && !stopWorker){
                try{
                    int availableCount = inStream.available();
                    if(availableCount > 0){
                        char data = (char)inStream.read();
                        try {
                            OnBluetoothSerialAvailable(data);
                        }catch (Exception e){}
                    }
                }
                catch (IOException ex){
                    stopWorker = true;
                }
            }
        }
    });
    workerThread.start();
}
    public void OnBluetoothSerialAvailable(Character serialData){
    //Log.i("BluetoothReceived", serialData.toString());
    if(currentReadMode==0){
        if(serialData == SENSOR_HEADER){
            currentReadMode=1;
        }
    }else if(currentReadMode ==1){
        if(serialData == SENSOR_SERIAL_TYPE){
            currentReadMode = 2;
        }
    }else if(currentReadMode == 2){
        switch (currentCount){
            case 1:
                tempValue += (serialData-48)*1000;
                currentCount = 2;
                break;
            case 2:
                tempValue += (serialData-48)*100;
                currentCount = 3;
                break;
            case 3:
                tempValue += (serialData-48)*10;
                currentCount = 4;
                break;
            case 4:
                tempValue += (serialData-48);
                currentCount = 1;
                currentReadMode = 3;
                break;
            default:
                currentCount = 1;
        }
    }else if(currentReadMode == 3){
        sensorValue[serialData-48] = tempValue;
        tempValue = 0;
        Log.i("sensor "+(serialData-48)+" Data",Integer.toString(sensorValue[serialData-48]));
        sensorChart[serialData-48+1].addEntry(sensorValue[serialData-48]);
        sensorChart[serialData-48+1].lineChart.invalidate();

        currentReadMode = 0;
    }
//        Log.i("currentReadMode",Integer.toString(currentReadMode));
}
    private void requestEnableBluetooth(){
    paired = mBluetoothAdapter.getBondedDevices();
    int deviceCount = 0;
    if(paired.size() > 0){
        for(BluetoothDevice device : paired){
            deviceCount++;
            itemList.add(/*"  "+deviceCount+". "+*/device.getName());
        }
        itemString = new String[itemList.size()];
        itemList.toArray(itemString);
    }

    MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
    dialog.title("Please Select your ROV");
    dialog.items(itemString);
    dialog.itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
            selected = which;
            Log.i("BD selected",selected+"");
            Log.i("BluetoothDeviceSelected", itemString[which]);
            if (paired.size() > 0) {
                for (BluetoothDevice device : paired) {
                    if (device.getName().equals(itemString[which])) {
                        thread = new BluetoothThread(device);
                        thread.start();
                        thread.AddMessage("connected");
                        beginListenForData();
                        Toast.makeText(SensorMoitor.this,"Connecting to "+itemString[which],Toast.LENGTH_LONG).show();
                        try{
                            Thread.sleep(2000);
                        }catch (InterruptedException e){}
                        Toast.makeText(SensorMoitor.this,"Connected",Toast.LENGTH_LONG).show();
                    }
                }
            }

            return false;
        }
    });
    dialog.positiveText("Connect");
    dialog.show();
}

}
