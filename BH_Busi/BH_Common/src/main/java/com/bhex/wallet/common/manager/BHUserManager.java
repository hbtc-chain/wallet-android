package com.bhex.wallet.common.manager;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.bhex.network.app.BaseApplication;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.FileUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;

import org.web3j.crypto.Credentials;

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

    private static volatile BHUserManager  _INSTANCE;

    private BHWallet tmpBhWallet;

    //当前使用的钱包
    private BHWallet mCurrentBhWallet;

    private Credentials tmpCredentials;

    //资产账户信息
    private AccountInfo mAccountInfo;

    private List<BHWallet> allWallet;

    //助记词列表
    private List<String> mWordList;

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

    public synchronized void setAllWallet(List<BHWallet> allWallet) {
        this.allWallet = allWallet;
        if(ToolUtils.checkListIsEmpty(allWallet)){
            return;
        }

        for (BHWallet bhWallet:allWallet) {
            if(bhWallet.isDefault==1){
                mCurrentBhWallet = bhWallet;
            }
        }
    }

    public synchronized List<BHWallet> getAllWallet() {
        return allWallet;
    }

    public synchronized boolean isHasWallet(){
        if(mCurrentBhWallet.id>0 || !ToolUtils.checkListIsEmpty(allWallet)){
            return true;
        }
        return false;
    }

    public synchronized BHWallet getCurrentBhWallet() {
        return mCurrentBhWallet;
    }

    public synchronized void setCurrentBhWallet(BHWallet mCurrentBhWallet) {
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

    public synchronized AccountInfo getAccountInfo() {
        return mAccountInfo;
    }

    public synchronized void setAccountInfo(AccountInfo accountInfo) {
        this.mAccountInfo = accountInfo;
    }

    public List<String> getWordList() {
        return mWordList;
    }

    public synchronized void saveUserBalanceList(List<BHBalance> list){
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

    public synchronized String getUserBalanceList(){
        String key = BHUserManager.getInstance().mCurrentBhWallet.getAddress()+"_balance";
        String result = MMKVManager.getInstance().mmkv().decodeString(key, BHConstants.COIN_DEFAULT_LIST);
        return result;
    }

    public synchronized String getSymbolList(){
        /*String symbol = MMKVManager.getInstance().mmkv().decodeString(BHConstants.SYMBOL_DEFAULT_KEY, BHConstants.COIN_DEFAULT_LIST);
        return symbol;*/
        StringBuffer sb = new StringBuffer("");
        ArrayMap<String,BHToken> map_tokens = SymbolCache.getInstance().getLocalToken();
        for(ArrayMap.Entry<String,BHToken> item:map_tokens.entrySet()){
            sb.append(item.getValue().symbol.toUpperCase()).append(",");
        }
        return sb.toString();
    }

    /**
     * 解密私钥
     */
    public String getOriginContext(String content,String inputPwd){
        String result = "";
        try{
            result = CryptoUtil.decryptPK(content,inputPwd);
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
        //targetClass = null;
        MainActivityManager._instance.setTargetClass(null);
        tmpBhWallet = new BHWallet();
    }
}
