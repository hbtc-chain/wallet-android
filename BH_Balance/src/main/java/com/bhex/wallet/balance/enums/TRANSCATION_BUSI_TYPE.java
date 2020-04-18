package com.bhex.wallet.balance.enums;

import android.content.Context;

import com.bhex.wallet.balance.R;

import java.util.Optional;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/18
 * Time: 16:28 hbtcchain/transfer/MsgDeposit
 * 交易类型
 *
 */
public enum  TRANSCATION_BUSI_TYPE {
    Transfer("hbtcchain/transfer/MsgSend"),
    Delegate("hbtcchain/MsgDelegate"),
    Undelegate("hbtcchain/MsgUndelegate"),
    KeyGen("hbtcchain/keygen/MsgKeyGen"),
    WithdrawDelegationReward("hbtcchain/MsgWithdrawDelegationReward"),
    Deposit("hbtcchain/transfer/MsgDeposit"),
    Withdrawal("hbtcchain/transfer/MsgWithdrawal"),
    Other("other");

    private String type;
    private String label;

    TRANSCATION_BUSI_TYPE(String type) {
        this.type = type;
    }

    TRANSCATION_BUSI_TYPE(String type,String label) {
        this.type = type;
        this.label = label;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static void init(Context context){
        Transfer.label = context.getResources().getString(R.string.transfer);
        Delegate.label = context.getResources().getString(R.string.entrust);
        Undelegate.label = context.getResources().getString(R.string.un_entrust);
        KeyGen.label = context.getResources().getString(R.string.crosslink_address);
        Deposit.label = context.getResources().getString(R.string.cross_deposit);
        WithdrawDelegationReward.label = context.getResources().getString(R.string.withdraw_reward);
        Withdrawal.label = context.getResources().getString(R.string.withdraw_reward);
        Other.label = context.getResources().getString(R.string.other);
    }

    public static String getValue(String type) {
        TRANSCATION_BUSI_TYPE[] transcationBusiTypes = values();
        for (TRANSCATION_BUSI_TYPE item : transcationBusiTypes) {
            if (item.getType().equals(type)) {
                return item.label;
            }
        }
        return Other.label;
    }

}
