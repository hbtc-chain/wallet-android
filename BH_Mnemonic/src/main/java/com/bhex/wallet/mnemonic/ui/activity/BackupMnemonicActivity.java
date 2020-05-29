package com.bhex.wallet.mnemonic.ui.activity;


import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.MnemonicAdapter;
import com.bhex.wallet.mnemonic.helper.MnemonicDataHelper;
import com.bhex.wallet.mnemonic.ui.item.MnemonicItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * gdy
 * 2020-3-4 21:09:06
 */
@Route(path = ARouterConfig.MNEMONIC_BACKUP)
public class BackupMnemonicActivity extends BaseCacheActivity {

    @BindView(R2.id.recycler_mnemonic)
    RecyclerView recycler_mnemonic;

    @BindView(R2.id.btn_start_verify)
    AppCompatButton btn_start_verify;

    private List<MnemonicItem> mnemonicItemList;

    private MnemonicAdapter mnemonicAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_backup_mnemonic;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addEvent() {
        mnemonicItemList = MnemonicDataHelper.makeMnemonic();

        mnemonicAdapter = new MnemonicAdapter(R.layout.item_mnemonic,mnemonicItemList);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler_mnemonic.setLayoutManager(layoutManager);
        recycler_mnemonic.setAdapter(mnemonicAdapter);
    }

    @OnClick({R2.id.btn_start_verify})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.btn_start_verify){
            NavigateUtil.startActivity(this, VerifyMnemonicActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(BHUserManager.getInstance().getTargetClass()!=null &&
                BHUserManager.getInstance().getTargetClass().equals(TrusteeshipManagerActivity.class)){
            EventBus.getDefault().post(new AccountEvent());
            NavigateUtil.startMainActivity(this,new String[]{BHConstants.BACKUP_TEXT, BHConstants.LATER_BACKUP});
            ActivityCache.getInstance().finishActivity();
            BHUserManager.getInstance().clear();
        }else{
            finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(BHUserManager.getInstance().getTargetClass()!=null &&
                BHUserManager.getInstance().getTargetClass().equals(TrusteeshipManagerActivity.class)){
            EventBus.getDefault().post(new AccountEvent());
            BHUserManager.getInstance().clear();
            ActivityCache.getInstance().finishActivity();
        }
        NavigateUtil.startMainActivity(this,
                new String[]{BHConstants.BACKUP_TEXT, BHConstants.LATER_BACKUP});

        return super.onOptionsItemSelected(item);
    }


}
