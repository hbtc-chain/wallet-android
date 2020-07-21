package com.bhex.wallet.balance.helper;

import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;

import java.util.Iterator;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/5
 * Time: 16:51
 */
public class BHBalanceHelper {

    public static BHBalance getBHBalanceBySymbol(String symbol){
        BHBalance item = new BHBalance();
        if(symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            item.resId = R.mipmap.ic_token;
        }else if(symbol.equalsIgnoreCase("btc")){
            item.resId = R.mipmap.ic_btc;
        }else if(symbol.equalsIgnoreCase("eth")){
            item.resId = R.mipmap.ic_eth;
        }else if(symbol.equalsIgnoreCase("eos")){
            item.resId = R.mipmap.ic_eos;
        }else if(symbol.equalsIgnoreCase("usdt")){
            item.resId = R.mipmap.ic_usdt;
        }else{
            item.resId = 0;
        }
        item.symbol = symbol;
        return item;
    }


    /**
     * 资产对应法币价值
     * @param amount
     * @param symbol
     * @return
     */
    public static String[]  getAmountToCurrencyValue(Context context,String amount, String symbol,boolean flag){
        String []result = new String[2];

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        //RatesCache ratesCache = CacheCenter.getInstance().getRatesCache();

        int decimals = bhToken!=null?bhToken.decimals:2;
        if(!flag){
            decimals = 0;
        }

        double displayAmount = NumberUtil.divide(amount, Math.pow(10,decimals)+"");
        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
        //result[0] = NumberUtil.formatValue(displayAmount,3);
        result[0] = NumberUtil.dispalyForUsertokenAmount(String.valueOf(displayAmount));

        //法币价值
        //BHRates.RatesBean ratesBean = ratesCache.getBHRate(symbol.toLowerCase());
        double symbolPrice = CurrencyManager.getInstance().getCurrencyRate(context,symbol);
        double asset = NumberUtil.mul(String.valueOf(displayAmount),String.valueOf(symbolPrice));
        result[1] = CurrencyManager.getInstance().getCurrencyDecription(context,asset);

        return result;
    }


    public static String getAmountForUser(BaseActivity context, String amount, String frozen_amount, String symbol) {
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());
        int decimals = bhToken!=null?bhToken.decimals:2;
        decimals = 0;
        String tmp = NumberUtil.sub(TextUtils.isEmpty(amount)?"0":amount,TextUtils.isEmpty(frozen_amount)?"0":frozen_amount);

        double displayAmount = NumberUtil.divide(tmp, Math.pow(10,decimals)+"");

        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
       return NumberUtil.dispalyForUsertokenAmount(String.valueOf(displayAmount));
    }

    public static void setTokenIcon(BaseActivity context, String symbol, AppCompatImageView iv){
        int resId = 0;
        if(symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            resId = R.mipmap.ic_token;
        }else if(symbol.equalsIgnoreCase("btc")){
            resId = R.mipmap.ic_btc;
        }else if(symbol.equalsIgnoreCase("eth")){
            resId = R.mipmap.ic_eth;
        }else if(symbol.equalsIgnoreCase("eos")){
            resId = R.mipmap.ic_eos;
        }else if(symbol.equalsIgnoreCase("usdt")){
            resId = R.mipmap.ic_usdt;
        }else{
            resId = R.mipmap.ic_default_coin;
        }

        iv.setImageDrawable(ContextCompat.getDrawable(context,resId));
    }

    /**
     * H
     * @param symbol
     * @return
     */
    public static BHBalance getBHBalanceFromAccount(String symbol){
        AccountInfo accountInfo = BHUserManager.getInstance().getAccountInfo();
        List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        BHBalance balance = new BHBalance();
        balance.amount="0";
        //balance.chain= BHConstants.BHT_TOKEN;
        balance.symbol = symbol;

        if(list==null || list.size()==0){
            return balance;
        }

        for (int i = 0; i < list.size(); i++) {
            AccountInfo.AssetsBean assetsBean = list.get(i);
            if(assetsBean.getSymbol()!=null && assetsBean.getSymbol().equalsIgnoreCase(symbol)){

                balance.symbol = assetsBean.getSymbol();
                balance.chain = assetsBean.getSymbol();
                balance.amount = assetsBean.getAmount();
                balance.frozen_amount = assetsBean.getFrozen_amount();
                balance.address = assetsBean.getExternal_address();
                return balance;
            }
        }
        return balance;
    }

    public static void removeCoinSeachBalance(List<BHBalance>originList,String symbol){
        if(ToolUtils.checkListIsEmpty(originList)||TextUtils.isEmpty(symbol)){
            return;
        }
        Iterator<BHBalance> iter = originList.iterator();
        while(iter.hasNext()){
            BHBalance bhBalance = iter.next();
            if(!TextUtils.isEmpty(bhBalance.symbol) && bhBalance.symbol.equalsIgnoreCase(symbol)){
                iter.remove();
            }
        }
    }

    public static void addCoinSeachBalance(List<BHBalance>originList,BHBalance bhBalance){
        if(originList==null||bhBalance==null){
            return;
        }
        if(originList.size()==0 && bhBalance!=null){
            originList.add(bhBalance);
            return;
        }
        //boolean flag = false;
        for (BHBalance item:originList) {
            if(!TextUtils.isEmpty(item.symbol) && item.symbol.equalsIgnoreCase(bhBalance.symbol)){
                //flag = true;
                LogUtils.d("CoinSearchActivity==>:","addCoinSeachBalance=33=");
                return;
            }
        }
        LogUtils.d("CoinSearchActivity==>:","addCoinSeachBalance==");

        originList.add(bhBalance);
    }
}
