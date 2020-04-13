package com.bhex.tools.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
            rate = divide_100(differ+"",openValue+"");
        }
        return rate;
    }

    public static double divide_100(String number1,String number2){
        BigDecimal b1 = new BigDecimal(number1);
        BigDecimal b2 = new BigDecimal(number2);
        double b3 = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return mul(Double.toString(b3),100+"");
    }

    public static double divide(String number1,String number2){
        BigDecimal b1 = new BigDecimal(number1);
        BigDecimal b2 = new BigDecimal(number2);
        double b3 = b1.divide(b2,BigDecimal.ROUND_HALF_UP).doubleValue();

        return b3;
    }

    public static double divide(String number1,String number2,int digit){
        double b3 = 0;
        if(TextUtils.isEmpty(number1) ){
            return  0;
        }
        BigDecimal b1 = new BigDecimal(number1);
        BigDecimal b2 = new BigDecimal(number2);
        b3 = b1.divide(b2, digit, BigDecimal.ROUND_HALF_UP).doubleValue();

        return b3;
    }

    public static double sub(String number1,String number2){
        BigDecimal b1 = new BigDecimal(number1);
        BigDecimal b2 = new BigDecimal(number2);
        double b3 = b1.subtract(b2).doubleValue();
        return b3;
    }


    /**
     * 相加
     * @param value1
     * @param value2
     * @return
     */
    public static Double add(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 两个数相乘
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(String d1, String d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();

    }


    /**
     * 格式化输出
     * @param val
     * @return
     */
    public static String formatValue(double val,int digit){
        return String.format("%."+digit + "f", val);
        //DecimalFormat format = new DecimalFormat("#.00");
    }

    public static String formatValueByString(String val,int digit){
        if(TextUtils.isEmpty(val)){
            return "0";
        }
        return String.format("%."+digit + "f", val);
    }

}
