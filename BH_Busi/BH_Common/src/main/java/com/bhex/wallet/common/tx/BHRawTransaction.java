package com.bhex.wallet.common.tx;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 22:45
 */
public class BHRawTransaction {
    public String chain_id = BHConstants.CHAIN_ID;
    //public String cu_number = "0";
    public TxFee fee;
    public String memo;
    public List<TxMsg> msgs;
    public String sequence;

    /**
     * 创建交易
     * @param sequence
     * @param from
     * @param to
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param memo
     * @return
     */
    public static BHRawTransaction createBHRawTransaction(String sequence, String from, String to,
                                                          BigInteger amount, BigInteger feeAmount, BigInteger gasPrice,
                                                          String memo, String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();
        //开始创建一个交易TxMsg
        TxMsg<TransferMsg> msg = new TxMsg<TransferMsg>();
        msg.type = TRANSCATION_BUSI_TYPE.转账.getType();

        TransferMsg transferMsg = new TransferMsg();
        msg.value = transferMsg;

        transferMsg.amount = new ArrayList<>();

        //转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        transferMsg.amount.add(coin);


        transferMsg.from_address = from;
        transferMsg.to_address = to;

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;

    }

    /**
     * @param sequence
     * @param from
     * @param to
     * @param feeAmount
     * @param gasPrice
     * @param memo
     * @param symbol
     * @return
     */
    public static BHRawTransaction createBHCrossGenerateTransaction(String sequence, String from, String to,
                                                                    BigInteger feeAmount, BigInteger gasPrice,
                                                                    String memo, String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<KeyGenMsg> msg = new TxMsg<KeyGenMsg>();


        //msg.type = "bhchain/keygen/MsgKeyGen";
        msg.type = TRANSCATION_BUSI_TYPE.跨链地址生成.getType();
        KeyGenMsg keyGenMsg = new KeyGenMsg();
        msg.value = keyGenMsg;

        keyGenMsg.from = from;
        keyGenMsg.to = to;
        keyGenMsg.order_id = UUID.randomUUID().toString();
        keyGenMsg.symbol = symbol;

        /*//转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        transferMsg.amount.add(coin);


        transferMsg.from_address = from;
        transferMsg.to_address = to;*/

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;

    }

    /**
     * @param sequence
     * @param from
     * @param to
     * @param feeAmount
     * @param gasPrice
     * @param memo
     * @param symbol
     * @return
     */
    public static BHRawTransaction createBHCrossWithdrawalTransaction(String sequence, String from, String to,
                                                                      BigInteger amount, BigInteger feeAmount, BigInteger gasFeeAmount, BigInteger gasPrice,
                                                                      String memo, String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<WithdrawalMsg> msg = new TxMsg<WithdrawalMsg>();
        msg.type = TRANSCATION_BUSI_TYPE.跨链提币.getType();
        WithdrawalMsg withdrawalMsg = new WithdrawalMsg();
        msg.value = withdrawalMsg;

        withdrawalMsg.from_cu = from;
        withdrawalMsg.to_multi_sign_address = to;
        withdrawalMsg.symbol = symbol;
        withdrawalMsg.amount = amount.toString(10);
        withdrawalMsg.gas_fee = gasFeeAmount.toString(10);
        withdrawalMsg.order_id = UUID.randomUUID().toString();

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;

    }


    /**
     * 委托
     *
     * @param sequence
     * @param delegatorAddress
     * @param validatorAddress
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param symbol
     * @return
     */
    public static BHRawTransaction createBHDoEntrustTransaction(String sequence, String delegatorAddress, String validatorAddress,
                                                                BigInteger amount, BigInteger feeAmount, BigInteger gasPrice,
                                                                String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个委托TxMsg
        TxMsg<DoEntrustMsg> msg = new TxMsg<DoEntrustMsg>();
        msg.type = TRANSCATION_BUSI_TYPE.委托.getType();

        DoEntrustMsg doEntrustMsg = new DoEntrustMsg();
        msg.value = doEntrustMsg;

        //转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        doEntrustMsg.amount = coin;


        doEntrustMsg.validator_address = validatorAddress;
        doEntrustMsg.delegator_address = delegatorAddress;

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;

    }


