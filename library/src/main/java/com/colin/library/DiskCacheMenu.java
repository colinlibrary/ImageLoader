package com.colin.library;

public enum DiskCacheMenu {
    ALL(1),
    NONE(2),
    DATA(3),
    RESOURCE(4),
    AUTOMATIC(5);
    public int cacheMode;

    DiskCacheMenu(int cacheMode) {
        this.cacheMode = cacheMode;
    }
}
