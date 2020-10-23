package com.bhex.wallet.bh_main.my.model;

/**
 * @author gongdongyang
 * 2020-10-16 17:37:11
 */
public class SecuritySettingItem {
    public int id;
    public String title;
    public boolean isSelected;

    public SecuritySettingItem(int id,String title, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
    }
}
