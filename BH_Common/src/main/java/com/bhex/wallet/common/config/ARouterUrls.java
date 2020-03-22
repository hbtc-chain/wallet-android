package com.bhex.wallet.common.config;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/21
 * Time: 22:30
 */
public enum  ARouterUrls {
    修改密码("/my/update/password");

    public String goUrl;

    ARouterUrls(String goUrl) {
        this.goUrl = goUrl;
    }

    public String getGoUrl() {
        return goUrl;
    }

    public void setGoUrl(String goUrl) {
        this.goUrl = goUrl;
    }
}
