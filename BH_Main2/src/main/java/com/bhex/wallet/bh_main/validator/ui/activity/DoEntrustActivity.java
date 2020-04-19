package com.bhex.wallet.bh_main.validator.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.CustomTextView;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.validator.enums.ENTRUST_BUSI_TYPE;
import com.bhex.wallet.bh_main.validator.presenter.DoEntrustPresenter;
import com.bhex.wallet.bh_main.validator.viewmodel.EnstrustViewModel;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.ValidatorDelegationInfo;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.google.android.material.button.MaterialButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zc
 * 2020-4-16
 * 委托
 */
@Route(path = ARouterConfig.Do_Entrust)
public class DoEntrustActivity extends BaseActivity<DoEntrustPresenter> {

    @Autowired(name = "validatorInfo")
    ValidatorInfo mValidatorInfo;
    @Autowired(name = "bussiType")
    int mBussiType;


    @BindView(R2.id.tv_tips)
    AppCompatTextView tv_tips;

    @BindView(R2.id.tv_to_address)
    WithDrawInput tv_to_address;

    @BindView(R2.id.tv_available_amount)
    CustomTextView tv_available_amount;

    @BindView(R2.id.ed_entrust_amount)
    WithDrawInput ed_entrust_amount;

    @BindView(R2.id.ed_entrust_fee)
    WithDrawInput ed_entrust_fee;

    @BindView(R2.id.ed_real_entrust_amount)
    WithDrawInput ed_real_entrust_amount;

    @BindView(R2.id.tv_tips_1)
    AppCompatTextView tv_tips_1;
    @BindView(R2.id.tv_tips_2)
    AppCompatTextView tv_tips_2;
    @BindView(R2.id.tv_tips_3)
    AppCompatTextView tv_tips_3;

