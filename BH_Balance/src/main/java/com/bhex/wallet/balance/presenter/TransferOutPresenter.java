package com.bhex.wallet.balance.presenter;

import android.text.TextUtils;

import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/15
 * Time: 11:00
 */
public class TransferOutPresenter extends BasePresenter {

    public TransferOutPresenter(BaseActivity activity) {
        super(activity);
    }

    public boolean checklinkInnerTransfer(String to_address,String transfer_amount,
                                          String available_amount,String fee_amount){
        if(!to_address.startsWith(BHConstants.BHT_TOKEN.toUpperCase())){
            ToastUtils.showToast("链内转账地址错误");
            return false;
        }

        if(TextUtils.isEmpty(available_amount) || Double.valueOf(available_amount)<=0){
            ToastUtils.showToast("没有可用余额");
            return false;
        }

        if(TextUtils.isEmpty(transfer_amount) && Double.valueOf(transfer_amount)<=0){
            ToastUtils.showToast("转账数量不能为空且大于0");
            return false;
        }

        if(TextUtils.isEmpty(fee_amount) && Double.valueOf(fee_amount)<=0){
            ToastUtils.showToast("交易手续不能为空且大于0");
            return false;
        }

        if( Double.valueOf(transfer_amount)>Double.valueOf(available_amount)){
            ToastUtils.showToast("转账数量大于可用余额");
            return false;
        }

        return true;
    }

    public boolean checklinkOutterTransfer(String to_address,String transfer_amount,
                                          String available_amount,
                                           String tx_fee_amount,String fee_amount){
        if(TextUtils.isEmpty(to_address)||to_address.startsWith(BHConstants.BHT_TOKEN.toUpperCase())){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.withdraw_address_error));
            return false;
        }

        if(TextUtils.isEmpty(transfer_amount) || Double.valueOf(transfer_amount)<=0){
            ToastUtils.showToast(getActivity().getResources().getString(R.string.input_withdraw_amount));
            return false;
        }

        if(TextUtils.isEmpty(tx_fee_amount) && Double.valueOf(tx_fee_amount)<=0){
            ToastUtils.showToast("请输入交易手续费");
            return false;
        }

        if(TextUtils.isEmpty(fee_amount) && Double.valueOf(fee_amount)<=0){
            ToastUtils.showToast("请输入提币手续费");
            return false;
        }

        if( Double.valueOf(transfer_amount)>Double.valueOf(available_amount)){
            ToastUtils.showToast("转账数量大于可用余额");
            return false;
        }

        return true;
    }
}
