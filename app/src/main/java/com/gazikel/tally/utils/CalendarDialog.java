package com.gazikel.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gazikel.tally.R;
import com.gazikel.tally.adapter.CalendarAdapter;
import com.gazikel.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {

    ImageView errorIv;
    GridView gv;
    LinearLayout hsvLayout;

    List<TextView> hsvViewList;
    List<Integer> yearList;

    // 正在被点击的年份的位置
    int selectPos = -1;
    int selectMonth = -1;
    private CalendarAdapter adapter;

    public CalendarDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_month_select);

        gv = findViewById(R.id.dialog_month_select_gv);
        errorIv = findViewById(R.id.dialog_month_select_iv);
        hsvLayout = findViewById(R.id.dialog_month_select_layout);

        errorIv.setOnClickListener(this);

        // 向横向的ScrollView当中添加View的方法
        addViewToLayout();

        initGridView();
    }

    private void initGridView() {
        int selectedYear = yearList.get(selectPos);

        adapter = new CalendarAdapter(getContext(), selectedYear);

        if (selectMonth==-1) {
            adapter.selectedPos = Calendar.getInstance().get(Calendar.MONTH);
        } else {
            adapter.selectedPos = selectMonth-1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>();

        yearList = DBManager.getYearListFromAccount();
        System.out.println(yearList);
        System.out.println(yearList.size());

        // 如果数据库当中没有记录，就添加今年的记录
        if (yearList.size() == 0 || yearList == null) {
            yearList.add(Calendar.getInstance().get(Calendar.YEAR));
        }

        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_select_month_hsv, null);
            // 将view添加到布局当中
            hsvLayout.addView(view);

            TextView hsvTv = view.findViewById(R.id.item_select_month_hsv_tv);
            hsvTv.setText(year + "");

        }

        if (selectPos == -1)
            selectPos = hsvViewList.size();

        // 将最后一个设置为选中状态
        changeTvBackground(selectPos);

        // 设置每一个View的监听事件
        setHsvClickListener();

    }

    /**
     * 给ScrollView当中的每一个TextView设置点击事件
     */
    private void setHsvClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView textView = hsvViewList.get(i);
            final int pos = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvBackground(pos);
                    selectPos = pos;
                }
            });
        }
    }

    /**
     * 改变某位置上的背景颜色和文字颜色
     *
     * @param selectPos 位置
     */
    private void changeTvBackground(int selectPos) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_month_select_iv:
                cancel();
                break;
            default:
                cancel();
                break;
        }
    }

    public void setDialogSize() {
        /*获取当前窗口对象*/
        Window window = getWindow();
        /*获取窗口对象参数*/
        WindowManager.LayoutParams attributes = window.getAttributes();
        /*获取屏幕宽度*/
        Display defaultDisplay = window.getWindowManager().getDefaultDisplay();
        /*对话框窗口为屏幕窗口*/
        attributes.width = (int) defaultDisplay.getWidth();

        attributes.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}
