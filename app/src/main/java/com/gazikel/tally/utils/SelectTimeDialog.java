package com.gazikel.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.gazikel.tally.R;

/**
 * 在记录页面弹出时间对话框
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    EditText et_hour, et_minute;
    DatePicker datePicker;
    Button btn_ensure, btn_cancel;

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public interface OnEnsureListener{
        public void onEnsure(String time, int year, int month, int day);
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_time_btn_ensure:

                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                String monthStr = String.valueOf(month);
                if (month < 10) {
                    monthStr = "0"+month;
                }

                String dayStr = String.valueOf(day);
                if (day < 10) {
                    dayStr = "0"+day;
                }

                /*获取输入的小时和分钟*/
                String hourStr = et_hour.getText().toString();
                String minuteStr = et_minute.getText().toString();

                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour%24;
                }

                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute = minute%60;
                }

                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if (hour<10) {
                    hourStr = "0"+hour;
                }
                if (minute<10){
                    minuteStr = "0"+minute;
                }

                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuteStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat, year, month, day);
                }
                break;
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);

        et_hour = findViewById(R.id.dialog_time_hour);
        et_minute = findViewById(R.id.dialog_time_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        btn_cancel = findViewById(R.id.dialog_time_btn_cancel);
        btn_ensure = findViewById(R.id.dialog_time_btn_ensure);

        btn_cancel.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);

        hideDatePickerHeader();
    }

    /*隐藏datePicker头布局*/
    private void hideDatePickerHeader() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);

        if (rootView==null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView!=null) {
            return;
        }
        //5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId==headerView.getId()) {
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParams);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);

            return;
        }

        //6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId==headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
