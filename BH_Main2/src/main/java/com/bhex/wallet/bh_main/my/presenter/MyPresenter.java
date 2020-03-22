package com.bhex.wallet.bh_main.my.presenter;

import com.bhex.lib.uikit.widget.util.ColorUtil;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.RegexUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.google.android.material.button.MaterialButton;

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
    public void checkPasswordIsInput(MaterialButton mb, String oldPwd, String newPwd){
        if(RegexUtils.checkPasswd(oldPwd) && RegexUtils.checkPasswd(newPwd)){
            mb.setBackgroundColor(ColorUtil.getColor(getActivity(), R.color.blue));
            mb.setEnabled(true);
        }else{
            mb.setBackgroundColor(ColorUtil.getColor(getActivity(), R.color.gray_E7ECF4));
            mb.setEnabled(false);
        }

    }

    public boolean checkPasswordEqual(String oldPwd){
        boolean flag = false;
        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        if(bhWallet.password.equals(MD5.md5(oldPwd))){
            return true;
        }else{
            ToastUtils.showToast("原密码错误");
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
        return item;
    }
}
