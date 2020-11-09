package com.bhex.wallet.balance.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.ChainAdapter;
import com.bhex.wallet.balance.event.BHCoinEvent;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.ui.TipsViewHolder;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.event.CurrencyEvent;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHChain;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.google.android.material.textview.MaterialTextView;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
    @BindView(value = R2.id.toolbar)
    Toolbar mToolBar;
    @BindView(R2.id.tv_wallet_name)
    AppCompatTextView tv_wallet_name;
    @BindView(R2.id.tv_balance_txt2)
    MaterialTextView tv_balance_txt2;
    @BindView(R2.id.recycler_balance)
    RecyclerView recycler_balance;
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


    private ChainAdapter mChainAdapter;
    //private List<BHBalance> mBalanceList = new ArrayList<>();
    private List<BHChain> mChainList;
    private BHWallet bhWallet;
    //private AccountInfo mAccountInfo;
    private BalanceViewModel balanceViewModel;
    //总资产
    private double allTokenAssets;

    public BalanceFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_balance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.app_bg).navigationBarDarkIcon(true).init();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        getYActivity().setSupportActionBar(mToolBar);
        getYActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);

        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);

        tv_wallet_name.setText(bhWallet.name);

        mChainList = CacheCenter.getInstance().getTokenMapCache().loadChains();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //layoutManager.setSmoothScrollbarEnabled(true);
        recycler_balance.getLayoutManager().setAutoMeasureEnabled(true);
        //recycler_balance.setLayoutManager(layoutManager);
        recycler_balance.setNestedScrollingEnabled(false);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(getYActivity(),68),0,
                ColorUtil.getColor(getContext(),R.color.global_divider_color));

        recycler_balance.addItemDecoration(ItemDecoration);

        mChainAdapter = new ChainAdapter(mChainList);
        recycler_balance.setAdapter(mChainAdapter);
        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(getYActivity());
    }

    @Override
    protected void addEvent() {
        //资产列表点击事件
        mChainAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHChain bhChain =  mChainAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Balance_chain_tokens)
                    .withObject("bhChain",bhChain)
                    .withString("title","hbc")
                    .navigation();
        });

        balanceViewModel = ViewModelProviders.of(MainActivityManager._instance.mainActivity).get(BalanceViewModel.class).build(getYActivity());
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            refreshLayout.finishRefresh();
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets();
            }
        });
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            balanceViewModel.getAccountInfo(getYActivity(),null);
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TipsViewHolder viewHolder = new TipsViewHolder();
        viewHolder.initView(getYActivity(),getYActivity().findViewById(R.id.layout_balance_tip));
    }

    /**
     * 更新用户资产
     * @param
     */

    private void updateAssets() {
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);
        //mAccountInfo = accountInfo;
        //BHUserManager.getInstance().setAccountInfo(mAccountInfo);
        //List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        //计算每一个币种的资产价值 和 总资产
        updateTopTokenAssets();
        mChainAdapter.notifyDataSetChanged();
    }

    //更新头部资产
    public void updateTopTokenAssets(){
        if(BHUserManager.getInstance().getAccountInfo()==null){
            return;
        }
        allTokenAssets = mPresenter.calculateAllTokenPrice(getYActivity(),BHUserManager.getInstance().getAccountInfo(),mChainList);
        LogUtils.d("BalanceFragment==>:","allTokenAssets=="+allTokenAssets);
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
            mPresenter.hiddenAssetExt(getYActivity(),tv_asset,iv_eye);
            mChainAdapter.setIsHidden(iv_eye.getTag().toString());
        }else if(view.getId()==R.id.iv_paste){
            ToolUtils.copyText(bhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }else if(view.getId()==R.id.iv_search){
            //币种搜索
            /*ARouter.getInstance().build(ARouterConfig.Balance_Search).
                    withObject("balanceList",mOriginBalanceList).navigation();*/
        }else if(view.getId()==R.id.ck_hidden_small){
            //隐藏小额币种
            ck_hidden_small.toggle();

            /*String searchText = ed_search_content.getText().toString().trim();
            List<BHBalance> result = mPresenter.hiddenSmallToken(getYActivity(),ck_hidden_small,mOriginBalanceList,searchText);

            if(result==null||result.size()==0){
                empty_layout.setVisibility(View.VISIBLE);
            }else{
                empty_layout.setVisibility(View.GONE);
            }
            mChainAdapter.getData().clear();
            mChainAdapter.addData(result);*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(AccountEvent walletEvent){
        //当前钱包用户
        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());
        //清空原始用户资产
        tv_wallet_name.setText(bhWallet.name);
        mChainAdapter.notifyDataSetChanged();
        //更新资产
        balanceViewModel.getAccountInfo(getYActivity(),null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
            tv_wallet_name.setText(bhWallet.name);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCurrency(CurrencyEvent currencyEvent){
        updateAssets();
    }

    /*private SimpleTextWatcher balanceTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String searchContent = ed_search_content.getText().toString().trim();
            *//*List<BHBalance> result = mPresenter.hiddenSmallToken(getYActivity(),ck_hidden_small,mOriginBalanceList,searchContent);

            if(result==null||result.size()==0){
                empty_layout.setVisibility(View.VISIBLE);
            }else{
                empty_layout.setVisibility(View.GONE);
            }
            mChainAdapter.getData().clear();
            mChainAdapter.addData(result);*//*

        }
    };*/

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_balance,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_add_balance){
            /*ARouter.getInstance().build(ARouterConfig.Balance_Search).
                    withObject("balanceList",mOriginBalanceList).navigation();*/
        }
        return super.onOptionsItemSelected(item);
    }
}
