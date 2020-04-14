package com.huobi.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.hb.cfdbase.baselib.core.CApplicationHelper;

import java.lang.reflect.Field;

public class TypefaceUtils {

    /**
     * 全局字体替换
     */
    public static void replaceSystemDefaultFont(Context context) {
        try {

            Field defaultField = Typeface.class.getDeclaredField("MONOSPACE");
            defaultField.setAccessible(true);
//            defaultField.set(null, getTextTypeface());  // 先中文开发，暂时使用苹方作为默认字体
            defaultField.set(null, getTextTypeface());
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }



    /**
     * 获取MPlus常规字体
     */
    public static Typeface getTextTypeface() {
        try {
            return  Typeface.createFromAsset(CApplicationHelper.getInstance().getApplication().getAssets(), "font/mplus1p_regular.ttf");
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    /**
     * 获取MPlus加粗字体
     */
    public static Typeface getTextWeightTypeface() {
        try {
            return Typeface.createFromAsset(CApplicationHelper.getInstance().getApplication().getAssets(), "font/mplus1p_medium.ttf");
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static void setTextTypeface(TextView textView) {
        textView.setTypeface(getTextTypeface());
    }

    public static void setTextWeightTypeface(TextView textView) {
        textView.setTypeface(getTextWeightTypeface());
    }
}