package com.bhex.lib.uikit.widget.editor;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bhex.lib.uikit.R;


/**
 * Created by gongdongyang on 2018/9/26.
 */

public class SmsCodeViewWrapper {

    private EditText mSmsCodeView;
    private TextView mGetSmsCodeView;

    private String mSmsCode;

    private CountDownTimer mTimer;//用来实现倒计时

    private onGetSmsCodeViewClickListener mOnGetSmsCodeViewClickListener;
    private TextWatcher mTextWatcher;

    public SmsCodeViewWrapper(View view) {
        mSmsCodeView = (EditText) view.findViewById(R.id.et_auth_code);
        EditTextWrapper wrapper = EditTextHelper.getEditTextDeleteIconWrapper(mSmsCodeView.getContext(), mSmsCodeView);
        wrapper.setTextWatcher(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    mSmsCodeView.setSelection(s.length());
                }
                mSmsCode = s.toString();
                if (!TextUtils.isEmpty(mSmsCode) && mSmsCode.length() > 6) {
                    mSmsCodeView.setText(mSmsCode.substring(0, 6));
                }
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });
        mGetSmsCodeView = (TextView) view.findViewById(R.id.tv_get_auth_code);
        mGetSmsCodeView.setText("获取验证码");
        mGetSmsCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnGetSmsCodeViewClickListener != null) {
                    mOnGetSmsCodeViewClickListener.onGetSmsCodeViewClick(v);
                }
            }
        });
    }

    public void setOnGetSmsCodeViewClickListener(onGetSmsCodeViewClickListener listener) {
        mOnGetSmsCodeViewClickListener = listener;
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        mTextWatcher = textWatcher;
    }

    public void setGetCodeEnabled(boolean enabled) {
        mGetSmsCodeView.setBackgroundResource(R.drawable.base_selector_bg_get_auth_code_orange);
        mGetSmsCodeView.setEnabled(enabled);
    }

    public void startCountDown() {
        setGetCodeEnabled(false);
        final StringBuilder sb = new StringBuilder();
        if (mTimer == null) {
            mTimer = new CountDownTimer(1000 * 60L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setGetCodeEnabled(false);
                    int count = (int) (millisUntilFinished / 1000);
                    sb.delete(0, sb.length());
                    sb.append("重新发送").append("（").append(count).append("s").append("）");
                    mGetSmsCodeView.setText("重新发送（" + count + "s）");
                }

                @Override
                public void onFinish() {
                    setGetCodeEnabled(true);
                    mGetSmsCodeView.setText("重新发送");
                }
            };
        }
        mTimer.start();
    }

    public void stopCountDown() {
        if (mTimer != null) {
            mTimer.cancel();
            setGetCodeEnabled(true);
            mGetSmsCodeView.setText("重新发送");
        }
    }

    public String getSmsCode() {
        return mSmsCodeView.getText().toString();
    }

    public interface onGetSmsCodeViewClickListener {
        void onGetSmsCodeViewClick(View view);
    }
}
