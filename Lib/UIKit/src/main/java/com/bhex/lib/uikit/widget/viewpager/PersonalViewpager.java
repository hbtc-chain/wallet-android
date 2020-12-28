package com.bhex.lib.uikit.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by  HONGDA on 2018/6/15.
 */
public class PersonalViewpager extends ViewPager {

    //是否可以进行滑动
    private boolean canScroll = true;

    public PersonalViewpager(Context context) {
        super(context);
    }

    public PersonalViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            /*if(child instanceof ConstraintLayout){

            }*/
            int count  = ((RecyclerView)((FrameLayout)((RelativeLayout)child).getChildAt(1)).getChildAt(0)).getChildCount();
            Log.d("PersonalViewpager===>:","count==="+count);
            if (h > height) height = h;
            //Log.d("PersonalViewpager===>:",child.getClass().getName()+"=i=="+i+"=2=h="+height);

        }
        //Log.d("PersonalViewpager===>:","count=2="+getChildCount()+"=h="+height);

        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll;
    }


}
