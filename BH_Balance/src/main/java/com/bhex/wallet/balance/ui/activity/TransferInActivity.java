package com.bhex.wallet.balance.ui.activity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-4-7 10:29:12
 * 转入
 */

@Route(path= ARouterConfig.Balance_transfer_in)
public class TransferInActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.root_view)
    ConstraintLayout mRootView;

    @BindView(R2.id.iv_qr_code)
    AppCompatImageView iv_qr_code;

    @BindView(R2.id.tv_transfer_in_address)
    AppCompatTextView tv_transfer_in_address;

    @BindView(R2.id.iv_coin_ic)
    AppCompatImageView iv_coin_ic;


    @Autowired(name="balance")
    BHBalance balance;

    //收款类型 1链内 2 跨链
    @Autowired(name = "way")
    int way;

    BHWallet mCurrentWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_in;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.make_collection));

        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        Bitmap bitmap = QREncodUtil.createQRCode(mCurrentWallet.address,
                PixelUtils.dp2px(this,180),PixelUtils.dp2px(this,180),null);

        iv_qr_code.setImageBitmap(bitmap);

        tv_transfer_in_address.setText(mCurrentWallet.getAddress());

        //iv_coin_ic.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_eth));
        BHBalanceHelper.setTokenIcon(this,balance.symbol,iv_coin_ic);

        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)){
            ImmersionBar.with(this).statusBarColor(com.bhex.network.R.color.blue).statusBarDarkFont(false).barColor(com.bhex.network.R.color.blue).fitsSystemWindows(true).init();
        }else if(way==1){
            ImmersionBar.with(this).statusBarColor(com.bhex.network.R.color.color_269A99).statusBarDarkFont(false).barColor(com.bhex.network.R.color.color_269A99).fitsSystemWindows(true).init();
            mRootView.setBackgroundColor(ContextCompat.getColor(this,R.color.color_269A99));
        }else if(way==2){
            ImmersionBar.with(this).statusBarColor(com.bhex.network.R.color.dark_black).statusBarDarkFont(false).barColor(com.bhex.network.R.color.dark_black).fitsSystemWindows(true).init();
            mRootView.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_black));
        }
    }

    @Override
    protected void addEvent() {

    }

}
