package com.bhex.wallet.mnemonic.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java8.util.stream.IntStreams;

/**
 * @author gongdongyang
 * 2020-12-2 12:13:13
 */
public class FunctionItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Context mContext;
    private int mDividerColor;
    private float mDividerHeight;
    private int [] mDividerPosition;

    public FunctionItemDecoration(Context context,int dividerColor,float dividerHeight, int... params) {
        mContext = context;
        mDividerColor = dividerColor;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        mDividerPosition = params;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        IntStreams.range(0,mDividerPosition.length).forEach(t->{
            if(mDividerPosition[t]==position){
                outRect.bottom = (int)mDividerHeight;
            }});
    }
    int i = 0;
    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int childCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        mPaint.setColor(mDividerColor);
        i = 0;
        for ( i = 0; i < childCount; i++) {
            View v = parent.getChildAt(i);
            IntStreams.range(0,mDividerPosition.length).forEach(t->{
                if(mDividerPosition[t]==i){
                    canvas.drawRect(left, v.getBottom(), right,
                            v.getBottom()+mDividerHeight, mPaint);
                }});

        }
    }


    public void setDividerColor(int mDividerColor) {
        this.mDividerColor = mDividerColor;
    }
}
