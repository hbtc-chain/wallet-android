package com.bhex.lib.uikit.widget.keyborad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;

import java.lang.reflect.Method;
import java.util.List;

import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;

/**
 * @author gongdongyang
 * 2020年11月30日10:21:22
 */

public class PasswordKeyBoardView extends KeyboardView  {

    private static final int KEY_DOT = -10;
    private Keyboard mKeyboard;
    private SparseArray<EditText> mEditTextArrays = new SparseArray();//对应输入框
    private EditText mCurrentEditText;
    private Drawable  mDeleteDrawable;
    private Drawable  mSpecialKeyBackground;

    private double iconRatio = 0.5f;
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mKeyTextSize;

    private OnKeyListener mListener;

    //输入框所在的根布局
    private ViewGroup mInputRootView;
    //自定义软键盘所在的根布局
    private ViewGroup mKeyBoardRoot;

    public PasswordKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PasswordKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTextPaint.setDither(true);
        mKeyTextSize = context.getResources().getDimensionPixelSize(R.dimen.keyboard_text_size);
        mTextPaint.setTextSize(mKeyTextSize);
        mTextPaint.setColor(ColorUtil.getColor(context,R.color.global_main_text_color));
        mDeleteDrawable = ContextCompat.getDrawable(context,R.mipmap.ic_delete);
        mSpecialKeyBackground = ContextCompat.getDrawable(context,R.drawable.king_keyboard_special_key_bg);
    }

    public void setAttachToEditText(Activity activity,EditText et, ViewGroup inputRootView, ViewGroup keyBoardRoot) {
        if (mKeyboard == null) {
            mKeyboard = new Keyboard(getContext(), R.xml.number);
        }
        this.mEditTextArrays.put(et.getId(),et);
        this.mInputRootView = inputRootView;
        this.mKeyBoardRoot = keyBoardRoot;
        keyBoardRoot.findViewById(R.id.btn_finish).setOnClickListener(v -> {
            hideKeyBoard();
        });

        et.setOnFocusChangeListener((v,hasFocus)->{
            if(hasFocus){
                mCurrentEditText = et;
                ToolUtils.hideSystemSofeKeyboard(activity,et);
                showMyKeyBoard();
            }
        });
        et.setOnTouchListener((v,event)->{
            if(event.getAction() == MotionEvent.ACTION_UP){
                mCurrentEditText = et;
                ToolUtils.hideSystemSofeKeyboard(activity,et);
                showMyKeyBoard();
            }
            return false;
        });

        ToolUtils.hideSystemSofeKeyboard(activity,et);
        showMyKeyBoard();
        mCurrentEditText = mEditTextArrays.valueAt(0);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mKeyboard==null){
            return;
        }
        List<Keyboard.Key> keys = mKeyboard.getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                //绘制删除键
                drawkeyDelete( canvas,key);
            }else if(key.codes[0] == KEY_DOT){
                drawkeyDot( canvas,key);
            }
        }
    }

    private void showMyKeyBoard() {
        setKeyboard(mKeyboard);
        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(onKeyboardListener);
        mKeyBoardRoot.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    /**隐藏键盘*/
    private void hideKeyBoard() {
        if (getVisibility() == VISIBLE) {
            mKeyBoardRoot.setVisibility(GONE);
            setVisibility(GONE);
            //hideResize();
        }
    }

   private void drawkeyDelete(Canvas canvas, Keyboard.Key key) {
        //绘制背景
        mSpecialKeyBackground.setBounds(key.x +this.getPaddingLeft() , key.y+this.getPaddingTop(), key.x +this.getPaddingLeft() + key.width, key.y + key.height+this.getPaddingTop());
        mSpecialKeyBackground.draw(canvas);

        key.icon = mDeleteDrawable;
        float iconWidth = key.icon.getIntrinsicWidth();
        float iconHeight = key.icon.getIntrinsicHeight();

        double widthRatio = iconWidth/(float)key.width;
        double heightRatio = iconHeight/(float)key.height;

        if (widthRatio <= heightRatio) {//当图标的宽占比小于等于高占比时，以高度比例为基准并控制在iconRatio比例范围内，进行同比例缩放
            double ratio = Math.min(heightRatio,iconRatio);
            iconWidth  = (float)((iconWidth/heightRatio)*ratio);
            iconHeight = (float)((iconHeight/heightRatio)*ratio);

        } else {//反之，则以宽度比例为基准并控制在iconRatio比例范围内，进行同比例缩放
            double ratio = Math.min(widthRatio,iconRatio);
            iconWidth  = (float)((iconWidth/widthRatio)*ratio);
            iconHeight = (float)((iconHeight/widthRatio)*ratio);
        }
        float left = (key.x+this.getPaddingLeft()+(key.width-iconWidth)/2);
        float top = (key.y+this.getPaddingTop()+(key.height-iconHeight)/2);
        float right = left+iconWidth;
        float bottom = top+iconHeight;

        //LogUtils.d("log===>:","left=="+left+" =top="+top+"=right="+right+"=bottom="+bottom);
        key.icon.setBounds((int)left, (int)top, (int)right, (int)bottom);
        key.icon.draw(canvas);
    }

    private void drawkeyDot(Canvas canvas, Keyboard.Key key) {
        mSpecialKeyBackground.setBounds(key.x +this.getPaddingLeft() , key.y+this.getPaddingTop(), key.x +this.getPaddingLeft() + key.width, key.y + key.height+this.getPaddingTop());
        mSpecialKeyBackground.draw(canvas);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        float x = key.x+getPaddingLeft()+key.width/2;
        float y = key.y+getPaddingTop()+key.height/2+  (mTextPaint.getTextSize()-mTextPaint.descent())/2;
        canvas.drawText(key.label.toString(),x,y,mTextPaint);
    }

    private SimpleKeyboardActionListener onKeyboardListener = new SimpleKeyboardActionListener(){
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if(primaryCode == KEY_DOT){
                return;
            }

            if(mListener==null){
                return;
            }

            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                mListener.onDelete();
                Editable editable = mCurrentEditText.getText();
                if (editable != null && editable.length() > 0) {
                    if (mCurrentEditText.getSelectionStart() > 0) {
                        editable.delete(mCurrentEditText.getSelectionStart() - 1, mCurrentEditText.getSelectionStart());
                    }
                }

            }else {
                LogUtils.d("PasswordKeyBoardView===>:","code==="+String.valueOf((char) primaryCode));
                mListener.onInput(String.valueOf((char) primaryCode));
                mCurrentEditText.setText(mCurrentEditText.getText()+String.valueOf((char) primaryCode));
                mCurrentEditText.setSelection(mCurrentEditText.getText().length());

            }
        }
    };






    public interface OnKeyListener {
        // 输入回调
        void onInput(String text);
        // 删除回调
        void onDelete();
    }

    public void setOnKeyListener(OnKeyListener listener) {
        this.mListener = listener;
    }

}
