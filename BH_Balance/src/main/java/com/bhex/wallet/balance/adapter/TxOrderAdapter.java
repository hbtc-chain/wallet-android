package com.bhex.wallet.balance.adapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.tools.utils.DateUtil;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 16:33
 */
public class TxOrderAdapter extends BaseQuickAdapter<TransactionOrder, BaseViewHolder> {

    public TxOrderAdapter(int layoutResId, @Nullable List<TransactionOrder> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder vh, @Nullable TransactionOrder txo) {
        TransactionOrder.ActivitiesBean bean = txo.getActivities().get(0);
        String tx_type = TransactionHelper.getTranscationType(getContext(),bean.getType());
        vh.setText(R.id.tv_tx_type,tx_type);
        AppCompatTextView tv_status = vh.getView(R.id.tv_tx_status);
        TransactionHelper.setTranscationStatus(getContext(),txo.isSuccess(),tv_status);

        //时间格式化
        String tv_time = DateUtil.transTimeWithPattern(txo.getTime()*1000,DateUtil.DATA_TIME_STYLE);
        vh.setText(R.id.tv_tx_time,tv_time);

        //转账金额
        TransactionOrder.ActivitiesBean.ValueBean.AmountBean bean1 = bean.getValue().getAmount().get(0);
        double amount = NumberUtil.divide(bean1.getAmount(),Math.pow(10,18)+"",2);
        String tv_amount = NumberUtil.formatValue(amount,2)+bean1.getDenom().toUpperCase();
        vh.setText(R.id.tv_tx_amount,tv_amount);
    }
}
