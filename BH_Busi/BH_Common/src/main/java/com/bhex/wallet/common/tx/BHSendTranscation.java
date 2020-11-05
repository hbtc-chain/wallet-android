package com.bhex.wallet.common.tx;

import java.util.ArrayList;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/2
 * Time: 17:52
 */
public class BHSendTranscation {

    public String mode;
    public TxReq tx = new TxReq();

    /**
     *
     * @param bhRawTransaction
     * @param sign
     * @return
     */
    /*public static BHSendTranscation createBHSendTransaction(BHRawTransaction bhRawTransaction,
                                                            BHCredentials bhCredentials,
                                                            String sign,String mode){
        BHSendTranscation bhSendTranscation = new BHSendTranscation();
        bhSendTranscation.mode = mode;
        bhSendTranscation.tx.memo= bhRawTransaction.memo;
        bhSendTranscation.tx.fee = bhRawTransaction.fee;
        bhSendTranscation.tx.msg = bhRawTransaction.msgs;
        bhSendTranscation.tx.signatures = new ArrayList<>();
        TxSignature signature = TxSignature.createTxSignature(bhCredentials,sign);
        bhSendTranscation.tx.signatures.add(signature);
        return bhSendTranscation;
    }*/

    public static BHSendTranscation createBHSendTransaction2(BHRawTransaction bhRawTransaction,
                                                             BHCredentials bhCredentials,
                                                             String sign, String mode){
        BHSendTranscation bhSendTranscation = new BHSendTranscation();
        bhSendTranscation.mode = mode;
        bhSendTranscation.tx.memo= bhRawTransaction.memo;
        bhSendTranscation.tx.fee = bhRawTransaction.fee;
        bhSendTranscation.tx.msg = bhRawTransaction.msgs;
        bhSendTranscation.tx.signatures = new ArrayList<>();
        TxReq.TxSignature signature = TxReq.TxSignature.createTxSignature(bhCredentials,sign);
        bhSendTranscation.tx.signatures.add(signature);
        return bhSendTranscation;
    }
}
