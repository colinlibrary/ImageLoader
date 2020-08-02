package com.colin.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.colin.library.progress.OnProgressListener
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * 图片处理
 */
interface ImageLoader {

    /**
     * drable 的方式获取资源
     */
    fun <CONTEXT, RES> displayWithDrable(context: CONTEXT, url: RES?): ImageLoader?

    /**
     * bitmap 的方式获取资源
     */
    fun <CONTEXT, RES> displayWithBitmap(context: CONTEXT, url: RES?): ImageLoader?

    /**
     * drable 的方式获取资源
     */
    fun <CONTEXT, RES> displayCircleWithDrable(context: CONTEXT, url: RES?): ImageLoader?

    /**
     * bitmap 的方式获取资源
     */
    fun <CONTEXT, RES> displayCircleWithBitmap(context: CONTEXT, url: RES?): ImageLoader?

    /**
     * drable 的方式获取资源
     */
    fun <CONTEXT, RES> displayRoundWithDrable(context: CONTEXT, url: RES?,roundRadius: Int): ImageLoader?

    /**
     * drable 的方式获取资源
     */
    fun <CONTEXT, RES> displayRoundWithDrable(context: CONTEXT, url: RES?,roundRadius: Int, cornerTypeMenu: CornerTypeMenu): ImageLoader?

    /**
     * bitmap 的方式获取资源
     */
    fun <CONTEXT, RES> displayRoundWithBitmap(context: CONTEXT, url: RES?,roundRadius: Int): ImageLoader?

    /**
     * bitmap 的方式获取资源
     */
    fun <CONTEXT, RES> displayRoundWithBitmap(context: CONTEXT, url: RES?,roundRadius: Int, cornerTypeMenu: CornerTypeMenu): ImageLoader?

    /**
     * 高斯模糊的方式加载
     */
    fun <CONTEXT, RES> displayWithBlur(context: CONTEXT, url: RES?, blurRadius: Int): ImageLoader?

    /**
     * 高斯模糊加圆角的方式加载
     */
    fun <CONTEXT, RES> displayWithBlurRound(
        context: CONTEXT,
        url: RES?,
        blurRadius: Int,
        roundRadius: Int
    ): ImageLoader?

    /**
     * 添加drable 加载监听
     */
    fun <CONTEXT, RES> displayWithDrableAndLoaderListener(
        context: CONTEXT,
        url: RES?,
        imageListener: ImageLoaderListener<Drawable>?
    ): ImageLoader?

    /**
     * 添加drable 加载进度监听
     */
    fun <CONTEXT, RES> displayWithDrableAndProgressListener(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener?
    ): ImageLoader?

    /**
     * 添加bitmap 加载监听
     */
    fun <CONTEXT, RES> displayWithBitmapAndLoaderListener(
        context: CONTEXT,
        url: RES?,
        imageListener: ImageLoaderListener<Bitmap>?
    ): ImageLoader?

    /**
     * 添加bitmap 加载进度监听
     */
    fun <CONTEXT, RES> displayWithBitmapAndProgressListener(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener?
    ): ImageLoader?

    /**
     * 重置ScaleType （此变换会覆盖GLide之前对资源做的变化）
     */
    fun resetScaleType(scaleType: ScaleTypeMenu): ImageLoader?

    /**
     * 指定大小
     */
    fun resetTargetSize(width: Int, height: Int): ImageLoader?

    /**
     * 重置占位图
     */
    fun <RESHOLDER, RESERROR>resetPlaceHolder(placeholder: RESHOLDER, error: RESERROR): ImageLoader?

    /**
     * 重置缓存策略
     */
    fun resetDiskCacheStrategy(strategy: DiskCacheStrategy):ImageLoader?

    /**
     * 设置显示view
     */
    fun intoTargetView(imageView: ImageView)

    /**
     * 内存缓存清理（主线程）
     */
    fun clearMemoryCache(context: Context?)

    /**
     * 磁盘缓存清理（子线程）
     */
    fun clearFileCache(context: Context?)
}