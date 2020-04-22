package com.bhex.wallet.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 10:54
 */
public class BHRates {


    /**
     * rates : {"cny":"0.28195708","jpy":"4.27087256","krw":"47.81268","usd":"0.04004446","usdt":"0.039352","vnd":"925.126168"}
     * token : bht
     */

    private RatesBean rates;
    private String token;

    public RatesBean getRates() {
        return rates;
    }

    public void setRates(RatesBean rates) {
        this.rates = rates;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class RatesBean {
        /**
         * cny : 0.28195708
         * jpy : 4.27087256
         * krw : 47.81268
         * usd : 0.04004446
         * usdt : 0.039352
         * vnd : 925.126168
         */

        @SerializedName("cny")
        private String cny;
        @SerializedName("jpy")
        private String jpy;
        @SerializedName("krw")
        private String krw;
        @SerializedName("usd")
        private String usd;
        @SerializedName("usdt")
        private String usdt;
        @SerializedName("vnd")
        private String vnd;

        public String getCny() {
            return cny;
        }

        public void setCny(String cny) {
            this.cny = cny;
        }

        public String getJpy() {
            return jpy;
        }

        public void setJpy(String jpy) {
            this.jpy = jpy;
        }

        public String getKrw() {
            return krw;
        }

        public void setKrw(String krw) {
            this.krw = krw;
        }

        public String getUsd() {
            return usd;
        }

        public void setUsd(String usd) {
            this.usd = usd;
        }

        public String getUsdt() {
            return usdt;
        }

        public void setUsdt(String usdt) {
            this.usdt = usdt;
        }

        public String getVnd() {
            return vnd;
        }

        public void setVnd(String vnd) {
            this.vnd = vnd;
        }
    }
}
