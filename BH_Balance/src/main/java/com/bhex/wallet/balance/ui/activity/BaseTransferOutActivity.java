package com.bhex.wallet.balance.ui.activity;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
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



}
