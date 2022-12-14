package com.gazikel.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gazikel.tally.R;
import com.gazikel.tally.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {

    Context context;
    List<TypeBean> mDatas;
    int selectPos = 0;//选中的位置

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
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

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfragment_gv, parent, false);

        /*查找布局当中的控件*/
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);

        /*获取指定位置的数据源*/
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypename());

        if (selectPos == position) {
            iv.setImageResource(typeBean.getsImageId());
        } else {
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
