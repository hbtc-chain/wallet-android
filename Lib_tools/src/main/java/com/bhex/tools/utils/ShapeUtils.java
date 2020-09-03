package com.bhex.tools.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.airbnb.lottie.model.content.GradientType;

/**
 * @author gongdongyang
 * 2020年9月1日16:51:06
 * 动态设置圆角
 */
public class ShapeUtils {
    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        float[] radius_f = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radius_f);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);

        return drawable;
    }

    public static GradientDrawable getRoundRectTopDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        float[] radius_f = {radius, radius, radius, radius, 0, 0, 0, 0};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radius_f);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }


    public static GradientDrawable getOvalDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
