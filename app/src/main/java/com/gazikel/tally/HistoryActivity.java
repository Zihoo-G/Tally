package com.gazikel.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gazikel.tally.adapter.AccountAdapter;
import com.gazikel.tally.db.AccountBean;
import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyLv;
    TextView timeTv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;

    int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
        initData();
    }

    private void initView() {
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);

        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);

        historyLv.setAdapter(adapter);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;

        timeTv.setText(year + "年" + month + "月");

        loadData();
    }

    private void loadData() {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccount(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                CalendarDialog calendarDialog = new CalendarDialog(this);
                calendarDialog.show();
                calendarDialog.setDialogSize();
                break;
        }
    }
}