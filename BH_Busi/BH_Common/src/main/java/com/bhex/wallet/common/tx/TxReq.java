package com.bhex.wallet.common.tx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/31
 * Time: 21:21
 */
public class TxReq{
    public TxFee fee;
    public String memo;
    public List<TxMsg> msg;
    public List<TxSignature> signatures;
}
