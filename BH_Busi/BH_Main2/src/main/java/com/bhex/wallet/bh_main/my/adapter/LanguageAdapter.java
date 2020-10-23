package com.bhex.wallet.bh_main.my.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.model.LanguageItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;


/**
 * Created by gdy on 2019/1/27
 * Describe:
 */


public class LanguageAdapter extends BaseQuickAdapter<LanguageItem, BaseViewHolder> {

    public LanguageAdapter(int layoutResId, @Nullable List<LanguageItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageItem item) {
        helper.setText(R.id.tv_language,item.getFullName());
        helper.getView(R.id.iv_choosed).setVisibility(View.INVISIBLE);
        if(item.isSelected()){
            helper.getView(R.id.iv_choosed).setVisibility(View.VISIBLE);
        }
    }
}
