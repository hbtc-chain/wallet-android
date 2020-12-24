package com.bhex.wallet.balance.ui.viewhodler;

import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.activity.ChainTokenActivity;
import com.bhex.wallet.balance.ui.fragment.AddressQRFragment;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.BHRawTransaction;
import com.bhex.wallet.common.tx.TxReq;
import com.bhex.wallet.common.ui.fragment.Password30Fragment;

import java.util.List;

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

        GradientDrawable drawable = ShapeUtils.getRoundRectDrawable(20,
                ColorUtil.getColor(mContext,R.color.dialog_fragment_divider_color));
        layout_token_address.setBackground(drawable);

        //Token-Logo
        AppCompatImageView iv_token_icon = viewHolder.findViewById(R.id.iv_token_icon);
        BHToken symbolToken = SymbolCache.getInstance().getBHToken(balance.symbol);
        ImageLoaderUtil.loadImageView(mContext,symbolToken.logo,iv_token_icon,R.mipmap.ic_default_coin);

        //hbc链地址
        AppCompatTextView tv_token_address_label = viewHolder.findViewById(R.id.tv_token_address_label);
        AppCompatTextView tv_token_address = viewHolder.findViewById(R.id.tv_token_address);

        if(symbolToken.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_token_address_label.setText(mContext.getResources().getString(R.string.hbtc_chain_address));
            AssetHelper.proccessAddress(tv_token_address,BHUserManager.getInstance().getCurrentBhWallet().address);
            layout_token_address.setOnClickListener(this::showAdddressQRFragment);
            tv_token_address.setTag(BHUserManager.getInstance().getCurrentBhWallet().address);
        }else{
            tv_token_address_label.setText(mContext.getResources().getString(R.string.crosslink_deposit_address));
            //跨链地址
            BHBalance chainBalance = BHBalanceHelper.getBHBalanceFromAccount(symbolToken.chain);
            if(!TextUtils.isEmpty(chainBalance.external_address)){
                AssetHelper.proccessAddress(tv_token_address,chainBalance.external_address);
                tv_token_address.setTextColor(ColorUtil.getColor(mContext,R.color.global_label_text_color));
                layout_token_address.setOnClickListener(this::showAdddressQRFragment);
                tv_token_address.setTag(chainBalance.external_address);
            }else{
                //创建跨链充值地址
                tv_token_address.setText(mContext.getString(R.string.create_crosslink_deposit_address));
                tv_token_address.setTextColor(ColorUtil.getColor(mContext,R.color.highlight_text_color));
                layout_token_address.setOnClickListener(v->{
                    ARouter.getInstance()
                            .build(ARouterConfig.Balance.Balance_cross_address)
                            .withString("symbol",balance.symbol).navigation();
                });
            }
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
