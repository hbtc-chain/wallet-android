package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/12
 * Time: 21:41
 */
public class TransferMsg{
    public String from_address;
    public String to_address;
    public List<TxCoin> amount;
}
