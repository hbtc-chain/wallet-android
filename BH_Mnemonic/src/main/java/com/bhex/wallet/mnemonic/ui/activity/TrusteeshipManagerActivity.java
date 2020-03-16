package com.bhex.wallet.mnemonic.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.TrustManagerAdapter;
import com.bhex.wallet.mnemonic.persenter.TrustManagerPresenter;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;
import com.bhex.wallet.mnemonic.viewmodel.WalletViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-3-14 20:26:34
 * 托管单元管理
 */
@Route(path = ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE)
public class TrusteeshipManagerActivity extends BaseActivity<TrustManagerPresenter> implements TrustManagerAdapter.OnCheckClickListener {


    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.recycler_trusteeship)
    RecyclerView recycler_trusteeship;

    @BindView(R2.id.btn_wallet_create)
    AppCompatTextView btnWalletCreate;

    @BindView(R2.id.btn_wallet_impot)
    AppCompatTextView btnWalletImpot;

    private TrustManagerAdapter mTrustManagerAdapter;

    private List<BHWalletItem> mAllWalletList;

    //
    WalletViewModel walletViewModel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_manager;
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = new TrustManagerPresenter(this);
    }

    @Override
    protected void initView() {
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);

        tv_center_title.setText(getString(R.string.trusteeship_manger));

        //初始化RecycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_trusteeship.setLayoutManager(layoutManager);

        mTrustManagerAdapter = new TrustManagerAdapter(R.layout.item_trusteeship_manager, mAllWalletList);
        recycler_trusteeship.setAdapter(mTrustManagerAdapter);
        mTrustManagerAdapter.setOnCheckClickListener(this);
    }

    @Override
    protected void addEvent() {
        /*mTrustManagerAdapter.setOnItemClickListener((adapter, view, position) -> {

        });*/

        walletViewModel.loadWallet(this);
        walletViewModel.mutableWallentLiveData.observe(this,listLoadDataModel -> {
            mAllWalletList = mPresenter.getAllBHWalletItem();
            mTrustManagerAdapter.getData().clear();
            mTrustManagerAdapter.addData(mAllWalletList);
            mTrustManagerAdapter.notifyDataSetChanged();
        });
    }



    @OnClick({R2.id.btn_wallet_create, R2.id.btn_wallet_impot})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_wallet_create){
            ARouterUtil.startActivityTarget(ARouterConfig.TRUSTEESHIP_FRIST_PAGE,TrusteeshipManagerActivity.class);
        }else if(view.getId()==R.id.btn_wallet_impot){

        }
    }

    @Override
    public void checkClickListener(final int position, BHWalletItem bhWalletItem) {
        int status = bhWalletItem.isDefault==0?1:0;
        walletViewModel.updateWallet(this,bhWalletItem.id,status);
        walletViewModel.mutableLiveData.observe(this,loadDataModel -> {
            if (loadDataModel.getLoadingStatus()== LoadingStatus.SUCCESS){
                updatePosition(position,status);
            }
        });
    }

    /**
     * 更新位置
     */
    private void updatePosition(int position,int status){
        if(status==1){
            for (int i = 0; i < mAllWalletList.size(); i++) {
                BHWalletItem item = mTrustManagerAdapter.getData().get(i);
                item.isDefault = 0;
            }
        }
        BHWalletItem item = mTrustManagerAdapter.getData().get(position);
        item.isDefault = status;
        mTrustManagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
        walletViewModel.loadWallet(this);
    }
}
