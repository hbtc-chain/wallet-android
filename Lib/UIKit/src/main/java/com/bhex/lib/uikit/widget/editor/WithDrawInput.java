package com.bhex.lib.uikit.widget.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
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

    public AppCompatEditText mInputEd;

    public AppCompatTextView btn_right_text;

    public AppCompatImageView iv_right;


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
        mInputEd = mRootView.findViewById(R.id.et_input_content);
        btn_right_text = mRootView.findViewById(R.id.btn_right_text);

        iv_right = mRootView.findViewById(R.id.iv_right);


        TypedArray ta = mContext.obtainStyledAttributes(attrs,R.styleable.With_Coin_Input);

        int rightTextColor = ta.getColor(R.styleable.With_Coin_Input_rightColor, ContextCompat.getColor(mContext,R.color.global_input_profix_text_color));
        btn_right_text.setTextColor(rightTextColor);

        String leftHint = ta.getString(R.styleable.With_Coin_Input_leftHint);
        mInputEd.setHint(leftHint);

        String rightText = ta.getString(R.styleable.With_Coin_Input_rightText);
        btn_right_text.setText(rightText);

        String leftText = ta.getString(R.styleable.With_Coin_Input_leftText);
        mInputEd.setText(leftText);

        boolean isEnble = ta.getBoolean(R.styleable.With_Coin_Input_isEditable,true);
        mInputEd.setEnabled(isEnble);
        ta.recycle();
    }

    public EditText getEditText() {
        return mInputEd;
    }

    public String getInputString() {
        return mInputEd.getText().toString().trim();
    }

    public String getInputStringTrim() {
        return mInputEd.getText().toString().trim().replaceAll(" ","");
    }

    public void setInputString(String value) {
        if(value==null){
            return;
        }
        mInputEd.setText(value);
        mInputEd.setSelection(value.length());
        mInputEd.requestFocus();
    }

}
