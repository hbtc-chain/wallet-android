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

    public static class TxMsg<T> {
        public String type;
        public T value;
    }

    public static class TxSignature{
        public TxSignature.Pubkey pub_key;
        public String signature;

        public class Pubkey{
            public String type;
            public String value;
        }

        public static TxSignature createTxSignature(BHCredentials credentials, String sign){
            TxSignature txSignature = new TxSignature();
            txSignature.pub_key = txSignature.createPubkey("tendermint/PubKeySecp256k1",credentials.base64Pubkey);
            txSignature.signature = sign;
            return txSignature;
        }

        public TxSignature.Pubkey createPubkey(String type, String value){
            TxSignature.Pubkey pubkey = new TxSignature.Pubkey();
            pubkey.type = type;
            pubkey.value = value;
            return pubkey;
        }
    }
}
