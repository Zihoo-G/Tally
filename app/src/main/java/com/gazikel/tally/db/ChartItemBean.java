package com.gazikel.tally.db;

import java.util.List;

public class ChartItemBean {
    int sImageId;
    String type;
    // 所占比例
    float ratio;
    // 此项的总钱数
    float totalMoney;



    public ChartItemBean() {
    }

    public ChartItemBean(int sImageId, String type, float ratio, float totalMoney) {
        this.sImageId = sImageId;
        this.type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}
