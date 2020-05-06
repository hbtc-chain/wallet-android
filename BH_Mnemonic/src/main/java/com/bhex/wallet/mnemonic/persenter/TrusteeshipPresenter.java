package com.bhex.wallet.mnemonic.persenter;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
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

    /**
     * 密码检查
     * @param inpPwd
     * @param btnNext
     * @param tv
     */
    public void checkPassword(InputView inpPwd, AppCompatButton btnNext, AppCompatTextView... tv){
        String pwd = inpPwd.getInputString();
        if(!RegexUtil.checkContainNum(pwd)){
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
            tv[3].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
        }else {
            tv[3].setTextColor(ColorUtil.getColor(getActivity(),R.color.dark_blue));
        }

        if(!RegexUtil.checkContainUpper(pwd)){
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
            tv[1].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
        }else{
            tv[1].setTextColor(ColorUtil.getColor(getActivity(),R.color.dark_blue));
        }

        if(!RegexUtil.checkContainLower(pwd)){
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
            tv[2].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
        }else{
            tv[2].setTextColor(ColorUtil.getColor(getActivity(),R.color.dark_blue));
        }

        if(pwd.length()<8){
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
            tv[4].setTextColor(ColorUtil.getColor(getActivity(),R.color.red));
        }else{
            tv[4].setTextColor(ColorUtil.getColor(getActivity(),R.color.dark_blue));
        }

        if (!RegexUtil.checkPasswd(pwd)) {
            btnNext.setBackgroundResource(R.drawable.btn_gray_e7ecf4);
            btnNext.setEnabled(false);
        }else{
            btnNext.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            tv[0].setTextColor(ColorUtil.getColor(getActivity(),R.color.dark_blue));
            btnNext.setEnabled(true);
        }
    }

    public void checkConfirmPassword(InputView inpPwd, AppCompatButton btnNext, String oldPwd, AppCompatCheckBox ck){
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

    /**
     * 设置Toolbar标题
     */
    public void setToolBarTitle(){
        AppCompatTextView tv_center_title = getActivity().findViewById(R.id.tv_center_title);
        int way = BHUserManager.getInstance().getTmpBhWallet().way;
        if(way== MAKE_WALLET_TYPE.创建助记词.getWay()){
            tv_center_title.setText(getActivity().getResources().getString(R.string.wallet_create_trusteeship));
        }else if(way== MAKE_WALLET_TYPE.导入助记词.getWay()){
            tv_center_title.setText(getActivity().getResources().getString(R.string.import_mnemonic));
        }else if(way== MAKE_WALLET_TYPE.PK.getWay()){
            tv_center_title.setText(getActivity().getResources().getString(R.string.import_private_key));
        }
    }
}
