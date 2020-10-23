package com.bhex.wallet.bh_main.my.model;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/18
 * Time: 19:01
 */
public class CurrencyItem {
    public int id;
    public String fullName;
    public String shortName;
    public boolean selected;


    public CurrencyItem(int id, String fullName, String shortName, boolean selected) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.selected = selected;
    }
}
