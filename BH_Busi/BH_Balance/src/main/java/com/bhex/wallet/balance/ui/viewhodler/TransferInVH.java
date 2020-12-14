package com.bhex.wallet.balance.ui.viewhodler;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.FileUtil;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.activity.TransferInActivity;
import com.bhex.wallet.balance.ui.fragment.AddressQRFragment;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;

/**
 * @author gongdongyang
 * 2020-12-6 22:59:29
 */
public class TransferInVH {
    public View mRootView;
    public TransferInActivity mActivity;
    public String mSymbol;
    public int mWay;

    public AppCompatTextView tv_token_address;

    public TransferInVH(View mRootView, TransferInActivity mActivity, String mSymbol,int way) {
        this.mRootView = mRootView;
        this.mActivity = mActivity;
        this.mSymbol = mSymbol;
        this.mWay = way;
    }

    public void initContnetView(String symbol){
        this.mSymbol = symbol;

        AppCompatImageView iv_qr_code = mRootView.findViewById(R.id.iv_qr_code);
        AppCompatTextView tv_account_label = mRootView.findViewById(R.id.tv_account_label);
        AppCompatTextView tv_account_label_1 = mRootView.findViewById(R.id.tv_account_label_1);

        tv_token_address = mRootView.findViewById(R.id.tv_token_address);
        AppCompatTextView tv_center_title = mRootView.findViewById(R.id.tv_center_title);
        AppCompatTextView tv_friend_tips = mRootView.findViewById(R.id.tv_friend_tips);

        AppCompatImageView iv_coin_ic = mRootView.findViewById(R.id.iv_coin_ic);

        AppCompatTextView tv_wallet_name = mRootView.findViewById(R.id.tv_wallet_name);

        //
        AppCompatImageView iv_coin_hbtc = mRootView.findViewById(R.id.iv_coin_hbtc);

        AppCompatImageView iv_switch_address_icon = mRootView.findViewById(R.id.iv_switch_address_icon);
        //箭头
        Drawable arrowDrawable = ColorUtil.getDrawable(mActivity,R.mipmap.ic_gray_arrow,R.color.highlight_text_color);
        iv_switch_address_icon.setImageDrawable(arrowDrawable);

        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        //BHBalance balance = BHBalanceHelper.getBHBalanceFromAccount(mSymbol);
        BHToken token = SymbolCache.getInstance().getBHToken(mSymbol);
        BHBalance chainBalance= BHBalanceHelper.getBHBalanceFromAccount(token.chain);
        BHToken hbcToken = SymbolCache.getInstance().getBHToken(BHConstants.BHT_TOKEN);
        //设置币种logo
        //BHBalanceHelper.loadTokenIcon(mSymbol,iv_coin_ic,token.symbol);
        ImageLoaderUtil.loadImageView(mActivity,token.logo,iv_coin_ic,R.mipmap.ic_default_coin);
        //设置跨链地址
        if(!token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN) && mWay == BH_BUSI_TYPE.链内转账.getIntValue()){
            iv_coin_hbtc.setVisibility(View.VISIBLE);
            ImageLoaderUtil.loadImageView(mActivity,hbcToken.logo,iv_coin_hbtc,R.mipmap.ic_default_coin);
        }else{
            iv_coin_hbtc.setVisibility(View.GONE);
        }

