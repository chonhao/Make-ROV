package com.rov.pcms.make_rov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;


public class SensorMoitor extends ActionBarActivity implements OnChartValueSelectedListener {
    //---------------ui--------------------------------
    public static String[] navBarChoices;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private android.support.v7.app.ActionBarDrawerToggle drawerListener;
    MyAdapter myAdapter;
    public static LineChart sensor1chart;


    //------------shared prefrence-------------
    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_moitor);
//-----------------UI init--------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Make-ROV Sensor Monitor");
//-------------check if it is first run--------------------------
        sharedPreferences = getSharedPreferences(BasicInformationActivity.ROV_BASIC_INFORMATION,MODE_PRIVATE);
        if(!sharedPreferences.getString(BasicInformationActivity.FIRST_TIME_SETUP,"true").equals("false")) //debug
            startActivity(new Intent(SensorMoitor.this,setup_BasicInformationActivity.class));
//-------------------INIT Nav Drawer---------------------------
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        myAdapter=new MyAdapter(this);
        drawerList.setAdapter(myAdapter);
        LayoutInflater inflater = getLayoutInflater();
        //add a picture in the Nav drawer as header
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
//---------------custom action starts-----------------------------
        sensor1chart = (LineChart)findViewById(R.id.sensor1chart);
        sensor1chart.setOnChartValueSelectedListener(this);
        sensor1chart.setDrawGridBackground(false);
        sensor1chart.setDescription("");

        // add an empty data object
        sensor1chart.setData(new LineData());
        sensor1chart.getXAxis().setEnabled(false);
        sensor1chart.setDrawBorders(true);
        sensor1chart.getAxisRight().setEnabled(false);
        sensor1chart.setDrawGridBackground(false);
//        mChart.getXAxis().setDrawLabels(false);
//        mChart.getXAxis().setDrawGridLines(false);

        sensor1chart.invalidate();
        addEntry();
        removeLastEntry();



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
        addEntry();
        sensor1chart.setVisibleXRange(15f);
        sensor1chart.moveViewToX(mdata+10);
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

    //debug

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
    int mdata = 0;
    private void addEntry() {

        LineData data = sensor1chart.getData();

        if(data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(set.getEntryCount() + "");

            // choose a random dataSet
            int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
            mdata++;
            data.addEntry(new Entry((float) (Math.random() * 10) + mdata, set.getEntryCount()), randomDataSetIndex);

            // let the chart know it's data has changed
            sensor1chart.notifyDataSetChanged();

//            mChart.setVisibleXRange(6);
//            mChart.setVisibleYRange(30, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
//            mChart.moveViewTo(data.getXValCount()-7, 55f, AxisDependency.LEFT);

            // redraw the chart
            sensor1chart.invalidate();
        }
    }
    private void removeLastEntry() {

        LineData data = sensor1chart.getData();

        if(data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntry(xIndex, dataSetIndex);

                sensor1chart.notifyDataSetChanged();
                sensor1chart.invalidate();
            }
        }
    }
    private void addDataSet() {

        LineData data = sensor1chart.getData();

        if(data != null) {

            int count = (data.getDataSetCount() + 1);

            // create 10 y-vals
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            if(data.getXValCount() == 0) {
                // add 10 x-entries
                for (int i = 0; i < 10; i++) {
                    data.addXValue("" + (i+1));
                }
            }

            for (int i = 0; i < data.getXValCount(); i++) {
                yVals.add(new Entry((float) (Math.random() * 50f) + 50f * count, i));
            }

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleSize(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            sensor1chart.notifyDataSetChanged();
            sensor1chart.invalidate();
        }
    }
    private void removeDataSet() {

        LineData data = sensor1chart.getData();

        if(data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            sensor1chart.notifyDataSetChanged();
            sensor1chart.invalidate();
        }
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected() {

    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }



}
