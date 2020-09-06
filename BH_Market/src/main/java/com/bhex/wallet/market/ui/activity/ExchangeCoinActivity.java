package com.bhex.wallet.market.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 16:37:06
 * 兑币页面
 */
@Route(path = ARouterConfig.Market_exchange_coin, name = "兑币")
public class ExchangeCoinActivity extends BaseActivity implements PasswordFragment.PasswordClickListener {

    @Autowired(name = "symbol")
    String mSymbol;

    private BHBalance mBhtBalance;
    private BHBalance mTokenBalance;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.tv_from_token)
    AppCompatTextView tv_from_token;
    @BindView(R2.id.tv_to_token)
    AppCompatTextView tv_to_token;
    @BindView(R2.id.inp_amount)
    AppCompatEditText inp_amount;
    @BindView(R2.id.tv_token_name)
    AppCompatTextView tv_token_name;
    @BindView(R2.id.btn_exchange_action)
    AppCompatTextView btn_exchange_action;

    protected TransactionViewModel mTransactionViewModel;
    protected BalanceViewModel mBalanceViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_coin;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getString(R.string.exchange_coin));
    }

    @Override
    protected void addEvent() {
        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        mTransactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });

        mBalanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });

    }

    @OnClick({R2.id.btn_exchange_action})
    public void onClickView(View view){
        if(view.getId()==R.id.btn_exchange_action){
            exchangeAction();
        }
    }

    //兑换
    private void exchangeAction() {
        String map_amount = inp_amount.getText().toString().trim();
        if(TextUtils.isEmpty(map_amount)){
            ToastUtils.showToast(getString(R.string.please_input_exchange_amount));
            return;
        }

        if(mTokenBalance==null || Double.valueOf(mTokenBalance.amount)<=0 ){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+mSymbol.toUpperCase());
            return;
        }

        if(Double.valueOf(mTokenBalance.amount)<Double.valueOf(map_amount)){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+mSymbol.toUpperCase());
            return;
        }

        if(mBhtBalance==null || Double.valueOf(mBhtBalance.amount)<=0){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+BHConstants.BHT_TOKEN.toUpperCase());
            return;
        }

        PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                PasswordFragment.class.getName(),
                this,0);
    }

    @Override
    public void confirmAction(String password, int position, int way) {
        String from_token = tv_from_token.getText().toString();
        String to_token = tv_to_token.getText().toString();
        String map_amount = inp_amount.getText().toString().trim();
        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.createMappingSwap(to_token,from_token,map_amount,
                    BHConstants.BHT_DEFAULT_FEE,suquece,password);
            mTransactionViewModel.sendTransaction(this,bhSendTranscation);
            return 0;
        });
    }

    //更新兑换状态
    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            ToastUtils.showToast("兑换成功");
        }else{
            ToastUtils.showToast("兑换失败");
        }
    }

    //更新资产
    private void updateAssets(AccountInfo accountInfo){
        if(accountInfo==null){
            return;
        }
        mBhtBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
        mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mSymbol);
    }
}