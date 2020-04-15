package com.bhex.wallet.balance.presenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/12
 * Time: 19:15
 */
public class AssetPresenter extends BasePresenter {

    public AssetPresenter(BaseActivity activity) {
        super(activity);
    }

    public BHBalance getBthBalanceWithAccount(AccountInfo accountInfo){
        if(accountInfo==null){
            return null;
        }
        BHBalance balance = new BHBalance();
        balance.amount="";
        balance.chain=BHConstants.BHT_TOKEN;
        balance.symbol = BHConstants.BHT_TOKEN;

        List<AccountInfo.AssetsBean> assetsBeanList = accountInfo.getAssets();
        if(assetsBeanList==null || assetsBeanList.size()==0){
            return balance;
        }

        for(AccountInfo.AssetsBean assetsBean:assetsBeanList){
            if(assetsBean.getSymbol().equalsIgnoreCase(BHConstants.BHT_TOKEN)){
                balance.symbol = assetsBean.getSymbol();
                balance.chain = assetsBean.getSymbol();
                balance.amount = assetsBean.getAmount();
                balance.frozen_amount = assetsBean.getFrozen_amount();
                balance.address = assetsBean.getExternal_address();
            }
        }


        return balance;
    }

}
