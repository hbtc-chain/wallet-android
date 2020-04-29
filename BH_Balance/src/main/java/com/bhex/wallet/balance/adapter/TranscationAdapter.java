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
import com.bhex.wallet.common.manager.BHUserManager;
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
public class TranscationAdapter extends BaseQuickAdapter<TransactionOrder.ActivitiesBean, BaseViewHolder> {

    public TranscationAdapter(int layoutResId, @Nullable List<TransactionOrder.ActivitiesBean> data) {
        super(layoutResId, data);
    }
    //BaseBean baseBean;
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable TransactionOrder.ActivitiesBean activitiesBean) {
        //
        viewHolder.setText(R.id.tv_title, TRANSCATION_BUSI_TYPE.getValue(activitiesBean.getType()));
        AppCompatTextView  tv_delegate_address = viewHolder.getView(R.id.tv_delegate_address);
        AppCompatTextView  tv_validator_address = viewHolder.getView(R.id.tv_validator_address);

        if(TRANSCATION_BUSI_TYPE.提取收益.getType().equalsIgnoreCase(activitiesBean.type)){
            //计算提取收益
            DelegateBean delegateBean = JsonUtils.fromJson(activitiesBean.getValue().toString(),DelegateBean.class);
            double amount = NumberUtil.divide(delegateBean.amount.amount, String.valueOf(BHConstants.BHT_DECIMALS),2);
            viewHolder.setText(R.id.tv_amount, String.valueOf(amount)+BHConstants.BHT_TOKEN.toUpperCase());

            viewHolder.setText(R.id.tv_delegate_address,delegateBean.delegator_address);
            viewHolder.setText(R.id.tv_validator_address,delegateBean.validator_address);

        }else if(TRANSCATION_BUSI_TYPE.委托.getType().equalsIgnoreCase(activitiesBean.type)){
            //计算委托数量
            DelegateBean delegateBean =
                    JsonUtils.fromJson(activitiesBean.value.toString(),DelegateBean.class);
            double amount = NumberUtil.divide(delegateBean.amount.amount, String.valueOf(BHConstants.BHT_DECIMALS),2);
            viewHolder.setText(R.id.tv_amount, String.valueOf(amount)+BHConstants.BHT_TOKEN.toUpperCase());
            viewHolder.setText(R.id.tv_delegate_address,delegateBean.delegator_address);
            viewHolder.setText(R.id.tv_validator_address,delegateBean.validator_address);
        }else if(TRANSCATION_BUSI_TYPE.转账.getType().equalsIgnoreCase(activitiesBean.type)){
            try{
                TransactionOrder.ActivitiesBean.ValueBean transferBean = JsonUtils.fromJson(activitiesBean.value.toString(),
                        TransactionOrder.ActivitiesBean.ValueBean.class);

                viewHolder.setText(R.id.tv_delegate_address,transferBean.getFrom_address());
                viewHolder.setText(R.id.tv_validator_address,transferBean.getTo_address());
                viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
                viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));
                //计算转账数量
                double amount = NumberUtil.divide(transferBean.getAmount().get(0).getAmount(), String.valueOf(BHConstants.BHT_DECIMALS));

                String amount_str = NumberUtil.dispalyForUsertokenAmount(amount+"");
                /*String signal = "-";
                if(BHUserManager.getInstance().getCurrentBhWallet().address.equals(transferBean.getTo_address())){
                    signal="+";
                }*/
                viewHolder.setText(R.id.tv_amount, amount_str+BHConstants.BHT_TOKEN.toUpperCase());
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
        public Amount amount;
        class Amount{
            public String amount;
            public String denom;
        }
    }


}
