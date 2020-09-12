package com.bhex.wallet.common.bridge;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.bhex.wallet.common.manager.BHUserManager;

public class AndroidJsInterface {
    private Context mContext;

    public AndroidJsInterface(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public String loadCurrentWallet(){
       return BHUserManager.getInstance().getCurrentBhWallet().address;
    }
}
