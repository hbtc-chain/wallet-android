package com.bhex.wallet.bh_main.my.adapter;

import com.bhex.tools.utils.DateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.model.BHMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author gongdongyang
 * 2020-5-21 16:56:11
 * 消息
 */
public class MessageAdapter extends BaseQuickAdapter<BHMessage, BaseViewHolder> {

    public MessageAdapter( @Nullable List<BHMessage> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, BHMessage bhm) {
        try{
            String []message_type = getContext().getResources().getStringArray(com.bhex.wallet.balance.R.array.Message_type);

            vh.setText(R.id.tv_tx_type,message_type[bhm.tx_type-1]);
            vh.setText(R.id.tv_tx_amount,bhm.amount+" "+bhm.symbol.toUpperCase());
            vh.setText(R.id.tv_tx_status, R.string.success);

            //时间格式化
            String tv_time = DateUtil.transTimeWithPattern(bhm.time*1000,DateUtil.DATA_TIME_STYLE);
            vh.setText(R.id.tv_tx_time,tv_time);

            if(bhm.read){
                vh.setTextColorRes(R.id.tv_tx_type, R.color.global_label_text_color);
                vh.setTextColorRes(R.id.tv_tx_amount, R.color.global_label_text_color);
            }else{
                vh.setTextColorRes(R.id.tv_tx_type, R.color.global_main_text_color);
                vh.setTextColorRes(R.id.tv_tx_amount, R.color.global_main_text_color);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
