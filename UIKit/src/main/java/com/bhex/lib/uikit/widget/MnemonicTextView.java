package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/6
 * Time: 15:15
 * 助记词 文本框
 */
public class MnemonicTextView extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mRootLayout;

    AppCompatTextView mTextWordIndex;

    AppCompatTextView mTextWord;

    public MnemonicTextView(Context context) {
        this(context,null);
    }

    public MnemonicTextView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MnemonicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    private void initLayout() {
        mRootLayout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.layout_mnemonic_text,this);

        Log.d("MnemonicTextView","mRootLayout=="+mRootLayout.toString());

        mTextWordIndex = mRootLayout.findViewById(R.id.text_word_index);


        Log.d("MnemonicTextView","mRootLayout=="+mTextWordIndex.getText());

        mTextWord = mRootLayout.findViewById(R.id.text_word);

        Log.d("MnemonicTextView","mRootLayout=="+mTextWord.getText());


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
}
