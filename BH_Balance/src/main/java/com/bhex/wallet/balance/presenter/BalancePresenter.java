package com.bhex.wallet.balance.presenter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.model.BHTokenItem;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.TransferMsg;
import com.bhex.wallet.common.tx.TxCoin;
import com.bhex.wallet.common.tx.TxFee;
import com.bhex.wallet.common.tx.TxMsg;
import com.bhex.wallet.common.tx.SendTxRequest;
import com.bhex.wallet.common.tx.Tx;
import com.bhex.wallet.common.tx.TxReq;
import com.bhex.wallet.common.tx.TxSignature;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/18
 * Time: 0:23
 */
public class BalancePresenter extends BasePresenter {

    public BalancePresenter(BaseActivity activity) {
        super(activity);
    }

    public List<BHBalance> makeBalanceList(){
        List<BHBalance> list = new ArrayList<>();
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        String[] coin_list = BHUserManager.getInstance().getUserBalanceList().split("_");
        for (int i = 0; i < coin_list.length; i++) {
            //String symbol = coin_list[i];
            BHBalance bhBalance = BHBalanceHelper.getBHBalanceBySymbol(coin_list[i]);
            BHToken bhToken = symbolCache.getBHToken(bhBalance.symbol.toLowerCase());
            if(bhToken!=null){
                bhBalance.chain = bhToken.chain;
                bhBalance.logo = bhToken.logo;
            }
            list.add(bhBalance);
        }
        return list;
    }


    //产生交易数据
    /*public String makeTranctionMsgJson(){
        Tx tx = new Tx();
        tx.chain_id = "bhchain-testnet";
        tx.cu_number = "0";
        tx.memo="";
        tx.sequence = "5";

        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();


        TxCoin coin = new TxCoin();
        coin.amount = "2000000000000000000";
        coin.denom = "bht";

        fee.amount.add(coin);

        fee.gas = "2000000";

        tx.fee = fee;
        tx.memo = "test memo";

        tx.msgs = new ArrayList<>();

        TxMsg msg = new TxMsg();
        msg.type = "cosmos-sdk/MsgSend";



        TransferMsg tmsg = new TransferMsg();
        //tmsg.from_address = "BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ";
        tmsg.from_address = "BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK";
        //tmsg.to_address = "BHV1EuSkWMYHWNa5bAUAm6DTbLzT4QPNoGn";
        tmsg.to_address = "BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ";

        tmsg.amount = new ArrayList<>();
        TxCoin coin0 = new TxCoin();
        coin0.amount = "50000000000000000000";
        coin0.denom="bht";

        tmsg.amount.add(coin0);

        msg.value = tmsg;

        tx.msgs.add(msg);

        String json = JsonUtils.toJson(tx);
        return json;
    }

    *//**
     * 模拟交易请求
     * @return
     *//*
    public String makeTranctionRequest(String sign,String pubBase64Value){
        //交易请求
        SendTxRequest sendTxRequest = new SendTxRequest();
        sendTxRequest.mode = "block";

        TxReq txReq = new TxReq();

        txReq.memo = "test memo";

        TxFee fee = new TxFee();
        fee.amount = new ArrayList<>();


        TxCoin coin = new TxCoin();
        coin.amount = "2000000000000000000";
        coin.denom = "bht";

        fee.amount.add(coin);

        fee.gas = "2000000";
        txReq.fee = fee;

        sendTxRequest.tx = txReq;

        txReq.msg = new ArrayList<>();

        //交易信息
        TxMsg msg = new TxMsg();
        msg.type = "cosmos-sdk/MsgSend";

        TransferMsg tmsg = new TransferMsg();
        tmsg.from_address = "BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK";
        tmsg.to_address = "BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ";

        tmsg.amount = new ArrayList<>();
        TxCoin coin0 = new TxCoin();
        coin0.amount = "50000000000000000000";
        coin0.denom="bht";

        tmsg.amount.add(coin0);

        msg.value = tmsg;

        //添加交易
        txReq.msg.add(msg);

        TxSignature txSignature = new TxSignature();
        txSignature.signature = sign;

        TxSignature.Pubkey pubkey = txSignature.new Pubkey();
        pubkey.type = "tendermint/PubKeySecp256k1";
        pubkey.value = pubBase64Value;

        txSignature.pub_key = pubkey;

        sendTxRequest.tx.signatures = new ArrayList<>();
        sendTxRequest.tx.signatures.add(txSignature);

        String json = JsonUtils.toJson(sendTxRequest);
        return json;
    }*/


