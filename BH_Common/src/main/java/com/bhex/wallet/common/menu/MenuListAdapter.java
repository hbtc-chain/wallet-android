package com.bhex.wallet.common.menu;

import com.bhex.wallet.common.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * @author gongdongyang
 * 2020-8-31 18:50:44
 */
public class MenuListAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

    public MenuListAdapter(@Nullable List data) {
        super(R.layout.item_menu_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MenuItem menuItem) {
        holder.setText(R.id.tv_title,menuItem.title);
    }
}
