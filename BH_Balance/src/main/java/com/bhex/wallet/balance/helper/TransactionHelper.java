package com.bhex.wallet.balance.helper;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.balance.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 17:25
 */
public class TransactionHelper {
    /**
     * 获取交易类型
     */
    public static String getTranscationType(Context context, String typeValue){
        String typeLabel = "";
        if(typeValue.equalsIgnoreCase("cosmos-sdk/MsgSend")){
            typeLabel = context.getResources().getString(R.string.transfer);
        }else if(typeValue.equalsIgnoreCase("cosmos-sdk/MsgDelegate")){
            typeLabel = context.getResources().getString(R.string.entrust);
        }else if(typeValue.equalsIgnoreCase("cosmos-sdk/MsgUndelegate")){
            typeLabel = context.getResources().getString(R.string.un_entrust);
        }
        return typeLabel;
    }


    public static void setTranscationStatus(Context context, boolean status, AppCompatTextView tv_status){
        String statusLabel = "";
        if(status){
            statusLabel = context.getResources().getString(R.string.success);
            tv_status.setTextColor(ContextCompat.getColor(context,R.color.color_green));
            tv_status.setBackgroundColor(ContextCompat.getColor(context,R.color.color_20_green));
            //tv_status.setTextAppearance(context,R.style.tx_status_success);
        }else{
            statusLabel = context.getResources().getString(R.string.fail);
            tv_status.setTextColor(ContextCompat.getColor(context,R.color.color_red));
            tv_status.setBackgroundColor(ContextCompat.getColor(context,R.color.color_20_red));
            //tv_status.setTextAppearance(context,R.style.tx_status_fail);
        }
        tv_status.setText(statusLabel);
    }
}
