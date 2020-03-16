package com.bhex.wallet.mnemonic.adapter;

import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/14
 * Time: 23:01
 */
public class TrustManagerAdapter extends BaseQuickAdapter<BHWalletItem, BaseViewHolder> {

    private OnCheckClickListener mOnCheckClickListener;


    public TrustManagerAdapter(int layoutResId, @Nullable List<BHWalletItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable BHWalletItem bhWalletItem) {
        int positoin = viewHolder.getAdapterPosition();

        viewHolder.setText(R.id.tv_wallet_name,bhWalletItem.name);
        viewHolder.setText(R.id.tv_wallet_address,bhWalletItem.address);




        AppCompatCheckedTextView ck = viewHolder.getView(R.id.ck_wallet);
        if(bhWalletItem.isDefault==1){
            ck.setChecked(true);
            viewHolder.getView(R.id.tv_wallet_default).setVisibility(View.VISIBLE);
        }else{
            ck.setChecked(false);
            viewHolder.getView(R.id.tv_wallet_default).setVisibility(View.INVISIBLE);
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
