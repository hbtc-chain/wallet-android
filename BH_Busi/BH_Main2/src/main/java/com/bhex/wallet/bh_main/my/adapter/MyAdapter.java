package com.bhex.wallet.bh_main.my.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.SwitchCompat;

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


    public MyAdapter(@Nullable List<MyItem> data) {
        super(R.layout.item_my, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable MyItem myItem) {
        viewHolder.setText(R.id.tv_title,myItem.title);

        if(myItem.isArrow){
            viewHolder.getView(R.id.iv_arrow).setVisibility(View.VISIBLE);
            //viewHolder.getView(R.id.tv_right_txt).setVisibility(View.INVISIBLE);
        }else{
            viewHolder.getView(R.id.iv_arrow).setVisibility(View.INVISIBLE);
            //viewHolder.getView(R.id.tv_right_txt).setVisibility(View.VISIBLE);
            //viewHolder.setText(R.id.tv_right_txt,myItem.rightTxt);
        }

        if(TextUtils.isEmpty(myItem.rightTxt)){
            viewHolder.getView(R.id.tv_right_txt).setVisibility(View.INVISIBLE);
        }else{
            viewHolder.getView(R.id.tv_right_txt).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_right_txt,myItem.rightTxt);
        }

    }
}
