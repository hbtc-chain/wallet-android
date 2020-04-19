package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.event.TransctionEvent;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 生成跨链地址
 * 2020-4-8 09:40:35
 */

@Route(path = ARouterConfig.Balance_cross_address)
public class GenerateAddressActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Autowired(name = "bhtBalance")
    BHBalance bhtBalance;

    @Autowired(name = "balance")
    BHBalance balance;

    @BindView(R2.id.tv_available_bht_amount)
    AppCompatTextView tv_available_bht_amount;

    @BindView(R2.id.ed_fee)
    WithDrawInput ed_fee;

    @BindView(R2.id.btn_crosslink_address)
    MaterialButton btn_crosslink_address;

    BHWallet mCurrentWallet;

    double available_bht_amount = 0;

    private TransactionViewModel transactionViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_generate_address;
    }


    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(getResources().getString(R.string.genarate_cross_address));

        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();

        String available_amount_str = BHBalanceHelper.getAmountForUser(this, bhtBalance.amount, "0", bhtBalance.symbol);
        available_bht_amount = Double.valueOf(available_amount_str);
        tv_available_bht_amount.setText(available_amount_str + bhtBalance.symbol.toUpperCase());

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm->{
            updateGenerateAddress(ldm);
        });

        ed_fee.ed_input.addTextChangedListener(simpleTextWatcher);
        ed_fee.ed_input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    protected void addEvent() {

    }


    @OnClick({R2.id.btn_crosslink_address})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_crosslink_address){
            generateCrossLinkAddress();
        }
    }

    SimpleTextWatcher simpleTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String text = ed_fee.ed_input.getText().toString().trim();
            if(RegexUtil.checkNumeric(text)){
                btn_crosslink_address.setEnabled(true);
                btn_crosslink_address.setBackgroundColor(ContextCompat.getColor(GenerateAddressActivity.this,R.color.blue));
            }else{
                btn_crosslink_address.setEnabled(false);
                btn_crosslink_address.setBackgroundColor(ContextCompat.getColor(GenerateAddressActivity.this,R.color.gray_E7ECF4));
            }
        }
    };

    /**
     * 生成跨链地址
     */
    private void generateCrossLinkAddress() {

        String hexPK = CryptoUtil.decryptPK(mCurrentWallet.privateKey,mCurrentWallet.password);

        //String from = "BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK";

        //String to = "BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ";
        String from_address = mCurrentWallet.getAddress();
        String to_address = mCurrentWallet.getAddress();
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        //String withDrawAmount = ed_transfer_amount.ed_input.getText().toString().toString();
        String feeAmount = ed_fee.ed_input.getText().toString().toString();

        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.crossLinkAddress(hexPK,from_address,to_address,feeAmount,
                    gasPrice,BHConstants.BH_MEMO,null,suquece,balance.symbol);

            transactionViewModel.sendTransaction(this,bhSendTranscation);
            return 0;
        });
    }

    /**
     * 更新地址状态
     * @param ldm
     */
    private void updateGenerateAddress(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            ToastUtils.showToast("地址生成中...");
            EventBus.getDefault().post(new TransctionEvent());
            finish();
        }
    }
}
