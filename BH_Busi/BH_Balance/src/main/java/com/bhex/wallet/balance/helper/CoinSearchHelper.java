package com.bhex.wallet.balance.helper;

import android.util.ArrayMap;

import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongdongyang
 * 2020年10月31日17:28:53
 */
public class CoinSearchHelper {

    public static List<BHToken> loadVerifiedToken(String chain) {
        ArrayMap<String,BHToken> map_tokens = CacheCenter.getInstance().getSymbolCache().getVerifiedToken();
        List<BHToken> res = new ArrayList<>();
        for (ArrayMap.Entry<String,BHToken> item : map_tokens.entrySet()) {
            if (!item.getValue().chain.equalsIgnoreCase(chain)) {
                continue;
            }
            res.add(item.getValue());
        }
        return res;
    }

    public static boolean isExistDefaultToken(String symbol) {
        ArrayMap<String,BHToken> map_tokens = CacheCenter.getInstance().getSymbolCache().getDefaultToken();
        if(map_tokens.get(symbol)!=null){
            return true;
        }
        return false;
    }



}
