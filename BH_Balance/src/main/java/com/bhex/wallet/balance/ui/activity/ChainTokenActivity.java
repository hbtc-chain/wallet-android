package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
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
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.ui.fragment.AddressQRFragment;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 10:57:14
 */
@Route(path = ARouterConfig.Balance_chain_tokens, name = "链下Token")
public class ChainTokenActivity extends BaseActivity<BalancePresenter> implements OnRefreshListener {

    @Autowired(name = "balance")
    BHBalance mBalance;


    @BindView(R2.id.layout_out_0)
    RelativeLayout layout_out_0;
    @BindView(R2.id.layout_index_1)
    RelativeLayout layout_index_1;
    @BindView(R2.id.layout_index_2)
    RelativeLayout layout_index_2;
    @BindView(R2.id.layout_index_3)
    RelativeLayout layout_index_3;
    @BindView(R2.id.layout_line)
    View layout_line;


    @BindView(R2.id.btn_make_address)
    AppCompatTextView btn_make_address;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.tv_token_name)
    AppCompatTextView tv_token_name;
    @BindView(R2.id.tv_hbc_address)
    AppCompatTextView tv_hbc_address;
    @BindView(R2.id.iv_token_qr)
    AppCompatImageView iv_token_qr;
    @BindView(R2.id.tv_token_address)
    AppCompatTextView tv_token_address;
    @BindView(R2.id.iv_qr)
    AppCompatImageView iv_qr;
    @BindView(R2.id.rcv_token_list)
    RecyclerView rcv_token_list;
    @BindView(R2.id.iv_paste)
    AppCompatImageView iv_paste;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.layout_scroll)
    NestedScrollView layout_scroll;

    BalanceAdapter mBalanceAdapter;
    private BalanceViewModel balanceViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_token;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        refreshLayout.setEnableLoadMore(false);
        tv_center_title.setText(mBalance.symbol.toUpperCase());
        layout_out_0.setBackground(ShapeUtils.getRoundRectTopDrawable(
                10, ColorUtil.getColor(this, R.color.blue_bg),
                true, 0));
        tv_token_name.setText(mBalance.symbol.toUpperCase());
        //设置地址
        setTokenAddress();
        refreshLayout.setOnRefreshListener(this);
    }

    //设置地址
    private void setTokenAddress() {
        tv_hbc_address.setTag(BHUserManager.getInstance().getCurrentBhWallet().address);
        AssetHelper.proccessAddress(tv_hbc_address,BHUserManager.getInstance().getCurrentBhWallet().address);
        tv_token_address.setTag(mBalance.external_address);
        AssetHelper.proccessAddress(tv_token_address,mBalance.external_address);
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(mBalance.chain)){
            layout_index_3.setVisibility(View.GONE);
            layout_line.setVisibility(View.GONE);
        } else if(TextUtils.isEmpty(mBalance.external_address)){
            layout_index_2.setVisibility(View.GONE);
            btn_make_address.setVisibility(View.VISIBLE);
        } else {
            layout_index_2.setVisibility(View.VISIBLE);
            btn_make_address.setVisibility(View.GONE);
        }
    }

    @Override
    protected void addEvent() {
        List<BHBalance> balanceList = BHBalanceHelper.loadBalanceByChain(mBalance.chain);
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

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        getLifecycle().addObserver(balanceViewModel);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(this);
    }

    @OnClick({R2.id.iv_paste,R2.id.iv_qr,R2.id.iv_token_paste,R2.id.iv_token_qr,R2.id.btn_make_address})
    public void onViewClicked(View view) {
        if(R.id.iv_qr == view.getId()){
            AddressQRFragment.showFragment(getSupportFragmentManager(),
                    AddressQRFragment.class.getSimpleName(),
                    mBalance.symbol.toUpperCase(),
                    tv_hbc_address.getTag().toString());
        }else if(R.id.iv_paste == view.getId()){
            ToolUtils.copyText(tv_hbc_address.getTag().toString(),this);
            ToastUtils.showToast(getString(R.string.copyed));
        }else if(R.id.iv_token_qr==view.getId()){
            AddressQRFragment.showFragment(getSupportFragmentManager(),
                    AddressQRFragment.class.getSimpleName(),
                    mBalance.symbol.toUpperCase(),
                    tv_token_address.getTag().toString());
        }else if(R.id.iv_token_paste == view.getId()){
            ToolUtils.copyText(tv_token_address.getTag().toString(),this);
            ToastUtils.showToast(getString(R.string.copyed));
        }else if(R.id.btn_make_address == view.getId()){
            BHBalance bthBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
            /*ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                    .withObject("balance", mBalance)
                    .withObject("bhtBalance",bthBalance)
                    .withInt("way", BH_BUSI_TYPE.跨链转账.getIntValue())
                    .navigation();*/
            Postcard postcard = ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                    .withObject("balance", mBalance)
                    .withObject("bhtBalance",bthBalance)
                    .withInt("way", BH_BUSI_TYPE.跨链转账.getIntValue());
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(this, postcard.getDestination());
            intent.putExtras(postcard.getExtras());
            startActivityForResult(intent,100);
        }

    }


    private void updateAssets(AccountInfo accountInfo) {
        BHUserManager.getInstance().setAccountInfo(accountInfo);
        //重新计算地址
        if(!BHConstants.BHT_TOKEN.equalsIgnoreCase(mBalance.symbol)){
            mBalance = BHBalanceHelper.getBHBalanceFromAccount(mBalance.symbol);
            setTokenAddress();
        }
        mPresenter.calculateAllTokenPrice(accountInfo,mBalanceAdapter.getData());
        mBalanceAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
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