package com.bhex.lib.uikit.widget.editor;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by gongdongyang on 2018/9/17.
 */

public class EditTextWrapper {

    private EditText mEditText;

    private Drawable mDrawableStart;
    private Drawable mDrawableTop;
    private Drawable mDrawableEnd;
    private Drawable mDrawableBottom;

    private TextWatcher mTextWatcher;

    private View.OnTouchListener mOnTouchListener;

    private onDrawableRightClickListener mOnDrawableRightClickListener;

    EditTextWrapper(EditText editText) {
        mEditText = editText;
        initDrawables();
        mEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mTextWatcher != null)
                    mTextWatcher.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTextWatcher != null)
                    mTextWatcher.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawables(s.toString());
                if (mTextWatcher != null)
                    mTextWatcher.afterTextChanged(s);
            }
        });
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return checkDrawableRightClick(v, event);
            }
        });
    }

    private void initDrawables() {
        Drawable[] drawables = mEditText.getCompoundDrawablesRelative();
        mDrawableStart = drawables[0];
        mDrawableTop = drawables[1];
        mDrawableEnd = drawables[2];
        mDrawableBottom = drawables[3];
    }

    private void setDrawables(String text) {
        if (TextUtils.isEmpty(text))
            mEditText.setCompoundDrawablesRelative(mDrawableStart, mDrawableTop, null, mDrawableBottom);
        else
            mEditText.setCompoundDrawablesRelative(mDrawableStart, mDrawableTop, mDrawableEnd, mDrawableBottom);
    }

    private boolean checkDrawableRightClick(View v, MotionEvent event) {
        Drawable[] drawables = mEditText.getCompoundDrawables();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (drawables[2] != null && event.getX() >
                    mEditText.getWidth() - mEditText.getPaddingRight() - drawables[2].getMinimumWidth()) {
                if (mOnDrawableRightClickListener != null) {
                    mOnDrawableRightClickListener.onDrawableRightClick();
                }
            } else {
                if (mOnTouchListener != null)
                    mOnTouchListener.onTouch(v, event);
            }
        }
        return false;
    }

    public void setDrawableRight(Drawable drawableRight) {
        if (drawableRight != null)
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        mDrawableEnd = drawableRight;
        setDrawables(mEditText.getText().toString());
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        mTextWatcher = textWatcher;
    }

    public EditTextWrapper setOnTouchListener(View.OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
        return this;
    }

    void setOnDrawableRightClickListener(onDrawableRightClickListener listener) {
        mOnDrawableRightClickListener = listener;
    }

    interface onDrawableRightClickListener {
        void onDrawableRightClick();
    }
}
