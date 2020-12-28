package com.bhex.wallet.mnemonic.persenter;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.SecuritySettingManager;
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
     * 设置按钮的状态
     */
    public void setButtonStatus(AppCompatButton btn_confirm,String pwd){
        boolean flag = false;
        flag = RegexUtil.checkPasswd(pwd);

        if (flag) {
            btn_confirm.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.global_button_bg_color));
            btn_confirm.setTextColor(getActivity().getResources().getColor(R.color.global_button_text_color));
            btn_confirm.setEnabled(true);
        }else{
            btn_confirm.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.global_button_enable_false_bg));
            btn_confirm.setTextColor(getActivity().getResources().getColor(R.color.global_button_enable_false_text));
            btn_confirm.setEnabled(false);
        }
    }

    /**
     * 密码验证
     */
    public void verifyPassword(String inputPwd, BHWallet bhWallet){
        if(MD5.verify(inputPwd,bhWallet.getPassword())){
            //SecuritySettingManager.getInstance().recordPwd(inputPwd);
            ARouter.getInstance().build(ARouterConfig.Main.main_mainindex).navigation();
            getActivity().finish();
        }else{
            ToastUtils.showToast(getActivity().getString(R.string.error_password));
        }
    }
}