        //地址切换
        AppCompatTextView btn_switch_address = mRootView.findViewById(R.id.btn_switch_address);
        if(!token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN) && mWay == BH_BUSI_TYPE.链内转账.getIntValue()){
            btn_switch_address.setText(mActivity.getResources().getString(R.string.switch_cross_link_address));
            btn_switch_address.setVisibility(View.VISIBLE);
            iv_switch_address_icon.setVisibility(View.VISIBLE);
        }else if(!token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN) && mWay == BH_BUSI_TYPE.跨链转账.getIntValue()){
            btn_switch_address.setText(mActivity.getResources().getString(R.string.switch_hbtc_address));
            btn_switch_address.setVisibility(View.VISIBLE);
            iv_switch_address_icon.setVisibility(View.VISIBLE);
        }else if(token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
            btn_switch_address.setVisibility(View.INVISIBLE);
            iv_switch_address_icon.setVisibility(View.INVISIBLE);
        }

        btn_switch_address.setOnClickListener(this::switchAddress);

        //设置顶部箭头
        Drawable drawableRight = ColorUtil.getDrawable(mActivity,R.mipmap.ic_arrow_token_d,R.color.white);
        tv_center_title.setCompoundDrawablesWithIntrinsicBounds(null,null, drawableRight, null);
        //地址 以及背景色
        String deposit_address = "";

        if(mWay == BH_BUSI_TYPE.链内转账.getIntValue()){
            deposit_address = bhWallet.address;
            //tv_account_label.setText(BHConstants.HBTC.toUpperCase()+mActivity.getResources().getString(R.string.trusteeship_address));
            //mRootView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.tranfer_in_inner_bg));
        }else{
            deposit_address = chainBalance.external_address;
            //tv_account_label.setText(mActivity.getResources().getString(R.string.cross_link_trusteeship_address));
            //mRootView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.tranfer_in_out_bg));
        }

        if(token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
            tv_account_label.setText(BHConstants.HBTC.toUpperCase()+mActivity.getResources().getString(R.string.trusteeship_address));
            mRootView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.tranfer_in_inner_bg));
        }else{
            tv_account_label.setText(mActivity.getResources().getString(R.string.cross_link_trusteeship_address));
            mRootView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.tranfer_in_out_bg));
        }

        tv_account_label_1.setText(token.name.toUpperCase()+mActivity.getResources().getString(R.string.trusteeship_address));
        if(mWay==BH_BUSI_TYPE.链内转账.getIntValue()){
            tv_center_title.setText(mActivity.getResources().getString(R.string.transfer_in)+" "+token.name.toUpperCase());
        }else{
            tv_center_title.setText(mActivity.getResources().getString(R.string.deposit)+" "+token.name.toUpperCase());
        }
        //地址
        tv_token_address.setText(deposit_address);
        //二维码
        Bitmap bitmap = QREncodUtil.createQRCode(deposit_address,
                PixelUtils.dp2px(mActivity,160),PixelUtils.dp2px(mActivity,160),null);
        iv_qr_code.setImageBitmap(bitmap);
        //数量
        if(!TextUtils.isEmpty(token.deposit_threshold) && Double.valueOf(token.deposit_threshold)>0){
            String v_amount_str = mActivity.getString(R.string.string_deposit_threshold_2)+" "+token.deposit_threshold+token.name.toUpperCase();
            tv_friend_tips.setText(v_amount_str);
            tv_friend_tips.setVisibility(View.VISIBLE);
        }else{
            tv_friend_tips.setVisibility(View.INVISIBLE);
        }


        //复制地址
        mRootView.findViewById(R.id.tv_paste_address).setOnClickListener(v -> {
            AddressQRFragment.showFragment(mActivity.getSupportFragmentManager(),
                    AddressQRFragment.class.getSimpleName(),
                    mSymbol,
                    tv_token_address.getText().toString());
        });

        //下载二维码

        //底部logo
        Drawable drawableStrart = ColorUtil.getDrawable(mActivity,R.mipmap.ic_bluehelix,R.color.white);
        tv_wallet_name.setCompoundDrawablesWithIntrinsicBounds(drawableStrart,
                null, null, null);



        mRootView.findViewById(R.id.btn_save_qr).setOnClickListener(v->{
            requestPermissions();
        });
    }

    private void switchAddress(View view) {
        //切换地址
        //1.当前在链内切换到链外
        if(mWay==BH_BUSI_TYPE.链内转账.getIntValue()){
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                    .withString("symbol", mSymbol)
                    .withInt("way", BH_BUSI_TYPE.跨链转账.getIntValue())
                    .navigation();
            mActivity.finish();
        }else if(mWay==BH_BUSI_TYPE.跨链转账.getIntValue()){
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                    .withString("symbol", mSymbol)
                    .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                    .navigation();
            mActivity.finish();
        }
    }

    //保存二维码
    private void requestPermissions() {
        //获取保存文件权限
        final RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        saveQRAction();
                        //startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogUtils.d(permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        LogUtils.d(permission.name + " is denied.");
                    }
                });


    }

    public void saveQRAction(){
        BHProgressObserver observer = new BHProgressObserver<Boolean>(mActivity) {
            @Override
            protected void onSuccess(Boolean o) {
                super.onSuccess(o);
                ToastUtils.showToast(mActivity.getResources().getString(R.string.save_success));
            }
        };

        Bitmap bitmap = QREncodUtil.createQRCode(tv_token_address.getText().toString(),
                PixelUtils.dp2px(mActivity,160),PixelUtils.dp2px(mActivity,160),null);

        Observable.create(emitter -> {
            boolean flag = FileUtil.saveImageToGallery(mActivity,bitmap,tv_token_address.getText().toString()+".png");
            emitter.onNext(flag);
            emitter.onComplete();
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mActivity)))
                .subscribe(observer);
    }
}
