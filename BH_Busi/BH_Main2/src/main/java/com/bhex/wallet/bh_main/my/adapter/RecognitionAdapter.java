package com.bhex.wallet.bh_main.my.adapter;

import android.util.Log;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author gongdongyang
 * 2020-5-20 10:13:15
 * 面容和指纹识别---
 */
public class RecognitionAdapter extends BaseQuickAdapter<MyItem, BaseViewHolder> {
    public RecognitionAdapter(int layoutResId, @Nullable List<MyItem> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.ck_select);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, MyItem myItem) {
        vh.setText(R.id.tv_recognition_name,myItem.title);
        AppCompatCheckedTextView ck = vh.getView(R.id.ck_select);

        //ck.setChecked(false);
        /*if(myItem.isArrow){
            ck.setChecked(true);
        }*/
        LogUtils.d("RecognitionActivity===>","ck.selected=="+ck.isSelected());
        /*ck.setOnClickListener(v->{
            //ck.setChecked(!ck.isSelected());
            LogUtils.d("RecognitionActivity===>","ck.selected=="+ck.isSelected());
        });*/
    }
}
