package com.bhex.wallet.common.model;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 0:16
 */
public class AccountInfo {


    /**
     * type : 1
     * address : BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK
     * sequence : 17
     * assets : [{"symbol":"bht","is_native":false,"amount":"9664542907594597466123","external_address":"","enable_sendtx":false,"frozen_amount":"0"}]
     * bonded : 0
     * unbonding : 0
     * claimed_reward : 0
     * unclaimed_reward : 0
     */

    private String type;
    private String address;
    private String sequence;
    private String available;
    private String bonded;
    private String unbonding;
    private String claimed_reward;
    private String unclaimed_reward;
    private List<AssetsBean> assets;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBonded() {
        return bonded;
    }

    public void setBonded(String bonded) {
        this.bonded = bonded;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getUnbonding() {
        return unbonding;
    }

    public void setUnbonding(String unbonding) {
        this.unbonding = unbonding;
    }

    public String getClaimed_reward() {
        return claimed_reward;
    }

    public void setClaimed_reward(String claimed_reward) {
        this.claimed_reward = claimed_reward;
    }

    public String getUnclaimed_reward() {
        return unclaimed_reward;
    }

    public void setUnclaimed_reward(String unclaimed_reward) {
        this.unclaimed_reward = unclaimed_reward;
    }

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsBean> assets) {
        this.assets = assets;
    }

    public static class AssetsBean {
        /**
         * symbol : bht
         * is_native : false
         * amount : 9664542907594597466123
         * external_address :
         * enable_sendtx : false
         * frozen_amount : 0
         */

        private String symbol;
        private boolean is_native;
        private String amount;
        private String external_address;
        private boolean enable_sendtx;
        private String frozen_amount;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public boolean isIs_native() {
            return is_native;
        }

        public void setIs_native(boolean is_native) {
            this.is_native = is_native;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getExternal_address() {
            return external_address;
        }

        public void setExternal_address(String external_address) {
            this.external_address = external_address;
        }

        public boolean isEnable_sendtx() {
            return enable_sendtx;
        }

        public void setEnable_sendtx(boolean enable_sendtx) {
            this.enable_sendtx = enable_sendtx;
        }

        public String getFrozen_amount() {
            return frozen_amount;
        }

        public void setFrozen_amount(String frozen_amount) {
            this.frozen_amount = frozen_amount;
        }
    }
}
