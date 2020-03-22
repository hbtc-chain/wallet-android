package com.bhex.wallet.balance.model;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/18
 * Time: 0:19
 */
public class Balance {
    public int resId;
    //币种
    public String coinName;
    //当前价格
    public double price;
    //数量
    public double volume;
    //账户余额
    public double asset;


    public Balance(int resId, String coinName) {
        this.resId = resId;
        this.coinName = coinName;
    }
}
