package com.bhex.wallet.common.tx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongdongyang
 * 币对映射
 * 2020-9-4 12:08:06
 */
public class MappingSwapMsg {

    //交易发起人
    public String from;
    //映射对的symbol
    public String issue_symbol;
    public List<TxCoin> coins = new ArrayList<>();

}
