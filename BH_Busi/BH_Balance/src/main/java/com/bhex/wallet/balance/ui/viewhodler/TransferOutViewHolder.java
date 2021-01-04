package com.bhex.wallet.balance.ui.viewhodler;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.google.android.material.button.MaterialButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.warkiz.widget.IndicatorSeekBar;

public class TransferOutViewHolder {
    private BaseActivity m_activity;
    private View mRootView;

    //转账方式 提币 转账
    public int m_transferout_way;

    //转账币种
    public String m_symbol;
    //转账token
    public BHToken tranferToken;
    //提币手续费token
    public BHToken withDrawFeeToken;
    //交易手续费token bht
    public BHToken hbtFeeToken;

    //转出地址
    AppCompatTextView tv_transfer_out_address_label;
    AppCompatTextView inp_transfer_out_address;
    FrameLayout layout_transfer_out_address;

    //转账或提币数量-输入
    public RelativeLayout layout_transfer_amount;
    //转账数量
    public AppCompatEditText input_transfer_amount;
    //转账或提币地址-输入
    public WithDrawInput input_to_address;
    //提币手续费-输入
    public WithDrawInput input_withdraw_fee;
    //交易手续费-输入
    public WithDrawInput input_tx_fee;

    //可用转账或提币数量
    public AppCompatTextView tv_available_amount;
    //可用提币手续费
    public AppCompatTextView tv_withdraw_fee_amount;
    //可用交易手续费
    public AppCompatTextView tv_available_bht_amount;

    //提币按钮
    //public MaterialButton btn_drawwith_coin;

    //转账资产
    public BHBalance tranferBalance;
    //提币收费资产
    public BHBalance withDrawFeeBalance;
    //交易手续费资产
    public BHBalance hbtFeeBalance;

    //可以提币或转账数量
    public double available_amount;

    public TransferOutViewHolder(BaseActivity activity,View rootView,  String symbol,int transferout_way){
        this.m_activity = activity;
        this.mRootView = rootView;
        this.m_transferout_way = transferout_way;
        this.m_symbol = symbol;
        initViewHolder();

    }
    //getWay()==BH_BUSI_TYPE.链内转账.getIntValue()
    //初始化ViewHolder
    public void initViewHolder(){
        tranferToken = CacheCenter.getInstance().getSymbolCache().getBHToken(m_symbol);
        withDrawFeeToken = CacheCenter.getInstance().getSymbolCache().getBHToken(tranferToken.chain);
        hbtFeeToken = CacheCenter.getInstance().getSymbolCache().getBHToken(BHConstants.BHT_TOKEN);

        tranferBalance = BHBalanceHelper.getBHBalanceFromAccount(tranferToken.symbol);
        withDrawFeeBalance = BHBalanceHelper.getBHBalanceFromAccount(tranferToken.chain);
        hbtFeeBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);
        BHWallet currentWallet = BHUserManager.getInstance().getCurrentBhWallet();

        //提币按钮
        MaterialButton btn_drawwith_coin = mRootView.findViewById(R.id.btn_drawwith_coin);
        AppCompatTextView tv_center_title = mRootView.findViewById(R.id.tv_center_title);

        //箭头
        //Drawable drawableRight = ColorUtil.getDrawable(m_activity,R.mipmap.ic_arrow_token_d,R.color.global_main_text_color);
        //tv_center_title.setCompoundDrawablesWithIntrinsicBounds(null,null, drawableRight, null);

        //转出地址
        tv_transfer_out_address_label = mRootView.findViewById(R.id.tv_transfer_out_address_label);
        inp_transfer_out_address = mRootView.findViewById(R.id.inp_transfer_out_address);
        inp_transfer_out_address.setText(currentWallet.getAddress());
        layout_transfer_out_address = mRootView.findViewById(R.id.layout_transfer_out_address);
        //转出币种
        AppCompatTextView tv_transfer_out_token = mRootView.findViewById(R.id.tv_transfer_out_token);
        tv_transfer_out_token.setText(tranferToken.name.toUpperCase());

        layout_transfer_amount = mRootView.findViewById(R.id.layout_transfer_amount);
        input_to_address  = mRootView.findViewById(R.id.input_to_address);
        input_withdraw_fee  = mRootView.findViewById(R.id.et_withdraw_fee);
        input_tx_fee  = mRootView.findViewById(R.id.et_tx_fee);

        tv_available_amount = mRootView.findViewById(R.id.tv_available_amount);
        tv_withdraw_fee_amount = mRootView.findViewById(R.id.tv_withdraw_fee_amount);
        tv_available_bht_amount = mRootView.findViewById(R.id.tv_available_bht_amount);

