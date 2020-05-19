package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.event.BHCoinEvent;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.event.CurrencyEvent;
import com.bhex.wallet.common.event.WalletEvent;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.google.android.material.textview.MaterialTextView;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资产首页
 *
 * @author gdy
 * 2020-3-12
 */
public class BalanceFragment extends BaseFragment<BalancePresenter> {

    private static  final String TAG = BalanceFragment.class.getSimpleName();

    @BindView(value = R2.id.toolbar)
    Toolbar mToolBar;

    @BindView(R2.id.tv_balance_txt2)
    MaterialTextView tv_balance_txt2;

    @BindView(R2.id.recycler_balance)
    SwipeRecyclerView recycler_balance;

    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_paste)
    AppCompatImageView iv_paste;


    @BindView(R2.id.iv_eye)
    AppCompatImageView iv_eye;

    @BindView(R2.id.tv_asset)
    AppCompatTextView tv_asset;

    @BindView(R2.id.iv_search)
    AppCompatImageView iv_search;

    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;

    @BindView(R2.id.ck_hidden_small)
    CheckedTextView ck_hidden_small;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R2.id.empty_layout)
    RelativeLayout empty_layout;

    private BalanceAdapter mBalanceAdapter;

    private List<BHBalance> mBalanceList;

    private List<BHBalance> mOriginBalanceList;

    private BHWallet bhWallet;

    private AccountInfo mAccountInfo;

    //总资产
    private double allTokenAssets;

    public BalanceFragment() {

    }

    private TransactionViewModel transactionViewModel;
    private BalanceViewModel balanceViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_balance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init();

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        getYActivity().setSupportActionBar(mToolBar);
        getYActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);

        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);

        mOriginBalanceList = mPresenter.makeBalanceList();
        mBalanceList = mPresenter.getBalanceList(mOriginBalanceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_balance.setLayoutManager(layoutManager);
        recycler_balance.setNestedScrollingEnabled(false);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(getYActivity(),68),0,
                ColorUtil.getColor(getContext(),R.color.global_divider_color));

        recycler_balance.addItemDecoration(ItemDecoration);

        mBalanceAdapter = new BalanceAdapter(R.layout.item_balance, mBalanceList);
        recycler_balance.setSwipeMenuCreator(swipeMenuCreator);
        recycler_balance.setOnItemMenuClickListener(mMenuItemClickListener);
        recycler_balance.setAdapter(mBalanceAdapter);

        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());

        ed_search_content.addTextChangedListener(balanceTextWatcher);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(getYActivity());
    }

    @Override
    protected void addEvent() {
        //资产列表点击事件
        mBalanceAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHBalance bhBalance =  mBalanceAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Balance_Assets_Detail)
                    .withObject("balance",bhBalance)
                    .withObject("accountInfo",mAccountInfo)
                    .navigation();
        });

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,loadDataModel -> {

        });

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(getYActivity());

        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            refreshLayout.finishRefresh();
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });

        getLifecycle().addObserver(balanceViewModel);

        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            balanceViewModel.getAccountInfo(getYActivity(),bhWallet.address);
        });
        refreshLayout.autoRefresh();
    }

    /**
     * 更新用户资产
     * @param accountInfo
     */

    private void updateAssets(AccountInfo accountInfo) {
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);
        mAccountInfo = accountInfo;
        BHUserManager.getInstance().setAccountInfo(mAccountInfo);
        List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        mBalanceAdapter.notifyDataSetChanged();
        //计算每一个币种的资产价值 和 总资产
        updateTopTokenAssets();
    }

    //更新头部资产
    public void updateTopTokenAssets(){
        if(mAccountInfo==null){
            return;
        }
        allTokenAssets = mPresenter.calculateAllTokenPrice(mAccountInfo,mOriginBalanceList);
        String allTokenAssetsText = CurrencyManager.getInstance().getCurrencyDecription(getYActivity(),allTokenAssets);
        //设置第一字符15sp
        String tag = iv_eye.getTag().toString();
        if(iv_eye.getTag().equals("0")){
            mPresenter.setTextFristSamll(tv_asset,allTokenAssetsText);
        }else{
            tv_asset.setText("***");
        }
    }


    @OnClick({R2.id.iv_eye,R2.id.tv_address,R2.id.iv_search,R2.id.ck_hidden_small,R2.id.iv_paste})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_eye){
            //隐藏资产
            mPresenter.hiddenAsset(getYActivity(),tv_asset,iv_eye,mBalanceAdapter);
        }else if(view.getId()==R.id.iv_paste){
            ToolUtils.copyText(bhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }else if(view.getId()==R.id.iv_search){
            //币种搜索
            ARouter.getInstance().build(ARouterConfig.Balance_Search).
                    withObject("balanceList",mOriginBalanceList).navigation();
        }else if(view.getId()==R.id.ck_hidden_small){
            //隐藏小额币种
            ck_hidden_small.toggle();

            String searchText = ed_search_content.getText().toString().trim();
            List<BHBalance> result = mPresenter.hiddenSmallToken(getYActivity(),ck_hidden_small,mOriginBalanceList,searchText);

            if(result==null||result.size()==0){
                empty_layout.setVisibility(View.VISIBLE);
            }else{
                empty_layout.setVisibility(View.GONE);
            }
            mBalanceAdapter.getData().clear();
            mBalanceAdapter.addData(result);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void balanceListChange(BHCoinEvent event){
        if(event.flag){
            //int size = mBalanceAdapter.getData().size();
            BHBalance balance = mPresenter.getBalanceByCoin(event.bhCoinItem);
            mOriginBalanceList.add(balance);
            //隐藏小额资产
            if(ck_hidden_small.isChecked()){
                //需要判断总资产是否小于等于0
               boolean isSmallToken = mPresenter.isSmallToken(mAccountInfo,event.bhCoinItem.symbol);
               if(!isSmallToken){
                   mBalanceList.add(balance);
               }
            }else{
                mBalanceList.add(balance);
            }
            empty_layout.setVisibility(View.GONE);
            mBalanceAdapter.notifyDataSetChanged();
        }else{
            int rv_index= mPresenter.getIndexByCoin(mBalanceList,event.bhCoinItem);
            int org_index= mPresenter.getIndexByCoin(mOriginBalanceList,event.bhCoinItem);
            if(rv_index>=0){
                mBalanceList.remove(rv_index);
                if(mBalanceAdapter.getData().size()==0){
                    empty_layout.setVisibility(View.VISIBLE);
                }else{
                    empty_layout.setVisibility(View.GONE);
                }
                mBalanceAdapter.notifyDataSetChanged();
            }
            if(org_index>=0){
                mOriginBalanceList.remove(org_index);
            }
        }
        //更新总资产
        updateTopTokenAssets();
        //持久化添加资产
        BHUserManager.getInstance().saveUserBalanceList(mOriginBalanceList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(WalletEvent walletEvent){
        //当前钱包用户
        //bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());

        //清空原始用户资产
        mBalanceAdapter.getData().clear();
        mOriginBalanceList = mPresenter.makeBalanceList();
        mBalanceList = mPresenter.getBalanceList(mOriginBalanceList);
        mBalanceAdapter.addData(mBalanceList);
        mBalanceAdapter.notifyDataSetChanged();

        //更新资产
        balanceViewModel.getAccountInfo(getYActivity(),bhWallet.address);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCurrency(CurrencyEvent currencyEvent){
        updateAssets(mAccountInfo);
    }

    private SwipeMenuCreator swipeMenuCreator = (leftMenu, rightMenu, position) -> {
        int width = PixelUtils.dp2px(getContext(),80);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem transferItem = new SwipeMenuItem(getContext())
                .setBackground(R.drawable.btn_0_blue)
                .setText(getResources().getString(R.string.transfer))
                .setTextColor(getResources().getColor(R.color.global_button_text_color))
                .setWidth(width)
                .setHeight(height);
        rightMenu.addMenuItem(transferItem);


        SwipeMenuItem makeCollectItem = new SwipeMenuItem(getContext())
                .setBackground(R.drawable.btn_0_blue)
                .setText(getResources().getString(R.string.make_collection))
                .setTextColor(getResources().getColor(R.color.global_button_text_color))
                .setWidth(width)
                .setHeight(height);
        leftMenu.addMenuItem(makeCollectItem);
    };

    private SimpleTextWatcher balanceTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String searchContent = ed_search_content.getText().toString().trim();
            List<BHBalance> result = mPresenter.hiddenSmallToken(getYActivity(),ck_hidden_small,mOriginBalanceList,searchContent);

            if(result==null||result.size()==0){
                empty_layout.setVisibility(View.VISIBLE);
            }else{
                empty_layout.setVisibility(View.GONE);
            }
            mBalanceAdapter.getData().clear();
            mBalanceAdapter.addData(result);

        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_balance,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_add_balance){
            ARouter.getInstance().build(ARouterConfig.Balance_Search).
                    withObject("balanceList",mOriginBalanceList).navigation();
        }
        return super.onOptionsItemSelected(item);
    }

    private OnItemMenuClickListener mMenuItemClickListener = (menuBridge, adapterPosition) -> {
        int direction = menuBridge.getDirection();
        BHBalance balance = mBalanceAdapter.getData().get(adapterPosition);

        if(direction == SwipeRecyclerView.RIGHT_DIRECTION){
            BHBalance bthBalance =mPresenter.getBthBalanceWithAccount(mAccountInfo);
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_out)
                    .withObject("balance", balance)
                    .withObject("bhtBalance",bthBalance)
                    .withInt("way",1)
                    .navigation();
        }else{
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_in)
                    .withObject("balance", balance)
                    .withInt("way",1)
                    .navigation();
        }
    };
}
