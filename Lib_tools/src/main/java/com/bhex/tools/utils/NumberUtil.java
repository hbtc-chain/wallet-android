package com.bhex.tools.utils;

import java.math.BigDecimal;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class NumberUtil {

    /**
     *
     * @param currentValue
     * @param openValue
     * @return
     */
    public static double changeRate(double currentValue,double openValue){
        double rate = 0.0f;
        double differ = currentValue-openValue;
        if(openValue>0){
            rate = divide(differ,openValue);
            LogUtils.d("NumberUtil","rate=="+rate);
        }
        return rate;
    }

    public static double divide(double number1,double number2){
        BigDecimal b1 = new BigDecimal(Double.toString(number1));
        BigDecimal b2 = new BigDecimal(Double.toString(number2));
        double b3 = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return mul(b3,100);
    }


    /**
     * 两个数相乘
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();

    }

}
