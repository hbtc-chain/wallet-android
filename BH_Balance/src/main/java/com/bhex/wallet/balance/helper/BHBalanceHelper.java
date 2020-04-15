package com.bhex.wallet.balance.helper;

import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.ui.activity.AssetDetailActivity;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.RatesCache;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHRates;
import com.bhex.wallet.common.model.BHToken;

import java.text.DecimalFormat;

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
            item.resId = R.mipmap.ic_bht;
        }else if(symbol.equalsIgnoreCase("btc")){
            item.resId = R.mipmap.ic_btc;
        }else if(symbol.equalsIgnoreCase("eth")){
            item.resId = R.mipmap.ic_eth;
        }else if(symbol.equalsIgnoreCase("eos")){
            item.resId = R.mipmap.ic_eos;
        }else if(symbol.equalsIgnoreCase("usdt")){
            item.resId = R.mipmap.ic_usdt;
        }else{
            item.resId = R.mipmap.ic_usdt;
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
    public static String[]  getAmountToCurrencyValue(Context context,String amount, String symbol){
        String []result = new String[2];

        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());

        //RatesCache ratesCache = CacheCenter.getInstance().getRatesCache();
        int decimals = bhToken!=null?bhToken.decimals:2;
        decimals = 0;
        double displayAmount = NumberUtil.divide(amount, Math.pow(10,decimals)+"",3);

        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
        result[0] = NumberUtil.formatValue(displayAmount,3);

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
        double tmp = NumberUtil.sub(TextUtils.isEmpty(amount)?"0":amount,TextUtils.isEmpty(frozen_amount)?"0":frozen_amount);

        double displayAmount = NumberUtil.divide(String.valueOf(tmp), Math.pow(10,decimals)+"",3);

        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
       return NumberUtil.formatValue(displayAmount,3);
    }

    public static void setTokenIcon(BaseActivity context, String symbol, AppCompatImageView iv){
        int resId = 0;
        if(symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            resId = R.mipmap.ic_bht;
        }else if(symbol.equalsIgnoreCase("btc")){
            resId = R.mipmap.ic_btc;
        }else if(symbol.equalsIgnoreCase("eth")){
            resId = R.mipmap.ic_eth;
        }else if(symbol.equalsIgnoreCase("eos")){
            resId = R.mipmap.ic_eos;
        }else if(symbol.equalsIgnoreCase("usdt")){
            resId = R.mipmap.ic_usdt;
        }else{
            resId = R.mipmap.ic_usdt;
        }

        iv.setImageDrawable(ContextCompat.getDrawable(context,resId));
    }
}
