package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: 周昌
 * Date: 2020/4/23
 * 提案
 */
public class CreateProposalMsg {
    public ProposalContent content;
    public String proposer;
    public List<TxCoin> initial_deposit;


    public static class ProposalContent {
        public String type;
        public ProposalValue value;
    }

    public static class ProposalValue {
        public String title;
        public String description;
    }
}
