package com.bhex.wallet.common.tx;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/14
 * Time: 20:56
 */
public class WithdrawalMsg {

    //发交易账户，本交易需要此地址的私钥签名
    public String from_cu;
    //跨链提币的地址
    public String to_multi_sign_address;
    //币种名称
    public String symbol;
    //数量
    public String amount;
    //跨链转账的手续费
    public String gas_fee;
    //全局唯一的字符串，需要客户端自己生成；
    public String order_id;
}
