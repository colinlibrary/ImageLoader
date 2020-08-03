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
![Alt text](https://raw.githubusercontent.com/colinlibrary/ImageLoader/master/shots/device-2020-08-03-131928%20(1).gif)
![Alt text](https://raw.githubusercontent.com/colinlibrary/ImageLoader/master/shots/device-2020-08-03-131928.gif)
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
   implementation 'com.github.colinlibrary:ImageLoader:1.0.0'
}
```
在Application的onCreate中初始化
    
```java
@Override
public void onCreate() {
    super.onCreate();
    GlideImageLoader.apply(DiskCacheMenu.RESOURCE)//设置缓存策略
                //设置展位图资源（支持Drawable和Int类型的资源）
                .apply(R.mipmap.placeholder,R.mipmap.error)
                //设置淡入动画时间（默认500毫秒）
                .apply(600)
                //设置缓存大小默认5M
                .apply(1025 * 1024 * 10L);
                //设置缓存路径默认/data/user/0/***/cache/GlideDisk
                //.apply("自定义缓存路径");
}

如果选用外部存储 请添加相应权限
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
以及动态申请相应权限
```

### 使用:

 #### 1.普通方式使用 
 
以下方式均 支持bitmap和drawable  想要加载GIF类型的资源选用drawable方式即可  
```java
   //context 支持 Activity，Fragment，View，Service类型
   GlideImageLoader.getInstance().displayWithDrawable(context,url)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayWithBitmap(context,url)?.intoTargetView(targetView)
```

 #### 2.加载圆型图片  
 
```java
   //context 支持 Activity，Fragment，View，Service类型
   GlideImageLoader.getInstance().displayCircleWithDrawable(context,url)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayCircleWithBitmap(context,url)?.intoTargetView(targetView)
```

 #### 3.加载圆角图片
 
```java
   //context 支持 Activity，Fragment，View，Service类型
   GlideImageLoader.getInstance().displayRoundWithDrawable(context,url,cornerRadius)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayRoundWithDrawable(context,url,cornerRadius,cornerTypeMenu)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayRoundWithBitmap(context,url,cornerRadius)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayRoundWithBitmap(context,url,cornerRadius,cornerTypeMenu)?.intoTargetView(targetView)
   
   cornerTypeMenu一共有 
   ALL,
   TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
   TOP, BOTTOM, LEFT, RIGHT,
   OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
   DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
   种类型可以选择
```
  
    
 #### 4.高斯模糊加圆角的方式加载
 
```java
   //context 支持 Activity，Fragment，View，Service类型
   GlideImageLoader.getInstance().displayWithBlur(context,url,cornerRadius)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayWithBlurRound(context,url,blurRadius,cornerRadius)?.intoTargetView(targetView)
   GlideImageLoader.getInstance().displayWithBlurRound(context,url,blurRadius,cornerRadius,cornerTypeMenu)?.intoTargetView(targetView)
   
   
   同样的cornerTypeMenu一共有 
   ALL,
   TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
   TOP, BOTTOM, LEFT, RIGHT,
   OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
   DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
   种类型可以选择
```

    
 #### 5.资源加载监听
 
```java
   //context 支持 Activity，Fragment，View，Service类型
   //bitmap方式加载
   GlideImageLoader.getInstance().displayWithBitmap(context,url,object : ImageLoaderListener<Bitmap> {
                override fun onRequestSuccess(resource: Bitmap?) {//成功回调
                    
                }

                override fun onRequestFailed() {//失败回调
                    
                }
            })?.intoTargetView(targetView)
   
   //drawable方式加载
   GlideImageLoader.getInstance().displayWithDrawable(context,url,object : ImageLoaderListener<Drawable> {
                override fun onRequestSuccess(resource: Drawable?) {//成功回调
                    
                }

                override fun onRequestFailed() {//失败回调
                    
                }
            })?.intoTargetView(targetView)
            
   //drawable方式加载     
   GlideImageLoader.getInstance().displayWithDrawable(context,url,object : OnProgressListener<Drawable>{
                override fun onProgress(isComplete: Boolean,percentage: Int,bytesRead: Long,totalBytes: Long) {//进度回调
                    
                }

                override fun onComplete(resource: Drawable?) {//成功回调
                    
                }
                override fun onFailed() {//失败回调
                    
                }
            } )?.intoTargetView(targetView)
            
   //bitmap方式加载     
   GlideImageLoader.getInstance().displayWithBitmap(context,url,object : OnProgressListener<Bitmap>{
                override fun onProgress(isComplete: Boolean,percentage: Int,bytesRead: Long,totalBytes: Long) {//进度回调
                    
                }

                override fun onComplete(resource: Bitmap?) {//成功回调
                    
                }
                override fun onFailed() {//失败回调
                    
                }
            } )?.intoTargetView(targetView)
```
#####  * 以上方式均支持重置加载资源宽高（针对本次加载生效）  例如：
```java
   GlideImageLoader.getInstance().displayWithDrawable(context,url)?.resetTargetSize(400,400)?.intoTargetView(targetView)
   
   这样就以400*400为目标view去加载资源
```


##### * 自定义缓存策略（针对本次加载生效）例如：

```java
   GlideImageLoader.getInstance().displayWithDrawable(context,url)?.resetDiskCacheStrategy(DiskCacheStrategy.DATA)?.intoTargetView(targetView)
```

##### *重置占位图（针对本次加载生效）placeholder,error 均支持 drawable和int 两种类型 例如：

```java
   GlideImageLoader.getInstance().displayWithDrawable(context,url)?.resetPlaceHolder(placeholder,error)?.intoTargetView(targetView)
```

##### * 重置ScaleType （针对本次加载生效 请谨慎使用）

```java
   GlideImageLoader.getInstance().displayWithDrawable(context,url)?.resetScaleType(ScaleTypeMenu.CenterCrop)?.intoTargetView(targetView)
   
   ScaleTypeMenu 共支持 
   Default,CenterCrop,CenterInside,FitCenter,CircleCrop
```

ScaleTypeMenu 共支持 
```java
    Default,CenterCrop,CenterInside,FitCenter,CircleCrop
    种类型可选择
```

 #### 6.获取URL资源
 
```java
  
   //bitmap方式
   GlideImageLoader.getInstance().getUrlWithBitmap(context,url,object :OnProgressListener<Bitmap>{
                override fun onProgress( isComplete: Boolean, percentage: Int,  bytesRead: Long, totalBytes: Long    ) {//进度回调
                    
                }

                override fun onComplete(resource: Bitmap?) {//成功回调
               
                }
                override fun onFailed() {//失败回调
                    
                }
            })
            
    //drawable方式
    GlideImageLoader.getInstance().getUrlWithDrawable(context,url,object :OnProgressListener<Drawable>{
                override fun onProgress( isComplete: Boolean, percentage: Int,  bytesRead: Long, totalBytes: Long    ) {//进度回调
                    
                }

                override fun onComplete(resource: Drawable?) {//成功回调
               
                }
                override fun onFailed() {//失败回调
                    
                }
            })
            
   //bitmap方式
   GlideImageLoader.getInstance().getUrlWithBitmap(context,url,object : ImageLoaderListener<Bitmap> {
                override fun onRequestSuccess(resource: Bitmap?) {//成功回调
                    
                }

                override fun onRequestFailed() {//失败回调
                    
                }
            })
            
    //drawable方式
    GlideImageLoader.getInstance().getUrlWithDrawable(context,url,object : ImageLoaderListener<Drawable> {
                override fun onRequestSuccess(resource: Drawable?) {//成功回调
                    
                }

                override fun onRequestFailed() {//失败回调
                    
                }
            })
```

 #### 7.缓存清理 
 
```java
   /**
     * 内存缓存清理（主线程）
     */
    override fun clearMemoryCache(context: Context?) {
        context?.let { GlideApp.get(it).clearMemory() }
    }

    /**
     * 磁盘缓存清理（子线程）
     */
    override fun clearFileCache(context: Context?) {
        Thread(Runnable { context?.let { GlideApp.get(it).clearDiskCache() } }).start()
    }
```
## 缺点

* 还有很多不足之处以后慢慢改进


