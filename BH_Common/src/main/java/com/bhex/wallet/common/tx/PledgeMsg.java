package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: 周昌
 * Date: 2020/4/23
 * 质押
 */
public class PledgeMsg {
    public String proposal_id;
    public String depositor;
    public List<TxCoin> amount;
}
