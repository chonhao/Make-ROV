package com.rov.pcms.make_rov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class SensorMoitor extends ActionBarActivity {
    //---------------ui--------------------------------
    public static String[] navBarChoices;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private android.support.v7.app.ActionBarDrawerToggle drawerListener;
    MyAdapter myAdapter;
    //------------shared prefrence-------------
    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_moitor);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Make-ROV Sensor Monitor");

        sharedPreferences = getSharedPreferences(BasicInformationActivity.ROV_BASIC_INFORMATION,MODE_PRIVATE);
        if(!sharedPreferences.getString(BasicInformationActivity.FIRST_TIME_SETUP,"true").equals("false")) //debug
            startActivity(new Intent(SensorMoitor.this,setup_BasicInformationActivity.class));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        myAdapter=new MyAdapter(this);
        drawerList.setAdapter(myAdapter);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.headerview, drawerList,false);
        drawerList.addHeaderView(header,null,false);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BasicInformationActivity.this, Long.toString(id), Toast.LENGTH_SHORT).show();
                switch ((int)id) {
                    case 1:
                        startActivity(new Intent(SensorMoitor.this,BasicInformationActivity.class));
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
}
