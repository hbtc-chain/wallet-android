package com.bhex.wallet.common.download;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;

import com.bhex.network.app.BaseApplication;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.R;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Func1;


/**
 * @author gongdongyang
 * 下载
 * 2020-5-9 14:57:36
 */
public class ApkDownLoadService  extends IntentService {

    private long downId;

    private DownloadInfo dlInfo;

    private DownloadManager dwManager;

    private NotificationCompat.Builder builder;

    private NotificationManager notificationManager;

    private Notification notification;

    //private Disposable disposable;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    //是否完成
    private boolean isCompleted;

    public ApkDownLoadService() {
        super("ApkDownLoadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(dlInfo==null || dwManager==null){
            this.dlInfo = (DownloadInfo) intent.getSerializableExtra("taskInfo");
            downloadApk(dlInfo);
            initNotification();
            //openFile();
            return;
        }

        dispose();


    }

    /**
     * 通知
     */
    @SuppressLint("StringFormatMatches")
    private void initNotification() {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "channel_1";
            NotificationChannel channel = new NotificationChannel(id, "Primary Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(Color.RED);

            channel.enableVibration(true);

            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, id);
        }else{
            builder = new NotificationCompat.Builder(this, null);
        }
        builder.setContentTitle(getString(R.string.update_prepare, new Object[] { "Bluehelix" }))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(4)
                .setPriority(2)
                .setAutoCancel(false)
                .setContentText(String.format(getString(R.string.update_prepare), new Object[] { Integer.valueOf(1), "%" }))
                .setProgress(100, 0, false);
        notification = this.builder.build();
    }

    /**
     *
     * @param dlInfo
     */
    private void downloadApk(DownloadInfo dlInfo) {
        dwManager = (DownloadManager) getSystemService(Service.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(dlInfo.getDownloadUrl());
        //下载请求
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //
        request.setTitle(getString(R.string.update_prepare, new Object[] { "Bluehelix" }));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);

        request.setAllowedOverRoaming(false);

        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();

        File file = new File(dlInfo.getApkLocalPath());
        if (file.exists()){
            file.delete();

        }
        request.setDestinationUri(Uri.fromFile(file));
        downId = this.dwManager.enqueue(request);

        sendApkUpdate();
    }

    /**
     * 下载更新
     */
    public void sendApkUpdate(){
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(aLong -> {
                    return isCompleted;
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        checkDownloadStatus();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("ApkDownLoadService===>","==onComplete====");
                        dispose();
                    }
                });
    }

    /**
     * 更新下载状态
     */
    private void checkDownloadStatus(){
        DownloadManager.Query query = new DownloadManager.Query();
        //筛选下载任务
        query.setFilterById(downId);
        Cursor cursor = dwManager.query(query);
        if (cursor.moveToFirst()) {
            //已下载
            long downloadedBytes =
                    cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            //文件大小
            long totalBytes = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            // 下载状态
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_RUNNING:
                    int progress = (int)((downloadedBytes*100)/totalBytes);
                    builder.setContentText(String.format(getString(R.string.update_progress), new Object[] { progress, "%" }));
                    //builder.setProgress(100,progress,false);
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //builder.setContentText(String.format(getString(R.string.update_progress), new Object[] { progress, "%" }));
                    isCompleted = true;
                    dispose();
                    openFile();
                    break;
            }
        }
    }

    /**
     * 打开文件安装
     */
    public void openFile(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(dlInfo.getApkLocalPath());

        LogUtils.d("ApkDownLoadService===>","file===="+file.getAbsolutePath());
        Uri  data = FileProvider.getUriForFile(getApplication(), "com.bluehelix.wallet.fileprovider", file);
        // 给目标应用一个临时授权
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(data,
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


    private void dispose(){
        if(mCompositeDisposable!=null &&!mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
    }

}
