package com.bhex.wallet.common.enums;

import android.content.Context;

import com.bhex.wallet.common.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/18
 * Time: 20:09
 */
public enum  CURRENCY_TYPE {

    CNY("CNY","","¥"),
    USD("USD","","$"),
    KRW("KRW","","₩"),
    JPY("JPY","","¥"),
    VND("VND","","₫");

    public String shortName = "";
    public String name="";
    public String character="";

    CURRENCY_TYPE(String shortName,String name,String character) {
        this.shortName = shortName;
        this.name = name;
        this.character = character;
    }

    public static CURRENCY_TYPE getValue(String shortName) {
        CURRENCY_TYPE[] currencyTypes = values();
        for (CURRENCY_TYPE item : currencyTypes) {
            if (item.shortName.equals(shortName)) {
                return item;
            }
        }
        return null;
    }

    public static void initCurrency(Context context){
        String []currencyArray = context.getResources().getStringArray(R.array.Currency_list);
        CNY.name = currencyArray[0];
        USD.name = currencyArray[1];
        KRW.name = currencyArray[2];
        JPY.name = currencyArray[3];
        VND.name = currencyArray[4];
    }
}
