package com.bhex.wallet.bh_main.validator.presenter;

import android.text.TextUtils;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.model.BHToken;

public class DoEntrustPresenter extends BasePresenter {

    public DoEntrustPresenter(BaseActivity activity) {
        super(activity);
    }

    public boolean checkDoEntrust(String validatorAddress,String delegatorAddress,String transfer_amount,
                                          String available_amount,String fee_amount){

        if(TextUtils.isEmpty(transfer_amount) || Double.valueOf(transfer_amount)<=0){
            ToastUtils.showToast("委托数量不能为空且大于0");
            return false;
        }

        if(TextUtils.isEmpty(fee_amount) || Double.valueOf(fee_amount)<=0){
            ToastUtils.showToast("手续不能为空且大于0");
            return false;
        }

        if(NumberUtil.add(transfer_amount,fee_amount) >Double.valueOf(available_amount)){
            ToastUtils.showToast("委托数量大于可用余额");
            return false;
        }

        return true;
    }

    public boolean checkReliveEntrust(String validatorAddress,String delegatorAddress,String transfer_amount, String wallet_available,
                                  String available_amount,String fee_amount){

        if(TextUtils.isEmpty(transfer_amount) || Double.valueOf(transfer_amount)<=0){
            ToastUtils.showToast("解委托数量不能为空且大于0");
            return false;
        }

        if(TextUtils.isEmpty(fee_amount) || Double.valueOf(fee_amount)<=0){
            ToastUtils.showToast("手续不能为空且大于0");
            return false;
        }

        if(Double.valueOf(fee_amount) >Double.valueOf(wallet_available)){
            ToastUtils.showToast("手续费数量不能大于可用余额");
            return false;
        }

        if(Double.valueOf(transfer_amount) >Double.valueOf(available_amount)){
            ToastUtils.showToast("解委托数量不能大于可用余额");
            return false;
        }

        return true;
    }


    public String getAmountForUser(String amount, String frozen_amount, String symbol) {
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());
        int decimals = bhToken!=null?bhToken.decimals:2;
        decimals = 0;
        double tmp = NumberUtil.sub(amount,frozen_amount);
        double displayAmount = NumberUtil.divide(String.valueOf(tmp), Math.pow(10,decimals)+"");

        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
        return NumberUtil.dispalyForUsertokenAmount(String.valueOf(displayAmount));
    }

}

