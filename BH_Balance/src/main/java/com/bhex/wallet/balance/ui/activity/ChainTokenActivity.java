package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.ui.BTCViewHolder;
import com.bhex.wallet.balance.ui.HBCViewHolder;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-8-31 10:57:14
 */
@Route(path = ARouterConfig.Balance_chain_tokens, name = "链下Token")
public class ChainTokenActivity extends BaseActivity<BalancePresenter> implements OnRefreshListener {

    @Autowired(name = "balance")
    BHBalance mBalance;

    @BindView(R2.id.layout_index_0)
    LinearLayout layout_index_0;
    @BindView(R2.id.layout_index_1)
    RelativeLayout layout_index_1;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.rcv_token_list)
    RecyclerView rcv_token_list;
    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;

    private BalanceViewModel balanceViewModel;

    private BalanceAdapter mBalanceAdapter;
    private HBCViewHolder mHbcViewHolder;
    private BTCViewHolder mBtcViewHolder;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_token;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(mBalance.symbol.toUpperCase());
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);

        mHbcViewHolder = HBCViewHolder.getInstance().initView(this,layout_index_0,mBalance);
        mBtcViewHolder = BTCViewHolder.getInstance().initView(this,layout_index_1,mBalance);
        setTokenAddress();
    }

    //设置地址
    private void setTokenAddress() {

        mHbcViewHolder.setTokenAddress(mBalance);
        mBtcViewHolder.setTokenAddress(mBalance);
    }

    @Override
    protected void addEvent() {
        List<BHBalance> balanceList = BHBalanceHelper.loadBalanceByChain(mBalance.chain);
        if(ToolUtils.checkListIsEmpty(balanceList)){
            empty_layout.showNoData();
            return;
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecycleViewExtDivider itemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,68),0,
                ColorUtil.getColor(this,R.color.global_divider_color));
        rcv_token_list.addItemDecoration(itemDecoration);

        rcv_token_list.setLayoutManager(llm);
        mBalanceAdapter = new BalanceAdapter(balanceList);
        rcv_token_list.setAdapter(mBalanceAdapter);
        rcv_token_list.setNestedScrollingEnabled(false);

        mBalanceAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHBalance bhBalance = mBalanceAdapter.getData().get(position);
            Postcard postcard = ARouter.getInstance().build(ARouterConfig.Balance_Token_Detail)
                    .withObject("balance",bhBalance);
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(this, postcard.getDestination());
            intent.putExtras(postcard.getExtras());
            startActivityForResult(intent,100);
        });

        balanceViewModel = ViewModelProviders.of(MainActivityManager._instance.mainActivity).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
            refreshLayout.finishRefresh();
        });
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(this);
    }

    private void updateAssets(AccountInfo accountInfo) {
        BHUserManager.getInstance().setAccountInfo(accountInfo);
        //重新计算地址
        if(!BHConstants.BHT_TOKEN.equalsIgnoreCase(mBalance.symbol)){
            mBalance = BHBalanceHelper.getBHBalanceFromAccount(mBalance.symbol);
            setTokenAddress();
        }
        mPresenter.calculateAllTokenPrice(this,accountInfo,mBalanceAdapter.getData());
        mBalanceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        balanceViewModel.getAccountInfo(this, CacheStrategy.onlyRemote());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            refreshLayout.autoRefresh();
        }
    }
}