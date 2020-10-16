package com.bhex.wallet.balance.adapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.utils.DateUtil;
import com.bhex.tools.utils.LogUtils;
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
    private String mSymbol;
    public TxOrderAdapter( @Nullable List<TransactionOrder> data,String symbol) {
        super(R.layout.item_tx_order, data);
        this.mSymbol = symbol;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder vh, @Nullable TransactionOrder txo) {
        String tx_type = TransactionHelper.getTranscationType(getContext(),txo);
        vh.setText(R.id.tv_tx_type,tx_type);
        AppCompatTextView tv_status = vh.getView(R.id.tv_tx_status);
        TransactionHelper.setTranscationStatus(getContext(),txo.success,tv_status,txo);

        //时间格式化
        //LogUtils.d("long=="+(txo.getTime()*1000));
        String tv_time = DateUtil.transTimeWithPattern(txo.time*1000,DateUtil.DATA_TIME_STYLE);
        vh.setText(R.id.tv_tx_time,tv_time);

        //转账金额
        AppCompatTextView tv_tx = vh.getView(R.id.tv_tx_amount);
        tv_tx.setText("");
        TransactionHelper.displayTranscationAmount(tv_tx,mSymbol,txo);

    }


}
