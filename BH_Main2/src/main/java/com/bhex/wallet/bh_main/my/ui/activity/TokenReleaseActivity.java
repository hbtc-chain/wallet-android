package com.bhex.wallet.bh_main.my.ui.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.FormatTextWatcher;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.lib.uikit.widget.toast.BHToast;
import com.bhex.lib_qr.XQRCode;
import com.bhex.lib_qr.util.QRCodeAnalyzeUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.PathUtils;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTokenRlease;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.google.android.material.button.MaterialButton;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-5-16 16:12:35
 * 代币发行
 */
@Route(path = ARouterConfig.Token_Release, name = "代币发行申请")
public class TokenReleaseActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.inp_to_address)
    WithDrawInput inp_to_address;
    @BindView(R2.id.inp_token_name)
    InputView inp_token_name;
    @BindView(R2.id.inp_token_release_count)
    InputView inp_token_release_count;
    @BindView(R2.id.inp_token_decimals)
    InputView inp_token_decimals;
    @BindView(R2.id.tv_available_bht_amount)
    AppCompatTextView tv_available_bht_amount;
    @BindView(R2.id.inp_tx_fee)
    WithDrawInput inp_tx_fee;

    @BindView(R2.id.btn_apply)
    MaterialButton btn_apply;

    AccountInfo mAccountInfo;

    BHBalance bhtBalance;

    TransactionViewModel transactionViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_token_release;
    }

    @Override
    protected void initView() {

        tv_center_title.setText(getString(R.string.token_release));
        mAccountInfo = BHUserManager.getInstance().getAccountInfo();
        bhtBalance = MyHelper.getBthBalanceWithAccount(mAccountInfo);
        //文本型
        inp_to_address.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        inp_token_name.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        //设置数值型-输入框
        inp_token_release_count.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inp_token_decimals.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inp_tx_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters = {new InputFilter.LengthFilter(2)};
        inp_token_decimals.getEditText().setFilters(filters);
        //初始化可用手续费
        String available_bht_amount_str =  MyHelper.getAmountForUser(this,bhtBalance.amount,"0",bhtBalance.symbol);

        tv_available_bht_amount.setText(getString(R.string.available)+" "+available_bht_amount_str+bhtBalance.symbol.toUpperCase());

        //
        inp_to_address.btn_right_text.setVisibility(View.GONE);
        inp_to_address.iv_right.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)inp_to_address.getEditText().getLayoutParams();
        lp.addRule(RelativeLayout.LEFT_OF, com.bhex.wallet.balance.R.id.iv_right);
        inp_to_address.getEditText().setLayoutParams(lp);

        inp_token_release_count.getEditText().addTextChangedListener(new FormatTextWatcher(inp_token_release_count.getEditText()));

    }

    @Override
    protected void addEvent() {

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });
        
        inp_to_address.getEditText().addTextChangedListener(textWatcher);
        inp_token_name.getEditText().addTextChangedListener(textWatcher);
        inp_token_release_count.getEditText().addTextChangedListener(textWatcher);
        inp_token_decimals.getEditText().addTextChangedListener(textWatcher);

        //二维码扫描
        inp_to_address.iv_right.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterConfig.Commom_scan_qr).navigation(this, BHQrScanActivity.REQUEST_CODE);
        });
    }

    @OnClick({R2.id.btn_apply})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_apply) {
            tokenApplyAction();
        }
    }

    /**
     * 代币申请
     */
    private void tokenApplyAction() {
        String token_name = inp_token_name.getInputString();
        if(token_name.length()<2||token_name.length()>16){
            ToastUtils.showToast(getResources().getString(R.string.token_name_rule_tip));
            return;
        }

        String token_decimals = inp_token_decimals.getInputString();
        if(TextUtils.isDigitsOnly(token_decimals)&&Integer.valueOf(token_decimals)>18){
            ToastUtils.showToast(getResources().getString(R.string.min_decimal_more_18));
            return;
        }


        String formAddress = BHUserManager.getInstance().getCurrentBhWallet().address;
        String toAddress = inp_to_address.getInputString();
        String tokenName  = inp_token_name.getInputString();
        String tokenCount = inp_token_release_count.getInputString().replaceAll(" ","");
        String tokenDecimals = inp_token_decimals.getInputString();

        tokenCount = NumberUtil.mulExt(tokenCount,String.valueOf(Math.pow(10,18))).toString(10);

        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));

        String feeAmount = inp_tx_fee.getInputString();

        BHTokenRlease tokenRlease = new BHTokenRlease(
                formAddress,toAddress,tokenName,tokenCount,tokenDecimals);

        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.hrc20TokenRelease(tokenRlease,feeAmount,gasPrice, suquece);
            transactionViewModel.sendTransaction(this,bhSendTranscation);
            return 0;
        });
    }

    private SimpleTextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            boolean flag = true;
            if (TextUtils.isEmpty(inp_to_address.getInputString())) {
                flag = false;
            }

            if (TextUtils.isEmpty(inp_token_name.getInputString())) {
                flag = false;
            }

            //tokenName的格式[a-z0-9]
            if(!RegexUtil.checkLowerLetterAndNum(inp_token_name.getInputString())){
                flag = false;
            }

            if (TextUtils.isEmpty(inp_token_release_count.getInputString())) {
                flag = false;
            }

            if (TextUtils.isEmpty(inp_token_decimals.getInputString())) {
                flag = false;
            }
            btn_apply.setEnabled(flag);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == BHQrScanActivity.REQUEST_CODE ) {
            if(resultCode == RESULT_OK){
                //处理扫描结果（在界面上显示）
                String qrCode  = data.getExtras().getString(XQRCode.RESULT_DATA);
                inp_to_address.getEditText().setText(qrCode);
            }else if(resultCode == BHQrScanActivity.REQUEST_IMAGE){
                getAnalyzeQRCodeResult(data.getData());
            }
        }
    }

    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                inp_to_address.getEditText().setText(result);
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.showToast(getResources().getString(com.bhex.wallet.balance.R.string.encode_qr_fail));
            }
        });
    }

    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            BHToast.showDefault(this,getResources().getString(R.string.apply_success)).show();
            finish();
        }else{
            BHToast.showDefault(this,getResources().getString(R.string.apply_fail)).show();
        }
    }



}
