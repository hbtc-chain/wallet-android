package com.bhex.wallet.common.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.keyborad.PasswordInputView;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.R;
import com.bhex.wallet.common.R2;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.SecuritySettingManager;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 免密码
 * 2020-11-9 16:14:17
 */
public class Password30Fragment extends BaseDialogFragment {

    @BindView(R2.id.btn_cancel)
    MaterialButton btn_cancel;

    @BindView(R2.id.btn_confirm)
    MaterialButton btn_confirm;

    @BindView(R2.id.inp_wallet_pwd)
    InputView inp_wallet_pwd;

    @BindView(R2.id.ck_password)
    CheckedTextView ck_password;
    @BindView(R2.id.iv_close)
    AppCompatImageView iv_close;
    @BindView(R2.id.input_password)
    PasswordInputView input_password;

    private Password30Fragment.PasswordClickListener passwordClickListener;

    private int position;

    private int  verifyPwdWay = BH_BUSI_TYPE.校验当前账户密码.getIntValue();

    @Override
    public int getLayout() {
        return R.layout.fragment_password30;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.centerDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.width = dm.widthPixels - PixelUtils.dp2px(BaseApplication.getInstance(), 48);
        //params.height = PixelUtils.dp2px(BaseApplication.getInstance(), 185);
        window.setAttributes(params);

    }


    @Override
    protected void initView() {
        input_password.setOnInputListener(new PasswordInputView.OnInputListener() {
            @Override
            public void onComplete(String input) {
                //
                ToolUtils.hintKeyBoard(getActivity(),inp_wallet_pwd.getEditText());
                checkPassword(input);
            }

            @Override
            public void onChange(String input) {

            }

            @Override
            public void onClear() {

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 显示密码对话框
     *
     * @param fm
     * @param tag
     */
    public static Password30Fragment showPasswordDialog(FragmentManager fm, String tag, Password30Fragment.PasswordClickListener listener, int position) {
        Password30Fragment pfrag = new Password30Fragment();
        pfrag.passwordClickListener = listener;
        pfrag.position = position;
        if(SecuritySettingManager.getInstance().notNeedPwd()){
            pfrag.passwordClickListener.confirmAction(
                    BHUserManager.getInstance().getCurrentBhWallet().pwd,
                    position,BH_BUSI_TYPE.校验当前账户密码.getIntValue());
        }else{
            pfrag.show(fm, tag);
        }
        return pfrag;
    }

    @OnClick({R2.id.btn_cancel, R2.id.btn_confirm,R2.id.ck_password,R2.id.iv_close})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_cancel) {
            dismiss();
        }

        if (view.getId() == R.id.btn_confirm) {
            if (passwordClickListener == null) {
                return;
            }

            ToolUtils.hintKeyBoard(getActivity(),inp_wallet_pwd.getEditText());
            //检验密码
            checkPassword(inp_wallet_pwd.getInputString().trim());
        }

        if(view.getId() == R.id.ck_password){
            ck_password.toggle();
        }

        if(view.getId() == R.id.iv_close){
            dismissAllowingStateLoss();
            ToolUtils.hintKeyBoard(getActivity());
        }
    }

    //检验密码
    private void checkPassword(String inputPassword){
        BHWallet currentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        if(TextUtils.isEmpty(inputPassword)){
            ToastUtils.showToast(getResources().getString(R.string.please_input_password));
            return;
        }

        //
        if(verifyPwdWay== BH_BUSI_TYPE.校验当前账户密码.getIntValue()){
            if(!ToolUtils.isVerifyPass(inputPassword,currentWallet.password)){
                ToastUtils.showToast(getResources().getString(R.string.error_password));
                return;
            }
            dismiss();
        }

        passwordClickListener.confirmAction(inputPassword,position,verifyPwdWay);
        if(ck_password.isChecked()){
            //开启30分钟计时
            SecuritySettingManager.getInstance().request_thirty_in_time(true,inputPassword);
        }else{
            //关闭30分钟计时
            SecuritySettingManager.getInstance().request_thirty_in_time(false,"");
        }
    }

    public void setPasswordClickListener(Password30Fragment.PasswordClickListener passwordClickListener) {
        this.passwordClickListener = passwordClickListener;
    }

    public interface PasswordClickListener {
        void confirmAction(String password, int position,int way);
    }

    public void setVerifyPwdWay(int verifyPwdWay) {
        this.verifyPwdWay = verifyPwdWay;
    }
}