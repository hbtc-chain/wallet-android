package com.bhex.wallet.bh_main.my.ui.item;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:20
 */
public class MyItem {

    public String title;
    public boolean isArrow;
    public String rightTxt;//右边文字

    public MyItem(String title, boolean isArrow,String rightTxt) {
        this.title = title;
        this.isArrow = isArrow;
        this.rightTxt = rightTxt;
    }
}
