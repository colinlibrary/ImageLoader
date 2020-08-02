package com.colin.imageloader;

import android.app.Application;

import com.colin.library.DiskCacheMenu;
import com.colin.library.GlideImageLoader;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GlideImageLoader.initdiskCacheMode(DiskCacheMenu.RESOURCE);
        GlideImageLoader.initPlaceHolderAndError(R.mipmap.header,R.mipmap.header);
        GlideImageLoader.initCrossTime(600);
        GlideImageLoader.initCacheSize(1025*1024*10);
//        GlideImageLoader.getInstance().initCachePath("cachpath");
    }
}