        ((SmartRefreshLayout)mRootView.findViewById(R.id.refreshLayout)).setEnableLoadMore(false);
        //((IndicatorSeekBar)mRootView.findViewById(R.id.sb_tx_fee)).setDecimalScale(4);

        //格式
        input_transfer_amount = layout_transfer_amount.findViewById(R.id.input_transfer_amount);
        AppCompatTextView btn_tranfer_all = layout_transfer_amount.findViewById(R.id.btn_tranfer_all);
        AppCompatTextView tv_token_unit = layout_transfer_amount.findViewById(R.id.tv_token_unit);
        //转账单位
        tv_token_unit.setText(tranferToken.name.toUpperCase());

        input_transfer_amount.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input_transfer_amount.setText("");

        input_withdraw_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input_tx_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //二维码扫描
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)input_to_address.getEditText().getLayoutParams();
        lp.addRule(RelativeLayout.LEFT_OF,R.id.iv_right);
        input_to_address.getEditText().setLayoutParams(lp);

        input_to_address.btn_right_text.setVisibility(View.GONE);
        input_to_address.iv_right.setVisibility(View.VISIBLE);
        input_to_address.iv_right.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterConfig.Common.commom_scan_qr).navigation(m_activity, BHQrScanActivity.REQUEST_CODE);
        });

        //初始化话手续费
        input_tx_fee.setInputString(BHUserManager.getInstance().getDefaultGasFee().displayFee);


        if(BHConstants.BHT_TOKEN.equalsIgnoreCase(tranferToken.chain)){
            btn_drawwith_coin.setText(m_activity.getResources().getString(R.string.confrim_transfer));
            tv_center_title.setText(m_activity.getResources().getString(R.string.transfer));
            //选择转账币种
            findText(R.id.tv_select_token_label).setText(m_activity.getResources().getString(R.string.select_transfer_token));
            //提示
            findText(R.id.tv_transfer_tips).setText(m_activity.getResources().getString(R.string.cross_transfer_inner_tips));
            //mRootView.findViewById(R.id.layout_transfer_out_tips).setVisibility(View.GONE);
            //转账地址
            //findText(R.id.tv_withdraw_address).setText(m_activity.getResources().getString(R.string.transfer_address));
            findText(R.id.tv_withdraw_address).setText(m_activity.getResources().getString(R.string.string_transfer_in_address));
            //转账数量
            findText(R.id.tv_transfer_amount).setText(m_activity.getResources().getString(R.string.transfer_amount));
            //全部转账
            btn_tranfer_all.setText(m_activity.getResources().getString(R.string.all));
            //提币手续费
            mRootView.findViewById(R.id.layout_withdraw_fee).setVisibility(View.GONE);
            input_withdraw_fee.setVisibility(View.GONE);

        }else if(m_transferout_way == BH_BUSI_TYPE.链内转账.getIntValue()){
            tv_center_title.setText(m_activity.getResources().getString(R.string.transfer));
            btn_drawwith_coin.setText(m_activity.getResources().getString(R.string.confrim_transfer));
            //选择转账币种
            findText(R.id.tv_select_token_label).setText(m_activity.getResources().getString(R.string.select_transfer_token));

            //RootView.findViewById(R.id.layout_transfer_out_tips).setVisibility(View.VISIBLE);
            //提示
            findText(R.id.tv_transfer_tips).setText(m_activity.getResources().getString(R.string.cross_transfer_inner_tips));
            //转账地址
            findText(R.id.tv_withdraw_address).setText(m_activity.getResources().getString(R.string.string_transfer_in_address));
            //转账数量
            findText(R.id.tv_transfer_amount).setText(m_activity.getResources().getString(R.string.transfer_amount));
            //全部转账
            btn_tranfer_all.setText(m_activity.getResources().getString(R.string.all));
            //提币手续费
            mRootView.findViewById(R.id.layout_withdraw_fee).setVisibility(View.GONE);
            input_withdraw_fee.setVisibility(View.GONE);
        }else {
            tv_center_title.setText(m_activity.getResources().getString(R.string.cross_withdraw));
            btn_drawwith_coin.setText(m_activity.getResources().getString(R.string.draw_coin));
            //选择转账币种
            findText(R.id.tv_select_token_label).setText(m_activity.getResources().getString(R.string.select_withdraw_token));

            //mRootView.findViewById(R.id.layout_transfer_out_tips).setVisibility(View.VISIBLE);
            //提示
            findText(R.id.tv_transfer_tips).setText(m_activity.getResources().getString(R.string.cross_transfer_out_tips));
            //提币地址
            findText(R.id.tv_withdraw_address).setText(m_activity.getResources().getString(R.string.draw_coin_address));
            //提币数量
            findText(R.id.tv_transfer_amount).setText(m_activity.getResources().getString(R.string.draw_coin_count));
            //全部提币
            btn_tranfer_all.setText(m_activity.getResources().getString(R.string.all));
            //提币手续费
            mRootView.findViewById(R.id.layout_withdraw_fee).setVisibility(View.VISIBLE);
            input_withdraw_fee.setVisibility(View.VISIBLE);

            tv_transfer_out_address_label.setVisibility(View.GONE);
            layout_transfer_out_address.setVisibility(View.GONE);
        }

        //默认最小提币手续费
        input_withdraw_fee.setInputString(tranferToken.withdrawal_fee);
        //更新余额
        updateBalance();

        btn_tranfer_all.setOnClickListener(this::allWithDrawListener);
        
        //选择币种
        //mRootView.setOnClickListener(this::selectTokenAction);
    }

    /*//选择币种
    private void selectTokenAction(View view) {

    }*/


    public AppCompatTextView findText(int resId){
        return mRootView.findViewById(resId);
    }

    //更新余额
    public void updateBalance() {
        tranferToken = CacheCenter.getInstance().getSymbolCache().getBHToken(m_symbol);

        tranferBalance = BHBalanceHelper.getBHBalanceFromAccount(tranferToken.symbol);
        //tranferBalance.amount = "3.4";
        withDrawFeeBalance = BHBalanceHelper.getBHBalanceFromAccount(tranferToken.chain);
        //withDrawFeeBalance.amount = "0.52";
        hbtFeeBalance = BHBalanceHelper.getBHBalanceFromAccount(BHConstants.BHT_TOKEN);

        //提币
        input_withdraw_fee.btn_right_text.setText(withDrawFeeToken.chain.toUpperCase());
        /*//最小提币数量
        if(TextUtils.isEmpty(input_withdraw_fee.getInputString())){
            input_withdraw_fee.setInputString(withDrawFeeToken.withdrawal_fee);
        }*/
        //转账数量
        String available_amount_str =  BHBalanceHelper.getAmountForUser(m_activity,tranferBalance.amount,"0",tranferBalance.symbol);
        available_amount = Double.valueOf(available_amount_str);
        tv_available_amount.setText(m_activity.getString(R.string.available)+" "+available_amount_str+" "+tranferToken.name.toUpperCase());

        //提币手续费数量
        String available_fee_amount_str =  BHBalanceHelper.getAmountForUser(m_activity,withDrawFeeBalance.amount,"0",withDrawFeeBalance.symbol);
        tv_withdraw_fee_amount.setText(m_activity.getString(R.string.available)+" "+available_fee_amount_str+" "+withDrawFeeBalance.name.toUpperCase());

        //hbc手续费数量
        if(hbtFeeBalance!=null){
            String available_bht_amount_str =  BHBalanceHelper.getAmountForUser(m_activity,hbtFeeBalance.amount,"0",hbtFeeBalance.symbol);
            tv_available_bht_amount.setText(m_activity.getString(R.string.available)+" "+available_bht_amount_str+" "+hbtFeeBalance.name.toUpperCase());
        }

        //input_tx_fee.setInputString(BHConstants.BHT_DEFAULT_FEE);
        input_tx_fee.setInputString(BHUserManager.getInstance().getDefaultGasFee().displayFee);
    }

    //全部提币或者转账事件
    private void allWithDrawListener(View view) {
        if(m_transferout_way==BH_BUSI_TYPE.跨链转账.getIntValue()){
            if(tranferToken.symbol.equalsIgnoreCase(tranferToken.chain) &&  RegexUtil.checkNumeric(input_withdraw_fee.getInputString()) ){
                //减去提币手续费
                //input_withdraw_fee.getEditText().setText(withDrawFeeToken.withdrawal_fee);
                String all_count = NumberUtil.sub(String.valueOf(available_amount),input_withdraw_fee.getInputString());
                all_count = Double.valueOf(all_count)<0?"0":all_count;
                input_transfer_amount.setText(all_count);
            }else{
                input_transfer_amount.setText(NumberUtil.toPlainString(available_amount));
            }
        }else{
            if(tranferToken.symbol.equalsIgnoreCase(tranferToken.chain)){
                //交易手续费
                String all_count = NumberUtil.sub(String.valueOf(available_amount),input_tx_fee.getInputString());
                all_count = Double.valueOf(all_count)<0?"0":all_count;
                input_transfer_amount.setText(all_count);
            }else{
                input_transfer_amount.setText(NumberUtil.toPlainString(available_amount));
            }
        }
        input_transfer_amount.setSelection(input_transfer_amount.getText().length());
        input_transfer_amount.requestFocus();
    }

    //更新视图
    public void upateViewContent(String symbol) {
        this.m_symbol = symbol;
        initViewHolder();
    }
}