    @BindView(R2.id.btn_do_entrust)
    MaterialButton btn_do_entrust;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.tv_entrust_to_title)
    AppCompatTextView tv_entrust_to_title;
    @BindView(R2.id.tv_entrust_amount_title)
    AppCompatTextView tv_entrust_amount_title;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    private String token = BHConstants.BHT_TOKEN;

    EnstrustViewModel mEnstrustViewModel;
    BalanceViewModel mBalanceViewModel;
    String mAvailabelTitle = "";

    String validatorAddress ="";
    private String available_amount;

    @Override
    protected void initPresenter() {
        mPresenter = new DoEntrustPresenter(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_do_entrust;
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);
        initValidatorView();
    }


    private void initValidatorView() {
        tv_to_address.ed_input.setEnabled(false);

        if (mBussiType==ENTRUST_BUSI_TYPE.TRANFER_ENTRUS.getTypeId()) {
            if (mValidatorInfo != null) {
                String address = mValidatorInfo.getOperator_address();
                if (mValidatorInfo.getDescription() != null) {
                    address = mValidatorInfo.getDescription().getMoniker() + "-" + addressReplace(address);
                }
                validatorAddress = mValidatorInfo.getOperator_address();
                tv_to_address.ed_input.setText(address);

                tv_to_address.btn_right_text.setVisibility(View.GONE);
                tv_to_address.iv_right.setVisibility(View.VISIBLE);
                tv_to_address.iv_right.setImageResource(R.drawable.ic_arrowdown);
                tv_to_address.setOnClickListener(v -> {
                    // TODO 转委托暂时不做

                });
            }
            mAvailabelTitle = "可转 ";
            tv_entrust_to_title.setText(getString(R.string.source_address));
            tv_entrust_amount_title.setText(getString(R.string.transfer_entrust_amount));
            btn_do_entrust.setText(getString(R.string.transfer_entrust));
            tv_center_title.setText(getString(R.string.transfer_entrust));
        } else if (mBussiType == ENTRUST_BUSI_TYPE.DO_ENTRUS.getTypeId()) {
            if (mValidatorInfo != null) {
                String address = mValidatorInfo.getOperator_address();
                validatorAddress = mValidatorInfo.getOperator_address();
                if (mValidatorInfo.getDescription() != null) {
                    address = mValidatorInfo.getDescription().getMoniker() + "-" + addressReplace(address);
                }
                tv_to_address.ed_input.setText(address);
            }
            mAvailabelTitle = "可用 ";
            tv_entrust_to_title.setText(getString(R.string.entrust_to));
            tv_entrust_amount_title.setText(getString(R.string.entrust_amount));
            btn_do_entrust.setText(getString(R.string.do_entrust));
            tv_center_title.setText(getString(R.string.do_entrust));
        } else {
            if (mValidatorInfo != null) {
                validatorAddress = mValidatorInfo.getOperator_address();
            }
            String address = getString(R.string.my_trusteeship) + "-" + addressReplace(BHUserManager.getInstance().getCurrentBhWallet().address);
            tv_to_address.ed_input.setText(address);
            mAvailabelTitle = "可解 ";
            tv_entrust_to_title.setText(getString(R.string.relieve_entrust_to));
            tv_entrust_amount_title.setText(getString(R.string.relieve_entrust_amount));
            btn_do_entrust.setText(getString(R.string.relieve_entrust));
            tv_center_title.setText(getString(R.string.relieve_entrust));
        }
        ed_real_entrust_amount.btn_right_text.setText(token.toUpperCase());
        ed_entrust_fee.btn_right_text.setText(token.toUpperCase());

        tv_available_amount.setText("可用 " + getString(R.string.string_placeholder) + token.toUpperCase());
    }

    @Override
    protected void addEvent() {
        ed_entrust_amount.btn_right_text.setOnClickListener(allListener);

        mEnstrustViewModel = ViewModelProviders.of(this).get(EnstrustViewModel.class);
        mEnstrustViewModel.mutableLiveData.observe(this,ldm -> {
            updateDoEntrustStatus(ldm);
        });
        mBalanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);
        //mEnstrustViewModel = ViewModelProviders.of(this).get(EnstrustViewModel.class);
        mBalanceViewModel.accountLiveData.observe(this, ldm -> {
            refreshLayout.finishRefresh();
            if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
                updateAssets(ldm.getData());
            }

        });
        mEnstrustViewModel.delegationLiveData.observe(this, ldm -> {
            refreshLayout.finishRefresh();
            if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
                updateValidatorAssets(ldm.getData());
            }

        });
        queryAssetInfo();

        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            queryAssetInfo();
        });
    }
    private String addressReplace(String originAddress) {
        if (originAddress == null || TextUtils.isEmpty(originAddress)) {
            return "";
        }
        if (originAddress.length()<21) {
            return originAddress;
        }
        return originAddress.replace(originAddress.substring(10,originAddress.length()-10),"...");
    }
    private void  queryAssetInfo() {
        if (mBussiType == ENTRUST_BUSI_TYPE.DO_ENTRUS.getTypeId()) {
            mBalanceViewModel.getAccountInfo(this,null);
        } else {
            mEnstrustViewModel.getCustDelegations(this);
        }
    }


    private void updateDoEntrustStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            if (mBussiType == ENTRUST_BUSI_TYPE.DO_ENTRUS.getTypeId()) {
                com.bhex.network.utils.ToastUtils.showToast(getString(R.string.do_entrust_succeed));
            } else if (mBussiType == ENTRUST_BUSI_TYPE.RELIEVE_ENTRUS.getTypeId()) {
                com.bhex.network.utils.ToastUtils.showToast(getString(R.string.relieve_entrust_succeed));
            } else if (mBussiType == ENTRUST_BUSI_TYPE.TRANFER_ENTRUS.getTypeId()) {
                com.bhex.network.utils.ToastUtils.showToast(getString(R.string.transfer_entrust_succeed));
            }
            finish();
        }else{
            if (mBussiType == ENTRUST_BUSI_TYPE.DO_ENTRUS.getTypeId()) {
                ToastUtils.showToast(getString(R.string.do_entrust_failed));
            } else if (mBussiType == ENTRUST_BUSI_TYPE.RELIEVE_ENTRUS.getTypeId()) {
                ToastUtils.showToast(getString(R.string.relieve_entrust_failed));
            } else if (mBussiType == ENTRUST_BUSI_TYPE.TRANFER_ENTRUS.getTypeId()) {
                ToastUtils.showToast(getString(R.string.transfer_entrust_failed));
            }
        }
    }

    @OnClick({R2.id.btn_do_entrust})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_do_entrust) {
            if (mBussiType == ENTRUST_BUSI_TYPE.DO_ENTRUS.getTypeId()) {
                sendDoEntrust();
            } else if (mBussiType == ENTRUST_BUSI_TYPE.RELIEVE_ENTRUS.getTypeId()) {
                sendRelieveEntrust();
            } else if (mBussiType == ENTRUST_BUSI_TYPE.TRANFER_ENTRUS.getTypeId()) {
                sendTransferEntrust();
            }
        }
    }

    private void sendTransferEntrust() {
        // TODO 转委托暂时不做
    }

    private void sendRelieveEntrust() {
        boolean flag = mPresenter.checkDoEntrust(validatorAddress,BHUserManager.getInstance().getCurrentBhWallet().getAddress(),
                ed_entrust_amount.ed_input.getText().toString(),
                String.valueOf(available_amount),
                ed_entrust_fee.ed_input.getText().toString().trim()
        );
        if(!flag){
            return;
        }

        String hexPK = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey,BHUserManager.getInstance().getCurrentBhWallet().password);
        String delegator_address = BHUserManager.getInstance().getCurrentBhWallet().getAddress();
        String validator_address = validatorAddress;
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        String entrustDrawAmount = ed_entrust_amount.ed_input.getText().toString();
        String feeAmount = ed_entrust_fee.ed_input.getText().toString();


        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.relieveEntrust(hexPK,delegator_address,validator_address,entrustDrawAmount,feeAmount,
                    gasPrice,BHConstants.BH_MEMO,null,suquece,token);
            mEnstrustViewModel.sendDoEntrust(this,bhSendTranscation);
            return 0;
        });
    }

    /**
     * 发送交易
     */
    private void sendDoEntrust(){
        boolean flag = mPresenter.checkDoEntrust(validatorAddress,BHUserManager.getInstance().getCurrentBhWallet().getAddress(),
                ed_entrust_amount.ed_input.getText().toString(),
                String.valueOf(available_amount),
                ed_entrust_fee.ed_input.getText().toString().trim()
        );
        if(!flag){
            return;
        }

        String hexPK = CryptoUtil.decryptPK(BHUserManager.getInstance().getCurrentBhWallet().privateKey,BHUserManager.getInstance().getCurrentBhWallet().password);
        String delegator_address = BHUserManager.getInstance().getCurrentBhWallet().getAddress();
        String validator_address = validatorAddress;
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        String entrustDrawAmount = ed_entrust_amount.ed_input.getText().toString();
        String feeAmount = ed_entrust_fee.ed_input.getText().toString();


        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.doEntrust(hexPK,delegator_address,validator_address,entrustDrawAmount,feeAmount,
                    gasPrice,BHConstants.BH_MEMO,null,suquece,token);
            mEnstrustViewModel.sendDoEntrust(this,bhSendTranscation);
            return 0;
        });
    }


    public View.OnClickListener allListener = v -> {
        if (available_amount==null || TextUtils.isEmpty(available_amount)) {
            ed_entrust_amount.ed_input.setText("");
            return;
        }
        String fee = ed_entrust_fee.ed_input.getText().toString().trim();
        if (TextUtils.isEmpty(fee)) {
            fee = "0";
        }
        double all_count = NumberUtil.sub(String.valueOf(available_amount),fee);
        ed_entrust_amount.ed_input.setText(String.valueOf(all_count));
    };

    private void updateAssets(AccountInfo data) {
        if (data == null) {
            return;
        }
        List<AccountInfo.AssetsBean> list = data.getAssets();
        if (list == null || list.size() == 0) {
            return;
        }
        for (AccountInfo.AssetsBean item : list) {
            if (item.getSymbol().equalsIgnoreCase(token)) {
                available_amount = mPresenter.getAmountForUser(item.getAmount(),item.getFrozen_amount(),token);
                tv_available_amount.setText("可用 "+available_amount+token.toUpperCase());
            }
        }
    }
    private void updateValidatorAssets(List<ValidatorDelegationInfo> data) {
        if (data == null || data.size()<1) {
            return;
        }

        for (ValidatorDelegationInfo item : data) {
            if (item.getValidator().equalsIgnoreCase(validatorAddress)) {
                String asset = data.get(0).getBonded();
                available_amount = mPresenter.getAmountForUser(asset,"0",token);
                tv_available_amount.setText("可用 "+available_amount+token.toUpperCase());
            }
        }
    }

    /*public String getAmountForUser(String amount, String frozen_amount, String symbol) {
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());
        int decimals = bhToken!=null?bhToken.decimals:2;
        decimals = 0;
        double tmp = NumberUtil.sub(amount,frozen_amount);
        double displayAmount = NumberUtil.divide(String.valueOf(tmp), Math.pow(10,decimals)+"");

        //LogUtils.d("BHBalanceHelper==>:","displayAmount==="+displayAmount);
        //DecimalFormat format = new DecimalFormat();
        return NumberUtil.dispalyForUsertokenAmount(String.valueOf(displayAmount));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
