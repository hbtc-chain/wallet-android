package com.bhex.tools.toast;

import android.content.Context;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.tools.R;
import com.hjq.toast.IToastStyle;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/21
 * Time: 17:57
 */
public class BHToastStyle implements IToastStyle {
    private boolean isNight;
    private Context mContext;
    public BHToastStyle(Context context,boolean isNight) {
        this.mContext = context;
        this.isNight = isNight;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getZ() {
        return 30;
    }

    @Override
    public int getCornerRadius() {
        return PixelUtils.dp2px(mContext,4);
    }

    @Override
    public int getBackgroundColor() {
        return this.isNight ? ContextCompat.getColor(mContext, R.color.card_backgound) :  ContextCompat.getColor(mContext, R.color.card_backgound);
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return PixelUtils.dp2px(mContext,4);
    }

    @Override
    public int getMaxLines() {
        return 5;
    }

    @Override
    public int getPaddingStart() {
        return PixelUtils.dp2px(mContext,24);
    }

    @Override
    public int getPaddingTop() {
        return PixelUtils.dp2px(mContext,16);
    }

    @Override
    public int getPaddingEnd() {
        return getPaddingStart();
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}
