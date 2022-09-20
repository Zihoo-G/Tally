package com.gazikel.tally.fragment_record;


import android.accounts.Account;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gazikel.tally.R;
import com.gazikel.tally.adapter.TypeBaseAdapter;
import com.gazikel.tally.db.AccountBean;
import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.db.TypeBean;
import com.gazikel.tally.utils.KeyboardUtils;
import com.gazikel.tally.utils.MarkDialog;
import com.gazikel.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    private KeyboardView keyboardView;
    private EditText et_money;
    private ImageView imageView;
    private TextView tv_type, tv_mark, tv_time;
    private GridView gv_type;
    List<TypeBean> typeBeanList;
    TypeBaseAdapter adapter;

    /*将用户填写的数据保存为AccountBean对象*/
    AccountBean accountBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountBean = new AccountBean();
        accountBean.setTypename("其它");
        accountBean.setsImageId(R.mipmap.ic_other);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);

        initView(view);
        /*给GridView填充数据的方法*/
        loadDataToGV();
        /*设置GridView的每一项的点击事件*/
        setGridViewListener();
        /*初始化当前时间*/
        initTime();
        return view;
    }



    public void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        et_money = view.findViewById(R.id.frag_record_et_money);
        imageView = view.findViewById(R.id.frag_record_iv);
        gv_type = view.findViewById(R.id.frag_record_gv);
        tv_mark = view.findViewById(R.id.frag_record_tv_mark);
        tv_time = view.findViewById(R.id.frag_record_tv_time);
        tv_type = view.findViewById(R.id.frag_record_tv_type);

        tv_time.setOnClickListener(this);
        tv_mark.setOnClickListener(this);

        //让自定义软键盘显示出来
        KeyboardUtils keyboard = new KeyboardUtils(keyboardView, et_money);
        keyboard.showKeyboard();

        /*设置接口，监听确定按钮被点击了*/
        keyboard.setOnEnsureListener(new KeyboardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                /*点击了确定按钮
                * 获取记录的信息保存在数据库当中
                * 返回上一级页面
                * */
                //获取输入钱数
                String moneyStr = et_money.getText().toString();
                if (!TextUtils.isEmpty(moneyStr) || "0".equals(moneyStr)) {
                    getActivity().finish();
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);

                //获取记录信息，保存至数据库当中
                saveAccount();
                //返回上一级页面
                getActivity().finish();
            }
        });
    }

    /*初始化时间*/
    public void initTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String time = simpleDateFormat.format(date);
        tv_time.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /*给GridView填充数据的方法*/
    public void loadDataToGV() {
        typeBeanList = new ArrayList<>();

        adapter = new TypeBaseAdapter(getContext(), typeBeanList);
        gv_type.setAdapter(adapter);

    }

    /*设置GridView的点击事件*/
    private void setGridViewListener() {
        gv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectPos(position);
                adapter.notifyDataSetInvalidated();

                TypeBean typeBean = typeBeanList.get(position);
                imageView.setImageResource(typeBean.getImageId());
                tv_type.setText(typeBean.getTypename());
                accountBean.setTypename(typeBean.getTypename());
                accountBean.setsImageId(typeBean.getImageId());

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_mark:
                showMarkDialog();
                break;
        }
    }

    /*弹出备注对话框*/
    public void showMarkDialog() {
        MarkDialog dialog = new MarkDialog(getContext());
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(new MarkDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String text = dialog.getText();

                if (!TextUtils.isEmpty(text)) {
                    tv_mark.setText(text);

                    accountBean.setMark(text);

                }
                dialog.cancel();
            }
        });
    }

    /*弹出时间对话框*/
    public void showTimeDialog() {
        SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.show();

        selectTimeDialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {

            @Override
            public void onEnsure(String time, int year, int month, int day) {
                tv_time.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });

    }

    public abstract void saveAccount();
}