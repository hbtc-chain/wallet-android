package com.bhex.wallet.bh_main.my.helper;

import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.PackageUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.model.SecuritySettingItem;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.enums.CURRENCY_TYPE;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.manager.SecuritySettingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:23
 */
public class MyHelper {

    public static List<MyItem> getAllItems(Context context){

        List<MyItem> myItems = new ArrayList<>();

        String [] res = context.getResources().getStringArray(R.array.my_list_item);
        MyItem item = null;
        for (int i = 0; i < res.length; i++) {
            if (i == 5) {
                item = new MyItem(i,res[i], true, "v"+PackageUtils.getVersionName(context)+"("+PackageUtils.getVersionCode(context)+")");
            } else {
                item = new MyItem(i,res[i], true, "");
            }
            myItems.add(item);
        }

        if(BHUserManager.getInstance().getCurrentBhWallet().getWay()!= MAKE_WALLET_TYPE.创建助记词.getWay()
            || BHUserManager.getInstance().getCurrentBhWallet().isBackup==1){
            myItems.remove(0);
        }

        return myItems;
    }

    /**
     * 地址掩码
     */
    public static void proccessAddress(AppCompatTextView tv_address, String address){
        StringBuffer buf = new StringBuffer("");
        if(!TextUtils.isEmpty(address)){
            buf.append(address.substring(0,10))
                    .append("***")
                    .append(address.substring(address.length()-10,address.length()));
            tv_address.setText(buf.toString());
        }

    }


    public static List<MyItem> getSettingItems(Context context){

        List<MyItem> myItems = new ArrayList<>();

        int  selectIndex = LocalManageUtil.getSetLanguageLocaleIndex(context);

        String []langArray = context.getResources().getStringArray(R.array.app_language_type);

        String [] res = context.getResources().getStringArray(R.array.set_list_item);

        for (int i = 0; i < res.length; i++) {
            MyItem item = new MyItem(i,res[i], true, "");
            myItems.add(item);
        }
        myItems.get(0).rightTxt = langArray[selectIndex-1];
        //语言
        CURRENCY_TYPE.initCurrency(context);
        String currency_name = MMKVManager.getInstance().mmkv().decodeString(BHConstants.CURRENCY_USED,CURRENCY_TYPE.USD.shortName);

        myItems.get(1).rightTxt = CURRENCY_TYPE.getValue(currency_name).name+"("+CURRENCY_TYPE.getValue(currency_name)+")";

        //
        String []secTipsArray = context.getResources().getStringArray(R.array.security_list);
        myItems.get(5).rightTxt = SecuritySettingManager.getInstance().thirty_in_time?secTipsArray[1]:secTipsArray[0];
        return myItems;
    }

    public static String getTitle(Context context,int position){
        String [] res = context.getResources().getStringArray(R.array.my_list_item);
        return res[position];
    }


    public static BHBalance getBthBalanceWithAccount(AccountInfo accountInfo){
        if(accountInfo==null){
            return null;
        }
        BHBalance balance = new BHBalance();
        balance.amount="";
        balance.chain= BHConstants.BHT_TOKEN;
        balance.symbol = BHConstants.BHT_TOKEN;

        List<AccountInfo.AssetsBean> assetsBeanList = accountInfo.assets;
        if(assetsBeanList==null || assetsBeanList.size()==0){
            return balance;
        }

        for(AccountInfo.AssetsBean assetsBean:assetsBeanList){
            if(assetsBean.symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
                balance.symbol = assetsBean.symbol;
                BHToken bhToken = SymbolCache.getInstance().getBHToken(balance.symbol);
                balance.chain = bhToken.chain;
                balance.amount = assetsBean.amount;
                balance.frozen_amount = assetsBean.frozen_amount;
                balance.address = assetsBean.external_address;
            }
        }


        return balance;
    }

    public static String getAmountForUser(BaseActivity context, String amount, String frozen_amount, String symbol) {
        SymbolCache symbolCache = CacheCenter.getInstance().getSymbolCache();
        BHToken bhToken = symbolCache.getBHToken(symbol.toLowerCase());
        int decimals = bhToken!=null?bhToken.decimals:2;
        decimals = 0;
        String tmp = NumberUtil.sub(TextUtils.isEmpty(amount)?"0":amount,TextUtils.isEmpty(frozen_amount)?"0":frozen_amount);

        double displayAmount = NumberUtil.divide(tmp, Math.pow(10,decimals)+"");

        return NumberUtil.dispalyForUsertokenAmount4Level(String.valueOf(displayAmount));
    }

    //安全设置
    public static List<SecuritySettingItem> getSecSettingItems(Context context){
        List<SecuritySettingItem> myItems = new ArrayList<>();
        String [] res = context.getResources().getStringArray(R.array.security_list);
        for (int i = 0; i < res.length; i++) {
            if(i==0){
                SecuritySettingItem item = new SecuritySettingItem(i,res[i],!SecuritySettingManager.getInstance().thirty_in_time);
                myItems.add(item);
            }else if(i==1){
                SecuritySettingItem item = new SecuritySettingItem(i,res[i], SecuritySettingManager.getInstance().thirty_in_time);
                myItems.add(item);
            }
        }
        return myItems;
    }

    //关于我们
    public static List<MyItem> getAboutUs(Context context){
        List<MyItem> myItems = new ArrayList<>();
        String []res = context.getResources().getStringArray(R.array.about_us_list);
        for (int i = 0; i < res.length; i++) {
            String right_text="";
            /*if(i==1){
                right_text =  "v"+PackageUtils.getVersionName(context)+"("+PackageUtils.getVersionCode(context)+")";
            }*/
            MyItem item = new MyItem(i,res[i], true, "");

            myItems.add(item);
        }
        return myItems;
    }
}
