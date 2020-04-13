package com.bhex.wallet.common.tx;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/12
 * Time: 21:54
 */
public class KeyGenMsg {

    public String OrderId;
    public String Symbol;
    public String From;
    public String To;

    public KeyGenMsg() {
    }

    public KeyGenMsg(String orderId, String symbol, String from, String to) {
        OrderId = orderId;
        Symbol = symbol;
        From = from;
        To = to;
    }
}
