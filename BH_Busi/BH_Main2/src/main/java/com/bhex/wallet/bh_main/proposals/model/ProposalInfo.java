package com.bhex.wallet.bh_main.proposals.model;

/**
 * Created by BHEX.
 * User: zhou chang
 * Date: 2020/4/21
 */
public class ProposalInfo {

    /**
     * id : 4
     * type : cosmos-sdk/TextProposal
     * title : Adjustment of blocks_per_year to come aligned with actual block time
     * description : This governance proposal is for adjustment of blocks_per_year parameter to normalize the inflation rate and reward rate.\n ipfs link: https://ipfs.io/ipfs/QmXqEBr56xeUzFpgjsmDKMSit3iqnKaDEL4tabxPXoz9xc
     * submit_time : 1583625427
     * deposit_end_time : 1584489427
     * voting_start_time : 1583645427
     * voting_end_time : 1584499427
     * status : 3
     * total_deposit : 512100000 bht
     * result : {"yes":"97118903526799","no":"402380577234","abstain":"320545400000","no_with_veto":"0"}
     * voting_proportion : 80.12
     */

    private String id;
    private String type;
    private String title;
    private String description;
    private long submit_time;
    private long deposit_end_time;
    private long voting_start_time;
    private long voting_end_time;
    private int status;
    private String total_deposit;
    private ResultBean result;
    private String voting_proportion;
    /**
     * id : 2
     * proposer : HBCgS6KSUhmudwbh88tRynzVu86fFghFA6Pg
     * submit_time : 1587212478
     * deposit_end_time : 0
     * voting_start_time : 1587212478
     * deposit_threshold : 10000000
     */

    private String proposer;
    private String deposit_threshold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(long submit_time) {
        this.submit_time = submit_time;
    }

    public long getDeposit_end_time() {
        return deposit_end_time;
    }

    public void setDeposit_end_time(long deposit_end_time) {
        this.deposit_end_time = deposit_end_time;
    }

    public long getVoting_start_time() {
        return voting_start_time;
    }

    public void setVoting_start_time(long voting_start_time) {
        this.voting_start_time = voting_start_time;
    }

    public long getVoting_end_time() {
        return voting_end_time;
    }

    public void setVoting_end_time(long voting_end_time) {
        this.voting_end_time = voting_end_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotal_deposit() {
        return total_deposit;
    }

    public void setTotal_deposit(String total_deposit) {
        this.total_deposit = total_deposit;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getVoting_proportion() {
        return voting_proportion;
    }

    public void setVoting_proportion(String voting_proportion) {
        this.voting_proportion = voting_proportion;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getDeposit_threshold() {
        return deposit_threshold;
    }

    public void setDeposit_threshold(String deposit_threshold) {
        this.deposit_threshold = deposit_threshold;
    }

    public static class ResultBean {
        /**
         * yes : 97118903526799
         * no : 402380577234
         * abstain : 320545400000
         * no_with_veto : 0
         */

        private String yes;
        private String no;
        private String abstain;
        private String no_with_veto;

        public String getYes() {
            return yes;
        }

        public void setYes(String yes) {
            this.yes = yes;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getAbstain() {
            return abstain;
        }

        public void setAbstain(String abstain) {
            this.abstain = abstain;
        }

        public String getNo_with_veto() {
            return no_with_veto;
        }

        public void setNo_with_veto(String no_with_veto) {
            this.no_with_veto = no_with_veto;
        }
    }
}
