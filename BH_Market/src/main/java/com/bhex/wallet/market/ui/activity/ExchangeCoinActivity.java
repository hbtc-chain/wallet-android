package com.bhex.wallet.market.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHTokenMapping;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;
import com.bhex.wallet.market.adapter.ChooseTokenAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 16:37:06
 * 兑币页面
 */
@Route(path = ARouterConfig.Market_exchange_coin, name = "兑币")
public class ExchangeCoinActivity extends BaseActivity
        implements PasswordFragment.PasswordClickListener,
        ChooseTokenFragment.ChooseTokenListener {

    @Autowired(name = "symbol")
    String mIssueSymbol;


    private BHBalance mBhtBalance;
    private BHBalance mTokenBalance;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_issue_token)
    AppCompatTextView tv_issue_token;
    @BindView(R2.id.tv_target_token)
    AppCompatTextView tv_target_token;
    @BindView(R2.id.inp_amount)
    AppCompatEditText inp_amount;
    @BindView(R2.id.tv_token_name)
    AppCompatTextView tv_token_name;
    @BindView(R2.id.btn_exchange_action)
    AppCompatTextView btn_exchange_action;

    @BindView(R2.id.layout_left)
    LinearLayout layout_left;

    @BindView(R2.id.layout_right)
    LinearLayout layout_right;

    @BindView(R2.id.iv_issue)
    AppCompatImageView iv_issue;
    @BindView(R2.id.iv_target)
    AppCompatImageView iv_target;


    protected TransactionViewModel mTransactionViewModel;
    protected BalanceViewModel mBalanceViewModel;

    private BHTokenMapping mTokenMapping;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_coin;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        //mappingSymbol = MappingSymbolManager.getInstance().mappingSymbolMap.get(mSymbol.toUpperCase());
        tv_center_title.setText(getString(R.string.exchange_coin));
        mTokenMapping = CacheCenter.getInstance().getTokenMapCache().getTokenMapping(mIssueSymbol.toUpperCase());

        //
        tv_issue_token.setText(mTokenMapping.issue_symbol.toUpperCase());
        tv_target_token.setText(mTokenMapping.target_symbol.toUpperCase());
        tv_token_name.setText(mTokenMapping.issue_symbol.toUpperCase());
        BHBalanceHelper.loadTokenIcon(this,iv_issue,mTokenMapping.issue_symbol.toUpperCase());
        BHBalanceHelper.loadTokenIcon(this,iv_target,mTokenMapping.target_symbol.toUpperCase());
    }

    @Override
    protected void addEvent() {
        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        mTransactionViewModel.mutableLiveData.observe(this, ldm -> {
            updateTransferStatus(ldm);
        });

        mBalanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm -> {
            if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
                updateAssets((AccountInfo) ldm.getData());
            }
        });
        mBalanceViewModel.getAccountInfo(this, CacheStrategy.onlyRemote());
    }

    int view_left_left, view_right_left;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        view_left_left = layout_left.getLeft();
        view_right_left = layout_right.getLeft();
    }

    @OnClick({R2.id.btn_exchange_action, R2.id.iv_exchange,R2.id.tv_issue_token})
    public void onClickView(View view) {
        if (view.getId() == R.id.btn_exchange_action) {
            exchangeAction();
        } else if (view.getId() == R.id.iv_exchange) {
            exchangeAnimatorAction();
        } else if(view.getId() == R.id.tv_issue_token){
            ChooseTokenFragment.showDialog(getSupportFragmentManager(),ChooseTokenFragment.class.getName(),mIssueSymbol,this);
        }
    }

    //兑换
    private void exchangeAction() {
        String map_amount = inp_amount.getText().toString().trim();
        if (TextUtils.isEmpty(map_amount)) {
            ToastUtils.showToast(getString(R.string.please_input_exchange_amount));
            return;
        }

        if (mTokenBalance == null || Double.valueOf(mTokenBalance.amount) <= 0) {
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount) + mIssueSymbol.toUpperCase());
            return;
        }

        if (Double.valueOf(mTokenBalance.amount) < Double.valueOf(map_amount)) {
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount) + mIssueSymbol.toUpperCase());
            return;
        }

        if (mBhtBalance == null || Double.valueOf(mBhtBalance.amount) <= 0) {
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount) + BHConstants.BHT_TOKEN.toUpperCase());
            return;
        }

        PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                PasswordFragment.class.getName(),
                this, 0);
    }

    boolean flag = true;

    public void exchangeAnimatorAction() {
        leftAnimator();
        rightAnimator();
        flag = !flag;
    }

    private void leftAnimator() {
        int offset = view_right_left - view_left_left;
        //LogUtils.d("left====="+offset);
        ObjectAnimator left_animator = null;
        if (flag) {
            left_animator = ObjectAnimator.ofFloat(layout_left, "x", view_right_left);
        } else {
            left_animator = ObjectAnimator.ofFloat(layout_left, "x", view_left_left);
        }
        left_animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tv_token_name.setText(mTokenMapping.issue_symbol.toUpperCase());
                mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mIssueSymbol);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        left_animator.setInterpolator(new AccelerateDecelerateInterpolator());
        left_animator.setDuration(800);
        left_animator.start();
    }

    private void rightAnimator() {
        ObjectAnimator right_animator = null;
        if (flag) {
            right_animator = ObjectAnimator.ofFloat(layout_right, "x", view_left_left);
        } else {
            right_animator = ObjectAnimator.ofFloat(layout_right, "x", view_right_left);
        }
        right_animator.setInterpolator(new AccelerateDecelerateInterpolator());
        right_animator.setDuration(800);
        right_animator.start();
    }

    @Override
    public void confirmAction(String password, int position, int way) {
        String from_token = mTokenMapping.issue_symbol;
        String to_token = mTokenMapping.target_symbol;
        String map_amount = inp_amount.getText().toString().trim();
        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.createMappingSwap(to_token, from_token, map_amount,
                    BHConstants.BHT_DEFAULT_FEE, suquece, password);
            mTransactionViewModel.sendTransaction(this, bhSendTranscation);
            return 0;
        });
    }

    //更新兑换状态
    private void updateTransferStatus(LoadDataModel ldm) {
        if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
            ToastUtils.showToast(getString(R.string.transfer_in_success));
        } else {
            ToastUtils.showToast(getString(R.string.transfer_in_fail));
        }
    }

    //更新资产
    private void updateAssets(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return;
        }
        mBhtBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
        mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mIssueSymbol);
    }

    @Override
    public void clickTokenPosition(BHTokenMapping tokenMapping) {
        flag = true;
        mTokenMapping = tokenMapping;
        mIssueSymbol = mTokenMapping.issue_symbol;
        mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mIssueSymbol);
        tv_issue_token.setText(mTokenMapping.issue_symbol.toUpperCase());
        tv_target_token.setText(mTokenMapping.target_symbol.toUpperCase());
        tv_token_name.setText(mTokenMapping.issue_symbol.toUpperCase());
    }
}