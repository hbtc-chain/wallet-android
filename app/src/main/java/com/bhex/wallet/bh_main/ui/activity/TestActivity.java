package com.bhex.wallet.bh_main.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bhex.lib.uikit.widget.keyborad.PasswordInputView;
import com.bhex.lib.uikit.widget.keyborad.PasswordKeyBoardView;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.R;

public class TestActivity extends AppCompatActivity {
    PasswordInputView passwordInputView;
    PasswordKeyBoardView my_keyboard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        passwordInputView = findViewById(R.id.input_password_view);
        passwordInputView.setOnInputListener(new PasswordInputView.OnInputListener() {
            @Override
            public void onComplete(String input) {
                LogUtils.d("PasswordInputView===>:","onComplete=="+input);
            }

            @Override
            public void onChange(String input) {
                LogUtils.d("PasswordInputView===>:","onChange=="+input);
            }

            @Override
            public void onClear() {
                //LogUtils.d("PasswordInputView===>:","onClear==");
            }
        });
        my_keyboard = findViewById(R.id.my_keyboard);
        my_keyboard.setOnKeyListener(new PasswordKeyBoardView.OnKeyListener() {
            @Override
            public void onInput(String text) {
                //LogUtils.d("PasswordInputView===>:","onInput=="+text);
                passwordInputView.m_input_content.setText(text);
                passwordInputView.onInputChange(passwordInputView.m_input_content.getEditableText());
            }

            @Override
            public void onDelete() {
                passwordInputView.onKeyDelete();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        my_keyboard.setAttachToEditText(passwordInputView.m_input_content,passwordInputView,findViewById(R.id.keyboard_root));
    }

    public void onPaintAction(View view) {

        //passwordInputView.requestLayout();
    }
}
