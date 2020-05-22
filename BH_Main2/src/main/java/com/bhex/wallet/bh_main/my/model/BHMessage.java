package com.bhex.wallet.bh_main.my.model;

/**
 * @author gongdongyang
 * 2020-5-21 14:49:04
 * 消息
 */
public class BHMessage {


    /**
     * id : 0
     * notification_type : 0
     * tx_type : 0
     * tx_hash : string
     * address : string
     * read : true
     * time : 0
     * amount : string
     * symbol : string
     */

    public int id;
    public int notification_type;
    public int tx_type;
    public String tx_hash;
    public String address;
    public boolean read;
    public int time;
    public String amount;
    public String symbol;


}
