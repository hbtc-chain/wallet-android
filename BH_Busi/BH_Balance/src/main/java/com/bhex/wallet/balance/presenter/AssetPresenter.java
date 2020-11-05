package com.bhex.wallet.balance.presenter;

import android.text.TextUtils;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.model.DelegateValidator;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.tx.TransactionMsg;
import com.bhex.wallet.common.tx.TxFee;

import java.math.BigDecimal;
import java.util.ArrayList;
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
                BHToken bhToken = CacheCenter.getInstance().getSymbolCache().getBHToken(balance.symbol);
                balance.chain = bhToken!=null?bhToken.chain:"";
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
            //LogUtils.d("AssetPresenter====>:","===null===");
            return;
        }
        balance.isHasToken = 1;
        balance.amount = assetsBean.getAmount();
        balance.is_native = assetsBean.isIs_native();
        //balance.external_address = assetsBean.getExternal_address();
        BHBalance chainBalance = BHBalanceHelper.getBHBalanceFromAccount(balance.chain);
        if(chainBalance!=null && !TextUtils.isEmpty(chainBalance.external_address)){
            balance.external_address = chainBalance.external_address;
        }
        BHToken bhToken = CacheCenter.getInstance().getSymbolCache().getBHToken(balance.symbol);
        balance.chain = bhToken!=null?bhToken.chain:"";
        balance.frozen_amount = assetsBean.getFrozen_amount();
    }

    //计算所有收益
    public double calAllReward(List<DelegateValidator> list){
        double res = 0.0f;
        if(list==null || list.size()==0){
            return  res;
        }
        for(DelegateValidator item:list){
            res = NumberUtil.add(String.valueOf(res),item.unclaimed_reward);
        }
        return res;
    }

    //获取所有的验证人消息
    public List<TransactionMsg.ValidatorMsg> getAllValidator(List<DelegateValidator> list){
        List<TransactionMsg.ValidatorMsg> validatorMsgs = new ArrayList<>();
        for(DelegateValidator item:list){
            TransactionMsg.ValidatorMsg validatorMsg = new TransactionMsg.ValidatorMsg(BHUserManager.getInstance().getCurrentBhWallet().address,item.validator);
            validatorMsgs.add(validatorMsg);
        }
        return validatorMsgs;
    }

    //创建所有委托消息
    public List<TransactionMsg.DoEntrustMsg> getAllEntrust(List<DelegateValidator> list){
        List<TransactionMsg.DoEntrustMsg> doEntrustMsgs = new ArrayList<>();
        for(DelegateValidator item:list){
            TransactionMsg.DoEntrustMsg doEntrust = new TransactionMsg.DoEntrustMsg();
            doEntrust.delegator_address = BHUserManager.getInstance().getCurrentBhWallet().address;
            doEntrust.validator_address = item.validator;

            //委托金额
            TxFee.TxCoin txCoin = new TxFee.TxCoin();
            txCoin.denom = BHConstants.BHT_TOKEN;
            double tx_amount = NumberUtil.mul(item.unclaimed_reward,String.valueOf(BHConstants.BHT_DECIMALS));
            txCoin.amount = BigDecimal.valueOf(tx_amount).toBigInteger().toString(10);
            doEntrust.amount = txCoin;

            doEntrustMsgs.add(doEntrust);


        }
        return doEntrustMsgs;
    }

}
