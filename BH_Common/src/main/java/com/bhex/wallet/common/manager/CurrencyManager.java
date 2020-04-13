package com.bhex.wallet.common.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.RatesCache;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.model.BHRates;
import com.bhex.wallet.common.model.BHToken;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 12:20
 */
public class CurrencyManager {

    private static CurrencyManager _instance;

    private String mCurrency;

    private NumberFormat currencyFormat = NumberFormat.getInstance();

    private CurrencyManager(){
        currencyFormat.setGroupingUsed(false);
        currencyFormat.setMaximumFractionDigits(3);
        currencyFormat.setMinimumFractionDigits(3);
        currencyFormat.setRoundingMode(RoundingMode.FLOOR);
    }

    public static CurrencyManager getInstance(){
        if(_instance==null){
            synchronized (CurrencyManager.class){
                if(_instance==null){
                    _instance = new CurrencyManager();
                }
            }
        }
        return _instance;
    }

    public String loadCurrency(Context context){
        mCurrency = "cny";
        return mCurrency;
    }

    public String getCurrencyRateDecription(Context context, String symbol){
        RatesCache ratesCache = CacheCenter.getInstance().getRatesCache();
        if(ratesCache==null){
            return "0";
        }

        BHRates.RatesBean ratesBean = ratesCache.getBHRate(symbol.toLowerCase());

        if(ratesBean==null){
            return "0";
        }
        if(CurrencyManager.getInstance().loadCurrency(context).equals("cny")){
            return "¥"+currencyFormat.format(Double.valueOf(ratesBean.getCny()));
        }else if(CurrencyManager.getInstance().loadCurrency(context).equals("usd")){
            return "$"+currencyFormat.format(Double.valueOf(ratesBean.getUsd()));
        }
        return "";
    }

    public String getCurrencyDecription(Context context, double value){

        if(CurrencyManager.getInstance().loadCurrency(context).equals("cny")){
            return "¥"+currencyFormat.format(value);
        }else if(CurrencyManager.getInstance().loadCurrency(context).equals("usd")){
            return "$"+currencyFormat.format(value);
        }
        return "";
    }

    public double getCurrencyRate(Context context, String symbol){
        RatesCache ratesCache = CacheCenter.getInstance().getRatesCache();
        if(ratesCache==null){
            return 0;
        }

        BHRates.RatesBean ratesBean = ratesCache.getBHRate(symbol.toLowerCase());

        if(ratesBean==null){
            return 0;
        }
        if(CurrencyManager.getInstance().loadCurrency(context).equals("cny")){
            return Double.valueOf(ratesBean.getCny());
        }else if(CurrencyManager.getInstance().loadCurrency(context).equals("usd")){
            return Double.valueOf(ratesBean.getUsd());
        }
        return 0;
    }

    public double getSymbolBalancePrice(Context context, String symbol,String amount){
        double balancePrice = 0;
        if(TextUtils.isEmpty(amount) || Double.valueOf(amount)==0){
            return balancePrice;
        }

        //获取汇率
        double symbolRate = getCurrencyRate(context,symbol);
        //获取精度
        BHToken token = CacheCenter.getInstance().getSymbolCache().getBHToken(symbol);

        double displayAmount = NumberUtil.divide(amount, Math.pow(10,token.decimals)+"",3);
        balancePrice = NumberUtil.mul(String.valueOf(displayAmount),String.valueOf(symbolRate));

        return balancePrice;
    }


    /**
     * 抽象货币类
     */
    private  abstract class AbsCurrency {
        // 货币符号
        protected String symbol;

        public AbsCurrency(String symbol) {
            this.symbol = symbol;
        }


        /**
         * 返回数量的描述文案。例如¥5、$5等
         * @param price
         * @return
         */
        public abstract String getDescription(String quote,double price, String defDescription);

        /**
         * 返回数量的描述文案。例如5¥、5$等
         * @param price
         * @return
         */
        public abstract String getDescriptionExt(String quote,double price, String defDescription);

    }


}
