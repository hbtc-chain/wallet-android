package com.bhex.wallet.common.tx;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/27
 * Time: 16:07
 */
public class ValidatorMsg {

    public String delegator_address;

    public String validator_address;

    public ValidatorMsg(String delegator_address, String validator_address) {
        this.delegator_address = delegator_address;
        this.validator_address = validator_address;
    }
}
