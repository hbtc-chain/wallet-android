package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib_qr.XQRCode;
import com.bhex.lib_qr.util.QRCodeAnalyzeUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.indicator.OnSampleSeekChangeListener;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.PathUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.presenter.TransferOutPresenter;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.warkiz.widget.SeekParams;

import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;

import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-4-7 12:04:20
 * 提币
 */

@Route(path = ARouterConfig.Balance_transfer_out)
public class TransferOutActivity extends BaseTransferOutActivity<TransferOutPresenter>
        implements PasswordFragment.PasswordClickListener{

    @Autowired(name = "balance")
    BHBalance balance;

    @Autowired(name = "bhtBalance")
    BHBalance bhtBalance;

    @Autowired(name="way")
    int way;

    //跨链提币可用余额
    BHBalance feeBalance;
    //BHToken bhToken;
    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        initTokenView();
        bhToken = SymbolCache.getInstance().getBHToken(balance.symbol.toLowerCase());
        String available_amount_str =  BHBalanceHelper.getAmountForUser(this,balance.amount,"0",balance.symbol);
        available_amount = Double.valueOf(available_amount_str);
        tv_available_amount.setText(getString(R.string.available)+" "+available_amount_str + balance.symbol.toUpperCase());

        tv_reach_amount.btn_right_text.setText( balance.symbol.toUpperCase());

        ed_transfer_amount.btn_right_text.setOnClickListener(allWithDrawListener);
        tv_reach_amount.getEditText().setEnabled(false);

        //初始化可用手续费
        if(bhtBalance!=null){
            String available_bht_amount_str =  BHBalanceHelper.getAmountForUser(this,bhtBalance.amount,"0",bhtBalance.symbol);
            tv_available_bht_amount.setText(getString(R.string.available)+" "+available_bht_amount_str+bhtBalance.symbol.toUpperCase());
        }
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });
        tv_to_address.btn_right_text.setVisibility(View.GONE);
        tv_to_address.iv_right.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)tv_to_address.getEditText().getLayoutParams();
        lp.addRule(RelativeLayout.LEFT_OF,R.id.iv_right);
        tv_to_address.getEditText().setLayoutParams(lp);

        feeBalance = BHBalanceHelper.getBHBalanceFromAccount(getBalance().chain);
        String available_fee_amount_str =  BHBalanceHelper.getAmountForUser(this,feeBalance.amount,"0",feeBalance.symbol);
        tv_withdraw_fee_amount.setText(getString(R.string.available)+" "+available_fee_amount_str+feeBalance.symbol.toUpperCase());
    }


    @Override
    protected void initPresenter() {
        mPresenter = new TransferOutPresenter(this);
    }

    /**
     * 手续费
     * @param ldm
     */
    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            ToastUtils.showToast(getResources().getString(R.string.transfer_in_success));
            EventBus.getDefault().post(new TransctionEvent());
            finish();
        }else{
            ToastUtils.showToast(getResources().getString(R.string.transfer_in_fail));
        }
    }

    @Override
    protected void addEvent() {
        mCurrentBhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        //二维码扫描
        tv_to_address.iv_right.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterConfig.Commom_scan_qr).navigation(this, BHQrScanActivity.REQUEST_CODE);
        });

        sb_tx_fee.setOnSeekChangeListener(new OnSampleSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                super.onSeeking(seekParams);
                et_tx_fee.setInputString(seekParams.progressFloat+"");
            }
        });

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
            refreshLayout.finishRefresh();
        });
    }

    public View.OnClickListener allWithDrawListener = v -> {
        if(way==BHConstants.CROSS_LINK){
            String all_count = NumberUtil.sub(String.valueOf(available_amount),bhToken.withdrawal_fee);
            all_count = Double.valueOf(all_count)<0?"0":all_count;
            ed_transfer_amount.getEditText().setText(all_count);
        }else if(balance.symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            //交易手续费
            String all_count = NumberUtil.sub(String.valueOf(available_amount),et_tx_fee.getInputString());
            all_count = Double.valueOf(all_count)<0?"0":all_count;
            ed_transfer_amount.getEditText().setText(all_count);
        }
    };

    @OnClick({R2.id.btn_drawwith_coin})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_drawwith_coin){
            if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)){
                sendTransfer();
            }else if(way==BH_BUSI_TYPE.跨链转账.getIntValue()){
                crossLinkWithDraw();
            }else if(way==BH_BUSI_TYPE.链内转账.getIntValue()){
                sendTransfer();
            }
        }
    }

    private void updateAssets(AccountInfo accountInfo) {
        BHUserManager.getInstance().setAccountInfo(accountInfo);
        balance = BHBalanceHelper.getBHBalanceFromAccount(balance.symbol);
        String available_amount_str =  BHBalanceHelper.getAmountForUser(this,balance.amount,"0",balance.symbol);
        available_amount = Double.valueOf(available_amount_str);
        tv_available_amount.setText(getString(R.string.available)+" "+available_amount_str + balance.symbol.toUpperCase());
    }

    /**
     * 发送交易
     */
    private void sendTransfer(){
        boolean flag = mPresenter.checklinkInnerTransfer(tv_to_address.getInputString(),
                ed_transfer_amount.getInputStringTrim(),
                String.valueOf(available_amount),et_tx_fee.getInputStringTrim());

        if(flag){
            PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                    PasswordFragment.class.getName(),
                    this,0);
        }
    }

    /**
     * 跨链提币
     */
    private void crossLinkWithDraw(){
        BHToken bhToken = SymbolCache.getInstance().getBHToken(balance.symbol);

        boolean flag = mPresenter.checklinkOutterTransfer(tv_to_address.getEditText().getText().toString(),
                ed_transfer_amount.getInputStringTrim(),
                String.valueOf(available_amount),
                et_tx_fee.getInputStringTrim(),
                et_withdraw_fee.getInputStringTrim(),
                bhToken.withdrawal_fee,feeBalance
        );


        if(flag){
            PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                    PasswordFragment.class.getName(),
                    this,0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == BHQrScanActivity.REQUEST_CODE ) {
            if(resultCode == RESULT_OK){
                //处理扫描结果（在界面上显示）
                String qrCode  = data.getExtras().getString(XQRCode.RESULT_DATA);
                tv_to_address.getEditText().setText(qrCode);
            }else if(resultCode == BHQrScanActivity.REQUEST_IMAGE){
                getAnalyzeQRCodeResult(data.getData());
            }

        }
    }


    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                tv_to_address.getEditText().setText(result);
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.showToast(getResources().getString(R.string.encode_qr_fail));
            }
        });
    }

    /**
     * 密码对话框回调
     * @param password
     * @param position
     */
    @Override
    public void confirmAction(String password, int position,int way) {

        //String from_address = mCurrentBhWallet.getAddress();
        String to_address = tv_to_address.getInputString();
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        String memo = input_memo.getInputString();
        //链内
        if(way==BH_BUSI_TYPE.链内转账.getIntValue()){
            String withDrawAmount = ed_transfer_amount.getInputStringTrim();
            String feeAmount = et_tx_fee.getInputString();

            BHTransactionManager.loadSuquece(suquece -> {
                BHSendTranscation bhSendTranscation = BHTransactionManager.transfer(to_address,withDrawAmount,feeAmount,
                        gasPrice,password,suquece,memo,balance.symbol);
                transactionViewModel.sendTransaction(this,bhSendTranscation);
                return 0;
            });

        }else if(way== BH_BUSI_TYPE.跨链转账.getIntValue()){//跨链
            //提币数量
            String withDrawAmount = ed_transfer_amount.getInputStringTrim();
            //交易手续费
            String feeAmount = et_tx_fee.getInputString();
            //提币手续费
            String withDrawFeeAmount = et_withdraw_fee.getInputString();

            BHTransactionManager.loadSuquece(suquece -> {
                BHSendTranscation bhSendTranscation = BHTransactionManager.crossLinkTransfer(to_address,withDrawAmount,feeAmount,
                        gasPrice,withDrawFeeAmount,password,suquece,memo,balance.symbol);

                transactionViewModel.sendTransaction(this,bhSendTranscation);
                return 0;
            });
        }
    }

    @Override
    public BHBalance getBalance() {
        return balance;
    }

    @Override
    public int getWay() {
        return way;
    }
}
