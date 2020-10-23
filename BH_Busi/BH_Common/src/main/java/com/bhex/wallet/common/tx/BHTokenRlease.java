package com.bhex.wallet.common.tx;

/**
 * @author gongdongyang
 * 代币发行
 * 2020年5月18日16:19:50
 */
public class BHTokenRlease {
    //交易发起人
    public String from;
    // 资产所有者
    public String to;
    //代币名称
    public String symbol;
    //代币总量
    public String total_supply;
    //代币精度，最大为 18
    public String decimals;

    public BHTokenRlease(String from, String to, String symbol, String total_supply, String decimals) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
        this.total_supply = total_supply;
        this.decimals = decimals;
    }
}
