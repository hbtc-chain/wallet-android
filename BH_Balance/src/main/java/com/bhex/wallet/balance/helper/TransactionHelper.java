package com.bhex.wallet.balance.helper;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.TransactionOrder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    public static String getTranscationType(Context context, TransactionOrder txo){
        TransactionOrder.ActivitiesBean bean = txo.getActivities().get(0);
        //LogUtils.e("bean.getType()=="+bean.getType());
        StringBuffer type_label = new StringBuffer(
                TRANSCATION_BUSI_TYPE
                        .getValue(
                        bean.getType())
        );

        if(bean.getType().equals(TRANSCATION_BUSI_TYPE.转账.getType())){
            TransactionOrder.ActivitiesBean.ValueBean transferBean = JsonUtils.fromJson(bean.value.toString(),
                    TransactionOrder.ActivitiesBean.ValueBean.class);
            String address = BHUserManager.getInstance().getCurrentBhWallet().address;
            if(transferBean.getTo_address().equals(address)){
                type_label.delete(0,type_label.length()).append(context.getResources().getString(R.string.make_collection));
            }
        }
        if(txo.getActivities().size()>1){
            //String type = bean.getType();
            for(TransactionOrder.ActivitiesBean bean1:txo.getActivities()){
                String type_str = TRANSCATION_BUSI_TYPE.getValue(bean1.getType());
                if(!type_label.toString().contains(type_str)){
                    type_label.append("+").append(type_str);
                }
            }

        }
        return type_label.toString();
    }

    public static String getTranscationType(Context context, TxOrderItem txo){
        TxOrderItem.ActivitiesBean bean = txo.getActivities().get(0);
        StringBuffer type_label = new StringBuffer(TRANSCATION_BUSI_TYPE.getValue(bean.getType()));
        if(txo.getActivities().size()>1){
            String type = bean.getType();
            for(TxOrderItem.ActivitiesBean bean1 : txo.getActivities()){
                String type_str = TRANSCATION_BUSI_TYPE.getValue(bean1.getType());
                if(!type_label.toString().contains(type_str)){
                    type_label.append("+").append(type_str);
                }
            }

        }
        return type_label.toString();
    }


    public static void setTranscationStatus(Context context, boolean status, AppCompatTextView tv_status){
        String statusLabel = "";
        if(status){
            statusLabel = context.getResources().getString(R.string.success);
            tv_status.setTextColor(ContextCompat.getColor(context,R.color.color_green));
            tv_status.setBackgroundColor(ContextCompat.getColor(context,R.color.color_20_green));
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
        txOrderItem.activities = new ArrayList<>();
        for(TransactionOrder.ActivitiesBean activitiesBean:txo.getActivities()){
            TxOrderItem.ActivitiesBean activitiesBean1 = new TxOrderItem.ActivitiesBean();
            activitiesBean1.valueIem = activitiesBean.getValue().toString();
            activitiesBean1.type = activitiesBean.getType();
            txOrderItem.activities.add(activitiesBean1);
        }
        return txOrderItem;
    }

    /**
     *
     * @param txOrderItem
     */
    public static void gotoTranscationDetail(TxOrderItem txOrderItem){
        ARouter.getInstance().build(ARouterConfig.Balance_transcation_view)
                //.withObject("txo",txOrderItem)
                .withString("transactionId",txOrderItem.hash)
                .navigation();
    }


    public static void displayTranscationAmount(Context context,AppCompatTextView tv,String txType,String json,String activitylist){
        if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.转账.getType())){

            TransactionOrder.ActivitiesBean.ValueBean valueBean
                    = JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.ValueBean.class);

            TransactionOrder.ActivitiesBean.ValueBean.AmountBean bean1 = valueBean.getAmount().get(0);
            String signal = "-";
            if(BHUserManager.getInstance().getCurrentBhWallet().address.equals(valueBean.getTo_address())){
                signal = "+";
            }
            setRealAmount(tv,bean1.getAmount(),bean1.getDenom(),signal);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链地址生成.getType())){
            TransactionOrder.ActivitiesBean.AddressGenBean addressGenBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.AddressGenBean.class);
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链提币.getType())){

            TransactionOrder.ActivitiesBean.WithdrawalBean withdrawalBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.WithdrawalBean.class);
            setRealAmount(tv,withdrawalBean.amount,withdrawalBean.symbol,"-");
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.跨链充值.getType())){
            TransactionOrder.ActivitiesBean.DepositBean depositBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DepositBean.class);

            setRealAmount(tv,depositBean.amount,depositBean.symbol,"+");
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.委托.getType())){
            TransactionOrder.ActivitiesBean.DelegateBean delegateBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.DelegateBean.class);

            setRealAmount(tv,delegateBean.amount.getAmount(),delegateBean.amount.getDenom(),"-");
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.发起治理提案.getType())){
            TransactionOrder.ActivitiesBean.SubmitProposalBean submitProposalBean =
                    JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.SubmitProposalBean.class);
            setRealAmount(tv,submitProposalBean.initial_deposit.get(0).amount,
                    submitProposalBean.initial_deposit.get(0).denom,"-");
        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.治理提案质押.getType())){
            TransactionOrder.ActivitiesBean.ValueBean valueBean
                    = JsonUtils.fromJson(json,TransactionOrder.ActivitiesBean.ValueBean.class);

            TransactionOrder.ActivitiesBean.ValueBean.AmountBean bean1 = valueBean.getAmount().get(0);

            setRealAmount(tv,bean1.getAmount(),bean1.getDenom(),"-");

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.治理提案投票.getType())){

        }else if(txType.equalsIgnoreCase(TRANSCATION_BUSI_TYPE.提取收益.getType())){

            List<TransactionOrder.ActivitiesBean> activitiesBeans = JsonUtils.getListFromJson(activitylist,TransactionOrder.ActivitiesBean.class);
            if(activitiesBeans==null || activitiesBeans.size()==0){
                return;
            }

            double amount = 0;
            for (TransactionOrder.ActivitiesBean activitiesBean:activitiesBeans){

                TransactionOrder.ActivitiesBean.DelegationRewardBean rewardBean  =
                        JsonUtils.fromJson(activitiesBean.getValue().toString(),TransactionOrder.ActivitiesBean.DelegationRewardBean.class);
                if(!txType.equals(activitiesBean.type)){
                    return;
                }
                if(activitiesBean.getType().equals(TRANSCATION_BUSI_TYPE.提取收益.getType())){
                    int decimals = SymbolCache.getInstance().getDecimals(rewardBean.amount.denom);
                    double rewardAmount = NumberUtil.divide(rewardBean.amount.amount,BHConstants.BHT_DECIMALS+"",decimals);
                    amount = NumberUtil.add(rewardAmount+"",rewardBean.amount.amount);
                }
                txType = activitiesBean.getType();
            }
            setRealAmount(tv, amount+"", BHConstants.BHT_TOKEN,"+");
        }
    }

    public static void setRealAmount(AppCompatTextView tv,String amount,String symbol,String flag){
        BHToken token = SymbolCache.getInstance().getBHToken(symbol);
        if(token!=null){
            double real_amount = NumberUtil.divide(amount,Math.pow(10,token.decimals)+"",token.decimals);
            String tv_amount = NumberUtil.dispalyForUsertokenAmount(String.valueOf(real_amount))+symbol.toUpperCase();
            tv.setText(flag+tv_amount);
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

        }
    }


    public  static boolean transactionIsProccess(String txType){
        boolean flag = false;
        if(TRANSCATION_BUSI_TYPE.兑换_输入确定.getType().equalsIgnoreCase(txType)
                || TRANSCATION_BUSI_TYPE.兑换_输出确定.getType().equalsIgnoreCase(txType)
                || TRANSCATION_BUSI_TYPE.添加流动性.getType().equalsIgnoreCase(txType)
                || TRANSCATION_BUSI_TYPE.移除流动性.getType().equalsIgnoreCase(txType)
                || TRANSCATION_BUSI_TYPE.撤单.getType().equalsIgnoreCase(txType)
                || TRANSCATION_BUSI_TYPE.限价单兑换.getType().equalsIgnoreCase(txType)){
            return flag;
        }
        return true;
    }
}
