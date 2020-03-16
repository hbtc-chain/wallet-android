package com.bhex.wallet.common.manager;

import com.bhex.wallet.common.db.entity.BHWallet;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 23:56
 */
public class BHUserManager {

    private BHWallet bhWalletExt;

    private List<BHWallet> allWallet;

    private Class targetClass;

    private static volatile BHUserManager  _INSTANCE;

    private BHUserManager(){
        bhWalletExt = new BHWallet();
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

    public BHWallet getBhWallet(){
        return bhWalletExt;
    }

    public void setBhWalletExt(BHWallet bhWalletExt) {
        this.bhWalletExt = bhWalletExt;
    }

    public void setAllWallet(List<BHWallet> allWallet) {
        this.allWallet = allWallet;
    }

    public List<BHWallet> getAllWallet() {
        return allWallet;
    }

    public boolean isHasWallet(){
        if(bhWalletExt.id>0){
            return true;
        }
        return false;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
}
