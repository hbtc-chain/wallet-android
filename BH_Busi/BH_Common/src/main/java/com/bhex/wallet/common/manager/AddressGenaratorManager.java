package com.bhex.wallet.common.manager;

import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.annotation.IntDef;

import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gongdongyang
 * 2021-1-1 09:42:16
 */
public class AddressGenaratorManager {
    public static final  Integer ADDRESS_NO = 0;
    public static final  Integer ADDRESS_ING = 1;
    public static final  Integer ADDRESS_EXISTS = 2;

    private static AddressGenaratorManager _instance = new AddressGenaratorManager();

    public ArrayMap<String,Integer> map = new ArrayMap<>();

    public static AddressGenaratorManager getInstance(){
        return _instance;
    }

    public void removeAddressStatus(AccountInfo accountInfo) {
        if(ToolUtils.checkListIsEmpty(accountInfo.assets)){
            map.clear();
        }

        for(AccountInfo.AssetsBean assetsBean:accountInfo.assets){
            //
            BHToken bhToken = SymbolCache.getInstance().getBHToken(assetsBean.symbol);
            /*if(!TextUtils.isEmpty(assetsBean.external_address)){
                if(map.get(bhToken.chain)!=null){
                    map.remove(bhToken.chain);
                }
            }*/
            if(!TextUtils.isEmpty(assetsBean.external_address) && map.get(bhToken.chain)!=null){
                map.remove(bhToken.chain);
            }
        }
    }

    /*public class AddressGenaratorAction{
        public @AddressStatus int addressStatus;
    }


    public  void updateStatus(){

    }


    @IntDef({ADDRESS_NO,ADDRESS_ING,ADDRESS_EXISTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AddressStatus {}*/

}
