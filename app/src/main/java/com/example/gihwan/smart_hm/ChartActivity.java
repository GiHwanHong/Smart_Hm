package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class ChartActivity extends Activity {
    PieChart pieChart_LED;
    LineChart lineChart_Feel;

    private SwipeRefreshLayout refreshLayout_Chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setupPieChart_LED();
        setupLineChart_Val();

        refreshLayout_Chart = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshChart);
        refreshLayout_Chart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout_Chart.setRefreshing(false);
            }
        });

    }

    private void setupPieChart_LED(){
        pieChart_LED = (PieChart)findViewById(R.id.piechart_LED);       // LED 사용량 계산
        pieChart_LED.setUsePercentValues(true);
        pieChart_LED.getDescription().setEnabled(false);
        pieChart_LED.setExtraOffsets(5,10,5,5);

        pieChart_LED.setDragDecelerationFrictionCoef(0.95f);

        pieChart_LED.setDrawHoleEnabled(false);
        pieChart_LED.setHoleColor(Color.WHITE);
        pieChart_LED.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(34f,"LED1"));
        yValues.add(new PieEntry(23f,"LED2"));
        yValues.add(new PieEntry(14f,"LED3"));
        yValues.add(new PieEntry(35f,"LED4"));

        Description description = new Description();
        description.setText("LED 전기 사용량 "); //라벨
        description.setTextSize(18);
        description.setTextColor(Color.BLACK);
        pieChart_LED.setDescription(description);

        pieChart_LED.animateY(1000, Easing.EasingOption.EaseInOutExpo); //애니메이션

        PieDataSet dataSet_LED = new PieDataSet(yValues,"LED Total");
        dataSet_LED.setSliceSpace(3f);
        dataSet_LED.setSelectionShift(5f);
        dataSet_LED.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData((dataSet_LED));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart_LED.setData(data);
        pieChart_LED.invalidate();
    }
    private void setupLineChart_Val(){
        lineChart_Feel = (LineChart)findViewById(R.id.chart_feel);      // 온도 사용량 계산

        List<Entry> list_Tmp = new ArrayList<Entry>();
        list_Tmp.add(new Entry(100,200));
        list_Tmp.add(new Entry(120,250));
        list_Tmp.add(new Entry(130,210));

        List<Entry> list_Hmi = new ArrayList<Entry>();
        list_Hmi.add(new Entry(120,120));
        list_Hmi.add(new Entry(100,200));
        list_Hmi.add(new Entry(80,180));

        LineDataSet TMP = new LineDataSet(list_Tmp, "Temperature");
        LineDataSet Humidity = new LineDataSet(list_Hmi, "Humidity");
        TMP.setDrawFilled(true);
        Humidity.setCircleColor(Color.DKGRAY);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        dataSets.add(TMP);
        dataSets.add(Humidity);

        LineData data1 = new LineData(dataSets);

        lineChart_Feel.setData(data1); // set the data and list of lables into chart
        lineChart_Feel.invalidate();
    }
}