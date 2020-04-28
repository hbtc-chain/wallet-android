package com.bhex.wallet.balance.adapter;

import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.TxOrderItem;
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
public class DelegateValidatorAdapter extends BaseQuickAdapter<TxOrderItem.ActivitiesBean, BaseViewHolder> {

    public DelegateValidatorAdapter(int layoutResId, @Nullable List<TxOrderItem.ActivitiesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable TxOrderItem.ActivitiesBean activitiesBean) {
        DelegateValidatorBean validator = JsonUtils.fromJson(activitiesBean.valueIem,DelegateValidatorBean.class);
        viewHolder.setText(R.id.tv_delegate_address,validator.delegator_address);
        viewHolder.setText(R.id.tv_validator_address,validator.validator_address);

        viewHolder.getView(R.id.iv_delegate_address_paste).setOnClickListener(v -> {
            ToolUtils.copyText(validator.delegator_address,getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });

        viewHolder.getView(R.id.iv_validator_paste).setOnClickListener(v -> {
            ToolUtils.copyText(validator.validator_address,getContext());
            ToastUtils.showToast(getContext().getResources().getString(R.string.copyed));
        });
    }

    class DelegateValidatorBean{
        public String delegator_address;
        public String validator_address;
    }
}
