package com.bhex.wallet.market.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.bhex.wallet.common.helper.BHWalletHelper;
import com.bhex.wallet.common.model.BHWalletItem;
import com.bhex.wallet.market.R;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BHWalletAdapter extends BaseQuickAdapter<BHWalletItem, BaseViewHolder> {
    private OnCheckClickListener mOnCheckClickListener;

    public BHWalletAdapter( @Nullable List<BHWalletItem> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BHWalletItem bhWalletItem) {
        int positoin = holder.getAdapterPosition();

        holder.setText(R.id.tv_wallet_name,bhWalletItem.name);
        //viewHolder.setText(R.id.tv_wallet_address,bhWalletItem.address);
        BHWalletHelper.proccessAddress(holder.getView(R.id.tv_wallet_address),bhWalletItem.address);

        AppCompatCheckedTextView ck = holder.getView(R.id.ck_wallet);
        if(bhWalletItem.isDefault==1){
            ck.setChecked(true);
            holder.getView(R.id.tv_wallet_default).setVisibility(View.VISIBLE);
        }else{
            ck.setChecked(false);
            holder.getView(R.id.tv_wallet_default).setVisibility(View.INVISIBLE);
        }
        ck.setOnClickListener(v -> {
            ck.setChecked(!ck.isChecked());
            if(mOnCheckClickListener!=null){
                mOnCheckClickListener.checkClickListener(positoin,bhWalletItem);
            }
        });
    }

    public interface OnCheckClickListener{
        void checkClickListener(int position,BHWalletItem bhWalletItem);
    }

    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.mOnCheckClickListener = onCheckClickListener;
    }
}
