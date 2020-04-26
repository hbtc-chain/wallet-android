package com.bhex.wallet.balance.adapter;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatImageView;

import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.BHTokenItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/3
 * Time: 15:20
 */
public class CoinSearchAdapter extends BaseQuickAdapter<BHTokenItem, BaseViewHolder> {

    public CoinSearchAdapter(int layoutResId, @Nullable List<BHTokenItem> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.ck_select);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable BHTokenItem coin) {
        viewHolder.setText(R.id.tv_coin_name,coin.symbol.toUpperCase());
        viewHolder.setText(R.id.tv_coin_full,coin.fullName);
        AppCompatCheckedTextView ck = viewHolder.getView(R.id.ck_select);
        ck.setChecked(false);
        if(coin.isSelected){
            ck.setChecked(true);
        }
        AppCompatImageView iv_coin_ic = viewHolder.getView(R.id.iv_coin_ic);
        //iv_coin_ic.setImageDrawable(getContext().getResources().getDrawable(coin.resId));
        if(coin.resId==0){
            ImageLoaderUtil.loadImageView(getContext(),coin.logo,iv_coin_ic,R.mipmap.ic_default_coin);
        }else{
            LogUtils.d("CoinSearchAdapter==>:","logo=="+coin.logo);
            iv_coin_ic.setImageDrawable(getContext().getResources().getDrawable(coin.resId));
        }
    }
}
