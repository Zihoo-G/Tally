package com.gazikel.tally.fragment_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gazikel.tally.R;
import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.db.TypeBean;

import java.util.List;

public class IncomeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        /* TODO:获取数据库当中的数据源 */
        List<TypeBean> inList = DBManager.getTypeList(1);
        typeBeanList.addAll(inList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveAccount() {
        accountBean.setKind(1);
        DBManager.addAccount(accountBean);
    }
}