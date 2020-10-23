package com.bhex.lib.uikit.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.util.Hashtable;

public class TypefaceUtils {


    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    /**
     * 全局字体替换
     */
    public static void replaceSystemDefaultFont(Context context) {
        try {
            Field defaultField = Typeface.class.getDeclaredField("MONOSPACE");
            defaultField.setAccessible(true);
            defaultField.set(null, get(context,"fonts/NotoSansSC-Regular.ttf"));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 获取MPlus常规字体
     */
    public static Typeface getMediumNotoTypeface(Context context) {
        return get(context,"fonts/NotoSansSC-Medium.ttf");
    }

    /**
     * 获取MPlus常规字体
     */
    public static Typeface getTextTypeface(Context context) {
        return get(context,"fonts/Lato-Regular.ttf");
    }

    /**
     * 获取MPlus加粗字体
     */
    public static Typeface getTextWeightTypeface(Context context) {
        return get(context,"fonts/Lato-Bold.ttf");
    }

    public static Typeface get(Context context,String path) {
        Typeface tf = fontCache.get(path);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), path);
            }
            catch (Exception e) {
                return Typeface.DEFAULT;
            }
            fontCache.put(path, tf);
        }
        return tf;
    }

    public static void setTextTypeface(Context context,TextView textView) {
        textView.setTypeface(getTextTypeface(context));
    }

    public static void setTextWeightTypeface(Context context,TextView textView) {
        textView.setTypeface(getTextWeightTypeface(context));
    }

    public static void setTextMediumNotoTypeface(Context context,TextView textView) {
        textView.setTypeface(getMediumNotoTypeface(context));
    }
}