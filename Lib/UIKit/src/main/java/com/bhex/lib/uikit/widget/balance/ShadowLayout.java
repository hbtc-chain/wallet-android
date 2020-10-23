package com.bhex.lib.uikit.widget.balance;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/6
 * Time: 22:14
 */
public class ShadowLayout extends FrameLayout {

    private static final String TAG = ShadowLayout.class.getSimpleName();


    public ShadowLayout(@NonNull Context context) {
        this(context,null);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

    }

}
