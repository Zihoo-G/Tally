package com.gazikel.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gazikel.tally.R;
import com.gazikel.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

public class AccountAdapter extends BaseAdapter {

    Context context;
    List<AccountBean> mDatas;
    int year, month, day;

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mainlv, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AccountBean bean = mDatas.get(position);
        viewHolder.iv_type.setImageResource(bean.getsImageId());
        viewHolder.tv_type.setText(bean.getTypename());
        viewHolder.tv_mark.setText(bean.getMark());
        if (bean.getKind() == 1) {
            viewHolder.tv_money.setText(" +"+bean.getMoney());
        } else {
            viewHolder.tv_money.setText(" -"+bean.getMoney());
        }


        if (bean.getYear()==year&&bean.getMonth()==month&&day==bean.getDay()) {
            String time = bean.getTime().split(" ")[1];
            viewHolder.tv_time.setText("今天 "+time);
        } else {
            viewHolder.tv_time.setText(bean.getTime());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv_type;
        TextView tv_type, tv_mark, tv_time, tv_money;

        public ViewHolder(View view) {
            iv_type = view.findViewById(R.id.item_mainlv_iv);
            tv_type = view.findViewById(R.id.item_mainlv_title);
            tv_mark = view.findViewById(R.id.item_mainlv_tv_mark);
            tv_time = view.findViewById(R.id.item_mainlv_tv_time);
            tv_money = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}
