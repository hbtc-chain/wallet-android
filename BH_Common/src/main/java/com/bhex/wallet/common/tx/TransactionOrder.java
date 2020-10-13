package com.bhex.wallet.common.tx;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 15:42
 */
public class TransactionOrder implements Serializable {

    /**
     * hash : D0DBCDBE1E336E6D751F35FA6ED73B6B75A9BB7D6B0A6025F61DE846BE752E55
     * height : 614
     * fee : 2 hbc
     * success : true
     * error_message : null
     * activities : [{"type":"hbtcchain/transfer/MsgSend","value":{"amount":[{"amount":"1000000000000000000000","denom":"hbc"}],"from_address":"HBCWa9Bsss6ufcrYAo7mQAUJ4duZiQHso5fn","to_address":"HBCU4g4vvvyCTpsMzGe2vW36Bp26g1fSEyFd"}}]
     * time : 1602557644
     * gas_used : 42460
     * gas_wanted : 2000000
     * memo :
     * balance_flows : [{"address":"HBCWa9Bsss6ufcrYAo7mQAUJ4duZiQHso5fn","symbol":"hbc","amount":"-1000"},{"address":"HBCU4g4vvvyCTpsMzGe2vW36Bp26g1fSEyFd","symbol":"hbc","amount":"1000"}]
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
    public List<ActivitiesBean> activities;
    public List<BalanceFlowsBean> balance_flows;

    public TransactionOrder() {
    }

    public static class ActivitiesBean {
        /**
         * type : cosmos-sdk/MsgSend
         * value : {"amount":[{"amount":"9223372036854775807","denom":"bht"}],"from_address":"BHYc5BsYgne5SPNKYreBGpjYY9jyXAHLGbK","to_address":"BHj2wujKtAxw9XZMA7zDDvjGqKjoYUdw1FZ"}
         */

        public String type;

        public JsonObject value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public JsonObject getValue() {
            return value;
        }

        public void setValue(JsonObject value) {
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

        public static class AddressGenBean{
            public String from;
            public String to;
            public String order_id;
            public String symbol;
        }

        public static class WithdrawalBean{
            public String amount;
            public String from_cu;
            public String gas_fee;
            public String order_id;
            public String symbol;
            public String to_multi_sign_address;
        }


        public static class DepositBean{

            /**
             * amount : 500000000000000000
             * from_cu : HBCbV2tuSYE2WG6sHEaxteiZwHbfU559avFC
             * height : 7714737
             * index : 0
             * memo :
             * order_id : a4fe6a96-1221-42d0-bf23-562cdfc2ab0b
             * symbol : eth
             * to_adddress : 0x218da933EAe48436b228FB65E2A57cB92E20Dc25
             * to_cu : HBCYu3Xf77dvNqAceLQQSmtto3utEi4kBd4r
             * txhash : 0x065b2d9d1d2378a1aa1635ffb8ad2b62c952878cc29a2c7e32a7ac8639d73765
             */
            public String amount;
            public String from_cu;
            public String height;
            public int index;
            public String memo;
            public String order_id;
            public String symbol;
            public String to_adddress;
            public String to_cu;
            public String txhash;

        }

        public static class DelegateBean{

            /**
             * amount : {"amount":"20000000000000000000","denom":"hbc"}
             * delegator_address : HBCYu3Xf77dvNqAceLQQSmtto3utEi4kBd4r
             * validator_address : hbcvaloper1we2ufxj2wpanrhzd2h7upw07hffudxfw472txx
             */

            public AmountBean amount;
            public String delegator_address;
            public String validator_address;

            public static class AmountBean {
                /**
                 * amount : 20000000000000000000
                 * denom : hbc
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

        public static class DelegationRewardBean{
            /**
             * amount : {"amount":"1331236960000000000","denom":"hbc"}
             * delegator_address : HBCYu3Xf77dvNqAceLQQSmtto3utEi4kBd4r
             * validator_address : hbcvaloper1j765j4hrkqqj6r88qleknx8070e505hny73e5h
             */

            public AmountBean amount;
            public String delegator_address;
            public String validator_address;
            public static class AmountBean {
                /**
                 * amount : 1331236960000000000
                 * denom : hbc
                 */

                public String amount;
                public String denom;


            }

            /**
             * amount : 1331529719774649893
             * delegator_address : HBCYu3Xf77dvNqAceLQQSmtto3utEi4kBd4r
             * validator_address : hbcvaloper1j765j4hrkqqj6r88qleknx8070e505hny73e5h
             */



        }


        public class SubmitProposalBean{
           public ContentBean content;
            public String proposer;
            public List<InitialDepositBean> initial_deposit;
            public  class ContentBean {
                /**
                 * type : hbtcchain/gov/TextProposal
                 * value : {"description":"tyhhh","title":"yuhhhg"}
                 */
                public String type;
                public ValueBean value;
                public  class ValueBean {
                    /**
                     * description : tyhhh
                     * title : yuhhhg
                     */
                    public String description;
                    public String title;
                }
            }

            public  class InitialDepositBean {
                /**
                 * amount : 100000000000000000000
                 * denom : hbc
                 */
                public String amount;
                public String denom;
            }
        }
    }

    public static class BalanceFlowsBean {
        /**
         * address : HBCWa9Bsss6ufcrYAo7mQAUJ4duZiQHso5fn
         * symbol : hbc
         * amount : -1000
         */

        public String address;
        public String symbol;
        public String amount;
    }
}
