package com.bhex.wallet.mnemonic.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.manager.SecuritySettingManager;
import com.bhex.wallet.common.model.BHWalletItem;
import com.bhex.wallet.common.ui.fragment.Password30PFragment;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.TrustManagerAdapter;
import com.bhex.wallet.mnemonic.persenter.TrustManagerPresenter;
import com.bhex.wallet.mnemonic.ui.fragment.DeleteTipFragment;
import com.google.android.material.button.MaterialButton;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-3-14 20:26:34
 * 托管单元管理
 */
@Route(path = ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE)
public class TrusteeshipManagerActivity extends BaseActivity<TrustManagerPresenter>
        implements TrustManagerAdapter.OnCheckClickListener {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.recycler_trusteeship)
    SwipeRecyclerView recycler_trusteeship;

    @BindView(R2.id.btn_wallet_create)
    MaterialButton btnWalletCreate;

    @BindView(R2.id.btn_wallet_impot)
    MaterialButton btnWalletImpot;

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

        tv_center_title.setText(getString(R.string.trustship_manager));

        //初始化RecycleView
        recycler_trusteeship.setSwipeMenuCreator(swipeMenuCreator);
        recycler_trusteeship.setOnItemMenuClickListener(mMenuItemClickListener);

        mTrustManagerAdapter = new TrustManagerAdapter(R.layout.item_trusteeship_manager, mAllWalletList);
        recycler_trusteeship.setAdapter(mTrustManagerAdapter);
        mTrustManagerAdapter.setOnCheckClickListener(this);
    }

    @Override
    protected void addEvent() {
        walletViewModel.loadWallet(this);
        walletViewModel.mutableWallentLiveData.observe(this,ldm -> {
            udpatWalletList(ldm,null);
        });

        walletViewModel.deleteLiveData.observe(this,ldm -> {
            udpatWalletList(ldm,"delete");
        });

        walletViewModel.mutableLiveData.observe(this,loadDataModel -> {
            if (loadDataModel.getLoadingStatus()== LoadingStatus.SUCCESS){
                EventBus.getDefault().post(new AccountEvent());
                finish();
                //
                SecuritySettingManager.getInstance().request_thirty_in_time(false,"");
            }
        });

        mTrustManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHWalletItem bhWalletItem = mTrustManagerAdapter.getData().get(position);
            if(bhWalletItem.isDefault==BH_BUSI_TYPE.默认托管单元.getIntValue()){
                BHWallet bhWallet = BHUserManager.getInstance().getAllWallet().get(position);
                walletViewModel.updateWallet(this,bhWallet,bhWalletItem.id,bhWalletItem.isDefault);
            }else{
                int status = (bhWalletItem.isDefault==BH_BUSI_TYPE.非默认托管单元.getIntValue())
                        ? BH_BUSI_TYPE.默认托管单元.getIntValue()
                        : BH_BUSI_TYPE.非默认托管单元.getIntValue();

                BHWallet bhWallet = BHUserManager.getInstance().getAllWallet().get(position);
                walletViewModel.updateWallet(this,bhWallet,bhWalletItem.id,status);
            }
        });
    }

    /**
     * 更新钱包列表
     */
    public void udpatWalletList(LoadDataModel ldm,String flag){
        if (ldm.getLoadingStatus()== LoadingStatus.SUCCESS){
            if("delete".equals(flag)){
                ToastUtils.showToast(getString(R.string.deleted));
            }
            mAllWalletList = mPresenter.getAllBHWalletItem();
            mTrustManagerAdapter.getData().clear();
            mTrustManagerAdapter.addData(mAllWalletList);
        }
    }

    @OnClick({R2.id.btn_wallet_create, R2.id.btn_wallet_impot})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_wallet_create){
            ARouterUtil.startActivityTarget(ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST,TrusteeshipManagerActivity.class);
        }else if(view.getId()==R.id.btn_wallet_impot){
            ARouter.getInstance().build(ARouterConfig.Trusteeship.Trusteeship_Add_Index).navigation();
            MainActivityManager.getInstance().setTargetClass(TrusteeshipManagerActivity.class);
        }
    }

    /*@Override
    public void checkClickListener(final int position, BHWalletItem bhWalletItem) {
        if(bhWalletItem.isDefault==BH_BUSI_TYPE.默认托管单元.getIntValue()){
            BHWallet bhWallet = BHUserManager.getInstance().getAllWallet().get(position);
            walletViewModel.updateWallet(this,bhWallet,bhWalletItem.id,bhWalletItem.isDefault);
        }else{
            int status = (bhWalletItem.isDefault==BH_BUSI_TYPE.非默认托管单元.getIntValue())
                    ? BH_BUSI_TYPE.默认托管单元.getIntValue()
                    : BH_BUSI_TYPE.非默认托管单元.getIntValue();

            BHWallet bhWallet = BHUserManager.getInstance().getAllWallet().get(position);
            walletViewModel.updateWallet(this,bhWallet,bhWalletItem.id,status);
        }
    }*/

    @Override
    public void onMenuClickListener(int position, BHWalletItem bhWalletItem) {
        walletViewModel.deleteWallet(this,bhWalletItem.id);
        walletViewModel.mutableLiveData.observe(this,loadDataModel -> {
            if (loadDataModel.getLoadingStatus()== LoadingStatus.SUCCESS){
            }
        });
    }

    /**
     * 更新位置
     */
    /*private void updatePosition(int position,int status){
        if(status==1){
            for (int i = 0; i < mAllWalletList.size(); i++) {
                BHWalletItem item = mTrustManagerAdapter.getData().get(i);
                item.isDefault = 0;
            }
        }
        BHWalletItem item = mTrustManagerAdapter.getData().get(position);
        item.isDefault = status;
        mTrustManagerAdapter.notifyDataSetChanged();
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
        walletViewModel.loadWallet(this);
    }


    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu swipeRightMenu, int position) {
            BHWalletItem bhWalletItem = mTrustManagerAdapter.getData().get(position);
            if(bhWalletItem.isDefault!=BH_BUSI_TYPE.默认托管单元.getIntValue()){
                int width = PixelUtils.dp2px(TrusteeshipManagerActivity.this,80);
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                SwipeMenuItem addItem = new SwipeMenuItem(TrusteeshipManagerActivity.this)
                        .setBackground(R.drawable.btn_0_blue)
                        .setText(getString(R.string.delete))
                        .setTextColor(getResources().getColor(R.color.global_button_text_color))
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem);
            }

        }
    };


    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();
            BHWalletItem bhWalletItem = mTrustManagerAdapter.getData().get(position);
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                deletBHWallet(bhWalletItem,position);
            }
        }
    };


    /**
     * 删除托管单元
     * @param bhWalletItem
     */
    private BHWalletItem deletBHWallet;
    Password30PFragment passwordFrag;
    public void deletBHWallet(BHWalletItem bhWalletItem,int position){
        deletBHWallet = bhWalletItem;
        //弹框确认
        DeleteTipFragment fragment = DeleteTipFragment.showFragment(position,this::deleteAction);
        fragment.show(getSupportFragmentManager(),DeleteTipFragment.class.getName());
        /*passwordFrag = PasswordFragment.showPasswordDialog(getSupportFragmentManager(),PasswordFragment.class.getSimpleName(),
                passwordClickListener,position);
        passwordFrag.setVerifyPwdWay(BH_BUSI_TYPE.校验选择账户密码.getIntValue());*/
    }

    //删除确认
    private void deleteAction(int i,int position) {
        if(i==1){
            passwordFrag = Password30PFragment.showPasswordDialog(getSupportFragmentManager(),Password30PFragment.class.getName(),
                    passwordClickListener,position,false);
            passwordFrag.setVerifyPwdWay(BH_BUSI_TYPE.校验选择账户密码.getIntValue());
        }
    }

    Password30PFragment.PasswordClickListener passwordClickListener = (password, position,way,isRight) -> {
        if(deletBHWallet==null){
            return;
        }
        if(way == BH_BUSI_TYPE.校验选择账户密码.getIntValue()){
            BHWalletItem bhWalletItem = mTrustManagerAdapter.getData().get(position);
            if(!ToolUtils.isVerifyPass(password,bhWalletItem.password)){
                ToastUtils.showToast(getResources().getString(com.bhex.wallet.common.R.string.error_password));
                return;
            }
            if(passwordFrag.getShowsDialog()){
                passwordFrag.dismiss();
            }
        }
        walletViewModel.deleteWallet(TrusteeshipManagerActivity.this,deletBHWallet.id);
    };

}
