package com.bhex.lib.uikit.widget.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 13:44
 * 提币输入框
 */
public class WithDrawInput extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mRootView;

    public AppCompatEditText ed_input;

    public AppCompatTextView btn_right_text;

    public WithDrawInput(Context context) {
        this(context,null);
    }

    public WithDrawInput(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WithDrawInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_withdraw_input,this);
        ed_input = mRootView.findViewById(R.id.et_input_content);
        btn_right_text = mRootView.findViewById(R.id.btn_right_text);

        TypedArray ta = mContext.obtainStyledAttributes(attrs,R.styleable.With_Coin_Input);

        int rightTextColor = ta.getColor(R.styleable.With_Coin_Input_rightColor, ContextCompat.getColor(mContext,R.color.gray_f9f9fb));
        btn_right_text.setTextColor(rightTextColor);

        String leftHint = ta.getString(R.styleable.With_Coin_Input_leftHint);
        ed_input.setHint(leftHint);

        String rightText = ta.getString(R.styleable.With_Coin_Input_rightText);
        btn_right_text.setText(rightText);

        String leftText = ta.getString(R.styleable.With_Coin_Input_leftText);
        ed_input.setText(leftText);
        ta.recycle();
    }
}
