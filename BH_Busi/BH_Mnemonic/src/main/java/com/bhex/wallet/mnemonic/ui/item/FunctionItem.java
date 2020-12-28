package com.bhex.wallet.mnemonic.ui.item;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/22
 * Time: 19:04
 */
public class FunctionItem implements MultiItemEntity {
    public final static int TYPE_ITEM = 0;
    public final static int TYPE_TITLE = 1;
    public int index=-1;
    public int resId;
    public String title;
    public String tips;
    public int fieldType;

    public FunctionItem(String title, int fieldType) {
        this.title = title;
        this.fieldType = fieldType;
    }

    public FunctionItem(int index, int resId, String title, String tips) {
        this.index = index;
        this.resId = resId;
        this.title = title;
        this.tips = tips;
    }

    public FunctionItem(String title, String tips, int resId) {
        this.title = title;
        this.tips = tips;
        this.resId = resId;
    }

    @Override
    public int getItemType() {
        return fieldType;
    }
}
