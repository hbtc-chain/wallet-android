package com.bhex.wallet.common.helper;

import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/5/2
 * Time: 18:00
 */
public class BHWalletHelper {

    //判断导入或创建的钱包是否已经存在
    public static boolean isExistBHWallet(BHWallet wallet){
        boolean flag = false;
        List<BHWallet> bhWallets = BHUserManager.getInstance().getAllWallet();
        if(bhWallets==null || bhWallets.size()==0){
            return flag;
        }

        for(BHWallet bhWallet:bhWallets){
            if(bhWallet.address.equals(wallet.address)){
                return true;
            }
        }
        return flag;
    }

    //判断导入或创建的钱包是否已经存在
    public static boolean isExistBHWallet(String address){
        boolean flag = false;
        List<BHWallet> bhWallets = BHUserManager.getInstance().getAllWallet();
        if(bhWallets==null || bhWallets.size()==0){
            return flag;
        }

        for(BHWallet bhWallet:bhWallets){
            if(bhWallet.address.equals(address)){
                return true;
            }
        }
        return flag;
    }
}
