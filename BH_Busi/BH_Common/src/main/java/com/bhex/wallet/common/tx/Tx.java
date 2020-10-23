package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/31
 * Time: 17:00
 */
public class Tx {

    public String cu_number;
    public String chain_id;
    public TxFee fee;
    public String memo;
    public List<TxMsg> msgs;
    public String sequence;


    /*public class TxFee{
        public List<TxCoin> amount;
        public String gas;

    }*/



}





