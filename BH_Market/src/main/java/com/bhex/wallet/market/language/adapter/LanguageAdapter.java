package com.bhex.wallet.market.language.adapter;

import android.view.View;


import androidx.annotation.Nullable;

import com.bhex.wallet.market.R;
import com.bhex.wallet.market.language.model.LanguageEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;


/**
 * Created by gdy on 2019/1/27
 * Describe:
 */


public class LanguageAdapter extends BaseQuickAdapter<LanguageEntity, BaseViewHolder> {

    public LanguageAdapter(int layoutResId, @Nullable List<LanguageEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageEntity item) {
        helper.setText(R.id.tv_language,item.getFullName());
        helper.getView(R.id.iv_choosed).setVisibility(View.INVISIBLE);
        if(item.isSelected()){
            helper.getView(R.id.iv_choosed).setVisibility(View.VISIBLE);
        }
    }
}
