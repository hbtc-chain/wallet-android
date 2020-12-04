package com.bhex.lib.uikit.widget.keyborad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PixelUtils;

/**
 * @author gongdongyang
 * 2020-11-30
 */
public class PasswordTextView extends AppCompatTextView {

    private float mRadius;

    public boolean mHasPassword;

    private Paint mCirclePaint;

    private Paint mPasswordPaint;

    //public  String text ="";

    //public int mWidth;

    public int input_border_normal_color;
    public int input_border_focus_color;

    public GradientDrawable input_border_normal_color_drawable;
    public GradientDrawable input_border_focus_color_drawable;

    //0无 1 有边框
    public boolean is_border;

    public PasswordTextView(@NonNull Context context) {
        this(context,null);
    }

    public PasswordTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PasswordTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context,attrs);
    }

    //初始化画笔
    private void initPaint(Context context,AttributeSet attrs) {
        setWillNotDraw(false);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(ColorUtil.getColor(context, R.color.password_circle_bound_color));
        mCirclePaint.setStrokeWidth(PixelUtils.dp2px(context,1));
        mCirclePaint.setStyle(Paint.Style.STROKE);

        //#475563
        mPasswordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPasswordPaint.setDither(true);
        //mPasswordPaint.setColor(Color.parseColor("#475563"));
        mPasswordPaint.setColor(ColorUtil.getColor(context, R.color.password_circle_fill_color));
        mPasswordPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        mRadius = Math.min(width,height)>>2;

        input_border_normal_color_drawable = ShapeUtils.getRoundRectStrokeDrawable(
                4, ContextCompat.getColor(getContext(),R.color.app_bg),input_border_normal_color,2
        );

        input_border_focus_color_drawable = ShapeUtils.getRoundRectStrokeDrawable(
                4, ContextCompat.getColor(getContext(),R.color.app_bg),input_border_focus_color,2
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(is_border){
            if(mHasPassword){
                canvas.drawCircle(getWidth() >> 1,getHeight() >> 1,mRadius,mPasswordPaint);
            }else{
                super.onDraw(canvas);
            }
        }else{
            if (mHasPassword) {
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRadius-PixelUtils.dp2px(getContext(),1), mPasswordPaint);
            }else{
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRadius-PixelUtils.dp2px(getContext(),1), mCirclePaint);
            }
        }

    }

    //清除密码
    public void clearPassword() {
        mHasPassword = false;
        if(is_border){
            setBackground(input_border_normal_color_drawable);
        }
        invalidate();
    }

    //绘制密码
    public void drawPassword() {
        mHasPassword = true;
        if(is_border){
            setBackground(input_border_focus_color_drawable);
        }
        postInvalidate();
    }


}
