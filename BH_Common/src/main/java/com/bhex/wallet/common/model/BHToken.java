package com.bhex.wallet.common.model;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 18:02
 */
public class BHToken {
    /**
     * issuer :
     * type : 2
     * is_send_enabled : true
     * is_deposit_enabled : false
     * is_withdrawal_enabled : false
     * decimals : 18
     * total_supply : 0
     * collect_threshold : 0
     * deposit_threshold : 0
     * open_fee : 0
     * sys_open_fee : 0
     * withdrawal_fee : 0
     * max_op_cu_number : 0
     * systransfer_amount : 0
     * op_cu_systransfer_amount : 0
     * gas_limit : 1000000
     * gas_price : 1
     */
    public String symbol;
    public String chain;
    public String issuer;
    public int type;
    public boolean is_send_enabled;
    public boolean is_deposit_enabled;
    public boolean is_withdrawal_enabled;
    public int decimals;
    public String total_supply;
    public String collect_threshold;
    public String deposit_threshold;
    public String open_fee;
    public String sys_open_fee;
    public String withdrawal_fee;
    public int max_op_cu_number;
    public String systransfer_amount;
    public String op_cu_systransfer_amount;
    public String gas_limit;
    public String gas_price;


}
