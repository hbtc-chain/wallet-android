package com.bhex.wallet.balance.ui.viewhodler;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.ui.activity.ChainTokenActivity;
import com.bhex.wallet.balance.ui.fragment.AddressQRFragment;
import com.bhex.wallet.common.cache.SymbolCache;
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
    public RelativeLayout viewHolder;

    public AppCompatTextView tv_token_address;
    public AppCompatTextView tv_token_name;
    public AppCompatImageView iv_token_icon;
    public LinearLayout btn_genernate_address;
    public ETHViewHolder(ChainTokenActivity activity, RelativeLayout view, BHBalance balance){
        viewHolder = view;
        mContext = activity;
        this.mBalance = balance;
        tv_token_address = view.findViewById(R.id.tv_token_address);
        tv_token_name = view.findViewById(R.id.tv_token_name);

        iv_token_icon = view.findViewById(R.id.iv_token_icon);

        btn_genernate_address = view.findViewById(R.id.btn_genernate_address);
    }

    public void initViewContent( BHBalance balance){
        this.mBalance = balance;
        tv_token_name.setText(mBalance.name.toUpperCase());
        BHToken symbolToken = SymbolCache.getInstance().getBHToken(balance.symbol);
        if(symbolToken.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
            tv_token_address.setTag(BHUserManager.getInstance().getCurrentBhWallet().address);
            AssetHelper.proccessAddress(tv_token_address,BHUserManager.getInstance().getCurrentBhWallet().address);
            btn_genernate_address.setOnClickListener(this::showAdddressQRFragment);
        }else{
            //跨链地址
            String ex_address = balance.external_address;
            if(TextUtils.isEmpty(ex_address)){
                tv_token_address.setText(mContext.getString(R.string.genarate_cross_address));
                btn_genernate_address.setOnClickListener(v -> {
                    mContext.generateCrossLinkAddress();
                });
            }else{
                tv_token_address.setTag(ex_address);
                AssetHelper.proccessAddress(tv_token_address,ex_address);
                btn_genernate_address.setOnClickListener(this::showAdddressQRFragment);
            }
        }
        ImageLoaderUtil.loadImageView(mContext,symbolToken.logo,iv_token_icon,R.mipmap.ic_default_coin);


    }

    //显示地址Fragment
    private void showAdddressQRFragment(View view) {
        AddressQRFragment.showFragment(mContext.getSupportFragmentManager(),
                AddressQRFragment.class.getSimpleName(),
                mBalance.symbol.toUpperCase(),
                tv_token_address.getTag().toString());
    }


}
