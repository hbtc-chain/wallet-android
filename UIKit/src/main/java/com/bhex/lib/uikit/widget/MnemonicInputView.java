package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/30
 * Time: 22:17
 */
public class MnemonicInputView extends ViewGroup {

    public MnemonicInputView(Context context) {
        super(context);
    }

    public MnemonicInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MnemonicInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算出所有ChildView的高度和宽度
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //
        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
