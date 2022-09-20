package com.gazikel.tally.fragment_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gazikel.tally.R;
import com.gazikel.tally.db.BarChartItemBean;
import com.gazikel.tally.db.DBManager;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


public class OutcomeChartFragment extends BaseChartFragment {
    int kind = 0;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();

        // 获取这个月每天的支出的总金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size()==0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        } else {
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            List<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries.add(entry);
            }

            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                // 获取日期
                int day = itemBean.getDay();
                // 根据天数获取x轴的位置
                int xIndex =day-1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY(itemBean.getSumMoney());

            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "");
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(8f);
            barDataSet.setColor(Color.RED);

            // 设置柱子上数据的显示格式
            barDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });

            sets.add(barDataSet);

            // 设置主子的宽度
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.5f);
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        // 获取本月收入最高的一天为多少，将它设置为最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);

        float max = (float) Math.ceil(maxMoney);

        // 设置y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);
        yAxis_right.setAxisMinimum(0f);
        // 不显示右边的y轴
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        // 设置不显示图例

    }

    @Override
    public void setDate(int year, int month) {

    }
}