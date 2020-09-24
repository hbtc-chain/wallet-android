package com.bhex.wallet.common.tx;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.JsonSortUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.utils.BHKey;
import com.google.gson.JsonObject;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/2
 * Time: 17:51
 */
public class BHTransactionManager {

    /**
     * @param to
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param data
     * @return
     */
    public static BHSendTranscation transfer(
                                      String to,
                                      String amount,
                                      String feeAmount,
                                      BigInteger gasPrice,
                                      String data,
                                      String sequence,
                                      String symbol){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),amount);
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        //获取交易的笔数
        String from = BHUserManager.getInstance().getCurrentBhWallet().address;
        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRawTransaction(sequence,
                from,to,
                double_amount,double_feeAmount
                ,gasPrice,BHConstants.BH_MEMO,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

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
    public static BHSendTranscation crossLinkAddress(
                                             String feeAmount,
                                             BigInteger gasPrice,
                                             String data,
                                             String sequence,String symbol){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        BHRawTransaction bhRawTransaction = null;

        BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);
        //地址
        String from_address = BHUserManager.getInstance().getCurrentBhWallet().getAddress();
        String to_address = BHUserManager.getInstance().getCurrentBhWallet().getAddress();
        //交易数据构建
        bhRawTransaction = BHRawTransaction.createBHCrossGenerateTransaction(sequence,
                from_address,to_address, long_feeAmount,gasPrice,BHConstants.BH_MEMO,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }

    /**
     * 跨链提币
     */
    public static BHSendTranscation crossLinkTransfer(
                                                     String to,
                                                     String amount,
                                                     String feeAmount,
                                                     BigInteger gasPrice,
                                                     String withDrawFeeAmount,
                                                     String data,
                                                     String sequence,String symbol){

        String from = BHUserManager.getInstance().getCurrentBhWallet().address;

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey,MD5.md5(data));

        BHToken symbolBHToken = SymbolCache.getInstance().getBHToken(symbol);

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,symbolBHToken.decimals)),amount);

        //交易手续费
        BigInteger double_feeAmount = NumberUtil.mulExt(String.valueOf(BHConstants.BHT_DECIMALS),feeAmount);

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        //提币手续费
        BHToken withDrawFeeBHToken = SymbolCache.getInstance().getBHToken(symbolBHToken.chain);

        BigInteger double_gas_fee =  NumberUtil.mulExt(String.valueOf(Math.pow(10,withDrawFeeBHToken.decimals)),withDrawFeeAmount);;

        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHCrossWithdrawalTransaction(sequence,
                from,to,
                double_amount,
                double_feeAmount,
                double_gas_fee,
                gasPrice,BHConstants.BH_MEMO,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }

    /**
     * 委托
     * @param validatorAddress
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param data
     * @param sequence
     * @param symbol
     * @return
     */
    public static BHSendTranscation doEntrust(
                                             String validatorAddress,
                                             String amount,
                                             String feeAmount,
                                             BigInteger gasPrice,
                                             String data,
                                             String sequence,
                                             String symbol){

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey,MD5.md5(data));

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),amount);

        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        String delegatorAddress = BHUserManager.getInstance().getCurrentBhWallet().address;

        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHDoEntrustTransaction(sequence,
                delegatorAddress,validatorAddress,double_amount,
                double_feeAmount
                ,gasPrice,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;
        //
    }

    /**
     * 解委托
     * @param validatorAddress
     * @param amount
     * @param feeAmount
     * @param gasPrice
     * @param data
     * @param sequence
     * @param symbol
     * @return
     */
    public static BHSendTranscation relieveEntrust(
                                              String validatorAddress,
                                              String amount,
                                              String feeAmount,
                                              BigInteger gasPrice,
                                              String data,
                                              String sequence,
                                              String symbol){

        String delegatorAddress = BHUserManager.getInstance().getCurrentBhWallet().address;

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),amount);

        //BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRelieveEntrustTransaction(sequence,
                delegatorAddress,validatorAddress,
                double_amount,double_feeAmount
                ,gasPrice,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;
        //
    }

    //投票
    public static BHSendTranscation doVeto(
                                             String delegatorAddress, String proposalId,
                                             String option,
                                             String feeAmount,
                                             BigInteger gasPrice,
                                             String data,
                                             String sequence,
                                             String symbol){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);


        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHDoVetoTransaction(sequence,
                delegatorAddress,option,proposalId,
                double_feeAmount,
                gasPrice,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;


        //
    }

    //质押
    public static BHSendTranscation doPledge(String delegatorAddress, String proposalId,
                                             String amount,
                                             String feeAmount,
                                             BigInteger gasPrice,
                                             String data,
                                             String sequence,
                                             String symbol){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),amount);

        //BigInteger long_feeAmount = BigInteger.valueOf((long)(BHConstants.BHT_DECIMALS *Double.valueOf(feeAmount)));
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);


        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHDoPledgeTransaction(sequence,
                delegatorAddress,proposalId,
                double_amount,double_feeAmount
                ,gasPrice,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;
    }

    //创建治理提案
    public static BHSendTranscation createProposal(
                                           String type,
                                           String title,String description,
                                           String amount,
                                           String feeAmount,
                                           BigInteger gasPrice,
                                           String data,
                                           String sequence,
                                           String symbol){

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),amount);

        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        //委托地址
        String delegatorAddress = BHUserManager.getInstance().getCurrentBhWallet().address;

        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHCreateProposalTransaction(sequence,
                delegatorAddress,type,title,description,
                double_amount,double_feeAmount,
                gasPrice,symbol);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;
        //
    }

    //提取收益
    public static BHSendTranscation withDrawReward(List<ValidatorMsg> list,
                                      String amount,
                                      String feeAmount,
                                      BigInteger gasPrice,
                                      String data,
                                      String sequence){

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        //交易数量
        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(BHConstants.BHT_DECIMALS),amount);
        //手续费数量
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRawRewardTransaction(sequence,double_amount,
                double_feeAmount,gasPrice,list);

        String raw_json = JsonUtils.toJson(bhRawTransaction);
        //LogUtils.d("BHTransactionManager===>:","raw_json=="+raw_json);

        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);
        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }


    //复投分红
    public static BHSendTranscation toReDoEntrust(List<ValidatorMsg> validatorMsgs,
                                                   List<DoEntrustMsg> doEntrustMsgs,
                                                   String amount,
                                                   String feeAmount,
                                                   BigInteger gasPrice,
                                                   String data,
                                                   String sequence){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);
        //交易数量
        //double double_amount = NumberUtil.mul(String.valueOf(BHConstants.BHT_DECIMALS),amount);
        //手续费数量
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        //交易数据构建
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRawReDoEntrust(sequence, double_feeAmount,gasPrice,
                BHConstants.BH_MEMO,validatorMsgs,doEntrustMsgs);

        String raw_json = JsonUtils.toJson(bhRawTransaction);

        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);
        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);

        return bhSendTranscation;
    }

    //代币发行
    public static BHSendTranscation hrc20TokenRelease(BHTokenRlease tokenRlease,String feeAmount,String sequence,String data){

        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));

        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);

        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));

        //构建代币发行交易数据
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRawTokenRelease(tokenRlease,double_feeAmount,sequence);
        String raw_json = JsonUtils.toJson(bhRawTransaction);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);

        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }

    /**
     * 映射
     * @return
     */
    public static BHSendTranscation createMappingSwap(String to_symbol,String fromSymbol,String mappingAmount,
                                                String feeAmount,String sequence,String data){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);
        //mappingAmount
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(fromSymbol.toLowerCase());
        BigInteger double_amount = NumberUtil.mulExt(String.valueOf(Math.pow(10,bhToken.decimals)),mappingAmount);
        //手续费数量
        BigInteger double_feeAmount = NumberUtil.mulExt(feeAmount,String.valueOf(BHConstants.BHT_DECIMALS));
        //构建映射交易数据类型
        String address = BHUserManager.getInstance().getCurrentBhWallet().address;
        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRawMappingSwap(address,to_symbol.toLowerCase(),fromSymbol.toLowerCase(),
                double_amount,double_feeAmount,sequence);
        String raw_json = JsonUtils.toJson(bhRawTransaction);
        LogUtils.d("BHSendTranscation===>:","raw_json=="+raw_json);
        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);
        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }

    /**
     * 增加流动性
     * @return
     */
    public static BHSendTranscation create_dex_transcation(String type,JsonObject json, String sequence, String data){
        String pk = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey, MD5.md5(data));
        BHCredentials bhCredentials = BHCredentials.createBHCredentials(pk);
        BigInteger double_feeAmount = NumberUtil.mulExt(BHConstants.BHT_DEFAULT_FEE,String.valueOf(BHConstants.BHT_DECIMALS));

        BHRawTransaction bhRawTransaction = BHRawTransaction.createBHRaw_transcation(type,json,double_feeAmount,sequence);
        String raw_json = JsonUtils.toJson(bhRawTransaction);

        raw_json = JSONObject.toJSONString(JSONObject.parseObject(raw_json), SerializerFeature.SortField.MapSortField);
        LogUtils.d("BHSendTranscation===>:","raw_json=="+raw_json);

        String sign = BHTransactionManager.signBHRawTranscation(bhCredentials,raw_json);
        //交易请求数据构建
        BHSendTranscation bhSendTranscation = BHSendTranscation.createBHSendTransaction(bhRawTransaction,bhCredentials,sign,BHConstants.TRANSCTION_MODE);
        return bhSendTranscation;
    }

    /**
     * 获取suquece
     */
    public interface CallbackSuquece{
        int onSuquece(String suquece);
    }
}
