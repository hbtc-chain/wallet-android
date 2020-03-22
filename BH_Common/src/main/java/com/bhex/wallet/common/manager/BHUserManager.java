package com.bhex.wallet.common.manager;

import com.bhex.wallet.common.db.entity.BHWallet;
import com.kenai.jffi.Main;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 23:56
 */
public class BHUserManager {

    private BHWallet tmpBhWallet;

    private BHWallet mCurrentBhWallet;

    private List<BHWallet> allWallet;

    private Class targetClass;

    private static volatile BHUserManager  _INSTANCE;

    private BHUserManager(){
        tmpBhWallet = new BHWallet();
        mCurrentBhWallet = new BHWallet();
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

    public BHWallet getTmpBhWallet() {
        return tmpBhWallet;
    }

    public void setTmpBhWallet(BHWallet tmpBhWallet) {
        this.tmpBhWallet = tmpBhWallet;
    }

    public void setAllWallet(List<BHWallet> allWallet) {
        this.allWallet = allWallet;
    }

    public List<BHWallet> getAllWallet() {
        return allWallet;
    }

    public boolean isHasWallet(){
        if(mCurrentBhWallet.id>0){
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

    public BHWallet getCurrentBhWallet() {
        return mCurrentBhWallet;
    }

    public void setCurrentBhWallet(BHWallet mCurrentBhWallet) {
        this.mCurrentBhWallet = mCurrentBhWallet;
        if(allWallet==null || allWallet.size()<=0){
            return;
        }
        for (int i = 0; i < allWallet.size(); i++) {
            BHWallet item = allWallet.get(i);
            if(item.id==mCurrentBhWallet.id){
                item.setIsDefault(1);
            }else{
                item.setIsDefault(0);
            }
        }
    }


}
