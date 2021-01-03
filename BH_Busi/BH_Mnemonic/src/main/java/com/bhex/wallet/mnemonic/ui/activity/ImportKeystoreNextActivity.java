package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.tools.utils.StringUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;
import com.bhex.wallet.mnemonic.ui.fragment.GlobalTipsFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-5-19 17:40:50
 * 导入Keystore设置用户名
 */
@Route(path = ARouterConfig.TRUSTEESHIP_IMPORT_PRIVATEKEY_NEXT, name="导入Keystore设置用户名")
public class ImportKeystoreNextActivity extends BaseCacheActivity<TrusteeshipPresenter>
        implements GlobalTipsFragment.GlobalOnClickListenter{

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.ck_agreement)
    AppCompatCheckBox ck_agreement;

    @BindView(R2.id.tv_agreement)
    AppCompatTextView tv_agreement;

    @BindView(R2.id.inp_wallet_name)
    InputView inp_wallet_name;

    @BindView(R2.id.tv_wallet_name_count)
    AppCompatTextView tv_wallet_name_count;

    @BindView(R2.id.btn_next)
    AppCompatButton btn_next;

    WalletViewModel walletViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_keystore_next;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);
    }

    @Override
    protected void initView() {
        inp_wallet_name.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        inp_wallet_name.addTextWatch(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                //
                getPresenter().checkUserName(inp_wallet_name,btn_next,ck_agreement);
                int count = inp_wallet_name.getInputString().length();
                tv_wallet_name_count.setText(String.format(getString(R.string.pwd_index),count));
            }
        });
        tv_center_title.setText(getString(R.string.import_keystore));
        SpannableString highlightText = StringUtils.highlight(this,
                getString(R.string.bh_register_agreement),
                getString(R.string.bh_register_agreement_sub),
                R.color.highlight_text_color,
                0, 0);
        tv_agreement.setText(highlightText);

        ck_agreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getPresenter().checkUserName(inp_wallet_name,btn_next,ck_agreement);
        });
    }

    @Override
    protected void addEvent() {
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.mutableLiveData.observe(this,ldm->{
            importKeyStoreStatus(ldm);
        });
    }

    @OnClick({R2.id.tv_agreement,R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.tv_agreement){
            GlobalTipsFragment.showDialog(getSupportFragmentManager(),"",
                    this,ck_agreement.isChecked());
        }else if(view.getId()==R.id.btn_next){
            String password = BHUserManager.getInstance().getTmpBhWallet().getPassword();
            String name = inp_wallet_name.getInputString();
            String keyStore = BHUserManager.getInstance().getTmpBhWallet().getKeystorePath();

            walletViewModel.importKeyStore(this,keyStore,name,password);

            ToolUtils.hintKeyBoard(this);
        }
    }

    @Override
    public void onCheckClickListener(View view, boolean isCheck) {
        ck_agreement.setChecked(isCheck);
    }

    /**
     * 导入KeyStore后的回调
     */
    private void importKeyStoreStatus(LoadDataModel<String> ldm){
        if(ldm.getLoadingStatus()== LoadingStatus.SUCCESS){
            if(ldm.getData().equals("1")){
                ToastUtils.showToast(getResources().getString(R.string.trusteeship_exist));
            }else{
                NavigateUtil.startMainActivity(this,new String[]{});
                ActivityCache.getInstance().finishActivity();
                EventBus.getDefault().post(new AccountEvent());
                ToastUtils.showToast(getResources().getString(R.string.import_keystore_success));
                BHUserManager.getInstance().clear();
            }

        }else if(ldm.getLoadingStatus()== LoadingStatus.ERROR){
            ToastUtils.showToast(ldm.msg);
        }
    }


}