    //获取balance位置
    public  int getIndexByCoin(List<BHBalance> list, BHTokenItem bhCoinItem){
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            BHBalance balance = list.get(i);
            if(balance.symbol.equalsIgnoreCase(bhCoinItem.symbol)){
                return i;
            }
        }
        return index;
    }

    public  BHBalance getBalanceByCoin(BHTokenItem coinItem){
        BHBalance balance = new BHBalance(coinItem.resId,coinItem.symbol);
        //LogUtils.d("BalancePresenter====>:",balance.symbol+"==resId=="+balance.resId);
        balance.chain = coinItem.chain;
        balance.logo = coinItem.logo;
        return balance;
    }


    public List<BHBalance> getBalanceList(List<BHBalance> list){
        List<BHBalance> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            res.add(list.get(i));

        }
        return res;
    }

    /**
     * 计算所有Token价值
     * @return
     */
    public double calculateAllTokenPrice(AccountInfo accountInfo,List<BHBalance> mOriginBalanceList){
        double allTokenPrice = 0;

        List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        if(list==null || list.size()==0){
            return allTokenPrice;
        }
        Map<String,AccountInfo.AssetsBean> map = new HashMap<>();
        for(AccountInfo.AssetsBean bean:list){
            map.put(bean.getSymbol(),bean);

            //计算每一个币种的资产价值
            double b1 = CurrencyManager.getInstance().getSymbolBalancePrice(mBaseActivity,bean.getSymbol(),bean.getAmount(),false);
            allTokenPrice = NumberUtil.add(b1,allTokenPrice);
        }
        for(BHBalance balance:mOriginBalanceList){
            AccountInfo.AssetsBean assetsBean = map.get(balance.symbol.toLowerCase());
            if(assetsBean==null){
                continue;
            }
            balance.isHasToken = 1;
            balance.amount = assetsBean.getAmount();
            balance.is_native = assetsBean.isIs_native();
            balance.external_address = assetsBean.getExternal_address();
            balance.frozen_amount = assetsBean.getFrozen_amount();
        }
        return allTokenPrice;
    }

    /**
     * 隐藏显示资产
     */
    public void hiddenAsset(BaseActivity context, AppCompatTextView tv_asset, AppCompatImageView eyeIv, BalanceAdapter balanceAdapter){
        String tag = (String) eyeIv.getTag();
        if(tag.equals(BH_BUSI_TYPE.显示.value)){
            tv_asset.setText("***");
            eyeIv.setTag(BH_BUSI_TYPE.隐藏.value);
            eyeIv.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_eye_close));
            balanceAdapter.setIsHidden(BH_BUSI_TYPE.隐藏.value);
        }else{
            String unhiddenText = tv_asset.getTag(R.id.tag_first).toString();
            SpannableString spanStr = new SpannableString(unhiddenText);
            spanStr.setSpan(new AbsoluteSizeSpan(PixelUtils.dp2px(getActivity(),15)), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_asset.setText(spanStr);
            eyeIv.setTag(BH_BUSI_TYPE.显示.value);
            eyeIv.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_eye));
            balanceAdapter.setIsHidden(BH_BUSI_TYPE.显示.value);
        }
    }


    /**
     * 隐藏小额资产
     * @param context
     * @param ck_hidden_small
     * @param mOriginBalanceList
     * @param text
     * @return
     */
    public List<BHBalance> hiddenSmallToken(BaseActivity context,CheckedTextView ck_hidden_small,List<BHBalance> mOriginBalanceList,String text){
        List<BHBalance> result = new ArrayList<>();

        if(ck_hidden_small.isChecked()){
            for(BHBalance item :mOriginBalanceList){
                if(!TextUtils.isEmpty(item.amount) && Double.valueOf(item.amount)>0){
                    if(!TextUtils.isEmpty(text)){
                        if(item.symbol.toLowerCase().contains(text.toLowerCase())){
                            result.add(item);
                        }
                    }else{
                        result.add(item);
                    }
                }
            }
            ck_hidden_small.setTextColor(ContextCompat.getColor(context,R.color.checkbox_checked_text_color));
        }else{
            for (BHBalance item :mOriginBalanceList) {
                if(!TextUtils.isEmpty(text)){
                    if(item.symbol.toLowerCase().contains(text.toLowerCase())){
                        result.add(item);
                    }
                }else{
                    result.add(item);
                }
            }
            ck_hidden_small.setTextColor(ContextCompat.getColor(context,R.color.checkbox_text_color));
        }

        return result;
    }


    public void setTextFristSamll(AppCompatTextView tv_asset, String allTokenAssetsText) {
        SpannableString spanStr = new SpannableString(allTokenAssetsText);
        spanStr.setSpan(new AbsoluteSizeSpan(PixelUtils.dp2px(getActivity(),15)), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_asset.setText(spanStr);
        tv_asset.setTag(R.id.tag_first,allTokenAssetsText);
    }


    public BHBalance getBthBalanceWithAccount(AccountInfo accountInfo){
        if(accountInfo==null){
            return null;
        }
        BHBalance balance = new BHBalance();
        balance.amount="";
        balance.chain= BHConstants.BHT_TOKEN;
        balance.symbol = BHConstants.BHT_TOKEN;

        List<AccountInfo.AssetsBean> assetsBeanList = accountInfo.getAssets();
        if(assetsBeanList==null || assetsBeanList.size()==0){
            return balance;
        }

        for(AccountInfo.AssetsBean assetsBean:assetsBeanList){
            if(assetsBean.getSymbol().equalsIgnoreCase(BHConstants.BHT_TOKEN)){
                balance.symbol = assetsBean.getSymbol();
                balance.chain = assetsBean.getSymbol();
                balance.amount = assetsBean.getAmount();
                balance.frozen_amount = assetsBean.getFrozen_amount();
                balance.address = assetsBean.getExternal_address();
            }
        }


        return balance;
    }

    /**
     * 是否为小额资产
     * @param accountInfo
     * @return
     */
    public boolean isSmallToken(AccountInfo accountInfo,String symbol){
        boolean flag = true;
        if(accountInfo.getAssets()==null ||accountInfo.getAssets().size()==0 ){
            return flag;
        }

        for(AccountInfo.AssetsBean assetsBean:accountInfo.getAssets()){
            if(assetsBean.getSymbol().equalsIgnoreCase(symbol)
                && !TextUtils.isEmpty(assetsBean.getAmount())
                && Double.valueOf(assetsBean.getAmount())>0){
                flag = false;
                return flag;
            }
        }
        return flag;
    }
}
