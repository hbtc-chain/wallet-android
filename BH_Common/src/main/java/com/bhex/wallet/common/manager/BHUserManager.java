package com.bhex.wallet.common.manager;

import android.text.TextUtils;
import android.util.Log;

import com.bhex.network.app.BaseApplication;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.FileUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.kenai.jffi.Main;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 23:56
 */
public class BHUserManager {

    private final static String TAG = BHUserManager.class.getSimpleName();

    private BHWallet tmpBhWallet;

    //当前使用的钱包
    private BHWallet mCurrentBhWallet;

    private Credentials tmpCredentials;

    //资产账户信息
    private AccountInfo mAccountInfo;

    private List<BHWallet> allWallet;

    //助记词列表
    private List<String> mWordList;

    private Class targetClass;

    private static volatile BHUserManager  _INSTANCE;

    private BHUserManager(){
        tmpBhWallet = new BHWallet();
        mCurrentBhWallet = new BHWallet();
        initWord();
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

    private void initWord(){
        try{
            String res = FileUtil.loadStringByAssets(BaseApplication.getInstance(),"en-mnemonic-word-list.txt");
            if(!TextUtils.isEmpty(res)){
                mWordList = Arrays.asList(res.split(" "));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BHWallet getTmpBhWallet() {
        return tmpBhWallet;
    }

    /*public void setTmpBhWallet(BHWallet tmpBhWallet) {
        this.tmpBhWallet = tmpBhWallet;
    }*/

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

    public AccountInfo getAccountInfo() {
        return mAccountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.mAccountInfo = accountInfo;
    }

    public List<String> getWordList() {
        return mWordList;
    }

    public void saveUserBalanceList(List<BHBalance> list){
        if(list==null || list.size()==0){
            return;
        }
        StringBuffer buffer = new StringBuffer("");
        for (BHBalance item:list) {
            buffer.append(item.symbol).append("_");
        }
        buffer.delete(buffer.length()-1,buffer.length());

        String key = BHUserManager.getInstance().mCurrentBhWallet.getAddress()+"_balance";
        MMKVManager.getInstance().mmkv().encode(key,buffer.toString());
    }

    public String getUserBalanceList(){
        String key = BHUserManager.getInstance().mCurrentBhWallet.getAddress()+"_balance";
        String result = MMKVManager.getInstance().mmkv().decodeString(key, BHConstants.COIN_DEFAULT_LIST);
        return result;
    }

    public String getSymbolList(){
        String symbol = MMKVManager.getInstance().mmkv().decodeString(BHConstants.SYMBOL_DEFAULT_KEY, BHConstants.COIN_DEFAULT_LIST);
        return symbol;
    }

    /**
     * 解密私钥
     */
    public String getOriginContext(String content){
        String result = "";
        try{
            result = CryptoUtil.decryptPK(content,mCurrentBhWallet.password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void setTmpCredentials(Credentials tmpCredentials) {
        this.tmpCredentials = tmpCredentials;
    }

    public Credentials getTmpCredentials() {
        return tmpCredentials;
    }

    public void clear(){
        tmpCredentials = null;
        targetClass = null;
        tmpBhWallet = new BHWallet();
        //mAccountInfo =
    }
}
