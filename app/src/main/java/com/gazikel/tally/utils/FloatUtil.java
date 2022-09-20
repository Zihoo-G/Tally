package com.gazikel.tally.utils;

import java.math.BigDecimal;

public class FloatUtil {

    public static float div(float v1, float v2) {
        float v3 = v1 / v2;
        BigDecimal b1 = new BigDecimal(v3);
        return b1.setScale(4, 4).floatValue();
    }

    public static String ratioToPercent(float val) {
        float v = val * 100;
        BigDecimal b1 = new BigDecimal(v);
        return b1.setScale(2, 4).floatValue() + "%";
    }
}
