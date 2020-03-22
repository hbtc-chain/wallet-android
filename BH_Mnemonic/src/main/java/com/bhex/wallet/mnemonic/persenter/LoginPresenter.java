package com.bhex.wallet.mnemonic.persenter;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.RegexUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.mnemonic.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/14
 */
public class LoginPresenter extends BasePresenter {

    public LoginPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 地址掩码
     */
    public void proccessAddress(AppCompatTextView tv_address,String address){
        StringBuffer buf = new StringBuffer("");
        if(!TextUtils.isEmpty(address)){
            buf.append(address.substring(0,15))
                    .append("***")
                    .append(address.substring(address.length()-8,address.length()));
            tv_address.setText(buf.toString());
        }

    }

    /**
     * 设置按钮的状态
     */
    public void setButtonStatus(AppCompatButton btn_confirm,String pwd){
        boolean flag = false;
        flag = RegexUtils.checkPasswd(pwd);

        if (flag) {
            btn_confirm.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            btn_confirm.setEnabled(true);
        }else{
            btn_confirm.setBackgroundResource(R.drawable.btn_gray_e7ecf4);
            btn_confirm.setEnabled(false);
        }
    }

    /**
     * 密码验证
     */
    public void verifyPassword(String inputPwd, BHWallet bhWallet){
        String pwdMd5 = MD5.md5(inputPwd);
        if(pwdMd5.equals(bhWallet.getPassword())){
            NavitateUtil.startMainActivity(getActivity());
            getActivity().finish();
        }else{
            ToastUtils.showToast(getActivity().getString(R.string.error_password));
        }
    }
}
