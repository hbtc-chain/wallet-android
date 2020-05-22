package com.bhex.wallet.common.manager;

import androidx.appcompat.app.AppCompatDelegate;

import com.bhex.tools.constants.BHConstants;
import com.tencent.mmkv.MMKV;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/8
 * Time: 18:12
 */
public class MMKVManager {

    private  MMKV mmkv;

    private static volatile MMKVManager  _INSTANCE;

    private MMKVManager(){

        mmkv = MMKV.defaultMMKV();
    }

    public static MMKVManager getInstance(){
        if(_INSTANCE==null){
            synchronized (MMKVManager.class){
                if(_INSTANCE==null){
                    _INSTANCE = new MMKVManager();
                }
            }
        }
        return _INSTANCE;
    }

    public MMKV mmkv() {
        return mmkv;
    }


    public int getSelectNightMode(){
        return mmkv.decodeInt(BHConstants.THEME_MODEL, AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void setSelectNightMode(int model){
        mmkv.encode(BHConstants.THEME_MODEL, model);
    }


}
