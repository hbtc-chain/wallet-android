package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/6
 * Time: 15:15
 * 助记词 文本框
 */
public class MnemonicTextViewExt extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mRootLayout;

    AppCompatTextView mTextWordIndex;

    AppCompatTextView mTextWord;

    AppCompatImageView mBtnDelete;

    public MnemonicTextViewExt(Context context) {
        this(context,null);
    }

    public MnemonicTextViewExt(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MnemonicTextViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs) {
        mRootLayout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.layout_mnemonic_text_ext,this);


        mTextWordIndex = mRootLayout.findViewById(R.id.text_word_index);


        mTextWord = mRootLayout.findViewById(R.id.text_word);


        mBtnDelete = mRootLayout.findViewById(R.id.btn_delete);
        TypedArray ta = mContext.obtainStyledAttributes(attrs,R.styleable.MnemonicTextViewExt);

        int bgColor = ta.getColor(R.styleable.MnemonicTextViewExt_bgColor, ContextCompat.getColor(mContext,R.color.input_background));
        mRootLayout.findViewById(R.id.layout_root).setBackgroundColor(bgColor);
        ta.recycle();
    }


    public void setMnemonicAndIndex(String content,String index){
        mTextWordIndex.setText(index);
        mTextWord.setText(content);
    }


    public AppCompatTextView getTextWordIndexView() {
        return mTextWordIndex;
    }



    public AppCompatTextView getTextWordView() {
        return mTextWord;
    }


    public RelativeLayout getRootLayout() {
        return mRootLayout;
    }

    public AppCompatImageView getBtnDelete() {
        return mBtnDelete;
    }
}
