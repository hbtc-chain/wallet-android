package com.bhex.wallet.balance.ui.activity;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.balance.CoinBottomBtn;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.TxOrderAdapter;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.DelegateValidator;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.balance.presenter.AssetPresenter;
import com.bhex.wallet.balance.ui.fragment.ReInvestShareFragment;
import com.bhex.wallet.balance.ui.fragment.WithDrawShareFragment;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.BHTokenMapping;
import com.bhex.wallet.common.tx.BHRawTransaction;
import com.bhex.wallet.common.tx.TransactionMsg;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.bhex.wallet.common.tx.TxReq;
import com.bhex.wallet.common.ui.fragment.Password30Fragment;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-4-4 23:34:33
 * 资产详情
 */
public abstract class TokenDetailActivity extends BaseActivity<AssetPresenter> {
    public BHBalance bthBalance;
    public BHBalance symbolBalance;
    public BHToken symbolToken;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.btn_item1)
    CoinBottomBtn btn_item1;
    @BindView(R2.id.btn_item2)
    CoinBottomBtn btn_item2;
    @BindView(R2.id.btn_item3)
    CoinBottomBtn btn_item3;
    @BindView(R2.id.btn_item4)
    CoinBottomBtn btn_item4;
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
    @BindView(R2.id.iv_coin_ic)
    AppCompatImageView iv_coin_ic;

    TxOrderAdapter mTxOrderAdapter;
    List<TransactionOrder> mOrderList;
    BalanceViewModel balanceViewModel;
    int mCurrentPage = 1;
    TransactionViewModel transactionViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_token_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AssetPresenter(this);
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);
        symbolBalance = BHBalanceHelper.getBHBalanceFromAccount(getSymbol());
        symbolToken  = CacheCenter.getInstance().getSymbolCache().getBHToken(BHConstants.BHT_TOKEN);
        bthBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);

        tv_center_title.setText(symbolBalance.name.toUpperCase());
        mTxOrderAdapter = new TxOrderAdapter(mOrderList,symbolBalance.symbol);
        recycler_order.setAdapter(mTxOrderAdapter);

        recycler_order.setNestedScrollingEnabled(false);
        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,24),0,
                ColorUtil.getColor(this,R.color.global_divider_color));

        recycler_order.addItemDecoration(ItemDecoration);

        refreshLayout.setEnableLoadMore(false);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        balanceViewModel = ViewModelProviders.of(MainActivityManager._instance.mainActivity).get(BalanceViewModel.class);

        transactionViewModel.initData(this,symbolBalance.symbol);
        //根据token 显示View
        initTokenView();
        initTokenAsset();
    }

    private void initTokenView() {
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(symbolBalance.symbol)){
            //转账
            btn_item2.tv_bottom_text.setText(getResources().getString(R.string.transfer));
            btn_item3.setActionMore(View.GONE);
            btn_item4.setActionMore(View.GONE);
            btn_item4.setVisibility(View.VISIBLE);
            btn_item4.tv_bottom_text.setText(getString(R.string.entrust_relive_entrust));
            findViewById(R.id.layout_divider).setVisibility(View.VISIBLE);
        } else if (BHConstants.BHT_TOKEN.equalsIgnoreCase(symbolBalance.chain)) {
            //原生代币
            btn_item3.setVisibility(View.GONE);
            btn_item4.setVisibility(View.GONE);
            tv_available_text.setVisibility(View.GONE);
            tv_available_value.setVisibility(View.GONE);
            tv_entrust_text.setVisibility(View.GONE);
            tv_entrust_value.setVisibility(View.GONE);
            tv_redemption_text.setVisibility(View.GONE);
            tv_redemption_value.setVisibility(View.GONE);
            tv_income_text.setVisibility(View.GONE);
            tv_income_value.setVisibility(View.GONE);
            //转账
            btn_item2.tv_bottom_text.setText(getResources().getString(R.string.transfer));
        } else {
            //跨链代币
            //转账
            btn_item2.tv_bottom_text.setText(getResources().getString(R.string.transfer));
            //跨链充币/跨链提币
            btn_item3.iv_coin_icon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_cross_link));
            btn_item3.tv_bottom_text.setText(getResources().getString(R.string.crosslink));
            btn_item3.setActionMore(View.VISIBLE);

            btn_item3.setId(R.id.cross_chian_transfer_in);

            tv_available_text.setVisibility(View.GONE);
            tv_available_value.setVisibility(View.GONE);
            tv_entrust_text.setVisibility(View.GONE);
            tv_entrust_value.setVisibility(View.GONE);
            tv_redemption_text.setVisibility(View.GONE);
            tv_redemption_value.setVisibility(View.GONE);
            tv_income_text.setVisibility(View.GONE);
            tv_income_value.setVisibility(View.GONE);
        }

        if(!BHConstants.BHT_TOKEN.equalsIgnoreCase(symbolToken.symbol)){
            //兑币功能
            BHTokenMapping tokenMapping = CacheCenter.getInstance().getTokenMapCache().getTokenMappingOne(symbolToken.symbol);
            btn_item4.setVisibility((tokenMapping==null)?View.GONE:View.VISIBLE);
            if(tokenMapping!=null){
                btn_item4.iv_coin_icon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_cross_trans_out));
                btn_item4.tv_bottom_text.setText(getResources().getString(R.string.mapping_swap));
                btn_item4.setId(R.id.cross_chian_withdraw);
                btn_item4.setActionMore(View.GONE);
            }
        }

    }

    private void initTokenAsset(){
        if(BHUserManager.getInstance().getAccountInfo()==null){
            return;
        }
        //计算持有资产
        String []res = BHBalanceHelper.getAmountToCurrencyValue(this,symbolBalance.amount,symbolToken.symbol,false);
        tv_coin_amount.setText(res[0]);
        //对应法币实际值
        tv_coin_currency.setText(res[1]);
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(symbolToken.symbol)){
            //可用数量
            String available_value = BHBalanceHelper.getAmountForUser(this,symbolBalance.amount,"0",symbolToken.symbol);
            tv_available_value.setText(available_value);
            //委托中
            String bonded_value = NumberUtil.dispalyForUsertokenAmount4Level(BHUserManager.getInstance().getAccountInfo().getBonded());
            tv_entrust_value.setText(bonded_value);
            //赎回中
            String unbonding_value = NumberUtil.dispalyForUsertokenAmount4Level(BHUserManager.getInstance().getAccountInfo().getUnbonding());
            tv_redemption_value.setText(unbonding_value);
            //已收益
            String claimed_reward_value = NumberUtil.dispalyForUsertokenAmount4Level(BHUserManager.getInstance().getAccountInfo().getClaimed_reward());
            tv_income_value.setText(claimed_reward_value);
        }

        //链上资产
    }

    @Override
    protected void addEvent() {
        empty_layout.showProgess();

        transactionViewModel.queryTransctionByAddress(this,
                BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, symbolToken.symbol, null);

        getLifecycle().addObserver(transactionViewModel);

        transactionViewModel.transLiveData.observe(this, ldm -> {
            //更新交易记录
            if (ldm.loadingStatus == LoadingStatus.SUCCESS ) {
                if((ldm.getData() == null || ldm.getData().size()==0)&& mTxOrderAdapter.getData().size()==0){
                    empty_layout.showNoData();
                }else {
                    empty_layout.loadSuccess();
                    updateTxOrder(ldm.getData());
                }
            } else if(ldm.loadingStatus == LoadingStatus.ERROR){
                if(mTxOrderAdapter.getData()==null || mTxOrderAdapter.getData().size()==0){
                    empty_layout.showNeterror(view -> {
                    });
                }
            }
        });

        mTxOrderAdapter.setOnItemClickListener((adapter, view,
                                                position) -> {
            TransactionOrder txo = mOrderList.get(position);
            TxOrderItem txOrderItem = TransactionHelper.getTxOrderItem(txo);
            TransactionHelper.gotoTranscationDetail(txOrderItem,symbolToken.symbol);
        });

        LiveDataBus.getInstance().with(BHConstants.Label_Account,LoadDataModel.class).observe(this,ldm->{
            updateAssest(ldm);
        });

        refreshLayout.setOnRefreshListener(refreshLayout -> {
            balanceViewModel.getAccountInfo(TokenDetailActivity.this,null);
            transactionViewModel.queryTransctionByAddress(this,
                    BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, symbolToken.symbol, null);
        });

        transactionViewModel.validatorLiveData.observe(this,ldm->{
            updateValidatorAddress(ldm);
        });

        balanceViewModel.getAccountInfo(TokenDetailActivity.this,null);
        EventBus.getDefault().register(this);
    }

    /**
     * 更新资产
     * @param ldm
     */
    private void updateAssest(LoadDataModel<AccountInfo> ldm) {
        //更新
        refreshLayout.finishRefresh();
        if(ldm.loadingStatus==LoadingStatus.SUCCESS){
            bthBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
            symbolBalance = BHBalanceHelper.getBHBalanceFromAccount(symbolToken.symbol);
            initTokenAsset();
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

    /**
     * 更新验证人列表
     */
    private List<DelegateValidator> mRewardList;
    private void updateValidatorAddress(LoadDataModel ldm) {
        if(ldm.loadingStatus==LoadDataModel.SUCCESS){
            if(ldm.code==1){
                toDoWithdrawShare(ldm);
            }else{
                toDoReDelegate(ldm);
            }
        }else if(ldm.loadingStatus==LoadDataModel.ERROR){
            ToastUtils.showToast(getResources().getString(R.string.no_profit));
        }
    }

    //提取收益--回调
    public void toDoWithdrawShare(LoadDataModel ldm){
        List<DelegateValidator> dvList =  (List<DelegateValidator>)ldm.getData();
        //计算所有收益
        double all_reward = mPresenter.calAllReward(dvList);
        String def_all_reward = NumberUtil.dispalyForUsertokenAmount4Level(all_reward+"");
        if(Double.valueOf(def_all_reward)<=0){
            ToastUtils.showToast(getResources().getString(R.string.no_profit));
        }else {
            mRewardList = dvList;
            WithDrawShareFragment.showWithDrawShareFragment(getSupportFragmentManager(),
                    WithDrawShareFragment.class.getSimpleName(), itemListener,def_all_reward);
        }
    }

    //复投分红回调
    public void toDoReDelegate(LoadDataModel ldm){
        List<DelegateValidator> dvList =  (List<DelegateValidator>)ldm.getData();
        //计算所有收益
        double all_reward = mPresenter.calAllReward(dvList);
        String def_all_reward = NumberUtil.dispalyForUsertokenAmount4Level(all_reward+"");
        if(Double.valueOf(def_all_reward)<=0){
            ToastUtils.showToast(getResources().getString(R.string.no_profit));
        }else {
            mRewardList = dvList;
            ReInvestShareFragment.showWithDrawShareFragment(getSupportFragmentManager(),
                    ReInvestShareFragment.class.getSimpleName(),fragmentItemListener,def_all_reward);
        }
    }

    Password30Fragment.PasswordClickListener withDrawPwdListener = (password, position,way) -> {
        if(position==1){

            List<TransactionMsg.ValidatorMsg> validatorMsgs = mPresenter.getAllValidator(mRewardList);
            List<TxReq.TxMsg> tx_msg_list = BHRawTransaction.createRewardMsg(validatorMsgs);
            transactionViewModel.transferInnerExt(this,password,BHUserManager.getInstance().getDefaultGasFee().displayFee,tx_msg_list);
        }else if(position==2){

            List<TransactionMsg.ValidatorMsg> validatorMsgs = mPresenter.getAllValidator(mRewardList);
            List<TransactionMsg.DoEntrustMsg> doEntrustMsgs = mPresenter.getAllEntrust(mRewardList);
            List<TxReq.TxMsg> tx_msg_list = BHRawTransaction.createReDoEntrustMsg(validatorMsgs,doEntrustMsgs);
            transactionViewModel.transferInnerExt(this,password,BHUserManager.getInstance().getDefaultGasFee().displayFee,tx_msg_list);
        }
    };

    //发送提取分红交易
    private WithDrawShareFragment.FragmentItemListener itemListener = (position -> {
        Password30Fragment.showPasswordDialog(getSupportFragmentManager(),Password30Fragment.class.getSimpleName(),withDrawPwdListener,1);
    });

    //发送复投分红交易
    private ReInvestShareFragment.FragmentItemListener fragmentItemListener = (position -> {
        Password30Fragment.showPasswordDialog(getSupportFragmentManager(),Password30Fragment.class.getSimpleName(),withDrawPwdListener,2);
    });

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTransction(TransctionEvent txEvent){
        transactionViewModel.queryTransctionByAddress(this,
                BHUserManager.getInstance().getCurrentBhWallet().address, mCurrentPage, symbolToken.symbol, null);
    }


    public abstract String getSymbol();
}
