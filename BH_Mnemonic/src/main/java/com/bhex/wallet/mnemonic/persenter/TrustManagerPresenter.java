package com.bhex.wallet.mnemonic.persenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/14
 * Time: 22:55
 */
public class TrustManagerPresenter extends BasePresenter {

    public TrustManagerPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     *
     * @return
     */
    public List<BHWalletItem> getAllBHWalletItem(){
        List<BHWalletItem> result = null;
        List<BHWallet> origin = BHUserManager.getInstance().getAllWallet();
        if(origin!=null && origin.size()>0){
            result = new ArrayList<>();
            for (BHWallet tmp:origin) {
                BHWalletItem item = BHWalletItem.makeBHWalletItem(tmp);
                result.add(item);
            }
        }
        return result;
    }
}
