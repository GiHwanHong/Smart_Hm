package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class ChartActivity extends Activity {
    PieChart pieChart_LED;
    BarChart BarData_Feel;

    private SwipeRefreshLayout refreshLayout_Chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        pieChart_LED = (PieChart) findViewById(R.id.piechart_LED);       // LED 사용량 계산
        BarData_Feel = (BarChart) findViewById(R.id.barchart_feel);      // 온도 사용량 계산

        refreshLayout_Chart = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshChart);
        refreshLayout_Chart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout_Chart.setRefreshing(false);
                pieChart_LED.setVisibility(View.INVISIBLE);
                BarData_Feel.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setupPieChart_LED() {

        pieChart_LED.setUsePercentValues(true);
        pieChart_LED.setExtraOffsets(5, 10, 5, 5);

        pieChart_LED.setDragDecelerationFrictionCoef(0.95f);

        pieChart_LED.setDrawHoleEnabled(false);
        pieChart_LED.setHoleColor(Color.WHITE);
        pieChart_LED.setTransparentCircleRadius(61f);

        ArrayList<Entry> yValues = new ArrayList<Entry>();

        yValues.add(new Entry(34f, 0));
        yValues.add(new Entry(23f, 1));
        yValues.add(new Entry(14f, 2));
        yValues.add(new Entry(35f, 3));

        PieDataSet dataSet_LED = new PieDataSet(yValues, "");
        dataSet_LED.setValueTextSize(15);
        dataSet_LED.setSliceSpace(3f);
        dataSet_LED.setSelectionShift(5f);
        dataSet_LED.setColors(ColorTemplate.COLORFUL_COLORS);

        final ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("거실");
        xVals.add("부엌");
        xVals.add("방");
        xVals.add("가스벨브");

        pieChart_LED.animateY(1000, Easing.EasingOption.EaseInOutExpo); //애니메이션

        PieData data = new PieData(xVals, dataSet_LED);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.WHITE);
        pieChart_LED.setData(data);
        pieChart_LED.setDescription("가스 • 전기 사용량");
        pieChart_LED.setDescriptionTextSize(18f);

        pieChart_LED.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                switch (dataSetIndex) {
                    case 0:
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("총 거실의 전기 사용량은 : " + e.getVal() + " 입니다.")
                                .show(ChartActivity.this);
                        break;
                    case 1:
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("총 부엌의 전기 사용량은 : " + e.getVal() + " 입니다.")
                                .show(ChartActivity.this);
                        break;
                    case 2:
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("총 방의 전기 사용량은 : " + e.getVal() + " 입니다.")
                                .show(ChartActivity.this);
                        break;
                    case 3:
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("총 가스벨브의 사용량은 : " + e.getVal() + " 입니다.")
                                .show(ChartActivity.this);
                        break;
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        pieChart_LED.setData(data);
        pieChart_LED.invalidate();
    }

    private void setupBarChart_Val() {

        ArrayList<String> month = new ArrayList<String>();
        month.add("1월");
        month.add("2월");
        month.add("3월");
        month.add("4월");
        month.add("5월");
        //month.add("6월");

        ArrayList<BarEntry> bar_Tmp = new ArrayList<>();
        bar_Tmp.add(new BarEntry(8, 0));
        bar_Tmp.add(new BarEntry(2, 1));
        bar_Tmp.add(new BarEntry(15, 2));
        bar_Tmp.add(new BarEntry(20, 3));
        //bar_Tmp.add(new BarEntry(5, 4));

        ArrayList<BarEntry> bar_Hmi = new ArrayList<>();
        bar_Hmi.add(new BarEntry(16, 0));
        bar_Hmi.add(new BarEntry(14, 1));
        bar_Hmi.add(new BarEntry(5, 2));
        bar_Hmi.add(new BarEntry(20, 3));
        //bar_Tmp.add(new BarEntry(5, 4));

        BarDataSet TMP_set = new BarDataSet(bar_Tmp, "온도");
        TMP_set.setColor(Color.rgb(244, 67, 54));
        TMP_set.setDrawValues(true);

        BarDataSet Humidity_set = new BarDataSet(bar_Hmi, "습도");
        Humidity_set.setColor(Color.rgb(255, 235, 59));
        Humidity_set.setDrawValues(true);

        ArrayList<IBarDataSet> dataSets_bar = new ArrayList<>();
        dataSets_bar.add(TMP_set);
        dataSets_bar.add(Humidity_set);
        BarData feel_data = new BarData(month, dataSets_bar);

        BarData_Feel.setData(feel_data); // initialize the Bardata with argument labels and dataSet
        BarData_Feel.animateY(2000);
        BarData_Feel.setDescription("온도,습도 사용량");
        BarData_Feel.setDescriptionTextSize(10f);
        // BarData_Feel.getLegend().setEnabled(false);
        BarData_Feel.invalidate();

    }

    public void Chart_Btn_Click(View v) {
        switch (v.getId()) {
            case R.id.LED_chart:

                setupPieChart_LED();
                pieChart_LED.setVisibility(View.VISIBLE);
                BarData_Feel.setVisibility(View.INVISIBLE);
                break;
            case R.id.grpah_show:
                setupBarChart_Val();
                BarData_Feel.setVisibility(View.VISIBLE);
                pieChart_LED.setVisibility(View.INVISIBLE);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  // 뒤로 가기 버튼 클릭 시 종료 여부를 물어보기 위함
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("종료하시겠습니까?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}