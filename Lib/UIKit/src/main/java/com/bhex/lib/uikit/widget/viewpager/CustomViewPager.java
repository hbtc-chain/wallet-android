package com.bhex.lib.uikit.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PixelUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CustomViewPager extends ViewPager {

    private int current =1;
    private int height = 0;

    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*try {
            int numChildren = getChildCount();
            for (int i = 0; i < numChildren; i++) {
                View child = getChildAt(i);
                if (child != null) {
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    heightMeasureSpec = Math.max(heightMeasureSpec, MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        /*int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams childLP = child.getLayoutParams();
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), childLP.height);

            child.measure(widthMeasureSpec,childHeightSpec);

            maxHeight = Math.max(maxHeight,child.getMeasuredHeight());
        }

        int mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);*/
        LogUtils.d("CustomViewPager===>","onMeasure=="+current);
        int height = 0;

        if (mChildrenViews.size() >= current) {
            View child = mChildrenViews.get(current);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        //LogUtils.d("CustomViewPager===>","layoutParams=="+getLayoutParams().getClass().getName());
        this.current = current;
        LogUtils.d("CustomViewPager===>","resetHeight=="+current);

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) getLayoutParams();

        if (mChildrenViews.size() > current) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        }else{
            layoutParams.height = height;
        }
        setLayoutParams(layoutParams);
    }

    public void setObjectForPosition(int position,View view) {
        mChildrenViews.put(position, view);
    }

}
