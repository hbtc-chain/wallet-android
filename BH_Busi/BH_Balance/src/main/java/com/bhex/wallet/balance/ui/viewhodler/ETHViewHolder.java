package com.bhex.wallet.balance.ui.viewhodler;

import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.ViewUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.activity.ChainTokenActivity;
import com.bhex.wallet.balance.ui.fragment.AddressQRFragment;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.helper.BHWalletHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.SequenceManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;

public class ETHViewHolder {

    public ChainTokenActivity mContext;
    public BHBalance mBalance;
    public LinearLayout viewHolder;

    public AppCompatTextView tv_token_address;
    public AppCompatTextView tv_token_name;
    public AppCompatImageView iv_token_icon;
    public LinearLayout btn_genernate_address;
    public ETHViewHolder(ChainTokenActivity activity, LinearLayout view, BHBalance balance){
        viewHolder = view;
        mContext = activity;
        this.mBalance = balance;

        /*tv_token_address = view.findViewById(R.id.tv_token_address);
        tv_token_name = view.findViewById(R.id.tv_token_name);
        iv_token_icon = view.findViewById(R.id.iv_token_icon);
        btn_genernate_address = view.findViewById(R.id.btn_genernate_address);*/
    }

    public void initViewContent( BHBalance balance){
        this.mBalance = balance;
        FrameLayout layout_token_address = viewHolder.findViewById(R.id.layout_token_address);

        GradientDrawable drawable = ShapeUtils.getRoundRectDrawable(PixelUtils.dp2px(mContext,20),
                ColorUtil.getColor(mContext,R.color.address_label_bg_color));
        layout_token_address.setBackground(drawable);

        //Token-Logo
        AppCompatImageView iv_token_icon = viewHolder.findViewById(R.id.iv_token_icon);
        BHToken symbolToken = SymbolCache.getInstance().getBHToken(balance.symbol);
        if(symbolToken!=null){
            ImageLoaderUtil.loadImageView(mContext,symbolToken.logo,iv_token_icon,R.mipmap.ic_default_coin);
        }
        setTokenAddress(mBalance.symbol);
    }

    //
    public void setTokenAddress(String symbol) {
        FrameLayout layout_token_address = viewHolder.findViewById(R.id.layout_token_address);
        BHToken symbolToken = SymbolCache.getInstance().getBHToken(symbol);
        //hbc链地址
        AppCompatTextView tv_token_address_label = viewHolder.findViewById(R.id.tv_token_address_label);
        tv_token_address = viewHolder.findViewById(R.id.tv_token_address);
        if(symbolToken==null){
            return;
        }

        if( symbolToken.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_token_address_label.setText(mContext.getResources().getString(R.string.hbtc_chain_address));
            BHWalletHelper.proccessAddress(tv_token_address,BHUserManager.getInstance().getCurrentBhWallet().address);
            layout_token_address.setOnClickListener(this::showAdddressQRFragment);
            tv_token_address.setTag(BHUserManager.getInstance().getCurrentBhWallet().address);
            return;
        }


        //跨链地址
        tv_token_address_label.setText(mContext.getResources().getString(R.string.crosslink_deposit_address));
        //跨链地址
        BHBalance chainBalance = BHBalanceHelper.getBHBalanceFromAccount(symbolToken.chain);
        //
        if(!TextUtils.isEmpty(chainBalance.external_address)){
            BHWalletHelper.proccessAddress(tv_token_address,chainBalance.external_address);
            tv_token_address.setTextColor(ColorUtil.getColor(mContext,R.color.global_label_text_color));
            layout_token_address.setOnClickListener(this::showAdddressQRFragment);
            tv_token_address.setTag(chainBalance.external_address);
        }else if(!TextUtils.isEmpty(SequenceManager.getInstance().getAddressStatus())){
            //创建跨链充值地址
            tv_token_address.setText(mContext.getString(R.string.cross_address_generatoring));
            ViewUtil.getListenInfo(layout_token_address);
        }else{
            tv_token_address.setText(mContext.getString(R.string.create_crosslink_deposit_address));
            tv_token_address.setTextColor(ColorUtil.getColor(mContext,R.color.highlight_text_color));
            layout_token_address.setOnClickListener(v->{
                ARouter.getInstance()
                        .build(ARouterConfig.Balance.Balance_cross_address)
                        .withString("symbol",symbol).navigation();
            });
        }
    }

    //显示地址Fragment
    private void showAdddressQRFragment(View view) {
        if(tv_token_address.getTag()==null){
            return;
        }
        AddressQRFragment.showFragment(mContext.getSupportFragmentManager(),
                AddressQRFragment.class.getSimpleName(),
                mBalance.symbol.toUpperCase(),
                tv_token_address.getTag().toString());
    }



}
