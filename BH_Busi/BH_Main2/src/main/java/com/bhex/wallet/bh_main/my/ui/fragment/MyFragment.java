package com.bhex.wallet.bh_main.my.ui.fragment;


import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.CircleView;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MyAdapter;
import com.bhex.wallet.bh_main.my.enums.BUSI_MY_TYPE;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.model.BHMessage;
import com.bhex.wallet.bh_main.my.ui.MyRecyclerViewDivider;
import com.bhex.wallet.bh_main.my.ui.activity.SettingActivity;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.bh_main.my.viewmodel.MessageViewModel;
import com.bhex.wallet.common.base.BaseFragment;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.enums.BH_BUSI_URL;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.helper.BHWalletHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHPage;
import com.bhex.wallet.common.model.UpgradeInfo;
import com.bhex.wallet.common.ui.fragment.Password30PFragment;
import com.bhex.wallet.common.ui.fragment.UpgradeFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.UpgradeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gdy
 * 2020-3-12
 * 我的
 */
public class MyFragment extends BaseFragment  {

    @BindView(R2.id.recycler_my)
    RecyclerView recycler_my;

    @BindView(R2.id.iv_message)
    AppCompatImageView iv_message;

    @BindView(R2.id.tv_username)
    AppCompatTextView tv_username;

    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_paste)
    AppCompatImageView iv_paste;

    @BindView(R2.id.iv_edit)
    AppCompatImageView iv_edit;

    @BindView(R2.id.layout_index_2)
    CardView layout_index_2;

    @BindView(R2.id.layout_index_3)
    CardView layout_index_3;

    @BindView(R2.id.iv_message_tip)
    CircleView iv_message_tip;

    private List<MyItem> mItems;

    private MyAdapter mMyAdapter;

    private BHWallet mBhWallet;

    private MessageViewModel msgViewModel;

    private UpgradeViewModel mUpgradeVM;

    public MyFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        mItems = MyHelper.getAllItems(getYActivity());

        mBhWallet = BHUserManager.getInstance().getCurrentBhWallet();

        recycler_my.setNestedScrollingEnabled(false);
        recycler_my.setAdapter(mMyAdapter = new MyAdapter( mItems));

        MyRecyclerViewDivider myRecyclerDivider = new MyRecyclerViewDivider(getContext(),
                ColorUtil.getColor(getContext(),R.color.global_divider_color),
                getResources().getDimension(R.dimen.default_item_divider_height),
                mMyAdapter.getData().size()==7?(new int[]{2,4}):(new int[]{1,3}));

        recycler_my.addItemDecoration(myRecyclerDivider);
        tv_username.setText(mBhWallet.getName());
        BHWalletHelper.proccessAddress(tv_address,mBhWallet.getAddress());

        msgViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        mUpgradeVM = ViewModelProviders.of(this).get(UpgradeViewModel.class);
        mUpgradeVM.upgradeLiveData.observe(getActivity(),ldm->{
            processUpgradeInfo(ldm);
        });
    }


    @Override
    protected void addEvent() {
        EventBus.getDefault().register(this);
        mMyAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyItem item = mItems.get(position);
            switch (BUSI_MY_TYPE.getType(item.id)){
                case 备份助记词:
                    Password30PFragment.showPasswordDialog(getChildFragmentManager(),Password30PFragment.class.getName(),
                            this::passwordListener,item.id,false);
                    break;
                case 备份私钥:
                    //提醒页
                    Password30PFragment.showPasswordDialog(getChildFragmentManager(),Password30PFragment.class.getName(),
                            this::passwordListener,item.id,false);
                    break;
                case 备份KS:
                    //提醒页
                    Password30PFragment.showPasswordDialog(getChildFragmentManager(),Password30PFragment.class.getName(),
                            this::passwordListener,item.id,false);
                    break;

                case 关于我们:
                    //设置
                    //NavigateUtil.startActivity(getYActivity(),SettingActivity.class);
                    ARouter.getInstance().build(ARouterConfig.My.My_About).navigation();
                    break;

                case 设置:
                    //设置
                    NavigateUtil.startActivity(getYActivity(),SettingActivity.class);
                    break;

                case 公告:
                    ARouter.getInstance().build(ARouterConfig.Market.market_webview)
                            .withString("url",BH_BUSI_URL.公告.getGotoUrl(getContext()))
                            .navigation();

                    break;
                case 帮助中心:
                    ARouter.getInstance().build(ARouterConfig.Market.market_webview)
                            .withString("url",BH_BUSI_URL.帮助中心.getGotoUrl(getContext()))
                            .navigation();
                    break;
            }
        });

        //助记词备份订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Mnemonic_Back, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                //更新item列表
                mItems = MyHelper.getAllItems(getYActivity());
                mMyAdapter.getData().clear();
                mMyAdapter.addData(mItems);
            }
        });

        //消息查询
        msgViewModel.messageLiveData.observe(this,ldm->{
            if(ldm.getLoadingStatus()==LoadingStatus.SUCCESS){
                BHPage<BHMessage> page =  (BHPage<BHMessage>)ldm.getData();
                if(page.unread>0){
                    iv_message_tip.setVisibility(View.VISIBLE);
                }else{
                    iv_message_tip.setVisibility(View.GONE);
                }
            }
        });
    }

    //消息查询
    private void passwordListener(String password,int position,int way) {
        //备份助记词
        LogUtils.d("MyFragments===>:","position==="+position);
        //MyItem item = mItems.get(position);
        if(position==BUSI_MY_TYPE.备份助记词.index){
            ARouter.getInstance().build(ARouterConfig.MNEMONIC_BACKUP)
                    .withString(BHConstants.INPUT_PASSWORD,password)
                    .withString("gotoTarget","MyFragment")
                    .navigation();
        }else if(position==BUSI_MY_TYPE.备份私钥.index){
            String title = MyHelper.getTitle(getYActivity(),position);
            ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_EXPORT_INDEX)
                    .withString("title",title)
                    .withString(BHConstants.INPUT_PASSWORD,password)
                    .withString("flag", BH_BUSI_TYPE.备份私钥.value)
                    .navigation();
        }else if(position==BUSI_MY_TYPE.备份KS.index){
            String title = MyHelper.getTitle(getYActivity(),position);
            //提醒页
            ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_EXPORT_INDEX)
                    .withString("title",title)
                    .withString(BHConstants.INPUT_PASSWORD,password)
                    .withString("flag",BH_BUSI_TYPE.备份KS.value)
                    .navigation();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            msgViewModel.loadMessageByAddress(this,1,null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        msgViewModel.loadMessageByAddress(this,1,null);
    }

    /**
     * 处理升级请求
     */
    private void processUpgradeInfo(LoadDataModel<UpgradeInfo> ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            UpgradeInfo upgradeInfo = ldm.getData();
            if(upgradeInfo.needUpdate==1){
                showUpgradeDailog(upgradeInfo);
            }else{
                ToastUtils.showToast(getString(R.string.app_up_to_minute_version));
            }
        }else if(ldm.loadingStatus== LoadingStatus.ERROR){

        }
    }


    @OnClick({R2.id.iv_message, R2.id.iv_default_man,R2.id.tv_address,R2.id.iv_paste,
            R2.id.iv_edit, R2.id.layout_index_2,R2.id.layout_index_3})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_message){
            ARouter.getInstance().build(ARouterConfig.My.My_Message).navigation();
        }else if(view.getId()==R.id.iv_paste){
            ToolUtils.copyText(mBhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }else if(view.getId()==R.id.iv_edit){
            UpdateNameFragment fragment = UpdateNameFragment.Companion.showFragment(dialogOnClickListener);
            fragment.showNow(getChildFragmentManager(), UpdateNameFragment.class.getName());
        }else if(view.getId()==R.id.layout_index_2){
            //ARouter.getInstance().build(ARouterConfig.Token_Release).navigation();
            ARouter.getInstance().build(ARouterConfig.Market.market_webview).withString("url",getTranscationUrl()).navigation();
            //ToastUtils.showToast("跳转交易记录");
        } else if(view.getId()==R.id.layout_index_3){
            ARouter.getInstance().build(ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE).navigation();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(AccountEvent walletEvent){
        mBhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        tv_username.setText(mBhWallet.getName());
        BHWalletHelper.proccessAddress(tv_address,mBhWallet.getAddress());

        mItems = MyHelper.getAllItems(getYActivity());
        mMyAdapter.getData().clear();
        mMyAdapter.addData(mItems);
        //mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 修改用户名
     */
    private UpdateNameFragment.DialogOnClickListener dialogOnClickListener = v -> {
        //
        mBhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        tv_username.setText(mBhWallet.getName());
        ToolUtils.hintKeyBoard(getYActivity());
    };

    /**
     * 显示升级对话框
     */
    private void showUpgradeDailog(UpgradeInfo upgradeInfo){
        //是否强制升级
        UpgradeFragment fragment = UpgradeFragment.Companion.showUpgradeDialog(upgradeInfo,upgradeDialogListener);
        fragment.show(getActivity().getSupportFragmentManager(),UpgradeFragment.class.getName());
    }

    UpgradeFragment.DialogOnClickListener upgradeDialogListener = v -> {
        ToastUtils.showToast(getActivity().getResources().getString(R.string.app_loading_now));
    };


    private String getTranscationUrl(){
        String v_local_display = ToolUtils.getLocalString(getYActivity());
        String url = BHConstants.API_BASE_URL
                        .concat("account/")
                        .concat(BHUserManager.getInstance().getCurrentBhWallet().address)
                        .concat("?type=transactions").concat("&lang=").concat(v_local_display);
        LogUtils.d("MyFragement==>:","url=="+url);
        return url;
    }


}
