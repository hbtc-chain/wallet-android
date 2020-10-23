package com.bhex.wallet.common.download

import java.io.Serializable

/**
 * @author gongdongyang
 * 2020-5-9 15:38:58
 * 下载实体类
 */
data class DownloadInfo (val downloadUrl:String,val apkVersion:String) :Serializable {
    public var apkLocalPath:String? = null

    fun getApkFileName():String{
        var sb = StringBuffer("");
        sb.append("v").append(this.apkVersion).append("-bhwallet.apk");
        return sb.toString()
    }
}