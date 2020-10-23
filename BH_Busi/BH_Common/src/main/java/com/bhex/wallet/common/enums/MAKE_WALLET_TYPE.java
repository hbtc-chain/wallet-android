package com.bhex.wallet.common.enums;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/22
 * Time: 23:26
 */
public enum  MAKE_WALLET_TYPE {
    创建助记词(0),导入助记词(1),PK(2),导入KS(3);

    private int way;

    MAKE_WALLET_TYPE(int way) {
        this.way = way;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }
}
