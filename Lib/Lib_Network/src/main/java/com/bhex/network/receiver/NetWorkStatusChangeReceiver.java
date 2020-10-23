package com.bhex.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.bhex.tools.utils.LogUtils;

/**
 * Created by gongdongyang
 * on 2019/12/18
 */
public class NetWorkStatusChangeReceiver extends BroadcastReceiver {

    public static int Network_Status_Mobile = 1;

    public static int Network_Status_None = 0;

    public static int Network_Status_WIFI = 2;
    private static int status;

    public static int getNectworkStatus() { return status; }

    public static void init(Context paramContext) {
        if (isNetworkConnected(paramContext)) {
            status = 1;
            return;
        }
        status = 0;

    }
     public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取连接类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {

        }

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        LogUtils.i("TAG", getConnectionType(info.getType()) + "连上");
                        //Toast.makeText(context, getConnectionType(info.getType()) + "连上", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    LogUtils.i("TAG", getConnectionType(info.getType()) + "断开");
                    //Toast.makeText(context, getConnectionType(info.getType()) + "断开", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
