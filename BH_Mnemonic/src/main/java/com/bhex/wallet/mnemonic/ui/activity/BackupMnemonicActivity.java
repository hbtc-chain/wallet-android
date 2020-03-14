package com.bhex.wallet.mnemonic.ui.activity;


import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.MnemonicAdapter;
import com.bhex.wallet.mnemonic.helper.MnemonicDataHelper;
import com.bhex.wallet.mnemonic.ui.item.MnemonicItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * gdy
 * 2020-3-4 21:09:06
 */
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
            NavitateUtil.startActivity(this, VerifyMnemonicActivity.class);
        }
    }


}
