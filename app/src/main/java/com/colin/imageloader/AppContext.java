package com.colin.imageloader;

import android.app.Application;

import com.colin.library.DiskCacheMenu;
import com.colin.library.GlideImageLoader;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GlideImageLoader.apply(DiskCacheMenu.RESOURCE)
                .apply(R.mipmap.header, R.mipmap.header)
                .apply(6000)
                .apply(1025 * 1024 * 10L);
//              .apply("cachpath");
    }
}
