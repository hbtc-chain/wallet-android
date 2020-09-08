package com.bhex.wallet.market.ui.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MappingSymbolManager {

    private static MappingSymbolManager _instance = new MappingSymbolManager();

    public Map<String,MappingSymbol> mappingSymbolMap;

    private MappingSymbolManager(){
        mappingSymbolMap = new HashMap<>();
        loadMappingSymbol();
    }

    private void loadMappingSymbol(){
        MappingSymbol mappingSymbol = new MappingSymbol();
        mappingSymbol.fromToken = "BTC";
        mappingSymbol.toToken = "CBTC";
        mappingSymbol.issueToken = "CBTC";
        mappingSymbolMap.put(mappingSymbol.fromToken,mappingSymbol);

        MappingSymbol cbtc_mappingSymbol = new MappingSymbol();
        cbtc_mappingSymbol.fromToken = "CBTC";
        cbtc_mappingSymbol.toToken = "BTC";
        cbtc_mappingSymbol.issueToken = "CBTC";
        mappingSymbolMap.put(cbtc_mappingSymbol.fromToken,cbtc_mappingSymbol);
    }

    public static MappingSymbolManager getInstance(){
        return _instance;
    }

}
