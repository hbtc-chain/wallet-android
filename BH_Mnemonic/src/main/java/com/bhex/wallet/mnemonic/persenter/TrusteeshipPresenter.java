package com.bhex.wallet.mnemonic.persenter;

import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatButton;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.utils.RegexUtils;
import com.bhex.wallet.mnemonic.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 23:32
 */
public class TrusteeshipPresenter extends BasePresenter {

    public TrusteeshipPresenter(BaseActivity activity) {
        super(activity);
    }

    public void checkPassword(InputView inpPwd, AppCompatButton btnNext){
        String pwd = inpPwd.getInputString();
        if (!RegexUtils.checkPasswd(pwd)) {
            btnNext.setBackgroundResource(R.drawable.btn_gray_e7ecf4);
            btnNext.setEnabled(false);
        }else{
            btnNext.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            btnNext.setEnabled(true);
        }
    }

    public void checkConfirmPassword(InputView inpPwd, AppCompatButton btnNext, String oldPwd, CheckedTextView ck){
        String confirmPwd = inpPwd.getInputString();
        boolean flag = confirmPwd.equals(oldPwd)&& ck.isChecked();
        setBtnIsClick(flag,btnNext);
    }

    /**
     * 设置btn_next 按钮
     * @param flag
     * @param btnNext
     */
    public void setBtnIsClick(boolean flag,AppCompatButton btnNext){
        if(flag){
            btnNext.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            btnNext.setEnabled(true);
        }else{
            btnNext.setBackgroundResource(R.drawable.btn_gray_e7ecf4);
            btnNext.setEnabled(false);
        }
    }
}
