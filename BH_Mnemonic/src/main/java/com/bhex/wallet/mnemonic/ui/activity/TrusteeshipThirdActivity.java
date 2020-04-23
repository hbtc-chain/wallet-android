package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.text.SpannableString;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.StringUtils;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;
import com.bhex.wallet.mnemonic.ui.fragment.GlobalTipsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 创建托管单元第三步
 * 2020-3-12 20:47:54
 */
public class TrusteeshipThirdActivity extends BaseCacheActivity<TrusteeshipPresenter>
        implements GlobalTipsFragment.GlobalOnClickListenter {

    WalletViewModel walletViewModel;

    @BindView(R2.id.inp_wallet_confirm_pwd)
    InputView inp_wallet_confirm_pwd;

    @BindView(R2.id.btn_create)
    AppCompatButton btn_create;

    @BindView(R2.id.ck_agreement)
    AppCompatCheckBox ck_agreement;

    @BindView(R2.id.tv_password_count)
    AppCompatTextView tv_password_count;

    @BindView(R2.id.tv_agreement)
    AppCompatTextView tv_agreement;


    String mOldPwd;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_third;
    }

    @Override
    protected void initView() {
        mOldPwd = BHUserManager.getInstance().getTmpBhWallet().getPassword();
        SpannableString highlightText = StringUtils.highlight(this,
                getString(R.string.bh_register_agreement),
                getString(R.string.bh_register_agreement_sub),
                R.color.blue,
                0, 0);
        tv_agreement.setText(highlightText);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);
    }

    @Override
    protected void addEvent() {
        //获取输入密码
        inp_wallet_confirm_pwd.addTextWatch(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                mPresenter.checkConfirmPassword(inp_wallet_confirm_pwd,btn_create,mOldPwd,ck_agreement);
                //
                int count = inp_wallet_confirm_pwd.getInputString().length();
                tv_password_count.setText(String.format(getString(R.string.pwd_index),count));
            }
        });

        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);

        ck_agreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getPresenter().checkConfirmPassword(inp_wallet_confirm_pwd,btn_create,mOldPwd,ck_agreement);
        });


        walletViewModel.mutableLiveData.observe(this,loadDataModel -> {
            if (loadDataModel.loadingStatus== LoadingStatus.SUCCESS) {
                if(BHUserManager.getInstance().getTmpBhWallet().getWay()==MAKE_WALLET_TYPE.导入助记词.getWay()){
                    NavitateUtil.startMainActivity(this,null);
                }else{
                    NavitateUtil.startActivity(TrusteeshipThirdActivity.this, TrusteeshipSuccessActivity.class);
                    ActivityCache.getInstance().finishActivity();
                }

            } else if(loadDataModel.loadingStatus== LoadingStatus.ERROR){
                ToastUtils.showToast(getString(R.string.create_fail));
            }
        });
    }


    @OnClick({R2.id.btn_create,R2.id.tv_agreement})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_create){

            //设置密码
            String userName = BHUserManager.getInstance().getTmpBhWallet().getName();

            int way = BHUserManager.getInstance().getTmpBhWallet().way;

            if(way== MAKE_WALLET_TYPE.创建助记词.getWay()){
                generateMnemonic(userName,mOldPwd);
            }else if(way== MAKE_WALLET_TYPE.导入助记词.getWay()){
                importMnemoic(userName,mOldPwd);
            }else if(way==MAKE_WALLET_TYPE.PK.getWay()){
                importPrivatekey(userName,mOldPwd);
            }

        }else if(view.getId()==R.id.tv_agreement){
            GlobalTipsFragment.showDialog(getSupportFragmentManager(),"",
                    this,ck_agreement.isChecked());
        }
    }

    /**
     * 生成助记词
     */
    private void generateMnemonic(String name, String pwd) {
        walletViewModel.generateMnemonic(this,name, pwd);
    }

    /**
     * 导入助记词
     * @param name
     * @param pwd
     */
    private void importMnemoic(String name, String pwd) {
        List<String> words = BHUserManager.getInstance().getTmpBhWallet().mWords;
        walletViewModel.importMnemonic(this,words,name,pwd);
    }

    /**
     * 导入私钥
     * @param name
     * @param pwd
     */
    private void importPrivatekey(String name,String pwd){
        walletViewModel.importPrivateKey(this,name,pwd);
    }

    @Override
    public void onCheckClickListener(View view, boolean isCheck) {
        ck_agreement.setChecked(isCheck);
    }
}
