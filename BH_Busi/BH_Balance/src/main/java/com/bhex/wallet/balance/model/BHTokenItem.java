package com.bhex.wallet.balance.model;

import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/3
 * Time: 15:29
 */
public class BHTokenItem extends BHToken {
    public String fullName = "Bitcoin";
    public int resId;
    public boolean isSelected;

    public BHBalance getBHBalance(){
        BHBalance bhBalance = new BHBalance();
        bhBalance.symbol = symbol;
        bhBalance.chain = chain;
        bhBalance.logo = logo;
        bhBalance.is_native = is_native;
        return bhBalance;
    }
}
