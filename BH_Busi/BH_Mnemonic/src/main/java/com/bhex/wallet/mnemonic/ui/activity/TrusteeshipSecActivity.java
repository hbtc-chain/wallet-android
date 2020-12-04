package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.keyborad.PasswordInputView;
import com.bhex.lib.uikit.widget.keyborad.PasswordKeyBoardView;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 创建托管单元第二步
 * 2020-3-12 20:47:54
 */
@Route(path = ARouterConfig.TRUSTEESHIP_MNEMONIC_SECOND)
public class TrusteeshipSecActivity extends BaseCacheActivity<TrusteeshipPresenter> {

    PasswordInputView mPasswordInputView;
    PasswordKeyBoardView mPasswordKeyboardView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_sec;
    }

    @Override
    protected void initView() {
        mPresenter.setToolBarTitle();
        mPasswordInputView = findViewById(R.id.input_password_view);
        mPasswordKeyboardView = findViewById(R.id.my_keyboard);;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);

    }

    @Override
    protected void addEvent() {
        mPasswordKeyboardView.setAttachToEditText(mPasswordInputView.m_input_content,mPasswordInputView,findViewById(R.id.keyboard_root));

        mPasswordKeyboardView.setOnKeyListener(new PasswordKeyBoardView.OnKeyListener() {
            @Override
            public void onInput(String text) {
                mPasswordInputView.m_input_content.setText(text);
                mPasswordInputView.onInputChange(mPasswordInputView.m_input_content.getEditableText());
            }

            @Override
            public void onDelete() {
                mPasswordInputView.onKeyDelete();
            }
        });

        mPasswordInputView.setOnInputListener(new PasswordInputView.OnInputListener() {
            @Override
            public void onComplete(String input) {
                //跳转密码确认
                BHUserManager.getInstance().getTmpBhWallet().setPassword(input);
                ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_MNEMONIC_THIRD)
                        .withString("password",input)
                        .navigation();
            }

            @Override
            public void onChange(String input) {

            }

            @Override
            public void onClear() {

            }
        });

    }


}
