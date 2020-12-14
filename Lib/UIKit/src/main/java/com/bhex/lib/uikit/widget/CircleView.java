package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.bhex.lib.uikit.R;

/**
 * @author gongdongyang
 * 2020-5-22 22:53:28
 * 圆形
 */
public class CircleView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mCircleRadius;
    private int mCircleColor;

    private int mCenterX,mCentetY;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Circle);
        //mCircleRadius = ta.getDimension(R.styleable.Circle_radius,8);
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) background;
            mCircleColor = colordDrawable.getColor();
            setBackground(null);
        }else{
            mCircleColor = ta.getColor(R.styleable.Circle_mcolor, Color.parseColor("#ED3756"));
        }

        ta.recycle();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
        mPaint.setColor(mCircleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX,mCentetY,mCircleRadius,mPaint);
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        mPaint.setColor(this.mCircleColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);

        int min = Math.min(width,heigth);
        mCenterX = width/2;
        mCentetY = heigth/2;
        mCircleRadius = min/2;
    }
}
