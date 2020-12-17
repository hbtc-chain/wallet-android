package com.bhex.wallet.market.ui;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.BHTokenMapping;
import com.bhex.wallet.market.R;

import java.util.List;

/**
 *
 */
public class SymbolViewHolder {

    private View m_layout_top;
    private AppCompatTextView tv_coin_balance;
    private AppCompatTextView tv_target_balance;

    private AppCompatTextView tv_coin_name;
    private AppCompatTextView tv_target_name;

    private AppCompatImageView iv_coin;
    private AppCompatImageView iv_target;

    WithDrawInput ed_transfer_coin_amount;
    WithDrawInput ed_transfer_target_amount;

    private AppCompatTextView tv_rate;

    private String mSymbol;
    private BHTokenMapping mTokenMapping;

    public void bindView(View parentView,String symbol){
        m_layout_top = parentView;
        mSymbol = symbol;

        tv_coin_balance = m_layout_top.findViewById(R.id.tv_coin_balance);
        tv_target_balance = m_layout_top.findViewById(R.id.tv_target_balance);

        tv_coin_name = m_layout_top.findViewById(R.id.tv_coin_token);
        tv_target_name = m_layout_top.findViewById(R.id.tv_target_token);

        iv_coin = m_layout_top.findViewById(R.id.iv_coin);
        iv_target = m_layout_top.findViewById(R.id.iv_target);

        tv_rate = m_layout_top.findViewById(R.id.tv_rate);

        ed_transfer_coin_amount = m_layout_top.findViewById(R.id.ed_transfer_coin_amount);
        ed_transfer_target_amount = m_layout_top.findViewById(R.id.ed_transfer_target_amount);
        ed_transfer_coin_amount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ed_transfer_target_amount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        ed_transfer_target_amount.getEditText().setFocusable(true);
        ed_transfer_coin_amount.getEditText().addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(ed_transfer_coin_amount.getEditText().getTag().toString().equals("1")){
                    ed_transfer_target_amount.setInputString( ed_transfer_coin_amount.getInputString());

                }
            }
        });

        ed_transfer_coin_amount.getEditText().setOnFocusChangeListener((v,hasFocus)->{
            if(hasFocus){
                ed_transfer_coin_amount.getEditText().setTag("1");
                ed_transfer_target_amount.getEditText().setTag("0");
            }

        });

        ed_transfer_target_amount.getEditText().setOnFocusChangeListener((v,hasFocus)->{
            if(hasFocus){
                ed_transfer_coin_amount.getEditText().setTag("0");
                ed_transfer_target_amount.getEditText().setTag("1");
            }
        });

        ed_transfer_target_amount.getEditText().addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                LogUtils.d("==hasFocus===11=="+ed_transfer_target_amount.getEditText().getTag().toString());

                if(ed_transfer_target_amount.getEditText().getTag().toString().equals("1")){
                    ed_transfer_coin_amount.setInputString( ed_transfer_target_amount.getInputString());
                }
            }
        });

        List<BHTokenMapping> mMappingToknList = CacheCenter.getInstance().getTokenMapCache().getTokenMapping(mSymbol.toUpperCase());
        if(ToolUtils.checkListIsEmpty(mMappingToknList) || mMappingToknList.size()<=1){
            m_layout_top.findViewById(R.id.iv_target_arrow).setVisibility(View.INVISIBLE);
        }else{
            m_layout_top.findViewById(R.id.iv_target_arrow).setVisibility(View.VISIBLE);
        }

        ed_transfer_coin_amount.btn_right_text.setOnClickListener(v -> {
            //String coin_name = tv_coin_name.getText().toString()
            //BHToken bh_coin_token = CacheCenter.getInstance().getSymbolCache().getBHToken(mTokenMapping.coin_symbol);

            ed_transfer_coin_amount.getEditText().setTag("1");
            ed_transfer_target_amount.getEditText().setTag("0");

            BHBalance bh_coin_balance = BHBalanceHelper.getBHBalanceFromAccount(mTokenMapping.coin_symbol);
            ed_transfer_coin_amount.setInputString(NumberUtil.dispalyForUsertokenAmount4Level(bh_coin_balance.amount));

            //BHBalance bh_target_balance= BHBalanceHelper.getBHBalanceFromAccount(mTokenMapping.target_symbol);
            //ed_transfer_target_amount.setInputString(NumberUtil.dispalyForUsertokenAmount4Level(bh_target_balance.amount));
        });

        ed_transfer_target_amount.btn_right_text.setOnClickListener(v->{
            ed_transfer_coin_amount.getEditText().setTag("0");
            ed_transfer_target_amount.getEditText().setTag("1");

            BHBalance bh_target_balance= BHBalanceHelper.getBHBalanceFromAccount(mTokenMapping.target_symbol);
            ed_transfer_target_amount.setInputString(NumberUtil.dispalyForUsertokenAmount4Level(bh_target_balance.amount));
        });
    }

    public void setTokenAsset(Context context,String symbol) {
        mSymbol = symbol;
        mTokenMapping = CacheCenter.getInstance().getTokenMapCache().getTokenMappingOne(mSymbol.toUpperCase());
        List<BHTokenMapping> mMappingToknList = CacheCenter.getInstance().getTokenMapCache().getTokenMapping(mSymbol.toUpperCase());
        if(ToolUtils.checkListIsEmpty(mMappingToknList) || mMappingToknList.size()<=1){
            m_layout_top.findViewById(R.id.iv_target_arrow).setVisibility(View.INVISIBLE);
        }else{
            m_layout_top.findViewById(R.id.iv_target_arrow).setVisibility(View.VISIBLE);
        }
        //BHToken bh_coin_token = CacheCenter.getInstance().getSymbolCache().getBHToken(mTokenMapping.coin_symbol);
        //BHToken bh_target_token = CacheCenter.getInstance().getSymbolCache().getBHToken(mTokenMapping.target_symbol);
        BHToken bh_coin_token = CacheCenter.getInstance().getTokenMapCache().getBHToken(mTokenMapping.coin_symbol);
        BHToken bh_target_token = CacheCenter.getInstance().getTokenMapCache().getBHToken(mTokenMapping.target_symbol);
        //设置余额
        BHBalance bh_coin_balance = BHBalanceHelper.getBHBalanceFromAccount(bh_coin_token.symbol.toLowerCase());
        tv_coin_balance.setText(context.getString(R.string.balance)+" "+ NumberUtil.dispalyForUsertokenAmount4Level(bh_coin_balance.amount));

        BHBalance bh_target_balance = BHBalanceHelper.getBHBalanceFromAccount(bh_target_token.symbol.toLowerCase());
        tv_target_balance.setText(context.getString(R.string.balance)+" "+ NumberUtil.dispalyForUsertokenAmount4Level(bh_target_balance.amount));

        //设置图标
        BHBalanceHelper.loadTokenIcon(context,iv_coin,mTokenMapping.coin_symbol.toUpperCase());
        BHBalanceHelper.loadTokenIcon(context,iv_target,mTokenMapping.target_symbol.toUpperCase());

        //设置Token-name
        if(bh_coin_token.name.equalsIgnoreCase(bh_target_token.name)){
            String coin_token = bh_coin_token.name.toUpperCase().concat("(").concat(bh_coin_token.chain.toUpperCase()).concat(")");
            tv_coin_name.setText(coin_token);

            String target_token = bh_target_token.name.toUpperCase().concat("(").concat(bh_target_token.chain.toUpperCase()).concat(")");
            tv_target_name.setText(target_token);
        }else{
            tv_coin_name.setText(bh_coin_token.name.toUpperCase());
            tv_target_name.setText(bh_target_token.name.toUpperCase());
        }
        //
        String tv_rate_str = "1 ".concat(tv_coin_name.getText().toString()).concat(" = 1 ").concat(tv_target_name.getText().toString());
        tv_rate.setText(tv_rate_str);
    }

    public void updateBalance(Context context){
        //BHToken bh_coin_token = CacheCenter.getInstance().getSymbolCache().getBHToken(mTokenMapping.coin_symbol);
        //BHToken bh_target_token = CacheCenter.getInstance().getSymbolCache().getBHToken(mTokenMapping.target_symbol);
        BHToken bh_coin_token = CacheCenter.getInstance().getTokenMapCache().getBHToken(mTokenMapping.coin_symbol);
        BHToken bh_target_token = CacheCenter.getInstance().getTokenMapCache().getBHToken(mTokenMapping.target_symbol);
        //设置余额
        BHBalance bh_coin_balance = BHBalanceHelper.getBHBalanceFromAccount(bh_coin_token.symbol.toLowerCase());
        tv_coin_balance.setText(context.getString(R.string.balance)+" "+ NumberUtil.dispalyForUsertokenAmount4Level(bh_coin_balance.amount));

        BHBalance bh_target_balance = BHBalanceHelper.getBHBalanceFromAccount(bh_target_token.symbol.toLowerCase());
        tv_target_balance.setText(context.getString(R.string.balance)+" "+ NumberUtil.dispalyForUsertokenAmount4Level(bh_target_balance.amount));
    }

}
