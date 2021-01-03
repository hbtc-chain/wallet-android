package com.bhex.wallet.common.helper;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.common.crypto.wallet.HWallet;
import com.bhex.wallet.common.crypto.wallet.HWalletFile;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

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

    /**
     * 解密私钥
     */
    public static String getOriginPK(String keyStore,String inputPwd){
        String result = "";
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            HWalletFile walletFile = objectMapper.readValue(keyStore, HWalletFile.class);
            Credentials credentials = Credentials.create(HWallet.decrypt(inputPwd, walletFile));
            result = Numeric.toHexStringNoPrefixZeroPadded(credentials.getEcKeyPair().getPrivateKey(), BHConstants.PRIVATE_KEY_LENGTH);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static String getOriginKeyStore(String keyStore){
        String result = "";

        try{
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            HWalletFile walletFile = objectMapper.readValue(keyStore, HWalletFile.class);
            ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {

                @Override
                public boolean shouldSkipField(FieldAttributes fa) {
                    return fa.getName().equals("encMnemonic");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }

            };
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(myExclusionStrategy) // <---
                    .create();
            result = gson.toJson(walletFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 地址掩码
     */
    public static void proccessAddress(AppCompatTextView tv_address, String address){
        StringBuffer buf = new StringBuffer("");
        if(!TextUtils.isEmpty(address)){
            buf.append(address.substring(0,8))
                    .append("***")
                    .append(address.substring(address.length()-8,address.length()));
            tv_address.setText(buf.toString());
        }

    }
}
