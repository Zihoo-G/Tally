package com.gazikel.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gazikel.tally.R;

import java.util.List;

public class MarkDialog extends Dialog implements View.OnClickListener {

    EditText et;
    Button btn_cancel;
    Button btn_ensure;
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public MarkDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnEnsureListener {
        public void onEnsure();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_mark_btn_cancel:
                cancel();
                break;
            case R.id.dialog_mark_btn_ensure:
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对话框显示布局
        setContentView(R.layout.dialog_mark);

        et = findViewById(R.id.dialog_mark_et);
        btn_cancel = findViewById(R.id.dialog_mark_btn_cancel);
        btn_ensure = findViewById(R.id.dialog_mark_btn_ensure);
        btn_ensure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    public String getText() {
        return et.getText().toString().trim();
    }

    /*设置dialog尺寸和屏幕尺寸一致*/

    public void setDialogSize() {
        /*获取当前窗口对象*/
        Window window = getWindow();
        /*获取窗口对象参数*/
        WindowManager.LayoutParams attributes = window.getAttributes();
        /*获取屏幕宽度*/
        Display defaultDisplay = window.getWindowManager().getDefaultDisplay();
        /*对话框窗口为屏幕窗口*/
        attributes.width = (int) defaultDisplay.getWidth();

        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        handler.sendEmptyMessageDelayed(1, 100);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            /*自动弹出软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
