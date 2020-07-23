package com.bhex.wallet.balance.ui.activity;

import android.text.InputType;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/15
 * Time: 11:01
 */
public abstract class BaseTransferOutActivity<P extends BasePresenter> extends BaseActivity<P> {

    protected TransactionViewModel transactionViewModel;

    protected BHWallet mCurrentBhWallet;

    protected  BHToken bhToken;

    protected double available_amount;


    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_withdraw_address)
    AppCompatTextView tv_withdraw_address;
    @BindView(R2.id.tv_transfer_amount)
    AppCompatTextView tv_transfer_amount;
    @BindView(R2.id.layout_transfer_out_tips)
    MaterialCardView layout_transfer_out_tips;

    @BindView(R2.id.layout_withdraw_fee)
    RelativeLayout layout_withdraw_fee;

    @BindView(R2.id.tv_withdraw_fee_amount)
    AppCompatTextView tv_withdraw_fee_amount;

    @BindView(R2.id.tv_to_address)
    WithDrawInput tv_to_address;
    @BindView(R2.id.tv_available_amount)
    AppCompatTextView tv_available_amount;
    @BindView(R2.id.ed_transfer_amount)
    WithDrawInput ed_transfer_amount;
    @BindView(R2.id.tv_reach_amount)
    WithDrawInput tv_reach_amount;
    @BindView(R2.id.et_tx_fee)
    WithDrawInput et_tx_fee;
    @BindView(R2.id.et_withdraw_fee)
    WithDrawInput et_withdraw_fee;
    @BindView(R2.id.tv_withdraw_fee)
    AppCompatTextView tv_withdraw_fee;
    @BindView(R2.id.tv_reach)
    AppCompatTextView tv_reach;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_out;
    }

    protected void initTokenView() {
        ed_transfer_amount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //ed_transfer_amount.getEditText().addTextChangedListener(new FormatTextWatcher(ed_transfer_amount.getEditText()));

        et_tx_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_withdraw_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_tx_fee.btn_right_text.setText(BHConstants.BHT_TOKEN.toUpperCase());

        et_tx_fee.getEditText().setText(BHConstants.BHT_DEFAULT_FEE);
        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(getBalance().chain)){
            tv_center_title.setText(getBalance().symbol.toUpperCase()+getResources().getString(R.string.transfer));
            tv_withdraw_address.setText(getResources().getString(R.string.transfer_address));
            tv_transfer_amount.setText(getResources().getString(R.string.transfer_amount));
            layout_transfer_out_tips.setVisibility(View.GONE);
            btn_drawwith_coin.setText(getResources().getString(R.string.transfer));
            ed_transfer_amount.btn_right_text.setText(getResources().getString(R.string.all_transfer_amount));
            et_withdraw_fee.setVisibility(View.GONE);
            tv_withdraw_fee.setVisibility(View.GONE);
            tv_reach.setVisibility(View.GONE);
            tv_reach_amount.setVisibility(View.GONE);
            layout_withdraw_fee.setVisibility(View.GONE);

        }else if(getWay()==BHConstants.INNER_LINK){
            layout_transfer_out_tips.setVisibility(View.VISIBLE);
            tv_center_title.setText(getBalance().symbol.toUpperCase()+getResources().getString(R.string.transfer));
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

            layout_withdraw_fee.setVisibility(View.GONE);


        }else if(getWay()==BHConstants.CROSS_LINK){
            layout_transfer_out_tips.setVisibility(View.VISIBLE);
            tv_center_title.setText(getBalance().symbol.toUpperCase()+getResources().getString(R.string.draw_coin));
            tv_transfer_out_tips_1.setText(getResources().getString(R.string.crosslink_withdraw_tip_1));
            tv_transfer_out_tips_2.setText(getResources().getString(R.string.crosslink_withdraw_tip_2));
            tv_transfer_out_tips_3.setText(getResources().getString(R.string.crosslink_withdraw_tip_3));

            tv_reach.setVisibility(View.GONE);
            tv_reach_amount.setVisibility(View.GONE);

            layout_withdraw_fee.setVisibility(View.VISIBLE);

            //提币手续费单位
            et_withdraw_fee.btn_right_text.setText(getBalance().chain.toUpperCase());
            bhToken = SymbolCache.getInstance().getBHToken(getBalance().symbol.toLowerCase());
            et_withdraw_fee.getEditText().setText(bhToken.withdrawal_fee);

        }

    }


    public abstract BHBalance getBalance();

    public abstract int getWay();
}
