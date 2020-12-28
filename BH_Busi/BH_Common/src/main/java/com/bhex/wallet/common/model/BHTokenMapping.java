package com.bhex.wallet.common.model;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Objects;

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
    public BHToken issue_token;
    public BHToken target_token;
    public boolean enabled;

    @Expose(serialize = false, deserialize = false)
    public String coin_symbol;

    public BHTokenMapping(String coin_symbol, String issue_symbol, String target_symbol, String total_supply, String issue_pool, boolean enabled) {
        this.coin_symbol = coin_symbol;
        this.issue_symbol = issue_symbol;
        this.target_symbol = target_symbol;
        this.total_supply = total_supply;
        this.issue_pool = issue_pool;
        this.enabled = enabled;
    }

    /*public class Mapping_Token{
        public String name;
        public String symbol;
        public String chain;
        public String decimals;
        public String logo;
    }*/



}
