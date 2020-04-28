package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib_qr.XQRCode;
import com.bhex.lib_qr.util.QRCodeAnalyzeUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.PathUtils;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.presenter.TransferOutPresenter;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;

import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;

import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-4-7 12:04:20
 * 提币
 */

@Route(path = ARouterConfig.Balance_transfer_out)
public class TransferOutActivity extends BaseTransferOutActivity<TransferOutPresenter> implements PasswordFragment.PasswordClickListener{

    @Autowired(name = "balance")
    BHBalance balance;

    @Autowired(name = "bhtBalance")
    BHBalance bhtBalance;

    @Autowired(name="way")
    int way;

    private double available_amount;
    BHToken bhToken;
    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        initTokenView();
        tv_to_address.ed_input.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        String available_amount_str =  BHBalanceHelper.getAmountForUser(this,balance.amount,"0",balance.symbol);
        available_amount = Double.valueOf(available_amount_str);
        tv_available_amount.setText("可用 "+available_amount_str + balance.symbol.toUpperCase());

        tv_reach_amount.btn_right_text.setText( balance.symbol.toUpperCase());


        ed_transfer_amount.btn_right_text.setOnClickListener(allWithDrawListener);
        tv_reach_amount.ed_input.setEnabled(false);

        //ed_transfer_amount.ed_input.addTextChangedListener(checkTextWatcher);
        //et_tx_fee.ed_input.addTextChangedListener(checkTextWatcher);

        //初始化可用手续费
        String available_bht_amount_str =  BHBalanceHelper.getAmountForUser(this,bhtBalance.amount,"0",bhtBalance.symbol);

