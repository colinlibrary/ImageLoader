# ImageLoader

中文 | [In English](docs/README.md) 

[![skin-support](https://img.shields.io/badge/release-v1.0.8-green.svg)](http://jcenter.bintray.com/skin/support)

* [介绍](#介绍)
  * [功能](#功能)
  * [TODO](#todo)
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
![app-in](https://github.com/ximsfei/Res/blob/master/skin/preview/app-in.png)
![plug-in](https://github.com/ximsfei/Res/blob/master/skin/preview/plug-in.png)

## 用法

[最新版本选择, 请查看更新日志](docs/ChangeLog.md)

### 导入:

#### support library

如果项目中还在使用support库，添加以下依赖
```xml
implementation 'skin.support:skin-support:3.1.4'                   // skin-support 基础控件支持
implementation 'skin.support:skin-support-design:3.1.4'            // skin-support-design material design 控件支持[可选]
implementation 'skin.support:skin-support-cardview:3.1.4'          // skin-support-cardview CardView 控件支持[可选]
implementation 'skin.support:skin-support-constraint-layout:3.1.4' // skin-support-constraint-layout ConstraintLayout 控件支持[可选]
```

在Application的onCreate中初始化
    
```java
@Override
public void onCreate() {
    super.onCreate();
    SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
            .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
            .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
            .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin();
}
```

> 如果项目中使用的Activity继承自AppCompatActivity，需要重载getDelegate()方法

```java
@NonNull
@Override
public AppCompatDelegate getDelegate() {
    return SkinAppCompatDelegateImpl.get(this, this);
}
```

#### AndroidX support:

如果项目中使用了[AndroidX](https://developer.android.google.cn/topic/libraries/support-library/androidx-overview), 添加以下依赖
```xml
implementation 'skin.support:skin-support:4.0.4'                   // skin-support
implementation 'skin.support:skin-support-appcompat:4.0.4'         // skin-support 基础控件支持
implementation 'skin.support:skin-support-design:4.0.4'            // skin-support-design material design 控件支持[可选]
implementation 'skin.support:skin-support-cardview:4.0.4'          // skin-support-cardview CardView 控件支持[可选]
implementation 'skin.support:skin-support-constraint-layout:4.0.4' // skin-support-constraint-layout ConstraintLayout 控件支持[可选]
```

*⚠️ 从3.x.x迁移至4.0.4+, 解耦了换肤库对appcompat包的依赖，需要新增以下代码*
```gradle
implementation 'skin.support:skin-support-appcompat:4.0.4'         // skin-support 基础控件支持
```

在Application的onCreate中初始化
    
```java
@Override
public void onCreate() {
    super.onCreate();
    SkinCompatManager.withoutActivity(this)
            .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
            .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
            .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
            .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin();
}
```

> 如果项目中使用的Activity继承自AppCompatActivity，需要重载getDelegate()方法

```java
@NonNull
@Override
public AppCompatDelegate getDelegate() {
    return SkinAppCompatDelegateImpl.get(this, this);
}
```

### 使用:


## 缺点

* 同一个LayoutInflater只能设置一次Factory，容易和同类库产生冲突


