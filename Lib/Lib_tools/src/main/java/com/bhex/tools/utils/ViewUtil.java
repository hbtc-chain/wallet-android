package com.bhex.tools.utils;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtil {

    //View 点击事件值空
    public static void getListenInfo(View view){
        try{
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            //设置权限
            method.setAccessible(true);
            //得到点击事件持有者
            Object listenerInfo = method.invoke(view);
            //得到点击事件对象
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field field = listenerInfoClz.getDeclaredField("mOnClickListener");
            //将点击事件代理类设置到“持有者中”
            field.set(listenerInfo, null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
