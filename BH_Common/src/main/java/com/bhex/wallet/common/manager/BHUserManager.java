package com.bhex.wallet.common.manager;

import com.bhex.wallet.common.db.entity.BHWalletExt;
import com.tencent.mmkv.MMKV;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 23:56
 */
public class BHUserManager {

    private BHWalletExt bhWalletExt;

    private List<BHWalletExt> allWallet;

    private static volatile BHUserManager  _INSTANCE;

    private BHUserManager(){
        bhWalletExt = new BHWalletExt();
    }

    public static BHUserManager getInstance(){
        if(_INSTANCE==null){
            synchronized (BHUserManager.class){
                if(_INSTANCE==null){
                    _INSTANCE = new BHUserManager();
                }
            }
        }
        return _INSTANCE;
    }

    public BHWalletExt getBhWallet(){
        return bhWalletExt;
    }

    public void setBhWalletExt(BHWalletExt bhWalletExt) {
        this.bhWalletExt = bhWalletExt;
    }

    public void setAllWallet(List<BHWalletExt> allWallet) {
        this.allWallet = allWallet;
    }

    public List<BHWalletExt> getAllWallet() {
        return allWallet;
    }

    public boolean isHasWallet(){
        if(bhWalletExt.getId()>0){
            return true;
        }
        return false;
    }
}
