package com.bhex.wallet.common.tx;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;

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
    public String chain_id = "hbtc-testnet";
    public String cu_number = "0";
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
    public static BHRawTransaction createBHRawTransaction(String sequence,  String from,String to,
                                                   BigInteger amount, BigInteger feeAmount,BigInteger gasPrice,
                                                          String memo,String symbol){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<TransferMsg> msg = new TxMsg<TransferMsg>();


        //msg.type = "cosmos-sdk/MsgSend";
        msg.type = "hbtcchain/transfer/MsgSend";

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
        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();

        TxCoin feeCoin = new TxCoin();
        feeCoin.amount = feeAmount.toString(10);
        feeCoin.denom = BHConstants.BHT_TOKEN;
        fee.amount.add(feeCoin);
        fee.gas = (long)NumberUtil.divide(feeAmount.toString(10),gasPrice.toString(10))+"";

        bhRawTransaction.fee = fee;
        return bhRawTransaction;

    }

    /**
     *
     * @param sequence
     * @param from
     * @param to
     * @param feeAmount
     * @param gasPrice
     * @param memo
     * @param symbol
     * @return
     */
    public static BHRawTransaction createBHCrossGenerateTransaction(String sequence,  String from,String to,
                                                          BigInteger feeAmount,BigInteger gasPrice,
                                                          String memo,String symbol){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<KeyGenMsg> msg = new TxMsg<KeyGenMsg>();


        //msg.type = "bhchain/keygen/MsgKeyGen";
        msg.type = "hbtcchain/keygen/MsgKeyGen";
        KeyGenMsg keyGenMsg = new KeyGenMsg();
        msg.value = keyGenMsg;

        keyGenMsg.From = from;
        keyGenMsg.To = to;
        keyGenMsg.OrderId = UUID.randomUUID().toString();
        keyGenMsg.Symbol = symbol;

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
        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();

        TxCoin feeCoin = new TxCoin();
        feeCoin.amount = feeAmount.toString(10);
        feeCoin.denom = BHConstants.BHT_TOKEN;
        fee.amount.add(feeCoin);
        fee.gas = (long)NumberUtil.divide(feeAmount.toString(10),gasPrice.toString(10))+"";

        bhRawTransaction.fee = fee;
        return bhRawTransaction;

    }

    /**
     *
     * @param sequence
     * @param from
     * @param to
     * @param feeAmount
     * @param gasPrice
     * @param memo
     * @param symbol
     * @return
     */
    public static BHRawTransaction createBHCrossWithdrawalTransaction(String sequence,  String from,String to,
                                                                      BigInteger amount,BigInteger feeAmount,BigInteger gasFeeAmount,BigInteger gasPrice,
                                                                    String memo,String symbol){
        BHRawTransaction bhRawTransaction = new BHRawTransaction();
        bhRawTransaction.memo = memo;
        bhRawTransaction.sequence = sequence;

        bhRawTransaction.msgs = new ArrayList<>();

        //开始创建一个交易TxMsg
        TxMsg<WithdrawalMsg> msg = new TxMsg<WithdrawalMsg>();


        //msg.type = "bhchain/keygen/MsgKeyGen";
        msg.type = "hbtcchain/transfer/MsgWithdrawal";
        WithdrawalMsg withdrawalMsg = new WithdrawalMsg();
        msg.value = withdrawalMsg;

        withdrawalMsg.from_cu = from;
        withdrawalMsg.to_multi_sign_address = to;
        withdrawalMsg.symbol = symbol;
        withdrawalMsg.amount = amount.toString(10);
        withdrawalMsg.gas_fee = gasFeeAmount.toString(10);
        withdrawalMsg.order_id = UUID.randomUUID().toString();

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
        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();

        TxCoin feeCoin = new TxCoin();
        feeCoin.amount = feeAmount.toString(10);
        feeCoin.denom = BHConstants.BHT_TOKEN;
        fee.amount.add(feeCoin);
        fee.gas = (long)NumberUtil.divide(feeAmount.toString(10),gasPrice.toString(10))+"";

        bhRawTransaction.fee = fee;
        return bhRawTransaction;

    }

    public BHRawTransaction addTransferMsg(TxMsg txMsg){
        this.msgs.add(txMsg);
        return this;
    }

    public static class Builder {
        //public String chain_id = "bhchain-testnet";
        public String chain_id = "hbtc-testnet";
        public String cu_number = "0";
        public TxFee fee;
        public String memo;
        public List<TxMsg> msgs;
        public String sequence;

        public BHRawTransaction build(){
            return new BHRawTransaction();
        }
    }
}