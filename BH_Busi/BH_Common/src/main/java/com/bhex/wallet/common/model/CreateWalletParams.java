package com.bhex.wallet.common.model;

import java.util.List;

/**
 * @author gongdongyang
 * 2021-1-8 12:15:39
 */
public class CreateWalletParams {
    public int way;//方式
    public String name;//钱包名称
    public List<String> mWords;//助记词
    public String mnemonic;//助记词
    public String privateKey;
    public String ks;
}
