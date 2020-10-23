package com.bhex.lib.uikit.widget.seekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;
import com.bhex.lib.uikit.util.PixelUtils;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 21:25
 */
public class BHSeekBar extends View {

    private Context mContext;

    private Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSecondProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mInnerCriclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mWidth,mHeight,mCenterY;

    private float mProgress = 0.5f;

    private int mProgressHeight;

    private int mInnerRadius;

    private Rect mProgressRect = new Rect();

    private Rect mSecondProgressRect = new Rect();
    private Rect mCircleRect = new Rect();

    private float touchStartX = 0;
    private float touchStartY = 0;
    private boolean isThumbOnDragging;

    private float mCircleCenterX;

    public BHSeekBar(Context context) {
        this(context,null);
    }

    public BHSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BHSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mProgressHeight = PixelUtils.dp2px(mContext,4);
        mInnerRadius = PixelUtils.dp2px(mContext,12.5f);


        mProgressPaint.setColor(ContextCompat.getColor(mContext, R.color.seek_bar_bg));
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setDither(true);
        mProgressPaint.setStrokeWidth(mProgressHeight);

        mSecondProgressPaint.setColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress_color));
        mSecondProgressPaint.setStyle(Paint.Style.FILL);
        mSecondProgressPaint.setDither(true);
        mSecondProgressPaint.setStrokeWidth(mProgressHeight);

        mInnerCriclePaint.setColor(Color.WHITE);
        mInnerCriclePaint.setDither(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCenterY = mHeight/2;

        mProgressRect.top = mCenterY - mProgressHeight/2;
        mProgressRect.bottom = mCenterY + mProgressHeight/2;
        mProgressRect.left = mInnerRadius;
        mProgressRect.right = mWidth-mInnerRadius;

        mCircleCenterX = mWidth*mProgress;
        //BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_s)
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //绘制背景进度

        canvas.drawRect(mProgressRect,mProgressPaint);

        //绘制进度
        int progressWidth = (int)(mWidth*mProgress);
        mSecondProgressRect.top = mProgressRect.top;
        mSecondProgressRect.bottom = mProgressRect.bottom;
        mSecondProgressRect.left = mProgressRect.left;
        mSecondProgressRect.right = mSecondProgressRect.left+progressWidth;

        canvas.drawRect(mSecondProgressRect,mSecondProgressPaint);

        mCircleRect.top = mCenterY-mInnerRadius;
        mCircleRect.bottom = mCenterY+mInnerRadius;
        mCircleRect.left = (int)mCircleCenterX-mInnerRadius;
        mCircleRect.right = (int)mCircleCenterX+mInnerRadius;
        //绘制Bitmap
        mInnerCriclePaint.setShadowLayer(PixelUtils.dp2px(mContext,2), 3, 3, Color.GRAY);
        canvas.drawCircle(mSecondProgressRect.right,mCenterY,mInnerRadius,mInnerCriclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getX();
                touchStartY = event.getY();

                isThumbOnDragging = touchThumbOnDragging(event);
                //Log.d("BHSeekBar==>:","isThumbOnDragging=="+isThumbOnDragging);

                if(isThumbOnDragging){
                    mCircleCenterX = touchStartX;
                    mCircleRect.set((int)mCircleCenterX-mInnerRadius,mCircleRect.top,(int)mCircleCenterX+mInnerRadius,mCircleRect.bottom);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isThumbOnDragging){
                    float touchX = event.getX();
                    float touchY = event.getY();
                    float moveX = touchX-touchStartX;
                    if((mCircleCenterX+moveX)>=0 && (mCircleCenterX+moveX+mInnerRadius) <= mWidth-mInnerRadius){
                        mCircleCenterX = mCircleCenterX+moveX;
                        mCircleRect.set((int)mCircleCenterX-mInnerRadius,mCircleRect.top,(int)mCircleCenterX+mInnerRadius,mCircleRect.bottom);
                        touchStartX = touchX;

                        touchStartY = touchY;

                        mProgress = touchStartX/mWidth;

                        postInvalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isThumbOnDragging = false;

                break;
        }
        return true;
    }

    private boolean touchThumbOnDragging(MotionEvent event){
        float x = event.getX();

        float y = event.getY();
        Log.d("BHSeekBar==>:","left=="+mCircleRect.left+"=top=="+mCircleRect.top+"=right="+mCircleRect.right+"=bottom="+mCircleRect.bottom);
        Log.d("BHSeekBar==>:","x=="+x+"=y="+y);
        if( mCircleRect.contains((int)x,(int)y) ){
            return true;
        }
        return false;
    }
}
