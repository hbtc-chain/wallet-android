package com.bhex.wallet.balance.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ImageLoaderUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.enums.CURRENCY_TYPE;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHChain;
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
public class ChainAdapter extends BaseQuickAdapter<BHChain, BaseViewHolder> {

    private String isHidden = "0";
    public ChainAdapter(@Nullable List<BHChain> data) {
        super(R.layout.item_chain, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable BHChain bhChain) {
        AppCompatImageView iv = viewHolder.getView(R.id.iv_coin);
        iv.setImageResource(0);

        SymbolCache symbolCache  = CacheCenter.getInstance().getSymbolCache();

        BHToken bhCoin = symbolCache.getBHToken(bhChain.chain.toLowerCase());

        ImageLoaderUtil.loadImageView(getContext(),bhCoin!=null?bhCoin.logo:"", iv,R.mipmap.ic_default_coin);

        viewHolder.setText(R.id.tv_coin_name,bhChain.chain.toUpperCase());

        AppCompatTextView tv_coin_type = viewHolder.getView(R.id.tv_coin_type);



        //实时价格
        //String symbol_prices = CurrencyManager.getInstance().getCurrencyRateDecription(getContext(),balanceItem.symbol);

        viewHolder.setText(R.id.tv_coin_price, bhChain.full_name);

        //价格
        double value_double = BHBalanceHelper.getAssetByChain(getContext(),bhChain.chain);
        String value_str = CurrencyManager.getInstance().getCurrencyDecription(getContext(),value_double);
        if(isHidden.equals("0")){
            viewHolder.setText(R.id.tv_coin_count,"≈"+value_str);
        }else {
            viewHolder.setText(R.id.tv_coin_count, "***");
        }

        tv_coin_type.setVisibility(View.VISIBLE);

        if(bhChain.chain.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
            tv_coin_type.setText(getContext().getString(R.string.native_token_test_list));
        }else{
            tv_coin_type.setText(getContext().getString(R.string.cross_chain_token_list));
        }

        //币的数量
        /*if(isHidden.equals("0")){
            if(!TextUtils.isEmpty(balanceItem.amount)&&Double.valueOf(balanceItem.amount)>0) {
                String []result = BHBalanceHelper.getAmountToCurrencyValue(getContext(),balanceItem.amount,balanceItem.symbol,false);
                viewHolder.setText(R.id.tv_coin_amount, result[0]);
                viewHolder.setText(R.id.tv_coin_count, "≈"+result[1]);
            }else{
                viewHolder.setText(R.id.tv_coin_amount, "0");
                viewHolder.setText(R.id.tv_coin_count, "≈"+
                        CURRENCY_TYPE.valueOf(CurrencyManager.getInstance().loadCurrency(getContext()).toUpperCase()).character+"0");
            }
        }else{
            viewHolder.setText(R.id.tv_coin_amount, "***");
            viewHolder.setText(R.id.tv_coin_count, "***");
        }*/

        //标签
        /*if(bhCoin==null){
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
        }*/
    }

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
        notifyDataSetChanged();
    }
}
