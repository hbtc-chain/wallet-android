package com.bhex.wallet.bh_main.my.presenter;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/21
 * Time: 23:13
 */
public class MyPresenter extends BasePresenter {

    public MyPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 检查密码
     * @param mb
     * @param oldPwd
     * @param newPwd
     */
    public void checkPasswordIsInput(AppCompatButton mb, String oldPwd, String newPwd, String newConfrimPwd, AppCompatTextView... tv){
        boolean flag = true;
        if(TextUtils.isEmpty(oldPwd)){
            flag = false;
        }

        if(TextUtils.isEmpty(newPwd)){
            flag = false;
        }

        if(TextUtils.isEmpty(newPwd)){
            flag = false;
        }

        if(!TextUtils.isEmpty(newPwd)){
            if(!RegexUtil.checkContainNum(newPwd)){
                tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                tv[3].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                flag = false;
            }else {
                tv[3].setTextColor(ColorUtil.getColor(getActivity(),R.color.global_secondary_text_color));
            }

            if(!RegexUtil.checkContainUpper(newPwd)){
                tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                tv[1].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                flag = false;
            }else{
                tv[1].setTextColor(ColorUtil.getColor(getActivity(),R.color.global_secondary_text_color));
            }

            if(!RegexUtil.checkContainLower(newPwd)){
                tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                tv[2].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                flag = false;
            }else{
                tv[2].setTextColor(ColorUtil.getColor(getActivity(),R.color.global_secondary_text_color));
            }

            if(newPwd.length()<8){
                tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                tv[4].setTextColor(ColorUtil.getColor(getActivity(),R.color.color_red));
                flag = false;
            }else{
                tv[4].setTextColor(ColorUtil.getColor(getActivity(),R.color.global_secondary_text_color));
            }
        }


        if(TextUtils.isEmpty(newConfrimPwd)) {
            flag = false;
        }

        if(flag){
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.global_secondary_text_color));
            mb.setBackgroundColor(ColorUtil.getColor(getActivity(), R.color.global_button_bg_color));
            mb.setTextColor(getActivity().getResources().getColor(R.color.global_button_text_color));
            mb.setEnabled(true);
        }else{
            mb.setBackgroundColor(ColorUtil.getColor(getActivity(), R.color.global_button_enable_false_bg));
            mb.setTextColor(getActivity().getResources().getColor(R.color.global_button_enable_false_text));
            mb.setEnabled(false);
        }

    }

    public boolean checkPasswordEqual(String oldPwd,String newPwd,String newConfrimPwd){
        boolean flag = true;
        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        /*if(!bhWallet.password.equals(MD5.md5(oldPwd))){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.error_oldpassword));
            return false;
        }*/
        if(!ToolUtils.isVerifyPass(oldPwd,bhWallet.getPassword())){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.error_oldpassword));
            return false;
        }

        if(TextUtils.isEmpty(newPwd)){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.please_input_newpassword));
            return false;
        }

        if(TextUtils.isEmpty(newConfrimPwd)){
            ToastUtils.showToast(getActivity().getString(R.string.please_input_confrim_newpwd));
            return false;
        }

        if(!newPwd.equals(newConfrimPwd)){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.tip_two_password_equal));
            return false;
        }

        return flag;
    }

    public BHWallet makeBhWallet(BHWallet wallet){
        BHWallet item = new BHWallet();
        item.id = wallet.id;
        item.name = wallet.name;
        item.address = wallet.address;
        item.password = wallet.password;
        item.mnemonic = wallet.mnemonic;
        item.isBackup = wallet.isBackup;
        item.isDefault = wallet.isDefault;
        item.privateKey = wallet.privateKey;
        item.way = wallet.way;
        item.keystorePath = wallet.keystorePath;
        item.publicKey = wallet.publicKey;
        return item;
    }
}