    public static BHRawTransaction createBHRelieveEntrustTransaction(String sequence, String delegatorAddress, String validatorAddress,
                                                                     BigInteger amount, BigInteger feeAmount, BigInteger gasPrice,
                                                                      String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个委托TxMsg
        TxMsg<DoEntrustMsg> msg = new TxMsg<DoEntrustMsg>();

        msg.type = TRANSCATION_BUSI_TYPE.取消委托.getType();
        DoEntrustMsg doEntrustMsg = new DoEntrustMsg();
        msg.value = doEntrustMsg;

        //转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        doEntrustMsg.amount = coin;


        doEntrustMsg.validator_address = validatorAddress;
        doEntrustMsg.delegator_address = delegatorAddress;

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;

    }

    public static BHRawTransaction createBHDoPledgeTransaction(String sequence, String delegatorAddress, String proposalId,
                                                               BigInteger amount, BigInteger feeAmount, BigInteger gasPrice,
                                                               String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<PledgeMsg> msg = new TxMsg<PledgeMsg>();
        msg.type = TRANSCATION_BUSI_TYPE.治理提案质押.getType();

        PledgeMsg pledgeMsg = new PledgeMsg();
        msg.value = pledgeMsg;

        pledgeMsg.amount = new ArrayList<>();

        //转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        pledgeMsg.amount.add(coin);


        pledgeMsg.depositor = delegatorAddress;
        pledgeMsg.proposal_id = proposalId;

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    public static BHRawTransaction createBHDoVetoTransaction(String sequence, String delegatorAddress, String option, String proposalId,
                                                             BigInteger feeAmount, BigInteger gasPrice,  String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<VetoMsg> msg = new TxMsg<VetoMsg>();

        msg.type = TRANSCATION_BUSI_TYPE.治理提案投票.getType();

        VetoMsg vetoMsg = new VetoMsg();
        msg.value = vetoMsg;

        vetoMsg.voter = delegatorAddress;

        vetoMsg.option = option;
        vetoMsg.proposal_id = proposalId;

        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    public static BHRawTransaction createBHCreateProposalTransaction(String sequence, String delegatorAddress, String type, String title,
                                                                     String description, BigInteger amount, BigInteger feeAmount, BigInteger gasPrice,
                                                                     String symbol) {
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<CreateProposalMsg> msg = new TxMsg<CreateProposalMsg>();

        msg.type = TRANSCATION_BUSI_TYPE.发起治理提案.getType();
        CreateProposalMsg createProposalMsg = new CreateProposalMsg();
        msg.value = createProposalMsg;

        createProposalMsg.initial_deposit = new ArrayList<>();

        //转账Amount
        TxCoin coin = new TxCoin();
        coin.denom = symbol;
        coin.amount = amount.toString(10);

        createProposalMsg.initial_deposit.add(coin);
        createProposalMsg.proposer = delegatorAddress;
        CreateProposalMsg.ProposalContent content = new CreateProposalMsg.ProposalContent();
        content.type = type;
        CreateProposalMsg.ProposalValue value = new CreateProposalMsg.ProposalValue();
        value.description = description;
        value.title = title;
        content.value = value;
        createProposalMsg.content = content;
        //完成创建一个交易TxMsg
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }


    //构建提取收益
    public static BHRawTransaction createBHRawRewardTransaction(String sequence, BigInteger amount, BigInteger feeAmount,
                                                                BigInteger gasPrice,List<ValidatorMsg>list){

        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        for(ValidatorMsg item:list){
            TxMsg<ValidatorMsg> msg = new TxMsg<ValidatorMsg>();
            msg.type = TRANSCATION_BUSI_TYPE.提取收益.getType();
            msg.value = item;
            bhRawTransaction.msgs.add(msg);
        }

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    //构建复投分红交易
    public static BHRawTransaction createBHRawReDoEntrust(String sequence, BigInteger feeAmount,
                                                                BigInteger gasPrice,String memo,List<ValidatorMsg>validatorMsgs,
                                                          List<DoEntrustMsg> doEntrustMsgs){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个提取收益交易
        for(ValidatorMsg item:validatorMsgs){
            TxMsg<ValidatorMsg> msg = new TxMsg<ValidatorMsg>();
            msg.type = TRANSCATION_BUSI_TYPE.提取收益.getType();
            msg.value = item;
            bhRawTransaction.msgs.add(msg);
        }

        //构建委托交易
        for(DoEntrustMsg item:doEntrustMsgs){
            //开始创建一个委托TxMsg
            TxMsg<DoEntrustMsg> msg = new TxMsg<DoEntrustMsg>();
            msg.type = TRANSCATION_BUSI_TYPE.委托.getType();
            msg.value = item;
            //转账Amount
            bhRawTransaction.msgs.add(msg);
        }


        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    //代币发行
    public static BHRawTransaction createBHRawTokenRelease(BHTokenRlease tokenRlease,
                                                           BigInteger feeAmount,
                                                           //BigInteger gasPrice,
                                                           String sequence) {
        BHRawTransaction bhRawTransaction =  new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();

        //代币发行数据
        TxMsg<BHTokenRlease> msg = new TxMsg<BHTokenRlease>();
        msg.value = tokenRlease;
        msg.type = TRANSCATION_BUSI_TYPE.代币发行.getType();
        bhRawTransaction.msgs.add(msg);

        //转账手续费
        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    //映射
    public static BHRawTransaction createBHRawMappingSwap(String fromUser, String issue_symbol, String coinSymbol, BigInteger amount, BigInteger feeAmount,String sequence){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = BHConstants.BH_MEMO;
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();
        TxMsg<MappingSwapMsg> txMsg = new TxMsg();
        bhRawTransaction.msgs.add(txMsg);

        txMsg.type = TRANSCATION_BUSI_TYPE.映射.getType();

        MappingSwapMsg mappingSwapMsg = new MappingSwapMsg();
        txMsg.value = mappingSwapMsg;

        mappingSwapMsg.from = fromUser;
        mappingSwapMsg.issue_symbol = issue_symbol;

        TxCoin txCoin = new TxCoin();
        txCoin.amount = amount.toString(10);
        txCoin.denom = coinSymbol;
        mappingSwapMsg.coins.add(txCoin);

        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    //流动性
    public static BHRawTransaction createBHRaw_transcation(String type, JsonObject json, BigInteger feeAmount, String sequence){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = "";
        bhRawTransaction.sequence = sequence;
        bhRawTransaction.msgs = new ArrayList<>();
        TxMsg<JsonObject> txMsg = new TxMsg();
        bhRawTransaction.msgs.add(txMsg);

        txMsg.type = type;
        txMsg.value = json;

        bhRawTransaction.fee = getTransactionFee(feeAmount);
        return bhRawTransaction;
    }

    public static TxFee getTransactionFee(BigInteger feeAmount){
        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();

        TxCoin feeCoin = new TxCoin();
        feeCoin.amount = feeAmount.toString(10);
        feeCoin.denom = BHConstants.BHT_TOKEN;
        fee.amount.add(feeCoin);
        fee.gas = NumberUtil.mulExt("2",String.valueOf(Math.pow(10,6))).toString();
        return  fee;
    }

    public static class Builder {
        public String chain_id = BHConstants.CHAIN_ID;
        public TxFee fee;
        public String memo;
        public List<TxMsg> msgs;
        public String sequence;

        public BHRawTransaction build(BigInteger feeAmount) {
            BHRawTransaction rawTransaction = new BHRawTransaction();
            rawTransaction.fee = getTransactionFee(feeAmount);
            return rawTransaction;
        }
    }
}
