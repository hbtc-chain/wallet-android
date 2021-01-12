package com.bhex.wallet.balance.ui.viewhodler;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.FileUtils;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.QRCodeEncoder;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.activity.TransferInActivity;
import com.bhex.wallet.balance.ui.fragment.DepositTipsFragment;
import com.bhex.wallet.common.cache.SymbolCache;
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

    public void initContnetView(String symbol, int way){
        this.mSymbol = symbol;
        this.mWay = way;
        AppCompatImageView iv_qr_code = mRootView.findViewById(R.id.iv_qr_code);
        AppCompatTextView tv_account_label = mRootView.findViewById(R.id.tv_account_label);

        tv_token_address = mRootView.findViewById(R.id.tv_token_address);
        AppCompatTextView tv_center_title = mRootView.findViewById(R.id.tv_center_title);
        AppCompatTextView tv_friend_tip_1 = mRootView.findViewById(R.id.tv_friend_tip_1);
        AppCompatTextView tv_friend_tip_2 = mRootView.findViewById(R.id.tv_friend_tip_2);
        AppCompatImageView iv_coin_ic = mRootView.findViewById(R.id.iv_coin_ic);
        //AppCompatTextView tv_wallet_name = mRootView.findViewById(R.id.tv_wallet_name);

        //
        AppCompatImageView iv_token_icon = mRootView.findViewById(R.id.iv_token_icon);
        AppCompatTextView tv_token_name = mRootView.findViewById(R.id.tv_token_name);

        LinearLayout layout_select_token = mRootView.findViewById(R.id.layout_select_token);
        FrameLayout layout_ring = mRootView.findViewById(R.id.layout_ring);

        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        BHToken token = SymbolCache.getInstance().getBHToken(mSymbol);
        BHBalance chainBalance = BHBalanceHelper.getBHBalanceFromAccount(token.chain);
        //BHToken chainToken = SymbolCache.getInstance().getBHToken(token.chain);
        BHToken hbcToken = SymbolCache.getInstance().getBHToken(BHConstants.BHT_TOKEN);
        //设置币种logo

        //地址 以及背景色
        String deposit_address = "";

        if(mWay == BH_BUSI_TYPE.链内转账.getIntValue()){
            deposit_address = bhWallet.address;
        }else{
            deposit_address = chainBalance.external_address;
        }

        if(TextUtils.isEmpty(deposit_address)){
            //mWay = BH_BUSI_TYPE.链内转账.getIntValue();
            initContnetView(mSymbol,BH_BUSI_TYPE.链内转账.getIntValue());
            return;
        }
        CardView layout_content = mRootView.findViewById(R.id.layout_content);

        if(token.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
            tv_account_label.setText(mActivity.getResources().getString(R.string.hbtc_chain_address));
            layout_select_token.setVisibility(View.GONE);
            layout_ring.setVisibility(View.VISIBLE);
            ImageLoaderUtil.loadImageView(mActivity,token.logo,iv_coin_ic,R.mipmap.ic_default_coin);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)layout_content.getLayoutParams();
            layoutParams.setMargins(PixelUtils.dp2px(mActivity,24),PixelUtils.dp2px(mActivity,28),PixelUtils.dp2px(mActivity,24),0);
            tv_friend_tip_1.setText(mActivity.getString(R.string.hbtc_transfer_in_tip));
            tv_friend_tip_2.setVisibility(View.GONE);
        }else{
            tv_account_label.setText(mActivity.getResources().getString(R.string.crosslink_deposit_address));
            layout_select_token.setVisibility(View.VISIBLE);
            layout_ring.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)layout_content.getLayoutParams();
            layoutParams.setMargins(PixelUtils.dp2px(mActivity,24),PixelUtils.dp2px(mActivity,8),PixelUtils.dp2px(mActivity,24),0);
            ImageLoaderUtil.loadImageView(mActivity,token.logo,iv_token_icon,R.mipmap.ic_default_coin);
            tv_token_name.setText(token.name.toUpperCase());
            tv_friend_tip_2.setVisibility(View.VISIBLE);
            //跨链充值最小费用
            String v_amount_str =  String.format(mActivity.getString(R.string.string_deposit_threshold),token.name.toUpperCase(),token.deposit_threshold+" "+token.name.toUpperCase());
            tv_friend_tip_1.setText(v_amount_str);
            tv_friend_tip_2.setVisibility(View.VISIBLE);
            //跨链充值入账费用
            String v_amount_str2 =  String.format(mActivity.getString(R.string.string_deposit_enter_fee),token.collect_fee+" "+token.chain.toUpperCase());
            tv_friend_tip_2.setText(v_amount_str2);
        }

        if(mWay==BH_BUSI_TYPE.链内转账.getIntValue()){
            tv_center_title.setText(mActivity.getResources().getString(R.string.transfer_in));
        }else{
            tv_center_title.setText(mActivity.getResources().getString(R.string.cross_deposit));
        }
        //地址
        tv_token_address.setText(deposit_address);
        //二维码
        /*Bitmap bitmap = QREncodUtil.createQRCode(deposit_address,
                PixelUtils.dp2px(mActivity,160),PixelUtils.dp2px(mActivity,160),null);*/

        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(deposit_address,PixelUtils.dp2px(mActivity,210), ColorUtil.getColor(mActivity,android.R.color.black));

        iv_qr_code.setImageBitmap(bitmap);

        //复制地址
        mRootView.findViewById(R.id.tv_paste_address).setOnClickListener(v -> {
            /*AddressQRFragment.showFragment(mActivity.getSupportFragmentManager(),
                    AddressQRFragment.class.getSimpleName(),
                    mSymbol,
                    tv_token_address.getText().toString());*/
            if (TextUtils.isEmpty(tv_token_address.getText())) {
                return;
            }

            ToolUtils.copyText(tv_token_address.getText().toString(), mActivity);
            ToastUtils.showToast(mActivity.getString(R.string.copyed));
        });

        //下载二维码

        //底部logo
        /*Drawable drawableStrart = ColorUtil.getDrawable(mActivity,R.mipmap.ic_bluehelix,R.color.white);
        tv_wallet_name.setCompoundDrawablesWithIntrinsicBounds(drawableStrart,
                null, null, null);*/
        mRootView.findViewById(R.id.btn_save_qr).setOnClickListener(v->{
            requestPermissions();
        });
        mRootView.findViewById(R.id.tv_friend_tip_2).setOnClickListener(v->{
            //充值提示
            DepositTipsFragment.newInstance().show(mActivity.getSupportFragmentManager(),DepositTipsFragment.class.getName());
        });
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

        /*Bitmap bitmap = QREncodUtil.createQRCode(tv_token_address.getText().toString(),
                PixelUtils.dp2px(mActivity,160),PixelUtils.dp2px(mActivity,160),null);*/

        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(tv_token_address.getText().toString(),PixelUtils.dp2px(mActivity,160),
                ColorUtil.getColor(mActivity,android.R.color.black));

        Observable.create(emitter -> {
            boolean flag = FileUtils.saveImageToGallery(mActivity,bitmap,tv_token_address.getText().toString()+".png");
            emitter.onNext(flag);
            emitter.onComplete();
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mActivity)))
                .subscribe(observer);
    }
}
