package com.bhex.wallet.balance.presenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.Balance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/18
 * Time: 0:23
 */
public class BalancePresenter extends BasePresenter {

    public BalancePresenter(BaseActivity activity) {
        super(activity);
    }

    public List<Balance> makeBalanceList(){

        List<Balance> list = new ArrayList<>();

        Balance btc = new Balance(R.mipmap.ic_btc,"BTC");
        list.add(btc);

        Balance usdt = new Balance(R.mipmap.ic_usdt,"USDT");
        list.add(usdt);

        Balance eth = new Balance(R.mipmap.ic_eth,"ETH");
        list.add(eth);

        Balance eos = new Balance(R.mipmap.ic_eos,"EOS");
        list.add(eos);

        return list;
    }
}
