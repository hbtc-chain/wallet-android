package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.MAIN_BUSI_TYPE;
import com.bhex.wallet.common.menu.MenuItem;
import com.bhex.wallet.common.menu.MenuListFragment;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * @author dongyang
 * 2020-8-31 20:53:42
 */
@Route(path = ARouterConfig.Balance_Token_Detail)
public class DexTokenDetailActivity extends TokenDetailActivity {

    @Autowired(name = "balance")
    BHBalance balance;

    @Autowired(name = "accountInfo")
    AccountInfo mAccountInfo;

    @Override
    public BHBalance getBHBalance() {
        return balance;
    }

    @Override
    public AccountInfo getAccountInfo() {
        return mAccountInfo;
    }

    @Override
    public void setAccountInfo(AccountInfo accountInfo) {
        this.mAccountInfo = accountInfo;
    }

    @OnClick({R2.id.btn_item1, R2.id.btn_item2,R2.id.btn_item3, R2.id.btn_item4})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_item1) {
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_in)
                    .withObject("balance", getBHBalance())
                    .withInt("way",1)
                    .navigation();
        } else if (view.getId() == R.id.btn_item2) {
            ARouter.getInstance().build(ARouterConfig.Balance_transfer_out)
                    .withObject("balance", getBHBalance())
                    .withObject("bhtBalance",bthBalance)
                    .withInt("way",1)
                    .navigation();
        } else if (view.getId() == R.id.btn_item3) {
            //提取收益
            withdrawShare();
        } else if (view.getId() == R.id.btn_item4) {
            //复投分红
            //reDelegate();
            ARouter.getInstance().build(ARouterConfig.Validator_Index)
                    .navigation();
        }else if(view.getId() == R.id.cross_chian_transfer_in){
            ArrayList<MenuItem> list = BHBalanceHelper.loadCrossActionList(this);
            MenuListFragment menuFragment = MenuListFragment.newInstance(list);
            menuFragment.setMenuListListener(crossOperatorListener);
            menuFragment.show(getSupportFragmentManager(),MenuListFragment.class.getSimpleName());
        }else if(view.getId() == R.id.cross_chian_withdraw){
            /*ArrayList<MenuItem> list = BHBalanceHelper.loadExchangeActionList(this);
            MenuListFragment menuFragment = MenuListFragment.newInstance(list);
            menuFragment.setMenuListListener(exchagneOperatorListener);
            menuFragment.show(getSupportFragmentManager(),MenuListFragment.class.getSimpleName());*/
            ARouter.getInstance().build(ARouterConfig.Market_exchange_coin).withString("symbol",balance.symbol).navigation();
        }
    }

    /**
     * 复投分红
     */
    private void reDelegate() {
        transactionViewModel.queryValidatorByAddress(this,2);
    }

    /**
     * 提取收益
     */
    private void withdrawShare() {
        transactionViewModel.queryValidatorByAddress(this,1);
    }

    public MenuListFragment.MenuListListener crossOperatorListener = (item, itemView, position) -> {
        if(position==0){
            if(TextUtils.isEmpty(balance.external_address)){
                //请求用户资产 获取链外地址
                ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();
            }else{

                ARouter.getInstance().build(ARouterConfig.Balance_transfer_in)
                        .withObject("balance", balance)
                        .withInt("way",2)
                        .navigation();
            }
        }else if(position==1){
            if(TextUtils.isEmpty(balance.external_address)){
                //请求用户资产 获取链外地址
                //balanceViewModel.getAccountInfo(this,bthBalance.address);
                ARouter.getInstance().build(ARouterConfig.Balance_cross_address)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();

            }else{
                ARouter.getInstance().build(ARouterConfig.Balance_transfer_out)
                        .withObject("balance", balance)
                        .withObject("bhtBalance",bthBalance)
                        .withInt("way",2)
                        .navigation();
            }
        }
    };


    public MenuListFragment.MenuListListener exchagneOperatorListener  = (item, itemView, position) -> {
        if(position==0){
            ARouter.getInstance().build(ARouterConfig.Market_exchange_coin).navigation();
        }else{
            Postcard postcard = ARouter.getInstance().build(ARouterConfig.APP_MAIN_PAGE);
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(this, postcard.getDestination());
            intent.putExtra("go_position", MAIN_BUSI_TYPE.市场.label);
            intent.putExtras(postcard.getExtras());
            startActivity(intent);
        }
    };
}
