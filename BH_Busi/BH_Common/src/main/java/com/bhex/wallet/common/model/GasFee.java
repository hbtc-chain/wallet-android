package com.bhex.wallet.common.model;

import android.util.Log;

import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;

import java.math.BigDecimal;

/**
 * @author gongdongyang
 * 2020-11-23 10:54:04
 */
public class GasFee {
    public String fee;
    public String gas;
    public String displayFee;

    /*public GasFee() {
        LogUtils.d("GasFee====>:","==GasFee()==");
    }*/

    public GasFee(String fee, String gas) {
        this.fee = fee;
        this.gas = gas;

        double d_displayFee = NumberUtil.divide(fee,String.valueOf(Math.pow(10,18)),5);
        displayFee = new BigDecimal(d_displayFee+"").stripTrailingZeros().toPlainString();
        LogUtils.d("GasFee====>:","==displayFee=="+displayFee);
    }

    public void setFee(String fee) {
        this.fee = fee;
        double d_displayFee = NumberUtil.divide(fee,String.valueOf(Math.pow(10,18)),5);
        displayFee = new BigDecimal(d_displayFee+"").stripTrailingZeros().toPlainString();
    }
}
