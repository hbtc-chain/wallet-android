package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib_qr.XQRCode;
import com.bhex.lib_qr.util.QRCodeAnalyzeUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PathUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.presenter.TransferOutPresenter;
import com.bhex.wallet.balance.ui.fragment.ChooseTokenFragment;
import com.bhex.wallet.balance.ui.viewhodler.TransferOutViewHolder;
import com.bhex.wallet.balance.viewmodel.TokenViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.BHRawTransaction;
import com.bhex.wallet.common.tx.TxReq;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.bhex.wallet.common.ui.fragment.Password30PFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;
import java.util.List;

/**
 * @author gongdongyang
 * 转账、提币代码重构
 */
@Route(path = ARouterConfig.Balance.Balance_transfer_out)
public class TransferOutActivity extends BaseActivity<TransferOutPresenter> {

    @Autowired(name="symbol")
    String m_symbol;

    @Autowired(name="way")
    int m_transferout_way;

    int def_dailog_count = 0;

    TransferOutViewHolder mTransferOutViewHolder;

    BalanceViewModel mBalanceViewModel;

    TransactionViewModel mTransactionViewModel;

    TokenViewModel mTokenViewModel;

    private SmartRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_out;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setEnableLoadMore(false);
        mTransferOutViewHolder = new TransferOutViewHolder(this,findViewById(R.id.root_view),m_symbol,m_transferout_way);
        getPresenter().mTransferViewHolder = mTransferOutViewHolder;
        //
        findViewById(R.id.btn_drawwith_coin).setOnClickListener(this::onSubmitAction);
        findViewById(R.id.btn_transfer_out_token).setOnClickListener(this::chooseTokenAction);
    }


    @Override
    protected void addEvent() {
        mBalanceViewModel = ViewModelProviders.of(MainActivityManager._instance.mainActivity).get(BalanceViewModel.class).build(MainActivityManager._instance.mainActivity);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
            refreshFinish();
        });

        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        mTransactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });

        mTokenViewModel = ViewModelProviders.of(this).get(TokenViewModel.class);
        mTokenViewModel.queryLiveData.observe(this,ldm->{
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                mTransferOutViewHolder.withDrawFeeToken = (BHToken) ldm.getData();
            }
            refreshFinish();
        });

        if(mTransferOutViewHolder.tranferToken==null){
            return;
        }
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            def_dailog_count = 0;
            mBalanceViewModel.getAccountInfo(this, CacheStrategy.onlyRemote());
            mTokenViewModel.queryToken(this,mTransferOutViewHolder.tranferToken.symbol);
        });
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TransferOutPresenter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == BHQrScanActivity.REQUEST_CODE ) {
            if(resultCode == RESULT_OK){
                //处理扫描结果（在界面上显示）
                String qrCode  = data.getExtras().getString(XQRCode.RESULT_DATA);
                mTransferOutViewHolder.input_to_address.setInputString(qrCode);
            }else if(resultCode == BHQrScanActivity.REQUEST_IMAGE){
                getAnalyzeQRCodeResult(data.getData());
            }
        }
    }

    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                mTransferOutViewHolder.input_to_address.setInputString(result);
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.showToast(getResources().getString(R.string.encode_qr_fail));
            }
        });
    }

    //更新资产
    private void updateAssets(AccountInfo accountInfo) {
        mTransferOutViewHolder.updateBalance();
    }

    //提交
    private void onSubmitAction(View view) {
        if(mTransferOutViewHolder.tranferToken==null){
            return;
        }
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(mTransferOutViewHolder.tranferToken.chain)){
            linkInnerTransfer();
        }else if(m_transferout_way== BH_BUSI_TYPE.跨链转账.getIntValue()){
            crossLinkWithDraw();
        }else if(m_transferout_way==BH_BUSI_TYPE.链内转账.getIntValue()){
            linkInnerTransfer();
        }
    }

    private void linkInnerTransfer(){
        boolean flag = mPresenter.checklinkInnerTransfer();
        if(flag){
            Password30PFragment.showPasswordDialog(
                    getSupportFragmentManager(),Password30PFragment.class.getName(),
                    this::confirmAction,0,true);
        }
    }

    private void crossLinkWithDraw(){
        boolean flag = mPresenter.checkCrossLinkTransfer( );
        if(flag){
            Password30PFragment.showPasswordDialog(getSupportFragmentManager(),
                    Password30PFragment.class.getName(),
                    this::confirmAction,0,true);
        }
    }

    /**
     * 密码对话框回调
     * @param password
     * @param position
     */
    public void confirmAction(String password, int position,int verifyPwdWay) {
        //String from_address = mCurrentBhWallet.getAddress();
        String to_address = mTransferOutViewHolder.input_to_address.getInputString();
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        //链内
        if(m_transferout_way==BH_BUSI_TYPE.链内转账.getIntValue()){
            String withDrawAmount = mTransferOutViewHolder.input_transfer_amount.getText().toString().trim();
            String feeAmount = mTransferOutViewHolder.input_tx_fee.getInputString();
            //创建转账信息
            List<TxReq.TxMsg> tx_msg_list = BHRawTransaction.createTransferMsg(to_address,withDrawAmount,mTransferOutViewHolder.tranferToken.symbol);
            mTransactionViewModel.transferInnerExt(this,password,feeAmount,tx_msg_list);

        }else if(m_transferout_way== BH_BUSI_TYPE.跨链转账.getIntValue()){//跨链
            //提币数量
            String withDrawAmount = mTransferOutViewHolder.input_transfer_amount.getText().toString().trim();
            //交易手续费
            String feeAmount = mTransferOutViewHolder.input_tx_fee.getInputString();
            //提币手续费
            String withDrawFeeAmount = mTransferOutViewHolder.input_withdraw_fee.getInputString();
            //创建提币信息
            List<TxReq.TxMsg> tx_msg_list = BHRawTransaction.createwithDrawWMsg(to_address,withDrawAmount,withDrawFeeAmount,mTransferOutViewHolder.tranferToken.symbol);
            mTransactionViewModel.transferInnerExt(this,password,feeAmount,tx_msg_list);
        }
    }

    //更新状态
    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            ToastUtils.showToast(getResources().getString(R.string.transfer_in_success));
            EventBus.getDefault().post(new TransctionEvent());
            finish();
        }else{
            ToastUtils.showToast(getResources().getString(R.string.transfer_in_fail));
        }
    }

    public void refreshFinish(){
        def_dailog_count++;
        if(def_dailog_count==2){
            mRefreshLayout.finishRefresh();
        }
    }

    //
    private void chooseTokenAction(View view) {
        ChooseTokenFragment fragment = ChooseTokenFragment.showFragment(m_symbol,String.valueOf(m_transferout_way),chooseTokenListener);
        fragment.show(getSupportFragmentManager(),ChooseTokenFragment.class.getName());
    }

    private ChooseTokenFragment.OnChooseTokenListener chooseTokenListener = new ChooseTokenFragment.OnChooseTokenListener() {
        @Override
        public void onChooseClickListener(String symbol, int position) {
            //更新token
            TransferOutActivity.this.m_symbol = symbol;
            //updateViewContent();
            mTransferOutViewHolder.upateViewContent(m_symbol);
        }
    };
}