        tv_available_bht_amount.setText("可用 "+available_bht_amount_str+bhtBalance.symbol.toUpperCase());


        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });
        tv_to_address.btn_right_text.setVisibility(View.GONE);
        tv_to_address.iv_right.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)tv_to_address.ed_input.getLayoutParams();
        lp.addRule(RelativeLayout.LEFT_OF,R.id.iv_right);
        tv_to_address.ed_input.setLayoutParams(lp);

    }




    private void initTokenView() {
        ed_transfer_amount.ed_input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_tx_fee.ed_input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)){
            tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.transfer));
            tv_withdraw_address.setText(getResources().getString(R.string.transfer_address));
            tv_transfer_amount.setText(getResources().getString(R.string.transfer_amount));
            layout_transfer_out_tips.setVisibility(View.GONE);
            btn_drawwith_coin.setText(getResources().getString(R.string.transfer));
            ed_transfer_amount.btn_right_text.setText(getResources().getString(R.string.all_transfer_amount));
            et_withdraw_fee.setVisibility(View.GONE);
            tv_withdraw_fee.setVisibility(View.GONE);
            tv_reach.setVisibility(View.GONE);
            tv_reach_amount.setVisibility(View.GONE);

        }else if(way==BHConstants.INNER_LINK){
            layout_transfer_out_tips.setVisibility(View.VISIBLE);
            tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.transfer));
            tv_withdraw_address.setText(getResources().getString(R.string.transfer_address));
            tv_transfer_amount.setText(getResources().getString(R.string.transfer_amount));
            btn_drawwith_coin.setText(getResources().getString(R.string.transfer));
            ed_transfer_amount.btn_right_text.setText(getResources().getString(R.string.all_transfer_amount));

            tv_transfer_out_tips_1.setText(getResources().getString(R.string.linkinner_withdraw_tip_1));
            tv_transfer_out_tips_2.setText(getResources().getString(R.string.linkinner_withdraw_tip_2));
            tv_transfer_out_tips_3.setText(getResources().getString(R.string.linkinner_withdraw_tip_3));

            et_withdraw_fee.setVisibility(View.GONE);
            tv_withdraw_fee.setVisibility(View.GONE);

            tv_reach.setVisibility(View.GONE);
            tv_reach_amount.setVisibility(View.GONE);

        }else if(way==BHConstants.CROSS_LINK){
            layout_transfer_out_tips.setVisibility(View.VISIBLE);
            tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.draw_coin));
            tv_transfer_out_tips_1.setText(getResources().getString(R.string.crosslink_withdraw_tip_1));
            tv_transfer_out_tips_2.setText(getResources().getString(R.string.crosslink_withdraw_tip_2));
            tv_transfer_out_tips_3.setText(getResources().getString(R.string.crosslink_withdraw_tip_3));

            tv_reach.setVisibility(View.GONE);
            tv_reach_amount.setVisibility(View.GONE);

            //提币手续费单位
            et_withdraw_fee.btn_right_text.setText(balance.symbol.toUpperCase());
             bhToken = SymbolCache.getInstance().getBHToken(balance.symbol.toLowerCase());
            //double withdraw_fee = NumberUtil.divide(bhToken.withdrawal_fee,String.valueOf(Math.pow(10,bhToken.decimals)),10);
            //double withdraw_fee = Double.
            et_withdraw_fee.ed_input.setText(bhToken.withdrawal_fee);
        }
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
        //EventBus.getDefault().register(this);
        //二维码扫描
        tv_to_address.iv_right.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterConfig.Commom_scan_qr).navigation(this, BHQrScanActivity.REQUEST_CODE);
        });
    }

    public View.OnClickListener allWithDrawListener = v -> {
        if(way==BHConstants.CROSS_LINK){
            double all_count = NumberUtil.sub(String.valueOf(available_amount),bhToken.withdrawal_fee);
            all_count = all_count<0?0:all_count;
            ed_transfer_amount.ed_input.setText(String.valueOf(all_count));
        }else if(balance.symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            double all_count = NumberUtil.sub(String.valueOf(available_amount),"2");
            all_count = all_count<0?0:all_count;
            ed_transfer_amount.ed_input.setText(String.valueOf(all_count));
        }
    };

    /*public SimpleTextWatcher checkTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String transfer_amount = ed_transfer_amount.ed_input.getText().toString().trim();
            String tx_fee_amount = et_tx_fee.ed_input.getText().toString().trim();
            if(RegexUtil.checkNumeric(transfer_amount) && RegexUtil.checkNumeric(tx_fee_amount)){
                btn_drawwith_coin.setEnabled(true);
                btn_drawwith_coin.setBackgroundColor(ContextCompat.getColor(TransferOutActivity.this,R.color.blue));
            }else{
                btn_drawwith_coin.setEnabled(false);
                btn_drawwith_coin.setBackgroundColor(ContextCompat.getColor(TransferOutActivity.this,R.color.gray_E7ECF4));

            }

        }
    };*/


    @OnClick({R2.id.btn_drawwith_coin})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_drawwith_coin){
            if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)){
                sendTransfer();
            }else if(way==2){
                crossLinkWithDraw();
            }else if(way==1){
                sendTransfer();
            }

        }
    }

    /**
     * 发送交易
     */
    private void sendTransfer(){
        boolean flag = mPresenter.checklinkInnerTransfer(tv_to_address.ed_input.getText().toString(),
                ed_transfer_amount.ed_input.getText().toString(),
                String.valueOf(available_amount),
                et_tx_fee.ed_input.getText().toString().trim()
        );
        if(!flag){
            return;
        }

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
        boolean flag = mPresenter.checklinkOutterTransfer(tv_to_address.ed_input.getText().toString(),
                ed_transfer_amount.ed_input.getText().toString(),
                String.valueOf(available_amount),
                et_tx_fee.ed_input.getText().toString().trim(),
                et_withdraw_fee.ed_input.getText().toString().trim()
        );
        if(!flag){
            return;
        }

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
                tv_to_address.ed_input.setText(qrCode);
            }else if(resultCode == BHQrScanActivity.REQUEST_IMAGE){
                getAnalyzeQRCodeResult(data.getData());
            }

        }
    }


    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                tv_to_address.ed_input.setText(result);
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
    public void confirmAction(String password, int position) {

        String from_address = mCurrentBhWallet.getAddress();
        String to_address = tv_to_address.ed_input.getText().toString().trim();
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        if(way==1){
            String withDrawAmount = ed_transfer_amount.ed_input.getText().toString();
            String feeAmount = et_tx_fee.ed_input.getText().toString();

            BHTransactionManager.loadSuquece(suquece -> {
                BHSendTranscation bhSendTranscation = BHTransactionManager.transfer(from_address,to_address,withDrawAmount,feeAmount,
                        gasPrice,BHConstants.BH_MEMO,null,suquece,balance.symbol);
                transactionViewModel.sendTransaction(this,bhSendTranscation);
                return 0;
            });
        }else if(way==2){
            String withDrawAmount = ed_transfer_amount.ed_input.getText().toString();
            String feeAmount = et_tx_fee.ed_input.getText().toString();

            BHTransactionManager.loadSuquece(suquece -> {
                BHSendTranscation bhSendTranscation = BHTransactionManager.crossLinkTransfer(from_address,to_address,withDrawAmount,feeAmount,
                        gasPrice,BHConstants.BH_MEMO,null,suquece,balance.symbol);

                transactionViewModel.sendTransaction(this,bhSendTranscation);
                return 0;
            });
        }
    }
}
