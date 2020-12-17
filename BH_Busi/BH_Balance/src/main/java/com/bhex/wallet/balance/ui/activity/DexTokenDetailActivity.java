package com.bhex.wallet.balance.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.event.RequestTokenEvent;
import com.bhex.wallet.common.menu.MenuItem;
import com.bhex.wallet.common.menu.MenuListFragment;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * @author dongyang
 * 2020-8-31 20:53:42
 */
@Route(path = ARouterConfig.Balance.Balance_Token_Detail)
public class DexTokenDetailActivity extends TokenDetailActivity {

    @Autowired(name = "symbol")
    String symbol;

    @OnClick({R2.id.btn_item1, R2.id.btn_item2,R2.id.btn_item3, R2.id.btn_item4})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_item1) {
            if(symbolToken.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                        .navigation();
            }else{
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way", BH_BUSI_TYPE.跨链转账.getIntValue())
                        .navigation();
            }

        } else if (view.getId() == R.id.btn_item2) {
            if(symbolToken.chain.toLowerCase().equals(BHConstants.BHT_TOKEN)){
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                        .navigation();
            }else{
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_out)
                        .withString("symbol", symbol)
                        .withInt("way",BH_BUSI_TYPE.跨链转账.getIntValue())
                        .navigation();
            }

        } else if (view.getId() == R.id.btn_item3) {
            //提取收益
            //withdrawShare();
            ARouter.getInstance().build(ARouterConfig.Market_swap_mapping).withString("symbol",symbol).navigation();
        } else if (view.getId() == R.id.btn_item4) {
            //reDelegate();
            //
            Postcard postcard =  ARouter.getInstance()
                    .build(ARouterConfig.Main.main_mainindex)
                    .withString("go_token",symbol)
                    .withString("go_position",BH_BUSI_TYPE.市场.value);
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(this, postcard.getDestination());
            intent.putExtras(postcard.getExtras());
            startActivity(intent);
            EventBus.getDefault().post(new RequestTokenEvent(symbol));

        }/*else if(view.getId() == R.id.cross_chian_transfer_in){
            ArrayList<MenuItem> list = BHBalanceHelper.loadCrossActionList(this);
            MenuListFragment menuFragment = MenuListFragment.newInstance(list);
            menuFragment.setMenuListListener(crossOperatorListener);
            menuFragment.show(getSupportFragmentManager(),MenuListFragment.class.getSimpleName());
        }else if(view.getId() == R.id.cross_chian_withdraw){
            //ARouter.getInstance().build(ARouterConfig.Market_swap_mapping).withString("symbol",symbol).navigation();
        }*/
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
            if(TextUtils.isEmpty(chainSymbolBalance.external_address)){
                //请求用户资产 获取链外地址
                //balanceViewModel.getAccountInfo(this, CacheStrategy.onlyRemote());
                ARouter.getInstance().build(ARouterConfig.Balance.Balance_cross_address)
                        .withString("symbol", symbol)
                        .withInt("way",BH_BUSI_TYPE.跨链转账.getIntValue())
                        .navigation();
            }else{

                ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                        .withString("symbol", symbol)
                        .withInt("way",BH_BUSI_TYPE.跨链转账.getIntValue())
                        .navigation();
            }
        }else if(position==1){
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_out)
                    .withString("symbol", symbol)
                    .withInt("way",BH_BUSI_TYPE.跨链转账.getIntValue())
                    .navigation();
        }
    };

    @Override
    public String getSymbol() {
        return symbol;
    }
}
