package com.gazikel.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gazikel.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    ImageView iv_cancel;
    Button btn_ensure;
    EditText et_budget;
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public interface OnEnsureListener{
        public void onEnsure(float money);
    }
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);

        iv_cancel = findViewById(R.id.dialog_budget_iv_back);
        btn_ensure = findViewById(R.id.dialog_budget_btn_ensure);
        et_budget = findViewById(R.id.dialog_budget_et);

        iv_cancel.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_budget_btn_ensure:
                /*获取输入数据数值*/
                String data = et_budget.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(), "输入数值不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                float budget = Float.parseFloat(data);
                if (budget < 0) {
                    Toast.makeText(getContext(), "预算金额需要大于0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(budget);
                }

                cancel();
                break;
            case R.id.dialog_budget_iv_back:
                cancel();
                break;
        }
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
