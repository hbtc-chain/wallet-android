package com.bhex.wallet.common.tx;

import android.text.TextUtils;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.api.TransactionApi;
import com.bhex.wallet.common.api.TransactionApiInterface;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.utils.BHKey;
import com.google.gson.JsonObject;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/2
 * Time: 17:51
 */
public class BHTransactionManager {

    /**
     * @param privateKey
     * @param from
     * @param to
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param data
     * @return
     */
    public static BHSendTranscation transfer(String privateKey,
                                      String from, String to,
                                      String amount,
                                      String feeAmount,
                                      BigInteger gasPrice,
                                      String memo,
                                      String data,
                                      String sequence,
                                      String symbol){

        BHRawTransaction bhRawTransaction = null;

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        double double_amount = NumberUtil.mul(String.valueOf(Math.pow(10,bhToken.decimals)),amount);

        //BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));
        double double_feeAmount = NumberUtil.mul(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(privateKey);


        //获取交易的笔数
        //String sequence = "";

        //交易数据构建
        bhRawTransaction = BHRawTransaction.createBHRawTransaction(sequence,
                from,to, BigDecimal.valueOf( double_amount).toBigInteger(),
                BigDecimal.valueOf( double_feeAmount).toBigInteger()
                ,gasPrice,memo,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,"block");

        return bhSendTranscation;


        //
    }

    public static String signBHRawTranscation(BHCredentials bhCredentials,String message){
        Sha256Hash input = Sha256Hash.wrap(Sha256.from(message.getBytes()).toString());
        ECKey.ECDSASignature signature =  bhCredentials.ecKey.sign(input);
        String sign = BHKey.signECDSA(signature);
        return sign;
    }

    public static void loadSuquece(CallbackSuquece callback){

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(jsonObject -> {
                    AccountInfo accountInfo = JsonUtils.fromJson(jsonObject.toString(),AccountInfo.class);
                    if(TextUtils.isEmpty(accountInfo.getSequence())){
                        return;
                    }
                    if(callback!=null){
                        callback.onSuquece(accountInfo.getSequence());
                    }

                });
    }


    /**
     * 跨链地址生成
     */
    public static BHSendTranscation crossLinkAddress(String privateKey,
                                             String from, String to,
                                             String feeAmount,
                                             BigInteger gasPrice,
                                             String memo,
                                             String data,
                                             String sequence,String symbol){

        BHRawTransaction bhRawTransaction = null;

        BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(privateKey);

        //交易数据构建
        bhRawTransaction = BHRawTransaction.createBHCrossGenerateTransaction(sequence,
                from,to, long_feeAmount,gasPrice,memo,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,"block");
        return bhSendTranscation;
    }

    /**
     * 跨链地址生成
     */
    public static BHSendTranscation crossLinkTransfer(String privateKey,
                                                     String from, String to,
                                                     String amount,
                                                     String feeAmount,
                                                     BigInteger gasPrice,
                                                     String memo,
                                                     String data,
                                                     String sequence,String symbol){

        BHRawTransaction bhRawTransaction = null;

        BHToken symbolBHToken = SymbolCache.getInstance().getBHToken(symbol);

        double double_amount = NumberUtil.mul(String.valueOf(Math.pow(10,symbolBHToken.decimals)),amount);

        //LogUtils.d("BHTransactionManager===>:","=long_Amount=="+BigDecimal.valueOf( double_amount).toBigInteger());
        //BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));

        double double_feeAmount = NumberUtil.mul(String.valueOf(BHConstants.BHT_DECIMALS),feeAmount);

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(privateKey);

        double double_gas_fee = Double.parseDouble(symbolBHToken.withdrawal_fee);
        //交易数据构建
        bhRawTransaction = BHRawTransaction.createBHCrossWithdrawalTransaction(sequence,
                from,to,
                BigDecimal.valueOf( double_amount).toBigInteger(),
                BigDecimal.valueOf(double_feeAmount).toBigInteger(),
                BigDecimal.valueOf(double_gas_fee).toBigInteger(),
                gasPrice,memo,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,"block");
        return bhSendTranscation;
    }
    /**
     * 获取suquece
     */
    public interface CallbackSuquece{
        int onSuquece(String suquece);
    }
}
