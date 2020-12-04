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
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R2.id.layout_index_0)
    LinearLayout layout_index_0;
    @BindView(R2.id.layout_index_5)
    LinearLayout layout_index_5;

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

    AppCompatTextView tv_switch_address;

    @BindView(R2.id.tv_paste)
    AppCompatTextView tv_paste;

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
        tv_switch_address = findViewById(R.id.btn_switch_address);
        BHToken token = CacheCenter.getInstance().getSymbolCache().getBHToken(symbol);

        if(token==null){
            return;
        }

        tv_center_title.setText(getResources().getString(R.string.make_collection)+token.name.toUpperCase());

        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();

        BHBalanceHelper.loadTokenIcon(this,iv_coin_ic,token.symbol);

        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(token.chain)){
            deposit_address = mCurrentWallet.address;
            tv_trusteeship_address.setText(BHConstants.HBTC.toUpperCase()+" "+getString(R.string.trusteeship_address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.blue_bg));
            layout_index_0.setVisibility(View.GONE);
            tv_switch_address.setVisibility(View.GONE);
        }else if( way == BH_BUSI_TYPE.链内转账.getIntValue()){
            deposit_address = mCurrentWallet.address;
            tv_trusteeship_address.setText(BHConstants.HBTC.toUpperCase()+" "+getString(R.string.trusteeship_address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.tranfer_in_inner_bg));
            ImmersionBar.with(this).statusBarColor(R.color.tranfer_in_inner_bg).statusBarDarkFont(false).barColor(com.bhex.network.R.color.tranfer_in_inner_bg).fitsSystemWindows(true).init();
            layout_index_0.setVisibility(View.GONE);
        } else if(way==BH_BUSI_TYPE.跨链转账.getIntValue()){
            BHBalance balance = BHBalanceHelper.getBHBalanceFromAccount(token.chain);
            deposit_address = balance.external_address;
            tv_trusteeship_address.setText(getString(R.string.cross_link_trusteeship_address));
            mRootView.setBackgroundColor(ColorUtil.getColor(this,R.color.tranfer_in_out_bg));
            ImmersionBar.with(this).statusBarColor(R.color.tranfer_in_out_bg).statusBarDarkFont(false).barColor(com.bhex.network.R.color.tranfer_in_out_bg).fitsSystemWindows(true).init();
            layout_index_0.setVisibility(View.VISIBLE);
            layout_index_5.setVisibility(View.VISIBLE);
            AppCompatTextView tv_tip_context = findViewById(R.id.tv_tip_context);
            BHToken bhToken = CacheCenter.getInstance().getSymbolCache().getBHToken(token.symbol);
            String deposit_threshold_str = String.format(getString(R.string.string_deposit_threshold),bhToken.deposit_threshold+bhToken.symbol.toUpperCase());
            tv_tip_context.setText(deposit_threshold_str);
        }

        Bitmap bitmap = QREncodUtil.createQRCode(deposit_address,
                PixelUtils.dp2px(this,180),PixelUtils.dp2px(this,180),null);
        iv_qr_code.setImageBitmap(bitmap);
        tv_transfer_in_address.setText(deposit_address);

    }

    @Override
    protected void addEvent() {
        /*BaseApplication.getMainHandler().postDelayed(()->{
            TransferInTipFragment.showDialog(
                    getSupportFragmentManager(),
                    TransferInTipFragment.class.getSimpleName(),way);
        },300);*/
        tv_switch_address.setOnClickListener(v -> {
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
        });
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
