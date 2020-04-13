package com.bhex.lib.uikit.util;

import android.content.Context;

import androidx.core.content.ContextCompat;


/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/10
 * Time: 21:15
 */
public class ColorUtil {

    /*public static int getDark(Context context) {
        return SkinColorUtil.getColor(context, CommonUtil.isBlackMode() ? R.color.dark_night : R.color.dark);
    }*/

    public static int getColor(Context context, int colorId){
        return ContextCompat.getColor(context, colorId);
    }
}
