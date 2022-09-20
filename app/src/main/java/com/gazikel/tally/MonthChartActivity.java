package com.gazikel.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gazikel.tally.adapter.ChartViewPagerAdapter;
import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.fragment_chart.IncomeChartFragment;
import com.gazikel.tally.fragment_chart.OutcomeChartFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn, outBtn;
    TextView dateTv, inTv, outTv;
    ViewPager chartVp;

    private int year;
    private int month;
    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartViewPagerAdapter chartViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);

        initView();
        initTime();
        initStatistics(year, month);
        initFrag();
        
        setViewPagerLinstener();
    }

    private void setViewPagerLinstener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    // 初始化控件
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);

        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);

        chartVp = findViewById(R.id.chart_viewpager);
    }

    // 统计某年某月的收支情况
    private void initStatistics(int year, int month) {
        // 收入总钱数
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        // 支出总钱数
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);

        int inCountItemOneMonth = DBManager.getCountItenOneMonth(year, month, 1);
        int outCountItemOneMonth = DBManager.getCountItenOneMonth(year, month, 0);

        dateTv.setText(year + "年" + month + "月账单");
        inTv.setText("共" + inCountItemOneMonth + "笔收入，￥" + inMoneyOneMonth);
        outTv.setText("共" + outCountItemOneMonth + "笔支出，￥" + outMoneyOneMonth);
    }

    // 初始化事件
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();

        // 添加Fragment的对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();

        // 添加数据到Fragment中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);

        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);

        // 将Fragment添加到数据源中
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        // 使用适配器
        chartViewPagerAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartViewPagerAdapter);

        // 将Fragment加载到Activity当中

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;

            case R.id.chart_iv_rili:
                break;

            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;

            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    // 设置按钮样式的改变
    // 支出 0
    // 收入 1
    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);

            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if(kind ==1) {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);

            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }


}