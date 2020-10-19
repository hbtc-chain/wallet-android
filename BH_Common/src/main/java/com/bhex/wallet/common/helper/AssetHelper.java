package com.bhex.wallet.common.helper;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/19
 * Time: 14:23
 */
public class AssetHelper {

    /**
     * 地址掩码
     */
    public static void proccessAddress(AppCompatTextView tv_address, String address){
        StringBuffer buf = new StringBuffer("");
        if(!TextUtils.isEmpty(address)){
            buf.append(address.substring(0,9))
                    .append("***")
                    .append(address.substring(address.length()-9,address.length()));
            tv_address.setText(buf.toString());
        }

    }
}
