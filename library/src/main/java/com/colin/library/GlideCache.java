package com.colin.library;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.colin.library.progress.ProgressManager;

import java.io.InputStream;

@GlideModule
public class GlideCache extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        super.applyOptions(context, builder);
        //通过builder.setXXX进行配置.
        //设置缓存大小
        long diskCacheSizeBytes = GlideImageLoader.getCacheSize();
        if (diskCacheSizeBytes == 0)
            diskCacheSizeBytes = 1024 * 1024 * 5; // 默认大小5 MB

        //手机app路径
        String cachePath = GlideImageLoader.getCachePath();
        if (TextUtils.isEmpty(cachePath))
            cachePath = context.getCacheDir().getPath() + "/GlideDisk";//默认保存在app 缓存里
        builder.setDiskCache(
                new DiskLruCacheFactory(cachePath, diskCacheSizeBytes)
        );

        Log.e("----",cachePath);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        //通过glide.register进行配置.  用okhttp实现网络请求
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}
