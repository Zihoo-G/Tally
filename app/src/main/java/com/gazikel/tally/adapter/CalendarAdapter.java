package com.gazikel.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gazikel.tally.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史账单界面
 */
public class CalendarAdapter extends BaseAdapter {

    Context context;
    List<String> mDatas;
    public int year;
    public int selectedPos = -1;

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.mDatas = new ArrayList<>();
        this.year = year;
        for (int i = 0; i < 12; i++) {
            String data = year + "-" + (i + 1);
            mDatas.add(data);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_select_month_gv, parent, false);
        TextView tv = convertView.findViewById(R.id.item_select_month_gv_tv);

        tv.setText(mDatas.get(position));
        tv.setBackgroundResource(R.color.grey);
        tv.setTextColor(Color.BLACK);
        if (position == selectedPos) {
            tv.setBackgroundResource(R.color.purple_200);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
