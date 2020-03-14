package com.bhex.wallet.bh_main.my.adapter;

import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:19
 */
public class MyAdapter extends BaseQuickAdapter<MyItem, BaseViewHolder> {


    public MyAdapter(int layoutResId, @Nullable List<MyItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable MyItem myItem) {
        viewHolder.setText(R.id.tv_title,myItem.title);
    }
}
