package com.bhex.wallet.common.tx;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/12
 * Time: 21:54
 */
public class KeyGenMsg {

    public String order_id;
    public String symbol;
    public String from;
    public String to;

    public KeyGenMsg() {
    }

    public KeyGenMsg(String order_id, String symbol, String from, String to) {
        this.order_id = order_id;
        this.symbol = symbol;
        this.from = from;
        this.to = to;
    }
}
