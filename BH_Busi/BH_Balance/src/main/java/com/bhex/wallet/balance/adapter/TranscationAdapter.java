package com.bhex.wallet.balance.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHToken;
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

    public TranscationAdapter(@Nullable List<TransactionOrder.ActivitiesBean> data) {
        super(R.layout.layout_reward, data);
    }
    //BaseBean baseBean;
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable TransactionOrder.ActivitiesBean activitiesBean) {
        //
        viewHolder.setText(R.id.tv_title, TRANSCATION_BUSI_TYPE.getValue(activitiesBean.getType()));
        AppCompatTextView  tv_delegate_address = viewHolder.getView(R.id.tv_delegate_address);
        AppCompatTextView  tv_validator_address = viewHolder.getView(R.id.tv_validator_address);
        viewHolder.itemView.setVisibility(View.VISIBLE);
        if(TRANSCATION_BUSI_TYPE.提取收益.getType().equalsIgnoreCase(activitiesBean.type)){
            //计算提取收益
            DelegateBean delegateBean = JsonUtils.fromJson(activitiesBean.getValue().toString(),DelegateBean.class);
            int decimals = SymbolCache.getInstance().getDecimals(delegateBean.amount.denom);
            double amount = NumberUtil.divide(delegateBean.amount.amount, String.valueOf(BHConstants.BHT_DECIMALS),decimals);
            //LogUtils.d("TranscationAdapter==>:","提取收益==amount=="+amount);
            String amount_str = NumberUtil.dispalyForUsertokenAmount4Level(String.valueOf(amount));
            viewHolder.setText(R.id.tv_amount, amount_str+BHConstants.BHT_TOKEN.toUpperCase());

            viewHolder.setText(R.id.tv_delegate_address,delegateBean.delegator_address);
            viewHolder.setText(R.id.tv_validator_address,delegateBean.validator_address);

        }else if(TRANSCATION_BUSI_TYPE.委托.getType().equalsIgnoreCase(activitiesBean.type)){
            //计算委托数量
            DelegateBean delegateBean =
                    JsonUtils.fromJson(activitiesBean.value.toString(),DelegateBean.class);
            int decimals = SymbolCache.getInstance().getDecimals(delegateBean.amount.denom);

            double amount = NumberUtil.divide(delegateBean.amount.amount, String.valueOf(BHConstants.BHT_DECIMALS),decimals);
            String amount_str = NumberUtil.dispalyForUsertokenAmount4Level(String.valueOf(amount));

            viewHolder.setText(R.id.tv_amount, amount_str+BHConstants.BHT_TOKEN.toUpperCase());
            viewHolder.setText(R.id.tv_delegate_address,delegateBean.delegator_address);
            viewHolder.setText(R.id.tv_validator_address,delegateBean.validator_address);
        }else if(TRANSCATION_BUSI_TYPE.转账.getType().equalsIgnoreCase(activitiesBean.type)){
            try{
                TransactionOrder.ActivitiesBean.ValueBean transferBean = JsonUtils.fromJson(activitiesBean.value.toString(),
                        TransactionOrder.ActivitiesBean.ValueBean.class);

                String address = BHUserManager.getInstance().getCurrentBhWallet().address;
                if(transferBean.getTo_address().equals(address)){
                    //type_label.delete(0,type_label.length()).append("收款");
                    viewHolder.setText(R.id.tv_title, R.string.make_collection);

                }

                viewHolder.setText(R.id.tv_delegate_address,transferBean.getFrom_address());
                viewHolder.setText(R.id.tv_validator_address,transferBean.getTo_address());
                viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
                viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));
                int decimals = SymbolCache.getInstance().getDecimals(transferBean.getAmount().get(0).getDenom());

                //计算转账数量

                double amount = NumberUtil.divide(transferBean.getAmount().get(0).getAmount(), String.valueOf(Math.pow(10,decimals)),decimals);

                String amount_str = NumberUtil.dispalyForUsertokenAmount4Level(amount+"");
                BHToken denomToken = SymbolCache.getInstance().getBHToken(transferBean.getAmount().get(0).getDenom());
                if(denomToken!=null){
                    viewHolder.setText(R.id.tv_amount, amount_str+denomToken.name.toUpperCase());
                }else{
                    viewHolder.setText(R.id.tv_amount, "");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(TRANSCATION_BUSI_TYPE.跨链地址生成.getType().equalsIgnoreCase(activitiesBean.type)){
            TransactionOrder.ActivitiesBean.AddressGenBean addressGenBean = JsonUtils.fromJson(activitiesBean.value.toString(),
                    TransactionOrder.ActivitiesBean.AddressGenBean.class);
            viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
            viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));
            viewHolder.setText(R.id.tv_delegate_address,addressGenBean.from);
            viewHolder.setText(R.id.tv_validator_address,addressGenBean.to);
        }else if(TRANSCATION_BUSI_TYPE.跨链充值.getType().equalsIgnoreCase(activitiesBean.type)){
            TransactionOrder.ActivitiesBean.DepositBean depositBean = JsonUtils.fromJson(activitiesBean.value.toString(),
                    TransactionOrder.ActivitiesBean.DepositBean.class);
            viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
            viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));
            viewHolder.setText(R.id.tv_delegate_address,depositBean.from_cu);
            viewHolder.setText(R.id.tv_validator_address,depositBean.to_cu);

            //充值数量
            int decimals = SymbolCache.getInstance().getDecimals(depositBean.symbol);
            double amount = NumberUtil.divide(depositBean.amount, String.valueOf(Math.pow(10,decimals)),decimals);
            String amount_str = NumberUtil.dispalyForUsertokenAmount4Level(amount+"");
            BHToken denomToken = SymbolCache.getInstance().getBHToken(depositBean.symbol);
            if(denomToken!=null){
                viewHolder.setText(R.id.tv_amount, amount_str+denomToken.name.toUpperCase());
            }else{
                viewHolder.setText(R.id.tv_amount, "");
            }
        }else if(TRANSCATION_BUSI_TYPE.跨链提币.getType().equalsIgnoreCase(activitiesBean.type)){
            TransactionOrder.ActivitiesBean.WithdrawalBean withdrawalBean = JsonUtils.fromJson(activitiesBean.value.toString(),
                    TransactionOrder.ActivitiesBean.WithdrawalBean.class);

            viewHolder.setText(R.id.tv_delegate_label,getContext().getString(R.string.transfer_out));
            viewHolder.setText(R.id.tv_validator_label,getContext().getString(R.string.transfer_in_ext));

            viewHolder.setText(R.id.tv_delegate_address,withdrawalBean.from_cu);
            viewHolder.setText(R.id.tv_validator_address,withdrawalBean.to_multi_sign_address);


            //提币数量
            int decimals = SymbolCache.getInstance().getDecimals(withdrawalBean.symbol);
            double amount = NumberUtil.divide(withdrawalBean.amount, String.valueOf(Math.pow(10,decimals)),decimals);
            String amount_str = NumberUtil.dispalyForUsertokenAmount4Level(amount+"");
            BHToken denomToken = SymbolCache.getInstance().getBHToken(withdrawalBean.symbol);
            if(denomToken!=null){
                viewHolder.setText(R.id.tv_amount, amount_str+denomToken.name.toUpperCase());
            }else{
                viewHolder.setText(R.id.tv_amount, "");
            }
            //viewHolder.setText(R.id.tv_amount, amount_str+withdrawalBean.symbol.toUpperCase());
        }else {
            viewHolder.itemView.setVisibility(View.GONE);
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
