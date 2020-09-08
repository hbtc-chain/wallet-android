package com.bhex.wallet.market.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
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
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;
import com.bhex.wallet.market.ui.model.MappingSymbol;
import com.bhex.wallet.market.ui.model.MappingSymbolManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 16:37:06
 * 兑币页面
 */
@Route(path = ARouterConfig.Market_exchange_coin, name = "兑币")
public class ExchangeCoinActivity extends BaseActivity implements PasswordFragment.PasswordClickListener {

    @Autowired(name = "symbol")
    String mSymbol;

    private BHBalance mBhtBalance;
    private BHBalance mTokenBalance;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_from_token)
    AppCompatTextView tv_from_token;
    @BindView(R2.id.tv_to_token)
    AppCompatTextView tv_to_token;
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


    protected TransactionViewModel mTransactionViewModel;
    protected BalanceViewModel mBalanceViewModel;
    //private String mFromToken = "BTC";
    //private String mToToken = "CBTC";
    private MappingSymbol mappingSymbol;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_coin;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mappingSymbol = MappingSymbolManager.getInstance().mappingSymbolMap.get(mSymbol.toUpperCase());
        tv_center_title.setText(getString(R.string.exchange_coin));
    }

    @Override
    protected void addEvent() {
        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        mTransactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });

        mBalanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });
        mBalanceViewModel.getAccountInfo(this, CacheStrategy.onlyRemote());
    }
    int view_left_left,view_right_left;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        view_left_left = layout_left.getLeft();
        view_right_left = layout_right.getLeft();
        LogUtils.d("ExchangeCoinActivity==>:","view_left_left=="+view_left_left+"=view_right_left="+view_right_left);
    }

    @OnClick({R2.id.btn_exchange_action,R2.id.iv_exchange})
    public void onClickView(View view){
        if(view.getId()==R.id.btn_exchange_action){
            exchangeAction();
        }else if(view.getId()==R.id.iv_exchange){
            exchangeAnimatorAction();
        }
    }

    //兑换
    private void exchangeAction() {
        String map_amount = inp_amount.getText().toString().trim();
        if(TextUtils.isEmpty(map_amount)){
            ToastUtils.showToast(getString(R.string.please_input_exchange_amount));
            return;
        }

        if(mTokenBalance==null || Double.valueOf(mTokenBalance.amount)<=0 ){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+mSymbol.toUpperCase());
            return;
        }

        if(Double.valueOf(mTokenBalance.amount)<Double.valueOf(map_amount)){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+mSymbol.toUpperCase());
            return;
        }

        if(mBhtBalance==null || Double.valueOf(mBhtBalance.amount)<=0){
            ToastUtils.showToast(getResources().getString(R.string.not_have_amount)+BHConstants.BHT_TOKEN.toUpperCase());
            return;
        }

        PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                PasswordFragment.class.getName(),
                this,0);
    }

    boolean flag = true;
    public void exchangeAnimatorAction(){
        leftAnimator();
        rightAnimator();
        flag = !flag;
    }

    private void leftAnimator(){
        int offset = view_right_left-view_left_left;
        LogUtils.d("left====="+offset);
        ObjectAnimator left_animator = null;
        if(flag){
            left_animator = ObjectAnimator.ofFloat(layout_left,"x",view_right_left);
        }else{
            left_animator = ObjectAnimator.ofFloat(layout_left,"x",view_left_left);
        }
        left_animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(flag){
                    //mFromToken = "BTC";
                    //mToToken = "CBTC";
                    mSymbol = "BTC";
                    mappingSymbol = MappingSymbolManager.getInstance().mappingSymbolMap.get(mSymbol);
                    tv_token_name.setText(mappingSymbol.fromToken);
                }else{
                    //mFromToken = "CBTC";
                    //mToToken = "CBTC";
                    mSymbol = "CBTC";
                    mappingSymbol = MappingSymbolManager.getInstance().mappingSymbolMap.get(mSymbol);
                    tv_token_name.setText(mappingSymbol.fromToken);
                }

                mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mSymbol);
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

    private void rightAnimator(){
        ObjectAnimator right_animator = null;
        if(flag){
            right_animator = ObjectAnimator.ofFloat(layout_right,"x",view_left_left);
        }else{
            right_animator = ObjectAnimator.ofFloat(layout_right,"x",view_right_left);
        }
        right_animator.setInterpolator(new AccelerateDecelerateInterpolator());
        right_animator.setDuration(800);
        right_animator.start();
    }

    @Override
    public void confirmAction(String password, int position, int way) {
        String from_token = mappingSymbol.fromToken;
        String to_token = mappingSymbol.issueToken;
        String map_amount = inp_amount.getText().toString().trim();
        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.createMappingSwap(to_token,from_token,map_amount,
                    BHConstants.BHT_DEFAULT_FEE,suquece,password);
            mTransactionViewModel.sendTransaction(this,bhSendTranscation);
            return 0;
        });
    }

    //更新兑换状态
    private void updateTransferStatus(LoadDataModel ldm) {
        if(ldm.loadingStatus== LoadingStatus.SUCCESS){
            ToastUtils.showToast("请求成功");
        }else{
            ToastUtils.showToast("请求失败");
        }
    }

    //更新资产
    private void updateAssets(AccountInfo accountInfo){
        if(accountInfo==null){
            return;
        }
        mBhtBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
        mTokenBalance = BHBalanceHelper.getBHBalanceFromAccount(mSymbol);
    }
}