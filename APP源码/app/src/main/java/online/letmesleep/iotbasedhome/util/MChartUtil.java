package online.letmesleep.iotbasedhome.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.bean.ApplicationEntityWithData;
import online.letmesleep.iotbasedhome.config.Config;

/**
 * Created by Letmesleep on 2018/5/6.
 */

public class MChartUtil {
    public static void getSettedChart(LineChart chart, List<Entry> data, ApplicationEntityWithData item){

        if(data.size()==0){
            return;
        }

        LineDataSet dataSet = new LineDataSet(data, Config.getApplicationType(item.getApplicationType()));
        dataSet.setColor(Color.parseColor("#8553fa"));
        dataSet.setValueTextColor(Color.parseColor("#8553fa"));
        dataSet.setLineWidth(1f);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        chart.invalidate(); // refresh

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);



        YAxis leftAxis = chart.getAxisLeft();
        List<LimitLine> limitLines= leftAxis.getLimitLines();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f);
        //预警线

        if(limitLines.size()==0&&item.getLimitlow().matches("\\d*")&&item.getLimithigh().matches("\\d*")){
            LimitLine lowlimit = new LimitLine(Integer.parseInt(item.getLimitlow()));
            lowlimit.setLineColor(Color.RED);
            lowlimit.setLineWidth(1f);
            LimitLine highlimit = new LimitLine(Integer.parseInt(item.getLimithigh()));
            highlimit.setLineColor(Color.RED);
            highlimit.setLineWidth(1f);
            leftAxis.addLimitLine(lowlimit);
            leftAxis.addLimitLine(highlimit);
        }




        chart.setData(lineData);
    }

    public static void getSettedChartWithoutLimitLine(LineChart chart, List<Entry> data, ApplicationEntityWithData item){

        if(data.size()==0){
            return;
        }

        LineDataSet dataSet = new LineDataSet(data, Config.getApplicationType(item.getApplicationType()));
        dataSet.setColor(Color.parseColor("#8553fa"));
        dataSet.setValueTextColor(Color.parseColor("#8553fa"));
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        chart.invalidate(); // refresh

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);



        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f);

        chart.setData(lineData);
    }
}
