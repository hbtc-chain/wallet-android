/*
 * *******************************************************************
 *   @项目名称: BHex Android
 *   @文件名称: ActivityCache.java
 *   @Date: 18-11-29 下午4:05
 *   @Author: ppzhao
 *   @Description:
 *   @Copyright（C）: 2018 BlueHelix Inc.   All rights reserved.
 *   注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的.
 *  *******************************************************************
 */

package com.bhex.wallet.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ActivityCache {
    private static ActivityCache instance;
    private Map<String, Activity> activityMap;
    private Map<String, Activity> financeMap;

    public static ActivityCache getInstance() {
        if (instance == null) {
            synchronized (ActivityCache.class) {
                if (instance == null) {
                    instance = new ActivityCache();
                }
            }
        }
        return instance;
    }

    private ActivityCache() {
        activityMap = new HashMap<String, Activity>();
        financeMap = new HashMap<String, Activity>();
    }

    public synchronized void addActivity(Activity activity) {
        activityMap.put(activity.getLocalClassName(), activity);
    }

    public synchronized void removeActivity(Activity activity) {
        activityMap.remove(activity.getLocalClassName());
        activity.finish();
    }

    public synchronized Activity getActivity(String activity) {
        return activityMap.get(activity);
    }

    public synchronized void finishActivity(){
        for(Map.Entry<String, Activity> entry:activityMap.entrySet()){
            entry.getValue().finish();
            //activity.finish();
        }
    }

    public synchronized void addFinanceActivity(Activity activity) {
        financeMap.put(activity.getLocalClassName(), activity);
    }

    public synchronized void removeFinanceActivity(Activity activity) {
        financeMap.remove(activity.getLocalClassName());
        activity.finish();
    }

    public synchronized Activity getFinanceActivity(String activity) {
        return financeMap.get(activity);
    }

    public synchronized void finishFinanceActivity(){
        for(Map.Entry<String, Activity> entry:financeMap.entrySet()){
            entry.getValue().finish();
            //activity.finish();
        }
    }
    //添加开户的activity集合集
    public static List<Activity> activities = new ArrayList<>();

    public void removeActivity(String simpleName) {
        activityMap.get(simpleName).finish();
    }
}
