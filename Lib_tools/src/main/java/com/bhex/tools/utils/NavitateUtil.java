package com.bhex.tools.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bhex.tools.constants.BHConstants;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class NavitateUtil {

    /**
     * @param context
     * @param cls
     */
    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param cls
     */
    public static void startActivity(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cls, String ...params) {
        Intent intent = new Intent(context, cls);
        if(params!=null && params.length>1){
            for(int index=0;index<params.length/2;index++){
                intent.putExtra(params[index],params[index+1]);
            }
        }
        context.startActivity(intent);
    }

    /**
     *
     * @param context
     * @param url
     */
    public static void startOutsideBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


    /**
     * 启动Main
     * @param context
     */
    public static void startMainActivity(Context context){
        Intent intent = new Intent(BHConstants.MAIN_PATH);
        context.startActivity(intent);
    }


    /**
     * 启动Main
     * @param context
     */
    public static void startMainActivity(Context context,String ...params){
        Intent intent = new Intent(BHConstants.MAIN_PATH);
        if(params!=null && params.length>1){
            for(int index=0;index<params.length/2;index++){
                intent.putExtra(params[index],params[index+1]);
            }
        }
        context.startActivity(intent);
    }


}
