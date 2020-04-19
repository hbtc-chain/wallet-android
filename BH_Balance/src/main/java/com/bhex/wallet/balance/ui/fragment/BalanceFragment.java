package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.recyclerview.MyLinearLayoutManager;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

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

    private static  final String TAG = BalanceFragment.class.getSimpleName();

    @BindView(value = R2.id.toolbar)
    Toolbar mToolBar;

    @BindView(R2.id.recycler_balance)
    SwipeRecyclerView recycler_balance;

    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_eye)
    AppCompatImageView iv_eye;

    @BindView(R2.id.tv_asset)
    AppCompatTextView tv_asset;

    @BindView(R2.id.btn_tx)
    AppCompatButton btn_tx;

    @BindView(R2.id.iv_search)
    AppCompatImageView iv_search;

    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;

    @BindView(R2.id.ck_hidden_small)
    CheckedTextView ck_hidden_small;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;



    private View mEmptyLayout;


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
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        getYActivity().setSupportActionBar(mToolBar);
        getYActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();

        //LogUtils.d("bhWallet===","==bhWallet="+bhWallet.getAddress());

        mOriginBalanceList = mPresenter.makeBalanceList();
        mBalanceList = mPresenter.getBalanceList(mOriginBalanceList);
        LinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_balance.setLayoutManager(layoutManager);
        recycler_balance.setNestedScrollingEnabled(false);

        RecycleViewDivider ItemDecoration = new RecycleViewDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                1,
                ColorUtil.getColor(getContext(),R.color.gray_E7ECF4));

        recycler_balance.addItemDecoration(ItemDecoration);
        //gray_f9f9fb
        //recycler_balance.addItemDecoration(divider);

        mBalanceAdapter = new BalanceAdapter(R.layout.item_balance, mBalanceList);
        recycler_balance.setAdapter(mBalanceAdapter);

        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());

        ed_search_content.addTextChangedListener(balanceTextWatcher);

        mEmptyLayout = LayoutInflater.from(getYActivity()).inflate(R.layout.layout_empty_asset,(ViewGroup) recycler_balance.getParent(),false);
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

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);
        balanceViewModel.accountLiveData.observe(this,ldm -> {
            refreshLayout.finishRefresh();
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets(ldm.getData());
            }

        });

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
        mAccountInfo = accountInfo;
        List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        if(list==null || list.size()==0){
            mBalanceAdapter.setEmptyView(mEmptyLayout);
        }
        //计算每一个币种的资产价值 和 总资产
        allTokenAssets = mPresenter.calculateAllTokenPrice(accountInfo,mOriginBalanceList);
        mBalanceAdapter.notifyDataSetChanged();
        String allTokenAssetsText = CurrencyManager.getInstance().getCurrencyDecription(getYActivity(),allTokenAssets);
        //设置第一字符15sp
        mPresenter.setTextFristSamll(tv_asset,allTokenAssetsText);

    }


    @OnClick({R2.id.iv_eye,R2.id.tv_address,R2.id.btn_tx,R2.id.iv_search,R2.id.ck_hidden_small})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_eye){
            //隐藏资产
            mPresenter.hiddenAsset(getYActivity(),tv_asset,iv_eye);
        }else if(view.getId()==R.id.tv_address){
            ToolUtils.copyText(bhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }else if(view.getId()==R.id.btn_tx){
            generateTranction();
        }else if(view.getId()==R.id.iv_search){
            //币种搜索
            ARouter.getInstance().build(ARouterConfig.Balance_Search).
                    withObject("balanceList",mOriginBalanceList).navigation();
        }else if(view.getId()==R.id.ck_hidden_small){
            //隐藏小额币种
            List<BHBalance> result = mPresenter.hiddenSmallToken(getYActivity(),ck_hidden_small,mOriginBalanceList);

            mBalanceAdapter.getData().clear();
            mBalanceAdapter.addData(result);
        }
    }

    private void generateTranction() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void balanceListChange(BHCoinEvent event){
        if(event.flag){
            BHBalance balance = mPresenter.getBalanceByCoin(event.bhCoinItem);
            mOriginBalanceList.add(balance);
            mBalanceList.add(balance);
            mBalanceAdapter.notifyDataSetChanged();
        }else{
           int index= mPresenter.getIndexByCoin(mOriginBalanceList,event.bhCoinItem);
           if(index<0){
               return;
           }
           mOriginBalanceList.remove(index);
           mBalanceList.remove(index);
           mBalanceAdapter.notifyItemRemoved(index);
        }
        //持久化添加资产
        BHUserManager.getInstance().saveUserBalanceList(mOriginBalanceList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(WalletEvent walletEvent){
        //当前钱包用户
        //bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
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
        LogUtils.d("BalanceFragment==>","=currencyEvent=");
        updateAssets(mAccountInfo);
    }

    private SwipeMenuCreator swipeMenuCreator = (leftMenu, rightMenu, position) -> {
        int width = PixelUtils.dp2px(getContext(),80);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem transferItem = new SwipeMenuItem(getContext())
                .setBackground(R.drawable.btn_0_blue)
                .setText(getResources().getString(R.string.transfer))
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);
        rightMenu.addMenuItem(transferItem);


        SwipeMenuItem makeCollectItem = new SwipeMenuItem(getContext())
                .setBackground(R.drawable.btn_0_blue)
                .setText(getResources().getString(R.string.make_collection))
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);
        leftMenu.addMenuItem(makeCollectItem);
    };

    private SimpleTextWatcher balanceTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String searchContent = ed_search_content.getText().toString().trim();
            List<BHBalance> result = new ArrayList<>();

            if(TextUtils.isEmpty(searchContent)){
                for(int i=0;i<mOriginBalanceList.size();i++){
                    BHBalance item = mOriginBalanceList.get(i);
                    result.add(item);
                }

            }else{
                for(int i=0;i<mOriginBalanceList.size();i++){
                    BHBalance item = mOriginBalanceList.get(i);
                    if(item.symbol.toLowerCase().contains(searchContent.toLowerCase())){
                        result.add(item);
                    }
                }
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
}
