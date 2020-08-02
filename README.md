# ImageLoader

中文 | [In English](docs/README.md) 

[![skin-support](https://img.shields.io/badge/release-v1.0.8-green.svg)](http://jcenter.bintray.com/skin/support)

* [介绍](#介绍)
  * [功能](#功能)
  * [TODO](#todo)
  * [目录结构](#目录结构)
* [Demo](#demo)
* [框架用法](#用法)
  * [导入](#导入)
  * [使用](#使用)
    * [初始化](#在application的oncreate中初始化)
    * [皮肤开关](#皮肤开关)
    * [加载插件皮肤库](#加载插件皮肤库)
    * [自定义view换肤](#自定义view换肤)
  * [应用内换肤](#应用内换肤)
  * [插件式换肤](#插件式换肤)
    * [新建皮肤工程](#新建android-application工程)
    * [添加皮肤资源](#将需要换肤的资源放到res目录下同名资源)
    * [生成皮肤插件](#打包生成apk-即为皮肤包)
    * [加载皮肤插件](#加载皮肤插件)
  * [自定义加载策略](#自定义加载策略)
    * [自定义sdcard路径](#自定义sdcard路径)
    * [zip包中加载资源](#zip包中加载资源)
  * [动态设置资源](#动态设置资源)
    * [动态设置颜色](#动态设置颜色)
    * [动态设置图片](#动态设置图片)
  * [获取当前使用皮肤](#获取当前使用皮肤)
* [第三方控件适配库](docs/ThirdPartSupport.md)
  * [hdodenhof/CircleImageView](docs/ThirdPartSupport.md#hdodenhofcircleimageview)
  * [H07000223/FlycoTabLayout](docs/ThirdPartSupport.md#h07000223flycotablayout)
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

### TODO

* [ ] `AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);`开关使用错误问题修正。
* [ ] 支持多语言。
* [ ] 支持多字体。
* [ ] 支持Preference。
* [ ] skin-sample 实现:
  * [x] 动态修改主题颜色值[案例](demo/skin-sample/src/main/java/com/ximsfei/skin/sample/activities/ColorPickerActivity.java)
  * [ ] 控件使用案例
  * [ ] 更新日志tab
  * [ ] 关于页面
* [ ] Wiki

### 目录结构

> [demo](demo)                                                        // 换肤demo 集合

>> [skin-sample](demo/skin-sample)([skin-app](demo/skin-app))         // demo app

>> [skin-night](demo/skin-night)                                      // 夜间模式皮肤工程

> [androidx](androidx)                                  // Android 原生控件

>> [skin-support](androidx／skin-support)                      // 换肤框架

>> [skin-support-appcompat](androidx／skin-support-appcompat)  // 换肤框架, 基础控件支持

>> [skin-support-cardview](androidx／skin-support-cardview)    // 换肤框架, CardView 支持

>> [skin-support-design](androidx／skin-support-design)        // 换肤框架, Material Design 支持

>> [skin-support-constraint-layout](androidx／skin-support-constraint-layout)  // 换肤框架, ConstraintLayout 支持

> [third-part-support](third-part-support)           // 第三方控件换肤支持

## Demo

![default](https://github.com/ximsfei/Res/blob/master/skin/preview/default.png)
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

#### 皮肤开关

如果项目中有特殊需求。例如, 股票控件: 控件颜色始终为红色或绿色, 不需要随着模式切换而换肤

那么可以使用类似的方法, 直接设置drawable
```xml
setBackgroundDrawable(redDrawable) // 不支持换肤
background="#ce3d3a"
```
而不是使用R.drawable.red
```xml
setBackgroundResource(R.drawable.red)
background="@drawable/red"
```

#### 加载插件皮肤库

```java
// 指定皮肤插件
SkinCompatManager.getInstance().loadSkin("new.skin"[, SkinLoaderListener], int strategy);

// 恢复应用默认皮肤
SkinCompatManager.getInstance().restoreDefaultTheme();
```

#### 自定义View换肤

要点:

1. 实现SkinCompatSupportable接口

  1. applySkin方法中实现换肤操作

2. 在构造方法中解析出需要换肤的resId

* 自定义View可以直接继承自SkinCompatView, SkinCompatLinearLayout等已有控件

  eg: [CustomTextView](demo/skin-app/src/main/java/com/ximsfei/skindemo/widget/CustomTextView.java)

* 不想继承自已有控件

  eg: [CustomTextView2](demo/skin-app/src/main/java/com/ximsfei/skindemo/widget/CustomTextView2.java)

* 需要换肤自定义属性

  // 需要换肤AutoCompleteTextView的R.attr.popupBackground属性

  eg: [SkinCompatAutoCompleteTextView](androidx/skin-support-appcompat/src/main/java/skin/support/widget/SkinCompatAutoCompleteTextView.java)

* 需要使用第三方库控件怎么办

  // 需要使用https://github.com/hdodenhof/CircleImageView 控件, 并且要支持换肤

  eg: [SkinCompatCircleImageView](third-part-support/circleimageview/src/main/java/skin/support/circleimageview/widget/SkinCompatCircleImageView.java)

### 应用内换肤:

应用内换肤，皮肤名为: night; 新增需要换肤的资源添加后缀或者前缀。

需要换肤的资源为R.color.windowBackgroundColor, 添加对应资源R.color.windowBackgroundColor_night。

加载应用内皮肤:
```java
SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_PREFIX_BUILD_IN); // 前缀加载
```

推荐将应用内换肤相关的皮肤资源放到单独的目录中

eg: [res-night](https://github.com/ximsfei/Android-skin-support/tree/master/demo/skin-app/src/main/res-night)

*注: 如果使用这种方式来增加换肤资源，记得在build.gradle 中配置一下这个资源目录 sourceSets {main {res.srcDirs = ['src/main/res', 'src/main/res-night']}}*


### 插件式换肤:

#### 新建Android application工程

皮肤工程包名不能和宿主应用包名相同.

例如:
```xml
宿主包名: com.ximsfei.skindemo
夜间模式: com.ximsfei.skindemo.night
```

#### 将需要换肤的资源放到res目录下(同名资源)

例如 APK中窗口背景颜色为

colors.xml
```xml
<color name="background">#ffffff</color>
```
那么夜间模式你可以在skin-night工程中设置

colors.xml
```xml
<color name="background">#000000</color>
```

#### 打包生成apk, 即为皮肤包

将打包生成的apk文件, 重命名为'xxx.skin', 防止apk结尾的文件造成混淆.

#### 加载皮肤插件

加载插件式皮肤, 将皮肤包放到assets/skins目录下
```java
SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
```

### 自定义加载策略:

#### 自定义sdcard路径

继承自`SkinSDCardLoader`，通过`getSkinPath`方法指定皮肤加载路径，通过`getType`方法指定加载器type。

```java
public class CustomSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        return new File(SkinFileUtils.getSkinDir(context), skinName).getAbsolutePath();
    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}
```

*注: 自定义加载器type 值最好从整数最大值开始递减，框架的type值从小数开始递增，以免将来框架升级造成type 值冲突*

在Application中，添加自定义加载策略:

```java
SkinCompatManager.withoutActivity(this)
        .addStrategy(new CustomSDCardLoader());          // 自定义加载策略，指定SDCard路径
```

*注: 自定义加载器必须在Application中注册，皮肤切换后，重启应用需要根据当前策略加载皮肤*

使用自定义加载器加载皮肤:

```java
SkinCompatManager.getInstance().loadSkin("night.skin", null, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
```

#### zip包中加载资源

继承自`SkinSDCardLoader`，在`loadSkinInBackground`方法中解压资源，在`getDrawable`等方法中返回加压后的资源。

```java
public class ZipSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_ZIP = Integer.MAX_VALUE - 1;

    @Override
    public String loadSkinInBackground(Context context, String skinName) {
        // TODO 解压zip包中的资源，同时可以根据skinName安装皮肤包(.skin)。
        return super.loadSkinInBackground(context, skinName);
    }

    @Override
    protected String getSkinPath(Context context, String skinName) {
        // TODO 返回皮肤包路径，如果自需要使用zip包，则返回""
        return new File(SkinFileUtils.getSkinDir(context), skinName).getAbsolutePath();
    }

    @Override
    public Drawable getDrawable(Context context, String skinName, int resId) {
        // TODO 根据resId来判断是否使用zip包中的资源。
        return super.getDrawable(context, skinName, resId);
    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_ZIP;
    }
}
```

*资源加载策略更灵活，不仅仅只有皮肤包，开发者可配置任意资源获取方式(Zip/Apk/Json...)。*

在Application中，添加自定义加载策略:

```java
SkinCompatManager.withoutActivity(this)
        .addStrategy(new ZipSDCardLoader());          // 自定义加载策略，加载zip包中的资源
```

### 动态设置资源

#### [动态设置颜色](demo/skin-app/src/main/java/com/ximsfei/skindemo/picker/ColorPickerActivity.java)

```java
SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimary, #ffffffff);

SkinCompatUserThemeManager.get().addColorState(R.color.colorPrimary, new ColorState.ColorBuilder().addXxx().build());

// 清除所有已有颜色值。
SkinCompatUserThemeManager.get().clearColors();
```

#### [动态设置图片](demo/skin-app/src/main/java/com/ximsfei/skindemo/picker/DrawablePickerActivity.java)

```java
SkinCompatUserThemeManager.get().addDrawablePath(R.drawable.windowBackground, "／sdcard/DCIM/Camera/xxx.jpg");

// 要换肤的资源id，图片路径，图片旋转角度(默认为0)
SkinCompatUserThemeManager.get().addDrawablePath(R.drawable.windowBackground, "／sdcard/DCIM/Camera/xxx.jpg", 90);

// 清除所有已有图片路径。
SkinCompatUserThemeManager.get().clearDrawables();
```

在设置完颜色及图片后，需要调用`apply()`方法来保存设置。

```java
SkinCompatUserThemeManager.get().apply();
```

*资源加载优先级: 用户自定义颜色值-加载策略中的资源-皮肤包资源-应用资源。*

### 获取当前使用皮肤

```
https://github.com/ximsfei/Android-skin-support/blob/master/androidx/skin-support/src/main/java/skin/support/utils/SkinPreference.java
```

## 缺点

* 同一个LayoutInflater只能设置一次Factory，容易和同类库产生冲突


