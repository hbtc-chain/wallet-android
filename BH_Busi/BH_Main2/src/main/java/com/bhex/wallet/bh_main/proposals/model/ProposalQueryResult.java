package com.bhex.wallet.bh_main.proposals.model;

import java.util.List;

/**
 * Created by BHEX.
 * User: zhou chang
 * Date: 2020/4/21
 */
public class ProposalQueryResult {

    /**
     * page : 1
     * page_size : 20
     * proposals : [{"id":"4","type":"cosmos-sdk/TextProposal","title":"Adjustment of blocks_per_year to come aligned with actual block time","description":"This governance proposal is for adjustment of blocks_per_year parameter to normalize the inflation rate and reward rate.\\n ipfs link: https://ipfs.io/ipfs/QmXqEBr56xeUzFpgjsmDKMSit3iqnKaDEL4tabxPXoz9xc","submit_time":1583625427,"deposit_end_time":1584489427,"voting_start_time":1583645427,"voting_end_time":1584499427,"status":3,"total_deposit":"512100000 bht","result":{"yes":"97118903526799","no":"402380577234","abstain":"320545400000","no_with_veto":"0"},"voting_proportion":"80.12"},{"id":"3","type":"cosmos-sdk/TextProposal","title":"ATOM Transfer Enablement","description":"A plan is proposed to set up a testnet using the Cosmos SDK v0.34.0 release, along with mainnet conditions, plus transfer enablement and increased block size, as a testing ground. Furthermore, a path for upgrading the cosmoshub-1 chain to use the Cosmos SDK release v0.34.0, along with the necessary updates to the genesis file, at block 425000, is outlined. IPFS: https://ipfs.io/ipfs/QmaUaMjXPE6i4gJR1NakQc15TZpSqjSrXNmrS1vA5veF9W","submit_time":1583625427,"deposit_end_time":1584489427,"voting_start_time":1583645427,"voting_end_time":1584499427,"status":4,"total_deposit":"562100000 bht","result":{"yes":"5195610593628","no":"58322135404940","abstain":"2619844783500","no_with_veto":"43483296883256"},"voting_proportion":"85.24"},{"id":"2","type":"cosmos-sdk/TextProposal","title":"Cosmos Guardian community pool grant request","description":"A plan is proposed to set up a testnet using the Cosmos SDK v0.34.0 release, along with mainnet conditions, plus transfer enablement and increased block size, as a testing ground. Furthermore, a path for upgrading the cosmoshub-1 chain to use the Cosmos SDK release v0.34.0, along with the necessary updates to the genesis file, at block 425000, is outlined. IPFS: https://ipfs.io/ipfs/QmaUaMjXPE6i4gJR1NakQc15TZpSqjSrXNmrS1vA5veF9W","submit_time":1583625427,"deposit_end_time":1584489427,"status":5,"total_deposit":"562100000 bht"},{"id":"1","type":"cosmos-sdk/TextProposal","title":"Are Validators Charging 0% Commission Harmful to the Success of the Cosmos Hub?","description":"A plan is proposed to set up a testnet using the Cosmos SDK v0.34.0 release, along with mainnet conditions, plus transfer enablement and increased block size, as a testing ground. Furthermore, a path for upgrading the cosmoshub-1 chain to use the Cosmos SDK release v0.34.0, along with the necessary updates to the genesis file, at block 425000, is outlined. IPFS: https://ipfs.io/ipfs/QmaUaMjXPE6i4gJR1NakQc15TZpSqjSrXNmrS1vA5veF9W","submit_time":1583625427,"deposit_end_time":1584489427,"voting_start_time":1583645427,"voting_end_time":1584499427,"status":2,"total_deposit":"562100000 bht"}]
     * total : 4
     */

    private int page;
    private int page_size;
    private int total;
    public List<ProposalInfo> items;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ProposalInfo> getProposals() {
        return items;
    }

    public void setProposals(List<ProposalInfo> proposals) {
        this.items = proposals;
    }


}
