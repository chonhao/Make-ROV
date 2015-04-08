package com.rov.pcms.make_rov;

import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartGraph {
    public LineChart lineChart;
    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
    public int mdata;

    public ChartGraph(){

    }


    public void chartInit(){
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");

        // add an empty data object
        lineChart.setData(new LineData());
        lineChart.getXAxis().setEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(false);
//        mChart.getXAxis().setDrawLabels(false);
//        mChart.getXAxis().setDrawGridLines(false);

        lineChart.invalidate();
        addEntry(10);
        removeLastEntry();
    }
    public void addEntry(float value) {

        LineData data = lineChart.getData();

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
            data.addEntry(new Entry((float)  value, set.getEntryCount()), randomDataSetIndex);
//(Math.random() * 10) +
            // let the chart know it's data has changed
            lineChart.notifyDataSetChanged();

//            mChart.setVisibleXRange(6);
//            mChart.setVisibleYRange(30, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
//            mChart.moveViewTo(data.getXValCount()-7, 55f, AxisDependency.LEFT);

            // redraw the chart
            lineChart.invalidate();

            lineChart.setVisibleXRange(15f);
            lineChart.moveViewToX(mdata+10);
        }
    }
    public void removeLastEntry() {

        LineData data = lineChart.getData();

        if(data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntry(xIndex, dataSetIndex);

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
//            mlineChart.moveViewToX(mdata-10);
        }
    }
    public void addDataSet(LineChart mlineChart) {

        LineData data = mlineChart.getData();

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
            mlineChart.notifyDataSetChanged();
            mlineChart.invalidate();
        }
    }
    public void removeDataSet(LineChart mlineChart) {

        LineData data = mlineChart.getData();

        if(data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mlineChart.notifyDataSetChanged();
            mlineChart.invalidate();
        }
    }

    public LineDataSet createSet() {

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
