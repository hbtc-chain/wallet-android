package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.FileUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.ui.fragment.ChooseTokenFragment;
import com.bhex.wallet.balance.ui.viewhodler.TransferInVH;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.gyf.immersionbar.ImmersionBar;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author gongdongyang
 * 2020-4-7 10:29:12
 * 转入
 */

@Route(path= ARouterConfig.Balance.Balance_transfer_in)
public class TransferInActivity extends BaseActivity {

    @Autowired(name="symbol")
    String  symbol;
    //收款类型 1链内 2 跨链
    @Autowired(name = "way")
    int way;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    //BHWallet mCurrentWallet;

    BHToken mSymbolToken;



    public TransferInVH mTransferInVH;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_in_ext;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mSymbolToken = SymbolCache.getInstance().getBHToken(symbol);
        findViewById(R.id.layout_ring).bringToFront();
        mTransferInVH = new TransferInVH(findViewById(R.id.root_view),this,symbol,way);
        mTransferInVH.initContnetView(symbol);
    }

    @Override
    protected void addEvent() {
        findViewById(R.id.tv_center_title).setOnClickListener(this::chooseTokenAction);
        //findViewById(R.id.btn_save_qr).setOnClickListener(this::saveQRAction);
        /*tv_switch_address.setOnClickListener(v -> {
            ToastUtils.showToast("way==="+way);
            if(way== BH_BUSI_TYPE.链内转账.getIntValue()){
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way", BH_BUSI_TYPE.跨链转账.getIntValue())
                        .navigation();
                finish();
            }else if(way== BH_BUSI_TYPE.跨链转账.getIntValue()){
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                        .navigation();
                finish();
            }
        });*/
    }




    @Override
    protected int getStatusColorValue() {
        return BHConstants.STATUS_COLOR_TRANS;
    }

    //选择币种
    private void chooseTokenAction(View view) {
        ChooseTokenFragment fragment = ChooseTokenFragment.showFragment(symbol,String.valueOf(way),chooseTokenListener);
        fragment.show(getSupportFragmentManager(),ChooseTokenFragment.class.getName());

    }

    private ChooseTokenFragment.OnChooseTokenListener chooseTokenListener = new ChooseTokenFragment.OnChooseTokenListener() {
        @Override
        public void onChooseClickListener(String symbol, int position) {
            //更新token
            TransferInActivity.this.symbol = symbol;
            //updateViewContent();
            mTransferInVH.initContnetView(symbol);
        }
    };

    /*@OnClick({R2.id.tv_paste})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.tv_paste){
            ToolUtils.copyText(deposit_address,this);
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }
    }*/

}
