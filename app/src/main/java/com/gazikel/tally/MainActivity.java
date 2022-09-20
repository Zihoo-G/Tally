package com.gazikel.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gazikel.tally.adapter.AccountAdapter;
import com.gazikel.tally.db.AccountBean;
import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.utils.BudgetDialog;
import com.gazikel.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*显示今日收支情况的ListView*/
    ListView lv_today_info;

    /*声明数据源*/
    List<AccountBean> mDats;

    ImageButton btn_more;
    Button btn_record;

    AccountAdapter adapter;

    int year, month, day;

    /*头布局相关控件*/
    TextView tv_top_out, tv_top_in, tv_top_budget, tv_top_condition;
    ImageView iv_show;
    View headerView;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTime();

        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        lv_today_info = findViewById(R.id.main_lv);

        btn_more = findViewById(R.id.main_btn_more);
        btn_record = findViewById(R.id.main_btn_record);
        btn_more.setOnClickListener(this);
        btn_record.setOnClickListener(this);

        /*添加ListView的头布局*/
        addListViewHeader();
        mDats = new ArrayList<>();

        /*设置适配器，加载每一行的数据*/
        adapter = new AccountAdapter(this, mDats);
        lv_today_info.setAdapter(adapter);

        setListViewLongClickListener();
    }

    /*当Activity获取焦点时会调用的方法*/
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        /*设置顶部布局的显示*/
        setTopTVShow();
    }

    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /*加载数据库中数据的方法*/
    public void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDay(year, month, day);
        mDats.clear();
        mDats.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加ListView头布局
     */
    public void addListViewHeader() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        lv_today_info.addHeaderView(headerView);
        /*查找可用控件*/
        tv_top_out = findViewById(R.id.item_mainlv_top_out);
        tv_top_in = findViewById(R.id.item_mainlv_top_tv_in);
        tv_top_budget = findViewById(R.id.item_mainlv_top_tv_budget);
        tv_top_condition = findViewById(R.id.item_mainlv_top_tv_day);
        iv_show = findViewById(R.id.item_mainlv_top_iv_hide);

        headerView.setOnClickListener(this);
        iv_show.setOnClickListener(this);
        tv_top_budget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.main_btn_record:
                intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_in:
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:

                /*切换Text明文与密文*/
                toggleShow();
                break;
            case R.id.main_iv_search:
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);
                break;
        }
        if (v==headerView) {
            intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);

        }
    }

    /*设置顶部布局文本内容的显示*/
    public void setTopTVShow(){
        /*获取今日支出和收入总金额*/
        float income = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcome = DBManager.getSumMoneyOneDay(year, month, day, 0);

        String infoOneday = "今日支出 ￥"+outcome+" 收入 ￥"+income;
        tv_top_condition.setText(infoOneday);

        /*获取本月支出和收入总金额*/
        float income2 = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcome2 = DBManager.getSumMoneyOneMonth(year, month, 0);
        tv_top_in.setText("+"+income2);
        tv_top_out.setText("-"+outcome2);

        /*设置显示预算剩余*/
        float budget = preferences.getFloat("bmoney", 0);
        tv_top_budget.setText(String.valueOf((budget-outcome2)));
    }

    boolean isShow = true;

    public void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod instance1 = PasswordTransformationMethod.getInstance();
            tv_top_in.setTransformationMethod(instance1);
            tv_top_out.setTransformationMethod(instance1);
            tv_top_budget.setTransformationMethod(instance1);
            iv_show.setImageResource(R.mipmap.ic_hide);
            isShow = false;
        } else {
            HideReturnsTransformationMethod instance2 = HideReturnsTransformationMethod.getInstance();
            tv_top_in.setTransformationMethod(instance2);
            tv_top_out.setTransformationMethod(instance2);
            tv_top_budget.setTransformationMethod(instance2);
            iv_show.setImageResource(R.mipmap.ic_show);
            isShow = true;
        }
    }

    /*显示预算对话框*/
    public void showBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setDialogSize();

        budgetDialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                /*将预算金额写入到共享参数当中*/
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", money);
                editor.commit();


                float sumMoney = DBManager.getSumMoneyOneMonth(year, month, 0);
                tv_top_budget.setText(String.valueOf((money-sumMoney)));
            }
        });
    }

    public void setListViewLongClickListener() {
        lv_today_info.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position-1;
                AccountBean bean = mDats.get(pos);
                int click_id = bean.getId();

                /*弹出提示用户删除的对话框*/
                showDeleteItemDialog(bean);


                return false;
            }
        });
    }

    /*弹出是否删除某一条记录的对话框*/
    public void showDeleteItemDialog(AccountBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除").setMessage("确定删除")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*执行删除操作*/
                        int i = DBManager.deleteAccount(bean.getId());

                        mDats.remove(bean);
                        adapter.notifyDataSetChanged();
                        setTopTVShow();
                    }
                });

        builder.create().show();
    }
}