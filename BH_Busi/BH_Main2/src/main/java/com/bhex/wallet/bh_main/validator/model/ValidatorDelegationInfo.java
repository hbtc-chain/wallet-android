package com.bhex.wallet.bh_main.validator.model;

/**
 * Created by BHEX.
 * User: zhou chang
 * Date: 2020/4/18
 */
public class ValidatorDelegationInfo {


    /**
     * validator : string
     * bonded : string
     * unclaimed_reward : string
     */

    private String validator;
    private String bonded;
    private String unclaimed_reward;

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getBonded() {
        return bonded;
    }

    public void setBonded(String bonded) {
        this.bonded = bonded;
    }

    public String getUnclaimed_reward() {
        return unclaimed_reward;
    }

    public void setUnclaimed_reward(String unclaimed_reward) {
        this.unclaimed_reward = unclaimed_reward;
    }
}
