package com.bhex.wallet.common.download;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * @author gongdongyang
 * 下载
 * 2020-5-9 14:57:36
 */
public class ApkDownLoadService  extends IntentService {

    private DownloadInfo dlInfo;

    private DownloadManager dwManager;

    public ApkDownLoadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.dlInfo = (DownloadInfo) intent.getSerializableExtra("taskInfo");
    }
}
