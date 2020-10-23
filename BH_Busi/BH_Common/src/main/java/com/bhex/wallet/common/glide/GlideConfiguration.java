package com.bhex.wallet.common.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bhex.tools.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.lang.annotation.Annotation;

/**
 * Created by gongdongyang on 2018/9/17.
 */
@GlideModule
public class GlideConfiguration extends AppGlideModule {

    //缓存大小
    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    public static String GLIDE_PATH;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 配置图片将缓存到SD卡
        ExternalPreferredCacheDiskCacheFactory externalCacheDiskCacheFactory = new ExternalPreferredCacheDiskCacheFactory(context);
        builder.setDiskCache(externalCacheDiskCacheFactory);
        GLIDE_PATH = context.getCacheDir().getPath() + "/GlideCacheFolder";

        LogUtils.d("GlideConfiguration==>","path=="+GLIDE_PATH);
        builder.setDiskCache(new DiskLruCacheFactory(context.getCacheDir().getPath() + "/GlideCacheFolder", DISK_CACHE_SIZE));
        //如果配置图片将缓存到SD卡后那么getPhotoCacheDir返回仍然没有变化
        //Log.w("jayuchou", Glide.getPhotoCacheDir(context).getPath());
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);

    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
