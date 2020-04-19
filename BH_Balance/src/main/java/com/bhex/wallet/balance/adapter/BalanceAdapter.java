package com.bhex.wallet.balance.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/18
 * Time: 0:18
 */
public class BalanceAdapter extends BaseQuickAdapter<BHBalance, BaseViewHolder> {

    public BalanceAdapter(int layoutResId, @Nullable List<BHBalance> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable BHBalance balanceItem) {
        AppCompatImageView iv = viewHolder.getView(R.id.iv_coin);
        iv.setImageDrawable(getContext().getResources().getDrawable(balanceItem.resId));
        viewHolder.setText(R.id.tv_coin_name,balanceItem.symbol.toUpperCase());

        AppCompatTextView tv_coin_type = viewHolder.getView(R.id.tv_coin_type);

        SymbolCache symbolCache  = CacheCenter.getInstance().getSymbolCache();

        BHToken bhCoin = symbolCache.getBHToken(balanceItem.symbol.toLowerCase());




        //实时价格
        String symbol_prices = CurrencyManager.getInstance().getCurrencyRateDecription(getContext(),balanceItem.symbol);

        viewHolder.setText(R.id.tv_coin_price, symbol_prices);
        //币的数量
        if(!TextUtils.isEmpty(balanceItem.amount)){
            String []result = BHBalanceHelper.getAmountToCurrencyValue(getContext(),balanceItem.amount,balanceItem.symbol,false);
            viewHolder.setText(R.id.tv_coin_amount, result[0]);
            viewHolder.setText(R.id.tv_coin_count, result[1]);
        }else{
            viewHolder.setText(R.id.tv_coin_amount, "0");
            viewHolder.setText(R.id.tv_coin_count, "0");
        }

        //标签
        if(bhCoin==null){
            return;
        }
        if(bhCoin.symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_coin_type.setVisibility(View.GONE);
        }else if(bhCoin.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_coin_type.setVisibility(View.VISIBLE);
            tv_coin_type.setText(R.string.native_token);
            tv_coin_type.setTextAppearance(getContext(),R.style.tx_status_success);
        }else if(!bhCoin.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_coin_type.setVisibility(View.VISIBLE);
            tv_coin_type.setText(R.string.no_native_token);
            tv_coin_type.setTextAppearance(getContext(),R.style.tx_cross_link_token);
        }
    }

}
