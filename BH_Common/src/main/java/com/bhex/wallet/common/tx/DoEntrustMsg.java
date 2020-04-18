package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: zhouchang
 * Date: 2020/4/17
 * 委托，取消委托
 */
public class DoEntrustMsg {
    public String delegator_address;
    public String validator_address;
    public TxCoin amount;
}
