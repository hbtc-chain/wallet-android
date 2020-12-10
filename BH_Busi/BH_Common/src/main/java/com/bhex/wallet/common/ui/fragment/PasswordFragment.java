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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.bhex.lib.uikit.widget.keyborad.PasswordInputView;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.network.utils.ToastUtils;
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
 * 2020-3-20 21:25:40
 * 密码安全验证对话框
 */
public class PasswordFragment extends BaseDialogFragment {

    /*@BindView(R2.id.btn_cancel)
    MaterialButton btn_cancel;

    @BindView(R2.id.btn_confirm)
    MaterialButton btn_confirm;*/

    @BindView(R2.id.inp_wallet_pwd)
    PasswordInputView inp_wallet_pwd;

    @BindView(R2.id.tv_sub_title)
    AppCompatTextView tv_sub_title;

    @BindView(R2.id.iv_close)
    AppCompatImageView iv_close;

    private String mSubTitle;

    private PasswordClickListener passwordClickListener;

    private int position;

    private int  verifyPwdWay = BH_BUSI_TYPE.校验当前账户密码.getIntValue();

    @Override
    public int getLayout() {
        return R.layout.fragment_password;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        window.setAttributes(params);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    protected void initView() {
        if(!TextUtils.isEmpty(mSubTitle)){
            tv_sub_title.setText(mSubTitle);
            tv_sub_title.setVisibility(View.VISIBLE);
        }
        inp_wallet_pwd.setOnInputListener(new PasswordInputView.OnInputListener() {
            @Override
            public void onComplete(String input) {
                if (passwordClickListener == null) {
                    return;
                }
                //String inputPassword = inp_wallet_pwd.getInputContent();
                ToolUtils.hintKeyBoard(getActivity(),inp_wallet_pwd.m_input_content);
                //检验密码
                checkPassword(inp_wallet_pwd.getInputContent().trim());
            }

            @Override
            public void onChange(String input) {

            }

            @Override
            public void onClear() {

            }
        });
    }

    /**
     * 显示密码对话框
     *
     * @param fm
     * @param tag
     */
    public static PasswordFragment showPasswordDialog(FragmentManager fm, String tag,PasswordClickListener listener,int position) {
        PasswordFragment pfrag = new PasswordFragment();
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

    public static PasswordFragment showPasswordDialogExt(FragmentManager fm, String tag,PasswordClickListener listener,int position) {
        PasswordFragment pfrag = new PasswordFragment();
        pfrag.passwordClickListener = listener;
        pfrag.position = position;
        //pfrag.show(fm, tag);
        return pfrag;
    }

    public void setTv_sub_title(String sub_title){
        //tv_sub_title.setText(sub_title);
        mSubTitle = sub_title;
    }

    @OnClick({R2.id.iv_close})
    public void onViewClicked(View view) {
        if(view.getId() == R.id.iv_close){
            dismissAllowingStateLoss();
            ToolUtils.hintKeyBoard(getActivity());
        }
    }

    public void setPasswordClickListener(PasswordClickListener passwordClickListener) {
        this.passwordClickListener = passwordClickListener;
    }

    //检验密码
    private void checkPassword(String inputPassword){
        BHWallet currentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        if(TextUtils.isEmpty(inputPassword)){
            ToastUtils.showToast(getResources().getString(R.string.please_input_password));
            return;
        }

        if(verifyPwdWay== BH_BUSI_TYPE.校验当前账户密码.getIntValue()){
            if(!ToolUtils.isVerifyPass(inputPassword,currentWallet.password)){
                ToastUtils.showToast(getResources().getString(R.string.error_password));
                return;
            }
            passwordClickListener.confirmAction(inputPassword,position,verifyPwdWay);
            dismiss();
        }else {
            passwordClickListener.confirmAction(inputPassword,position,verifyPwdWay);
        }
    }

    public interface PasswordClickListener {
        void confirmAction(String password, int position,int way);
    }

    public void setVerifyPwdWay(int verifyPwdWay) {
        this.verifyPwdWay = verifyPwdWay;
    }
}
