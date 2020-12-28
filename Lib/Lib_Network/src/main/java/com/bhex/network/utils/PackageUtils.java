package com.bhex.network.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.bhex.tools.utils.LogUtils;

/**
 * 提供一些获取应用信息的方法
 * Created by gongdongyang on 2018/9/25.
 */

public class PackageUtils {

    /**
     * 获取App版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (TextUtils.isEmpty(pi.versionName))
                return "";
            else
                return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("VersionName"+ "Exception");
            return 1;
        }
    }

    /**
     * 获取App名称
     *
     * @param context 上下文
     * @return 名称
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ApplicationInfo info = pi.applicationInfo;
            return context.getResources().getString(info.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getApplicationId(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ApplicationInfo info = pi.applicationInfo;
            return info.processName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

}
