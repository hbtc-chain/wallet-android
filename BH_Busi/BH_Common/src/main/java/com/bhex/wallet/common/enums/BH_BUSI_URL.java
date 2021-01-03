package com.bhex.wallet.common.enums;

import android.content.Context;

import com.bhex.tools.language.LocalManageUtil;
import com.bhex.wallet.common.config.ARouterConfig;

import java.util.Locale;

public  enum BH_BUSI_URL{
    帮助中心(1, ARouterConfig.英文.帮助中心,ARouterConfig.中文.帮助中心),
    公告(2,ARouterConfig.英文.公告,ARouterConfig.中文.公告),
    服务协议(3,ARouterConfig.英文.HBTC_Wallet服务协议,ARouterConfig.中文.HBTC_Wallet服务协议),
    联系我们(4,ARouterConfig.英文.联系我们,ARouterConfig.中文.联系我们),
    版本更新日志(5,ARouterConfig.英文.版本更新日志,ARouterConfig.中文.版本更新日志),
    审计报告 (6,ARouterConfig.英文.审计报告,ARouterConfig.中文.审计报告);
    public int index;
    public String en_url;
    public String zh_url;

    BH_BUSI_URL(int index, String en_url, String zh_url) {
        this.index = index;
        this.en_url = en_url;
        this.zh_url = zh_url;
    }

    public String getGotoUrl(Context context){
        Locale locale = LocalManageUtil.getSetLanguageLocale(context);
        if(locale.getLanguage().contains("zh")){
            return zh_url;
        }
        return en_url;
    }
}
