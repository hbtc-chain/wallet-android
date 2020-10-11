package com.bhex.wallet.common.model;

import com.google.gson.annotations.Expose;

/**
 * @author gongdongyang
 * 2020-10-10 14:18:36
 * 币对映射
 */
public class BHTokenMapping {


    /**
     * issue_symbol : tbtc
     * target_symbol : btc
     * total_supply : 21000000
     * issue_pool : 21000000
     * enabled : true
     */

    public String issue_symbol;
    public String target_symbol;
    public String total_supply;
    public String issue_pool;
    public boolean enabled;

    @Expose(serialize = false, deserialize = false)
    public String coin_symbol;

    public BHTokenMapping() {
    }

    public BHTokenMapping(String issue_symbol, String target_symbol, String total_supply, String issue_pool, boolean enabled) {
        this.issue_symbol = issue_symbol;
        this.target_symbol = target_symbol;
        this.total_supply = total_supply;
        this.issue_pool = issue_pool;
        this.enabled = enabled;
    }
}
