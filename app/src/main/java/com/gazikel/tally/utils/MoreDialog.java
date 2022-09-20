package com.gazikel.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gazikel.tally.AboutActivity;
import com.gazikel.tally.HistoryActivity;
import com.gazikel.tally.MonthChartActivity;
import com.gazikel.tally.R;

public class MoreDialog extends Dialog implements View.OnClickListener {

    Button aboutBtn, settingsBtn, historyBtn, infoBtn;
    ImageView errorIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingsBtn = findViewById(R.id.dialog_more_btn_settings);
        historyBtn = findViewById(R.id.dialog_more_btn_history);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv);

        aboutBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.dialog_more_btn_about:
                intent = new Intent(getContext(), AboutActivity.class);
                getContext().startActivity(intent);

                break;
            case R.id.dialog_more_btn_settings:
                break;
            case R.id.dialog_more_btn_history:
                intent=new Intent(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent = new Intent(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_iv:

                break;
        }

        cancel();

    }

    /**
     * 设置Dialog的尺寸和屏幕尺寸一致
     */
    public void setDialogSize() {
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();

        wlp.width = d.getWidth();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
