package com.bhex.wallet.mnemonic.ui.activity;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.Constants;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.ui.fragment.GlobalTipsFragment;
import com.bhex.wallet.mnemonic.ui.fragment.ScreenShotTipsFragment;
import com.bhex.wallet.mnemonic.viewmodel.WalletViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * gdy
 * 2020-3-4 20:44:35
 * 托管单元创建成功
 */
public class TrusteeshipSuccessActivity extends BaseCacheActivity implements ScreenShotTipsFragment.IKnowListener {

    @BindView(R2.id.btn_at_once_backup)
    AppCompatButton btn_at_once_backup;
    @BindView(R2.id.btn_later_backup)
    AppCompatTextView btn_later_backup;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_success;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addEvent() {

    }


    @OnClick({R2.id.btn_at_once_backup, R2.id.btn_later_backup})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.btn_at_once_backup){
            ScreenShotTipsFragment fragment = ScreenShotTipsFragment.showDialog(getSupportFragmentManager(),"");
            fragment.setIKnowListener(TrusteeshipSuccessActivity.this);
        }else if(view.getId()== R.id.btn_later_backup){
            if(BHUserManager.getInstance().getTargetClass().equals(TrusteeshipManagerActivity.class)){
                ARouterUtil.startActivity(ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE);
            }else{
                NavitateUtil.startMainActivity(this,
                        new String[]{Constants.BACKUP_TEXT,Constants.LATER_BACKUP});
            }

            ActivityCache.getInstance().finishActivity();
        }

    }

    @Override
    public void clickBtn() {
        NavitateUtil.startActivity(this, BackupMnemonicActivity.class);
        ActivityCache.getInstance().finishActivity();
    }
}
