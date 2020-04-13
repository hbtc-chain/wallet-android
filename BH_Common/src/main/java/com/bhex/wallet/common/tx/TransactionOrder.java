package com.bhex.wallet.common.tx;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 15:42
 */
public class TransactionOrder {

    /**
     * hash : A2BB12B074A23339D0C072551F7B96B1949882B89B2139B67A180ABC08A26E88
     * height : 369283
     * fee : 2 bht
     * success : true
     * error_message : null
     * activities : [{"type":"cosmos-sdk/MsgSend","value":{"amount":[{"amount":"9223372036854775807","denom":"bht"}],"from_address":"BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK","to_address":"BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ"}}]
     * time : 1586503293
     * gas_used : 42276
     * gas_wanted : 2000000
     * memo : test memo
     */

    private String hash;
    private int height;
    private String fee;
    private boolean success;
    private Object error_message;
    private long time;
    private int gas_used;
    private int gas_wanted;
    private String memo;
    private List<ActivitiesBean> activities;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError_message() {
        return error_message;
    }

    public void setError_message(Object error_message) {
        this.error_message = error_message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getGas_used() {
        return gas_used;
    }

    public void setGas_used(int gas_used) {
        this.gas_used = gas_used;
    }

    public int getGas_wanted() {
        return gas_wanted;
    }

    public void setGas_wanted(int gas_wanted) {
        this.gas_wanted = gas_wanted;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class ActivitiesBean {
        /**
         * type : cosmos-sdk/MsgSend
         * value : {"amount":[{"amount":"9223372036854775807","denom":"bht"}],"from_address":"BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK","to_address":"BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ"}
         */

        private String type;
        private ValueBean value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ValueBean getValue() {
            return value;
        }

        public void setValue(ValueBean value) {
            this.value = value;
        }

        public static class ValueBean {
            /**
             * amount : [{"amount":"9223372036854775807","denom":"bht"}]
             * from_address : BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK
             * to_address : BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ
             */

            private String from_address;
            private String to_address;
            private List<AmountBean> amount;

            public String getFrom_address() {
                return from_address;
            }

            public void setFrom_address(String from_address) {
                this.from_address = from_address;
            }

            public String getTo_address() {
                return to_address;
            }

            public void setTo_address(String to_address) {
                this.to_address = to_address;
            }

            public List<AmountBean> getAmount() {
                return amount;
            }

            public void setAmount(List<AmountBean> amount) {
                this.amount = amount;
            }

            public static class AmountBean {
                /**
                 * amount : 9223372036854775807
                 * denom : bht
                 */

                private String amount;
                private String denom;

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getDenom() {
                    return denom;
                }

                public void setDenom(String denom) {
                    this.denom = denom;
                }
            }
        }
    }
}
