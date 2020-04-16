package com.bhex.wallet.balance.presenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 更新用户资产
     * @param accountInfo
     * @param balance
     */
    public void updateBalance(AccountInfo accountInfo,BHBalance balance){

        List<AccountInfo.AssetsBean> list = accountInfo.getAssets();
        if(list==null || list.size()==0){
            return;
        }

        Map<String,AccountInfo.AssetsBean> map = new HashMap<>();
        for(AccountInfo.AssetsBean bean:list){
            map.put(bean.getSymbol(),bean);
        }

        AccountInfo.AssetsBean assetsBean = map.get(balance.symbol.toLowerCase());
        if(assetsBean==null){
            return;
        }

        balance.amount = assetsBean.getAmount();
        balance.is_native = assetsBean.isIs_native();
        balance.external_address = assetsBean.getExternal_address();
        balance.frozen_amount = assetsBean.getFrozen_amount();
    }

}
