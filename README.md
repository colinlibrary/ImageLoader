# ImageLoader

[![skin-support](https://img.shields.io/badge/release-v1.0.8-green.svg)](http://jcenter.bintray.com/skin/support)

* [介绍](#介绍)
  * [功能](#功能)
* [Demo](#demo)
* [用法](#用法)
  * [导入](#导入)
  * [使用](#使用)
* [缺点](#缺点)

## 介绍

ImageLoader: 基于Glide+glide：okhttp+Glide图片变换库做的一个简单的封装.极大的简化使用Glide成本：
```java
   GlideImageLoader.getInstance().displayWithDrable(context,url)?.intoTargetView(imageview)
```
最基础的使用，这样便可实现普通图片以及GIF图的加载显示。

### 功能

* [x] 支持图片圆角话圆型化处理。
* [x] 支持高色模糊处理。
* [x] 支持图片加载监听以及进度监听。
* [x] 支持动态修改placeholder以及error展位图。
* [x] 支持动态修改加载图片的宽高。
* [x] 支持动态修改加载图片的缓存策略。

## Demo

![default](https://github.com/colinlibrary/ImageLoader/blob/master/radio/device-2020-08-01-220928.mp4)

## 用法

   ### 导入:

Add it in your root build.gradle at the end of repositories:
```xml
allprojects {
    repositories {
       ...
       maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```xml
dependencies {
   implementation 'com.github.colinlibrary:ImageLoader:1.0.8'
}
```
在Application的onCreate中初始化
    
```java
@Override
public void onCreate() {
    super.onCreate();
    //设置缓存策略
    GlideImageLoader.initdiskCacheMode(DiskCacheMenu.RESOURCE);
    //设置展位图资源（支持Drawable和Int类型的资源）
    GlideImageLoader.initPlaceHolderAndError(R.mipmap.placeholder,R.mipmap.error);
    //设置缓存大小默认5M
    GlideImageLoader.initCacheSize(1025*1024*10);
    //设置缓存路径默认/data/user/0/***/cache/GlideDisk
    GlideImageLoader.initCachePath("自定义缓存路径");
    //设置淡入动画时间（默认500毫秒）
    GlideImageLoader.initCrossTime(600);
}
```

### 使用:
1.普通方式使用
```java
   //context 支持 Activity，Fragment，View，Service类型
   GlideImageLoader.getInstance().displayWithDrable(context,url)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayWithBitmap(context,url)?.intoTargetView(targetView)
```

## 缺点

* 同一个LayoutInflater只能设置一次Factory，容易和同类库产生冲突


