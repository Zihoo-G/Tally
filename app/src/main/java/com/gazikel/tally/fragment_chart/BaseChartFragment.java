package com.gazikel.tally.fragment_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gazikel.tally.R;
import com.gazikel.tally.adapter.ChartItemAdapter;
import com.gazikel.tally.db.ChartItemBean;
import com.gazikel.tally.db.DBManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseChartFragment extends Fragment {

    ListView chartLv;
    int year;
    int month;
    // 柱状图控件
    BarChart barChart;
    // 如果没有收支情况，显示的文本
    TextView chartTv;

    List<ChartItemBean> mDatas;
    private ChartItemAdapter chartItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);

        // 获取Activity传递的数据
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        // 设置数据源
        mDatas = new ArrayList<>();

        // 设置适配器
        chartItemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(chartItemAdapter);

        // 添加头布局
        addLvHeaderView();

        return view;
    }

    protected void addLvHeaderView() {
        // 将布局转换为View对象
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top, null);
        // 将View添加到ListView的头布局上
        chartLv.addHeaderView(headerView);
        // 查找头布局当中包含的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        // 设置柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        // 设置柱状图的内边距
        barChart.setExtraOffsets(20, 20, 20, 20);
        // 设置坐标轴
        setAxis(year, month);

    }

    /**
     * 设置柱状图坐标轴的显示
     * 方法必须重写
     * @param year
     * @param month
     */
    protected void setAxis(int year, int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        // 设置x轴标签的个数
        xAxis.setLabelCount(31);
        // x轴标签的大小
        xAxis.setTextSize(12f);

        // 设置x轴显示值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if(val == 0) {
                    return month + "-1";
                }

                if (val == 14) {
                    return month + "-15";
                }

                // 根据不同的月份显示最后一天的位置
                if (month==2) {
                    if (val == 27) {
                        return month + "-28";
                    }
                } else if(month == 1 ||month==3|| month == 5||month==7||month==8||month==10||month==12){
                    if (val==30)
                        return month + "-31";
                } else if(month==4||month==6||month==9||month==11){
                    if (val==29)
                        return month +"-31";
                }
                return "";
            }
        });
        xAxis.setYOffset(12);
        
        // y轴在子类中设置
        setYAxis(year, month);
        // 设置坐标轴显示的数据
        setAxisData(year, month);
    }

    /**
     * 设置坐标轴显示的数据
     * @param year
     * @param month
     */
    protected abstract void setAxisData(int year, int month);

    protected abstract void setYAxis(int year, int month);


    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccount(year, month, kind);

        mDatas.clear();
        mDatas.addAll(list);

        chartItemAdapter.notifyDataSetChanged();
    }

    abstract public void setDate(int year, int month);

}
