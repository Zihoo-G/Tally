package com.gazikel.tally.fragment_record;

import com.gazikel.tally.db.DBManager;
import com.gazikel.tally.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        /* TODO:获取数据库当中的数据源 */
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeBeanList.addAll(outList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveAccount() {
        accountBean.setKind(0);
        DBManager.addAccount(accountBean);
    }
}
