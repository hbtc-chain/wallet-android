package com.bhex.wallet.common.model;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 18:02
 */
public class BHToken {


    /**
     * symbol : btc
     * issuer :
     * chain : btc
     * type : 1
     * is_send_enabled : true
     * is_deposit_enabled : true
     * is_withdrawal_enabled : true
     * decimals : 8
     * total_supply : 21000000
     * collect_threshold : 0.0002
     * deposit_threshold : 0.0002
     * open_fee : 28
     * sys_open_fee : 28
     * withdrawal_fee : 0.00003
     * max_op_cu_number : 6
     * systransfer_amount : 0.00003
     * op_cu_systransfer_amount : 0.0001
     * is_native : false
     * custodian_amount : 0.02
     * logo :
     * opcus : ["HBCZAhTS5fknRkD69SgxARzTy5FViBHkt1qh","HBCM2s43tEC77x2yfycYUxPwKSjTxHYsMkSy","HBChzkC1FpVhJV24Kaw26wPcxJntPPCjQVx2","HBCjHfVzoupu8FAGVkg1NKqrPHt3brxMbUu7"]
     */
    public String name;
    public String symbol;
    public String issuer;
    public String chain;
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
    //public String withdrawal_fee_rate;
    public int max_op_cu_number;
    public String systransfer_amount;
    public String op_cu_systransfer_amount;
    public boolean is_native;
    public String custodian_amount;
    public String logo;
    public List<String> opcus;

    /*public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIs_send_enabled() {
        return is_send_enabled;
    }

    public void setIs_send_enabled(boolean is_send_enabled) {
        this.is_send_enabled = is_send_enabled;
    }

    public boolean isIs_deposit_enabled() {
        return is_deposit_enabled;
    }

    public void setIs_deposit_enabled(boolean is_deposit_enabled) {
        this.is_deposit_enabled = is_deposit_enabled;
    }

    public boolean isIs_withdrawal_enabled() {
        return is_withdrawal_enabled;
    }

    public void setIs_withdrawal_enabled(boolean is_withdrawal_enabled) {
        this.is_withdrawal_enabled = is_withdrawal_enabled;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(String total_supply) {
        this.total_supply = total_supply;
    }

    public String getCollect_threshold() {
        return collect_threshold;
    }

    public void setCollect_threshold(String collect_threshold) {
        this.collect_threshold = collect_threshold;
    }

    public String getDeposit_threshold() {
        return deposit_threshold;
    }

    public void setDeposit_threshold(String deposit_threshold) {
        this.deposit_threshold = deposit_threshold;
    }

    public String getOpen_fee() {
        return open_fee;
    }

    public void setOpen_fee(String open_fee) {
        this.open_fee = open_fee;
    }

    public String getSys_open_fee() {
        return sys_open_fee;
    }

    public void setSys_open_fee(String sys_open_fee) {
        this.sys_open_fee = sys_open_fee;
    }

    public String getWithdrawal_fee() {
        return withdrawal_fee;
    }

    public void setWithdrawal_fee(String withdrawal_fee) {
        this.withdrawal_fee = withdrawal_fee;
    }

    public int getMax_op_cu_number() {
        return max_op_cu_number;
    }

    public void setMax_op_cu_number(int max_op_cu_number) {
        this.max_op_cu_number = max_op_cu_number;
    }

    public String getSystransfer_amount() {
        return systransfer_amount;
    }

    public void setSystransfer_amount(String systransfer_amount) {
        this.systransfer_amount = systransfer_amount;
    }

    public String getOp_cu_systransfer_amount() {
        return op_cu_systransfer_amount;
    }

    public void setOp_cu_systransfer_amount(String op_cu_systransfer_amount) {
        this.op_cu_systransfer_amount = op_cu_systransfer_amount;
    }

    public boolean isIs_native() {
        return is_native;
    }

    public void setIs_native(boolean is_native) {
        this.is_native = is_native;
    }

    public String getCustodian_amount() {
        return custodian_amount;
    }

    public void setCustodian_amount(String custodian_amount) {
        this.custodian_amount = custodian_amount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getOpcus() {
        return opcus;
    }

    public void setOpcus(List<String> opcus) {
        this.opcus = opcus;
    }*/
}
