package com.bhex.wallet.balance.enums;

/**
 * 2020-12-12 09:51:25
 * 公告类型
 */
public enum  BUSI_ANNOUNCE_TYPE {
    //1-警告，2-公告，3-活动，4-提示
    警告(1,"警告"),
    公告(2,"公告"),
    活动(3,"活动"),
    提示(4,"提示");

    public int index;
    public String label;

    BUSI_ANNOUNCE_TYPE(int index, String label) {
        this.index = index;
        this.label = label;
    }
}
