/*
 * *******************************************************************
 *   @项目名称: BHex Android
 *   @文件名称: VerticalScrollView.java
 *   @Date: 18-11-29 下午4:05
 *   @Author: ppzhao
 *   @Description:
 *   @Copyright（C）: 2018 BlueHelix Inc.   All rights reserved.
 *   注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的.
 *  *******************************************************************
 */

package com.bhex.lib.uikit.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;

public class VerticalScrollExtView extends NestedScrollView {
    public VerticalScrollExtView(Context context) {
        super(context);
    }

    public VerticalScrollExtView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalScrollExtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private float mDownPosX = 0;
    private float mDownPosY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截

                if (deltaX > deltaY) {// 左右滑动不拦截
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
