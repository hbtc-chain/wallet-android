package com.bhex.wallet.common.tx;

import java.util.ArrayList;
import java.util.List;

public class TransactionMsg {

    //转账
    public static class TransferMsg{
        public String from_address;
        public String to_address;
        public List<TxFee.TxCoin> amount;
    }

    //治理提案
    public static class CreateProposalMsg {
        public ProposalContent content;
        public String proposer;
        public List<TxFee.TxCoin> initial_deposit;


        public static class ProposalContent {
            public String type;
            public ProposalValue value;
        }

        public static class ProposalValue {
            public String title;
            public String description;
        }
    }

    //委托
    public static class DoEntrustMsg {
        public String delegator_address;
        public String validator_address;
        public TxFee.TxCoin amount;
    }

    //地址生成
    public static class KeyGenMsg {

        public String order_id;
        public String symbol;
        public String from;
        public String to;

        public KeyGenMsg() {
        }

        public KeyGenMsg(String order_id, String symbol, String from, String to) {
            this.order_id = order_id;
            this.symbol = symbol;
            this.from = from;
            this.to = to;
        }
    }

    //验证人
    public static class ValidatorMsg {

        public String delegator_address;

        public String validator_address;

        public ValidatorMsg(String delegator_address, String validator_address) {
            this.delegator_address = delegator_address;
            this.validator_address = validator_address;
        }
    }

    //投票
    public static class VetoMsg {
        public String option;
        public String proposal_id;
        public String voter;
    }

    //提币
    public static class WithdrawalMsg {

        //发交易账户，本交易需要此地址的私钥签名
        public String from_cu;
        //跨链提币的地址
        public String to_multi_sign_address;
        //币种名称
        public String symbol;
        //数量
        public String amount;
        //跨链转账的手续费
        public String gas_fee;
        //全局唯一的字符串，需要客户端自己生成；
        public String order_id;
    }

    //质押
    public static class PledgeMsg {
        public String proposal_id;
        public String depositor;
        public List<TxFee.TxCoin> amount;
    }

    //映射兑换
    public static class MappingSwapMsg {

        //交易发起人
        public String from;
        //映射对的symbol
        public String issue_symbol;
        public List<TxFee.TxCoin> coins = new ArrayList<>();

    }
}
