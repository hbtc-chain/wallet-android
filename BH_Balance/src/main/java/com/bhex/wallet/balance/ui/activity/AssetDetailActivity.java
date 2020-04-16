package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.balance.CoinBottomBtn;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.TxOrderAdapter;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.balance.presenter.AssetPresenter;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.ui.fragment.ReInvestShareFragment;
import com.bhex.wallet.balance.ui.fragment.WithDrawShareFragment;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;

/**
 * @author gongdongyang
 * 2020-4-4 23:34:33
 * 资产详情
 */
@Route(path = ARouterConfig.Balance_Assets_Detail)
public class AssetDetailActivity extends BaseActivity<AssetPresenter> {

    @Autowired(name = "balance")
    BHBalance balance;
    @Autowired(name = "accountInfo")
    AccountInfo mAccountInfo;

    BHBalance bthBalance;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.btn_transfer_in)
    CoinBottomBtn btn_transfer_in;
    @BindView(R2.id.btn_transfer_out)
    CoinBottomBtn btn_transfer_out;
    @BindView(R2.id.btn_draw_share)
    CoinBottomBtn btn_draw_share;
    @BindView(R2.id.btn_reinvest_share)
    CoinBottomBtn btn_reinvest_share;
    @BindView(R2.id.recycler_order)
    RecyclerView recycler_order;
    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;

    @BindView(R2.id.tv_coin_amount)
    AppCompatTextView tv_coin_amount;
    @BindView(R2.id.tv_coin_currency)
    AppCompatTextView tv_coin_currency;
    @BindView(R2.id.tv_available_text)
    AppCompatTextView tv_available_text;
    @BindView(R2.id.tv_available_value)
    AppCompatTextView tv_available_value;
    @BindView(R2.id.tv_entrust_text)
    AppCompatTextView tv_entrust_text;
    @BindView(R2.id.tv_entrust_value)
    AppCompatTextView tv_entrust_value;
    @BindView(R2.id.tv_redemption_text)
    AppCompatTextView tv_redemption_text;
    @BindView(R2.id.tv_redemption_value)
    AppCompatTextView tv_redemption_value;
    @BindView(R2.id.tv_income_text)
    AppCompatTextView tv_income_text;
    @BindView(R2.id.tv_income_value)
    AppCompatTextView tv_income_value;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    TxOrderAdapter mTxOrderAdapter;
    TransactionViewModel transactionViewModel;
    List<TransactionOrder> mOrderList;

    int mCurrentPage = 1;

    BalanceViewModel balanceViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AssetPresenter(this);
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);
        tv_center_title.setText(balance.symbol.toUpperCase());

        bthBalance = mPresenter.getBthBalanceWithAccount(mAccountInfo);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_order.setLayoutManager(lm);

        mTxOrderAdapter = new TxOrderAdapter(R.layout.item_tx_order, mOrderList);
        recycler_order.setAdapter(mTxOrderAdapter);

        recycler_order.setNestedScrollingEnabled(false);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);
        //根据token 显示View
        initTokenView();
    }

    private void initTokenView() {

        //计算持有资产
        String []res = BHBalanceHelper.getAmountToCurrencyValue(this,balance.amount,balance.symbol,false);
        tv_coin_amount.setText(res[0]);
        //对应法币实际值
        tv_coin_currency.setText(res[1]);

        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.symbol)){
            //可用数量
            String available_value = BHBalanceHelper.getAmountForUser(this,balance.amount,"0",balance.symbol);
            tv_available_value.setText(available_value);
            //委托中
            String bonded_value = NumberUtil.dispalyForUsertokenAmount(mAccountInfo.getBonded());
            tv_entrust_value.setText(bonded_value);
            //赎回中
            String unbonding_value = NumberUtil.dispalyForUsertokenAmount(mAccountInfo.getUnbonding());
            tv_redemption_value.setText(unbonding_value);
            //已收益
            String claimed_reward_value = NumberUtil.dispalyForUsertokenAmount(mAccountInfo.getClaimed_reward());
            tv_income_value.setText(claimed_reward_value);

            //转账
            btn_transfer_out.tv_bottom_text.setText(getResources().getString(R.string.transfer));

        } else if (BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)) {
            //原生代币
            btn_draw_share.setVisibility(View.GONE);
            btn_reinvest_share.setVisibility(View.GONE);
            tv_available_text.setVisibility(View.GONE);
            tv_available_value.setVisibility(View.GONE);
            tv_entrust_text.setVisibility(View.GONE);
            tv_entrust_value.setVisibility(View.GONE);
            tv_redemption_text.setVisibility(View.GONE);
            tv_redemption_value.setVisibility(View.GONE);
            tv_income_text.setVisibility(View.GONE);
            tv_income_value.setVisibility(View.GONE);
            //转账
            btn_transfer_out.tv_bottom_text.setText(getResources().getString(R.string.transfer));
        } else {

            //跨链代币
            //转账
            btn_transfer_out.tv_bottom_text.setText(getResources().getString(R.string.transfer));
            //跨链充币
            btn_draw_share.iv_coin_icon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_cross_trans_in));
            btn_draw_share.tv_bottom_text.setText(getResources().getString(R.string.cross_chian_trans_in));

            btn_draw_share.setId(R.id.cross_chian_transfer_in);
            //跨链提币
            btn_reinvest_share.iv_coin_icon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_cross_trans_out));
            btn_reinvest_share.tv_bottom_text.setText(getResources().getString(R.string.cross_chian_trans_out));
            btn_reinvest_share.setId(R.id.cross_chian_withdraw);

            tv_available_text.setVisibility(View.GONE);
            tv_available_value.setVisibility(View.GONE);
            tv_entrust_text.setVisibility(View.GONE);
            tv_entrust_value.setVisibility(View.GONE);
            tv_redemption_text.setVisibility(View.GONE);
            tv_redemption_value.setVisibility(View.GONE);
            tv_income_text.setVisibility(View.GONE);
            tv_income_value.setVisibility(View.GONE);
        }

        Flowable.timer(4L, TimeUnit.SECONDS).subscribe();
    }

    @Override
    protected void addEvent() {
        empty_layout.showProgess();

        transactionViewModel.queryTransctionByAddress(this,
                BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, balance.symbol, null);

        transactionViewModel.transLiveData.observe(this, ldm -> {
            //更新交易记录
            if (ldm.loadingStatus == LoadingStatus.SUCCESS && ldm.getData() != null && ldm.getData().size() > 0) {
                empty_layout.loadSuccess();
                updateTxOrder(ldm.getData());
            } else if (ldm.getData() == null || ldm.getData().size() == 0) {
                empty_layout.showNoData();
            } else {
                empty_layout.showNeterror(view -> {

                });
            }
        });

        mTxOrderAdapter.setOnItemClickListener((adapter, view, position) -> {
            TransactionOrder txo = mOrderList.get(position);
            TxOrderItem txOrderItem = TransactionHelper.getTxOrderItem(txo);
            ARouter.getInstance().build(ARouterConfig.Balance_transcation_detail)
                    .withObject("txo",txOrderItem)
                    .navigation();
        });

        balanceViewModel.accountLiveData.observe(this,ldm->{
            updateAssest(ldm);
        });

        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            balanceViewModel.getAccountInfo(AssetDetailActivity.this,bthBalance.address);
            transactionViewModel.queryTransctionByAddress(this,
                    BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, balance.symbol, null);
        });
        EventBus.getDefault().register(this);
    }

    /**
     * 更新资产
     * @param ldm
     */
    private void updateAssest(LoadDataModel<AccountInfo> ldm) {

        LogUtils.d("AssetDetailActivity==>:","==updateAssest==");
        //更新
        refreshLayout.finishRefresh();
        if(ldm.loadingStatus==LoadingStatus.SUCCESS){
            mAccountInfo = ldm.getData();
            mPresenter.updateBalance(mAccountInfo,balance);
            initTokenView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新交易记录
     *
     * @param data
     */
    private void updateTxOrder(List<TransactionOrder> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mTxOrderAdapter.getData().clear();
        mOrderList = data;
        mTxOrderAdapter.addData(data);
    }

    @OnClick({R2.id.btn_transfer_in, R2.id.btn_transfer_out,
            R2.id.btn_draw_share, R2.id.btn_reinvest_share})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_transfer_in) {
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_in)
                    .withObject("balance", balance)
                    .withInt("way",1)
                    .navigation();
        } else if (view.getId() == R.id.btn_transfer_out) {
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_out)
                    .withObject("balance", balance)
                    .withObject("bhtBalance",bthBalance)
                    .withInt("way",1)
                    .navigation();
        } else if (view.getId() == R.id.btn_draw_share) {
            WithDrawShareFragment.showWithDrawShareFragment(getSupportFragmentManager(),
                    WithDrawShareFragment.class.getSimpleName(), itemListener);
        } else if (view.getId() == R.id.btn_reinvest_share) {
            ReInvestShareFragment.showWithDrawShareFragment(getSupportFragmentManager(),
                    ReInvestShareFragment.class.getSimpleName(), fragmentItemListener);
        }else if(view.getId() == R.id.cross_chian_transfer_in){
            if(!TextUtils.isEmpty(balance.external_address)){
                /*ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();
                return;*/
                ARouter.getInstance().build(ARouterConfig.Balance_transfer_in)
                        .withObject("balance", balance)
                        .withInt("way",2)
                        .navigation();
            }else{
                //请求用户资产 获取链外地址

            }

        }else if(view.getId() == R.id.cross_chian_withdraw){
            if(!TextUtils.isEmpty(balance.external_address)){
                /*ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();
                return;*/
                ARouter.getInstance().build(ARouterConfig.Balance_transfer_out)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();
            }else{
                //请求用户资产 获取链外地址

            }

        }
    }

    private WithDrawShareFragment.FragmentItemListener itemListener = (position -> {

    });

    private ReInvestShareFragment.FragmentItemListener fragmentItemListener = (position -> {

    });


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTransction(TransctionEvent txEvent){
        transactionViewModel.queryTransctionByAddress(this,
                BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, balance.symbol, null);
    }

}
