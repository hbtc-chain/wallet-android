package com.bhex.wallet.common.enums;

/**
 * @author gongdongyang
 * 2020-5-11 14:46:24
 * 备份
 */
public enum BACK_WALLET_TYPE {

    未备份(0),已备份(1);
    public int value;

    BACK_WALLET_TYPE(int value) {
        this.value = value;
    }
}
