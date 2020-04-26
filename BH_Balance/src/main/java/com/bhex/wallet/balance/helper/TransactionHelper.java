package com.bhex.wallet.balance.helper;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.TransactionOrder;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 17:25
 */
public class TransactionHelper {
    /**
     * 获取交易类型
     *
     */
    public static String getTranscationType(Context context, String typeValue){
        return TRANSCATION_BUSI_TYPE.getValue(typeValue);
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

    /**
     * 交易详情用
     * @param context
     * @param status
     * @param tv_status
     */
    public static void setTranscationStatusExt(Context context, boolean status, AppCompatTextView tv_status){
        String statusLabel = "";
        if(status){
            statusLabel = context.getResources().getString(R.string.success);
            tv_status.setTextColor(ContextCompat.getColor(context,R.color.color_green));
            //tv_status.setBackgroundColor(ContextCompat.getColor(context,R.color.color_20_green));
            //tv_status.setTextAppearance(context,R.style.tx_status_success);
        }else{
            statusLabel = context.getResources().getString(R.string.fail);
            tv_status.setTextColor(ContextCompat.getColor(context,R.color.color_red));
            //tv_status.setBackgroundColor(ContextCompat.getColor(context,R.color.color_20_red));
            //tv_status.setTextAppearance(context,R.style.tx_status_fail);
        }
        tv_status.setText(statusLabel);
    }

    public static TxOrderItem getTxOrderItem(TransactionOrder txo){
        String jsonString = JsonUtils.toJson(txo);
        TxOrderItem txOrderItem = JsonUtils.fromJson(jsonString,TxOrderItem.class);
        txOrderItem.value = txo.getActivities().get(0).getValue().toString();
        return txOrderItem;
    }


    public static void displayTranscationAmount(Context context,AppCompatTextView tv,String txType,String json){
        if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.转账.getType())){

            TransactionOrder.ActivitiesBean.ValueBean valueBean
                    = JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.ValueBean.class);

            TransactionOrder.ActivitiesBean.ValueBean.AmountBean bean1 = valueBean.getAmount().get(0);

            setRealAmount(tv,bean1.getAmount(),bean1.getDenom());
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链地址生成.getType())){
            TransactionOrder.ActivitiesBean.AddressGenBean addressGenBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.AddressGenBean.class);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链提币.getType())){

            TransactionOrder.ActivitiesBean.WithdrawalBean withdrawalBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.WithdrawalBean.class);
            setRealAmount(tv,withdrawalBean.amount,withdrawalBean.symbol);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链充值.getType())){
            TransactionOrder.ActivitiesBean.DepositBean depositBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DepositBean.class);

            setRealAmount(tv,depositBean.amount,depositBean.symbol);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.委托.getType())){
            TransactionOrder.ActivitiesBean.DelegateBean delegateBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DelegateBean.class);

            setRealAmount(tv,delegateBean.amount.getAmount(),delegateBean.amount.getDenom());
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.发起治理提案.getType())){

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.治理提案质押.getType())){

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.治理提案投票.getType())){

        }
    }

    public static void setRealAmount(AppCompatTextView tv,String amount,String symbol){
        BHToken token = SymbolCache.getInstance().getBHToken(symbol);
        if(token!=null){
            double real_amount = NumberUtil.divide(amount,Math.pow(10,token.decimals)+"");
            String tv_amount = NumberUtil.dispalyForUsertokenAmount(String.valueOf(real_amount))+symbol.toUpperCase();
            tv.setText(tv_amount);
        }

    }


    public static void displayTranscationFromTo(Context context,AppCompatTextView tv_from,
                                                AppCompatTextView tv_to,String txType,String json){

        if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.转账.getType())){

            TransactionOrder.ActivitiesBean.ValueBean valueBean
                    = JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.ValueBean.class);

            TransactionOrder.ActivitiesBean.ValueBean.AmountBean bean1 = valueBean.getAmount().get(0);

            tv_from.setText(valueBean.getFrom_address());
            tv_to.setText(valueBean.getTo_address());

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链地址生成.getType())){
            TransactionOrder.ActivitiesBean.AddressGenBean addressGenBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.AddressGenBean.class);

            tv_from.setText(addressGenBean.from);
            tv_to.setText(addressGenBean.to);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链提币.getType())){

            TransactionOrder.ActivitiesBean.WithdrawalBean withdrawalBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.WithdrawalBean.class);
            tv_from.setText(withdrawalBean.from_cu);
            tv_to.setText(withdrawalBean.to_multi_sign_address);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链充值.getType())){
            TransactionOrder.ActivitiesBean.DepositBean depositBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DepositBean.class);

            tv_from.setText(depositBean.from_cu);
            tv_to.setText(depositBean.to_adddress);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.委托.getType())){
            TransactionOrder.ActivitiesBean.DelegateBean delegateBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DelegateBean.class);

            tv_from.setText(delegateBean.delegator_address);

            tv_to.setText(delegateBean.validator_address);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.取消委托.getType())){

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.取消委托.getType())){

        }
    }
}
