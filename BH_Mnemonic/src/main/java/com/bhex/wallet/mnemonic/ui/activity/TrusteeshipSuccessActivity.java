package com.bhex.wallet.mnemonic.ui.activity;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.ui.fragment.ScreenShotTipsFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * gdy
 * 2020-3-4 20:44:35
 * 托管单元创建成功
 */
@Route(path=ARouterConfig.TRUSTEESHIP_CREATE_OK_PAGE)
public class TrusteeshipSuccessActivity extends BaseCacheActivity implements ScreenShotTipsFragment.IKnowListener {

    @BindView(R2.id.btn_at_once_backup)
    AppCompatButton btn_at_once_backup;
    @BindView(R2.id.btn_later_backup)
    AppCompatTextView btn_later_backup;

    @Autowired(name=BHConstants.INPUT_PASSWORD)
    String inputPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_success;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
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

            if(MainActivityManager.getInstance().getTargetClass()!=null &&
                    MainActivityManager.getInstance().getTargetClass().equals(TrusteeshipManagerActivity.class)){
                EventBus.getDefault().post(new AccountEvent());
                //BHUserManager.getInstance().clear();
            }
            NavigateUtil.startMainActivity(this,
                    new String[]{BHConstants.BACKUP_TEXT, BHConstants.LATER_BACKUP});
            ActivityCache.getInstance().finishActivity();
        }

    }

    @Override
    public void clickBtn() {
        //NavigateUtil.startActivity(this, BackupMnemonicActivity.class);
        ARouter.getInstance().build(ARouterConfig.MNEMONIC_BACKUP).withString(BHConstants.INPUT_PASSWORD,inputPwd).navigation();
        ActivityCache.getInstance().finishActivity();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ScreenShotTipsFragment fragment = ScreenShotTipsFragment.showDialog(getSupportFragmentManager(),"");
        fragment.setIKnowListener(TrusteeshipSuccessActivity.this);
    }
}
