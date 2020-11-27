package com.bhex.wallet.bh_main.my.enums;

/**
 * @author gongdongyang
 * 2020-11-18 10:39:32
 *
 */
public enum  BUSI_MY_TYPE {
    备份助记词(0,"备份助记词"),
    修改安全密码(1,"修改安全密码"),
    备份私钥(2,"备份私钥"),
    备份KS(3,"备份Keystore"),
    设置(4,"设置"),
    联系我们(5,"联系我们"),
    版本号(6,"版本号");
    public int index;
    public String label;

    BUSI_MY_TYPE(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public static BUSI_MY_TYPE getType(int index){
        for (BUSI_MY_TYPE item : values()) {
            if(item.index!=index){
                continue;
            }
            return  item;
        }
        return null;
    }
}
