package com.bhex.tools.utils;

import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author gdy
 * 2020-8-31 16:40:13
 */
public class BottomNavigationViewUtil {
    public static void hideToast(BottomNavigationView bnv){
        ViewGroup menuViewGroup = (ViewGroup) bnv.getChildAt(0);

        for (int i = 0; i < menuViewGroup.getChildCount(); i++) {
            menuViewGroup.getChildAt(i).setOnLongClickListener(v -> {
                return true;
            });
        }
    }
}
