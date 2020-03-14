package com.bhex.wallet.market.model;


import com.bhex.network.base.BaseResponse;

import java.util.List;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class MarketAllEntity extends BaseResponse {
    public List<MarketItemEntity> data;

    public class MarketItemEntity{
        public String m;
        public String s;
        public String ba;
        public String qa;
        public String i;
        public String t;
        public String o;
        public String c;
        public String h;
        public String l;
        public String v;

        @Override
        public String toString() {
            return "MarketItemEntity{" +
                    "m='" + m + '\'' +
                    ", s='" + s + '\'' +
                    ", ba='" + ba + '\'' +
                    ", qa='" + qa + '\'' +
                    ", i='" + i + '\'' +
                    ", t='" + t + '\'' +
                    ", o='" + o + '\'' +
                    ", c='" + c + '\'' +
                    ", h='" + h + '\'' +
                    ", l='" + l + '\'' +
                    ", v='" + v + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MarketAllEntity{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
