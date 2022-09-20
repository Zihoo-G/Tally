package com.gazikel.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.gazikel.tally.adapter.RecordPagerAdapter;
import com.gazikel.tally.fragment_record.IncomeFragment;
import com.gazikel.tally.fragment_record.BaseRecordFragment;
import com.gazikel.tally.fragment_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        /*查找控件*/
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        /*设置viewPager*/
        initPager();
    }

    /*点击事件*/
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }

    private  void initPager() {
        /*初始化ViewPager页面的集合*/
        List<Fragment> fragmentList = new ArrayList<>();

        /*创建收入和支出页面放置在Fragment当中*/
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        IncomeFragment incomeFragment = new IncomeFragment();

        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        /*创建适配器*/
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        /*设置适配器*/
        viewPager.setAdapter(recordPagerAdapter);

        /*将TabLayout和ViewPager进行关联*/
        tabLayout.setupWithViewPager(viewPager);
    }
}