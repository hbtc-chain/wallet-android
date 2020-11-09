package com.bhex.wallet.common.tx;

import com.bhex.tools.crypto.HexUtils;

import org.bitcoinj.core.ECKey;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 14:58
 */
/*public class TxSignature{
    public Pubkey pub_key;
    public String signature;

    public class Pubkey{
        public String type;
        public String value;
    }

    public static TxSignature createTxSignature(BHCredentials credentials,String sign){
        TxSignature txSignature = new TxSignature();
        txSignature.pub_key = txSignature.createPubkey("tendermint/PubKeySecp256k1",credentials.base64Pubkey);
        txSignature.signature = sign;
        return txSignature;
    }

    public  Pubkey createPubkey(String type,String value){
        Pubkey pubkey = new Pubkey();
        pubkey.type = type;
        pubkey.value = value;
        return pubkey;
    }
}*/
