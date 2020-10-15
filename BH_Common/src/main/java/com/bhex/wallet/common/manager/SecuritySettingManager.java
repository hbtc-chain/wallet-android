package com.bhex.wallet.common.manager;

import com.bhex.wallet.common.db.entity.BHWallet;

/**
 * @author gongdongyang
 * 2020-10-16 01:22:43
 */
public class SecuritySettingManager {

    private static volatile SecuritySettingManager _instance = new SecuritySettingManager();

    public static final String every_time_pwd_label = "every_time_pwd";
    public static final String thirty_in_time_label = "thirty_in_time";

    public boolean every_time_pwd = true;
    public boolean thirty_in_time = false;


    private SecuritySettingManager(){

    }

    public static SecuritySettingManager getInstance(){
        return _instance;
    }

    public void initSecuritySetting(){
        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        every_time_pwd = MMKVManager.getInstance().mmkv().decodeBool(every_time_pwd_label+"_"+bhWallet.address,
                every_time_pwd);
        thirty_in_time =  MMKVManager.getInstance().mmkv().decodeBool(thirty_in_time_label+"_"+bhWallet.address,
                thirty_in_time);
    }

    public void save_every_time_pwd(boolean checked ){
        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        String key = every_time_pwd_label+"_"+bhWallet.address;
        SecuritySettingManager.getInstance().every_time_pwd = checked;
        MMKVManager.getInstance().mmkv().encode(key,checked);
    }

    public void save_thirty_in_time(boolean checked) {
        BHWallet bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        String key = thirty_in_time_label+"_"+bhWallet.address;
        SecuritySettingManager.getInstance().thirty_in_time = checked;
        MMKVManager.getInstance().mmkv().encode(key,checked);
    }

    public void recordPwd(String inputPwd) {
        if(!every_time_pwd || thirty_in_time){
            BHWallet currentBHWallet = BHUserManager.getInstance().getCurrentBhWallet();
            currentBHWallet.pwd = inputPwd;
        }
    }

    public boolean notNeedPwd(){
        return (!every_time_pwd || thirty_in_time);
    }


}
