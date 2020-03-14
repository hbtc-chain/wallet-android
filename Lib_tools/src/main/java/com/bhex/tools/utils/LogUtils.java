package com.bhex.tools.utils;

/**
 * Created by gongdongyang on 2018/9/17.
 */

import android.util.Log;

/**
 * Log日志打印操作
 *
 */
public class LogUtils {

    private static final boolean DEBUG = true;
    private static final String TAG = "LogUtils";

    /**
     * 获取当前类名
     *
     * @return
     */
    private static String getClassName() {
        // 这里的数组的index，即2，是根据你工具类的层级取的值，可根据需求改变
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }


    public static void w(String logString) {
        if (DEBUG) {
            Log.w(TAG+" "+getClassName(), logString);
        }
    }

    /**
     * debug log
     *
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * error log
     *
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * debug log
     *
     * @param msg
     */
    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG+" "+getClassName(), msg);
        }
    }

    /**
     * debug log
     *
     * @param msg
     */
    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG+" "+getClassName(), msg);
        }
    }

    /**
     * error log
     *
     * @param msg
     */
    public static void e(String msg) {
        if (DEBUG) {
            Log.d(TAG+" "+getClassName(), msg);
        }
    }

    public static void i(String tag, String logString) {
        if (DEBUG) {
            Log.i(tag, logString);
        }
    }


    public static void w(String tag, String logString) {
        if (DEBUG) {
            Log.w(tag, logString);
        }
    }

    public static boolean isDebuggable() {
        return DEBUG;
    }
}

