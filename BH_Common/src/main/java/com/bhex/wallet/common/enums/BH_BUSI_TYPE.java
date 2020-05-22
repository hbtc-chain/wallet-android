package com.bhex.wallet.common.enums;

/**
 * @author gongdongyang
 * 2020-5-11 14:46:24
 * 备份
 */
public enum BH_BUSI_TYPE {

    未备份("0"),已备份("1"),
    备份私钥("1"), 备份KS("2"),
    链内转账("1"), 跨链转账("2"),
    转账通知("1"), 系统通知("2");
    public String value;

    BH_BUSI_TYPE(String value) {
        this.value = value;
    }

    public int getIntValue(){
        return Integer.valueOf(value);
    }
}
