package com.bhex.wallet.balance.adapter;

import androidx.appcompat.widget.AppCompatImageView;

import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.Balance;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/18
 * Time: 0:18
 */
public class BalanceAdapter extends BaseQuickAdapter<Balance, BaseViewHolder> {

    public BalanceAdapter(int layoutResId, @Nullable List<Balance> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable Balance balanceItem) {
        AppCompatImageView iv = viewHolder.getView(R.id.iv_coin);
        iv.setImageDrawable(getContext().getResources().getDrawable(balanceItem.resId));
        viewHolder.setText(R.id.tv_coin_name,balanceItem.coinName);
    }
}
