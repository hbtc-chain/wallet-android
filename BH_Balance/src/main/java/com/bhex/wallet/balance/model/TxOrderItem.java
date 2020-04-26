package com.bhex.wallet.balance.model;

import com.bhex.wallet.common.tx.TransactionOrder;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/14
 * Time: 18:17
 */
public class TxOrderItem {


    public TxOrderItem() {
    }

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



    public String hash;
    public int height;
    public String fee;
    public boolean success;
    public Object error_message;
    public long time;
    public int gas_used;
    public int gas_wanted;
    public String memo;
    public String value;
    //public String txType;
    public List<TxOrderItem.ActivitiesBean> activities;

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

    public List<TxOrderItem.ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<TxOrderItem.ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class ActivitiesBean {
        /**
         * type : cosmos-sdk/MsgSend
         * value : {"amount":[{"amount":"9223372036854775807","denom":"bht"}],"from_address":"BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK","to_address":"BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ"}
         */

        public String type;
        //public String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        /*public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }*/

        public static class ValueBean {
            /**
             * amount : [{"amount":"9223372036854775807","denom":"bht"}]
             * from_address : BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK
             * to_address : BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ
             */

            public String from_address;
            public String to_address;
            public List<TxOrderItem.ActivitiesBean.ValueBean.AmountBean> amount;

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

            public List<TxOrderItem.ActivitiesBean.ValueBean.AmountBean> getAmount() {
                return amount;
            }

            public void setAmount(List<TxOrderItem.ActivitiesBean.ValueBean.AmountBean> amount) {
                this.amount = amount;
            }

            public static class AmountBean {
                /**
                 * amount : 9223372036854775807
                 * denom : bht
                 */

                public String amount;
                public String denom;

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

        public static class AddressGenBean{
            public String from;
            public String to;
            public String order_id;
            public String symbol;
        }
    }




}
