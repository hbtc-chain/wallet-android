package com.bhex.wallet.balance.ui.activity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.fragment.TransferInTipFragment;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-4-7 10:29:12
 * 转入
 */

@Route(path= ARouterConfig.Balance_transfer_in)
public class TransferInActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.layout_index_0)
    LinearLayout layout_index_0;

    @BindView(R2.id.root_view)
    ConstraintLayout mRootView;

    @BindView(R2.id.iv_qr_code)
    AppCompatImageView iv_qr_code;

    @BindView(R2.id.tv_transfer_in_address)
    AppCompatTextView tv_transfer_in_address;

    @BindView(R2.id.iv_coin_ic)
    AppCompatImageView iv_coin_ic;

    @BindView(R2.id.tv_trusteeship_address)
    AppCompatTextView tv_trusteeship_address;

    @BindView(R2.id.tv_paste)
    AppCompatTextView tv_paste;

    @Autowired(name="balance")
    BHBalance balance;

    //收款类型 1链内 2 跨链
    @Autowired(name = "way")
    int way;

    BHWallet mCurrentWallet;

    //二维码地址
    String deposit_address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_in;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(balance.symbol.toUpperCase()+" "+getResources().getString(R.string.make_collection));

        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();

        BHBalanceHelper.setTokenIcon(this,balance.symbol,iv_coin_ic);

        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(balance.chain)){
            deposit_address = mCurrentWallet.address;
            tv_trusteeship_address.setText(BHConstants.BHT_TOKEN.toUpperCase()+getResources().getString(R.string.trusteeship_address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.blue_bg));
            layout_index_0.setVisibility(View.GONE);
        }else if( way == BH_BUSI_TYPE.链内转账.getIntValue()){
            deposit_address = mCurrentWallet.address;
            tv_trusteeship_address.setText(BHConstants.BHT_TOKEN.toUpperCase()+getResources().getString(R.string.trusteeship_address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.tranfer_in_inner_bg));
            ImmersionBar.with(this).statusBarColor(R.color.tranfer_in_inner_bg).statusBarDarkFont(false).barColor(com.bhex.network.R.color.tranfer_in_inner_bg).fitsSystemWindows(true).init();
            layout_index_0.setVisibility(View.GONE);
        } else if(way==BH_BUSI_TYPE.跨链转账.getIntValue()){
            deposit_address = balance.external_address;
            tv_trusteeship_address.setText(balance.symbol.toUpperCase()+getResources().getString(R.string.address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.tranfer_in_out_bg));
            ImmersionBar.with(this).statusBarColor(R.color.tranfer_in_out_bg).statusBarDarkFont(false).barColor(com.bhex.network.R.color.tranfer_in_out_bg).fitsSystemWindows(true).init();
            layout_index_0.setVisibility(View.VISIBLE);
        }

        Bitmap bitmap = QREncodUtil.createQRCode(deposit_address,
                PixelUtils.dp2px(this,180),PixelUtils.dp2px(this,180),null);
        iv_qr_code.setImageBitmap(bitmap);
        tv_transfer_in_address.setText(deposit_address);

    }

    @Override
    protected void addEvent() {
        BaseApplication.getMainHandler().postDelayed(()->{
            TransferInTipFragment.showDialog(
                    getSupportFragmentManager(),
                    TransferInTipFragment.class.getSimpleName(),way);
        },300);
    }

    @Override
    protected int getStatusColorValue() {
        return BHConstants.STATUS_COLOR_BLUE;

    }

    @OnClick({R2.id.tv_paste})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.tv_paste){
            ToolUtils.copyText(deposit_address,this);
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }
    }

}
