package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-4-7 12:04:20
 * 提币
 */

@Route(path = ARouterConfig.Balance_transfer_out)
public class TransferOutActivity extends BaseActivity  {

    @Autowired(name = "balance")
    BHBalance balance;

    @Autowired(name = "bhtBalance")
    BHBalance bhtBalance;

    @Autowired(name="way")
    int way;


    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_withdraw_address)
    AppCompatTextView tv_withdraw_address;
    @BindView(R2.id.tv_transfer_amount)
    AppCompatTextView tv_transfer_amount;

    @BindView(R2.id.layout_transfer_out_tips)
    MaterialCardView layout_transfer_out_tips;
    @BindView(R2.id.tv_to_address)
    WithDrawInput tv_to_address;
    @BindView(R2.id.tv_available_amount)
    AppCompatTextView tv_available_amount;
    @BindView(R2.id.ed_transfer_amount)
    WithDrawInput ed_transfer_amount;
    @BindView(R2.id.tv_reach_amount)
    WithDrawInput tv_reach_amount;
    @BindView(R2.id.et_input_fee)
    WithDrawInput et_input_fee;
    @BindView(R2.id.tv_available_bht_amount)
    AppCompatTextView tv_available_bht_amount;

    @BindView(R2.id.tv_transfer_out_tips_1)
    AppCompatTextView tv_transfer_out_tips_1;
    @BindView(R2.id.tv_transfer_out_tips_2)
    AppCompatTextView tv_transfer_out_tips_2;
    @BindView(R2.id.tv_transfer_out_tips_3)
    AppCompatTextView tv_transfer_out_tips_3;

    @BindView(R2.id.btn_drawwith_coin)
    MaterialButton btn_drawwith_coin;

    private double available_amount;

    private TransactionViewModel transactionViewModel;

    private BHWallet mCurrentBhWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_out;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);

        initTokenView();

        String available_amount_str =  BHBalanceHelper.getAmountForUser(this,balance.amount,balance.frozen_amount,balance.symbol);
        available_amount = Double.valueOf(available_amount_str);
        tv_available_amount.setText(available_amount_str + balance.symbol.toUpperCase());

        tv_reach_amount.btn_right_text.setText( balance.symbol.toUpperCase());

        ed_transfer_amount.btn_right_text.setOnClickListener(allWithDrawListener);
        tv_reach_amount.ed_input.setEnabled(false);

        ed_transfer_amount.ed_input.addTextChangedListener(withDrawAmountTextWatcher);

        //初始化可用手续费
        String available_bht_amount_str =  BHBalanceHelper.getAmountForUser(this,bhtBalance.amount,bhtBalance.frozen_amount,balance.symbol);

        tv_available_bht_amount.setText(available_bht_amount_str+bhtBalance.symbol.toUpperCase());


        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });
    }

    private void initTokenView() {
        if(balance.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.transfer));
            tv_withdraw_address.setText(getResources().getString(R.string.transfer_address));
            tv_transfer_amount.setText(getResources().getString(R.string.transfer_amount));
            layout_transfer_out_tips.setVisibility(View.GONE);
            btn_drawwith_coin.setText(getResources().getString(R.string.transfer));
        }else{
            layout_transfer_out_tips.setVisibility(View.VISIBLE);
            tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.draw_coin));
            if(way==BHConstants.INNER_LINK){
                tv_transfer_out_tips_1.setText(getResources().getString(R.string.linkinner_withdraw_tip_1));
                tv_transfer_out_tips_2.setText(getResources().getString(R.string.linkinner_withdraw_tip_2));
                tv_transfer_out_tips_3.setText(getResources().getString(R.string.linkinner_withdraw_tip_3));
            }else if(way==BHConstants.CROSS_LINK){
                tv_transfer_out_tips_1.setText(getResources().getString(R.string.crosslink_withdraw_tip_1));
                tv_transfer_out_tips_2.setText(getResources().getString(R.string.crosslink_withdraw_tip_2));
                tv_transfer_out_tips_3.setText(getResources().getString(R.string.crosslink_withdraw_tip_3));
            }
        }
    }

    /**
     * 手续费
     * @param ldm
     */
    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){

        }else{

        }
    }

    @Override
    protected void addEvent() {
        mCurrentBhWallet = BHUserManager.getInstance().getCurrentBhWallet();
    }


    public View.OnClickListener allWithDrawListener = v -> {
        ed_transfer_amount.ed_input.setText(String.valueOf(available_amount));

    };

    public SimpleTextWatcher withDrawAmountTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String withDrawAmonut = ed_transfer_amount.ed_input.getText().toString().trim();
            if(!TextUtils.isEmpty(withDrawAmonut)){
                tv_reach_amount.ed_input.setText(withDrawAmonut);
            }else{
                tv_reach_amount.ed_input.setText("0");
            }
        }
    };


    @OnClick({R2.id.btn_drawwith_coin})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_drawwith_coin){
            sendTranscation();
        }
    }

    /**
     * 发送交易
     */
    private void sendTranscation(){
        String hexPK = CryptoUtil.decryptPK(mCurrentBhWallet.privateKey,mCurrentBhWallet.password);
        //String from = "BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK";
        //String to = "BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ";
        String from_address = mCurrentBhWallet.getAddress();
        String to_address = mCurrentBhWallet.getAddress();
        BigInteger gasPrice = BigInteger.valueOf ((long)(BHConstants.BHT_GAS_PRICE));
        String withDrawAmount = ed_transfer_amount.ed_input.getText().toString().toString();
        String feeAmount = et_input_fee.ed_input.getText().toString().toString();

        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.transfer(hexPK,from_address,to_address,withDrawAmount,feeAmount,
                    gasPrice,"test memo",null,suquece,balance.symbol);

            transactionViewModel.sendTransaction(this,bhSendTranscation);
            return 0;
        });
    }


}
