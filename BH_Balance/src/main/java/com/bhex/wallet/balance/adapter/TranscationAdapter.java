package com.bhex.wallet.balance.adapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/28
 * Time: 0:01
 */
public class TranscationAdapter extends BaseQuickAdapter<TxOrderItem.ActivitiesBean, BaseViewHolder> {

    public TranscationAdapter(int layoutResId, @Nullable List<TxOrderItem.ActivitiesBean> data) {
        super(layoutResId, data);
    }
    //BaseBean baseBean;
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable TxOrderItem.ActivitiesBean activitiesBean) {
        //
        viewHolder.setText(R.id.tv_title, TRANSCATION_BUSI_TYPE.getValue(activitiesBean.type));
        AppCompatTextView  tv_delegate_address = viewHolder.getView(R.id.tv_delegate_address);
        AppCompatTextView  tv_validator_address = viewHolder.getView(R.id.tv_validator_address);

        //LogUtils.d("TransactionHelper===>:","json=="+activitiesBean.valueIem);

        DelegateBean delegateBean = JsonUtils.fromJson(activitiesBean.valueIem,DelegateBean.class);

        viewHolder.setText(R.id.tv_delegate_address,delegateBean.delegator_address);
        viewHolder.setText(R.id.tv_validator_address,delegateBean.validator_address);
        viewHolder.getView(R.id.iv_delegate_address_paste).setOnClickListener(v -> {
            ToolUtils.copyText(delegateBean.delegator_address,getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });

        viewHolder.getView(R.id.iv_validator_paste).setOnClickListener(v -> {
            ToolUtils.copyText(delegateBean.validator_address,getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });

        if(TRANSCATION_BUSI_TYPE.提取收益.getType().equalsIgnoreCase(activitiesBean.type)){
            //RewardValidatorBean validator = JsonUtils.fromJson(activitiesBean.valueIem,RewardValidatorBean.class);
            //计算提取收益
            double amount = NumberUtil.divide(delegateBean.amount.get(0).amount, String.valueOf(BHConstants.BHT_DECIMALS),2);
            viewHolder.setText(R.id.tv_amount, String.valueOf(amount)+BHConstants.BHT_TOKEN.toUpperCase());

        }else if(TRANSCATION_BUSI_TYPE.委托.getType().equalsIgnoreCase(activitiesBean.type)){
            //计算委托数量
            double amount = NumberUtil.divide(delegateBean.amount.get(0).amount, String.valueOf(BHConstants.BHT_DECIMALS),2);
            viewHolder.setText(R.id.tv_amount, String.valueOf(amount)+BHConstants.BHT_TOKEN.toUpperCase());
        }else if(TRANSCATION_BUSI_TYPE.转账.getType().equalsIgnoreCase(activitiesBean.type)){
            try{
                LogUtils.d("TransactionHelper===>:","-transcationAdapter-"+activitiesBean.valueIem);
                TransactionOrder.ActivitiesBean.ValueBean transferBean = JsonUtils.fromJson(activitiesBean.valueIem,
                        TransactionOrder.ActivitiesBean.ValueBean.class);

                viewHolder.setText(R.id.tv_delegate_address,transferBean.getFrom_address());
                viewHolder.setText(R.id.tv_validator_address,transferBean.getTo_address());
                viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
                viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));
                //计算转账数量
                double amount = NumberUtil.divide(transferBean.getAmount().get(0).getAmount(), String.valueOf(BHConstants.BHT_DECIMALS),2);
                viewHolder.setText(R.id.tv_amount, amount+BHConstants.BHT_TOKEN.toUpperCase());
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        viewHolder.getView(R.id.iv_delegate_address_paste).setOnClickListener(v -> {
            ToolUtils.copyText(tv_delegate_address.getText().toString(),getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });

        viewHolder.getView(R.id.iv_validator_paste).setOnClickListener(v -> {
            ToolUtils.copyText(tv_validator_address.getText().toString(),getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });
    }

    class DelegateBean {
        public String delegator_address;
        public String validator_address;
        public List<Amount> amount;
        class Amount{
            public String amount;
            public String denom;
        }
    }


}
