package com.colin.library

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.colin.library.progress.OnProgressListener
import com.colin.library.progress.ProgressManager
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Glide图片处理类
 */
@Suppress("DEPRECATION")
class GlideImageLoader : ImageLoader {


    //view默认宽高
    private val defaultViewWidth = 423
    private val defaultViewHeight = 537

    //Glide实现渐入动画效果
    private val drawableCrossFadeFactory: DrawableCrossFadeFactory =
        DrawableCrossFadeFactory.Builder(CROSS_TIME).setCrossFadeEnabled(true).build()
    private val drawableCrossFade = DrawableTransitionOptions().crossFade(drawableCrossFadeFactory)
    private val bitmapCrossFade = BitmapTransitionOptions().crossFade(drawableCrossFadeFactory)

    //Glide ScaleType样式
    private val defaultOptions: RequestOptions =
        RequestOptions().centerCrop()
    private val centerCropOptions: RequestOptions =
        RequestOptions().centerCrop()
    private val centerInsideOptions: RequestOptions =
        RequestOptions().centerInside()
    private val fitCenterOptions: RequestOptions =
        RequestOptions().fitCenter()
    private val circleCropOptions: RequestOptions =
        RequestOptions().circleCrop()
    private var REQUESTINSTANCE: GlideRequest<*>? = null


    /**
     * 单例方式获取
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ImageLoader? = null

        //缓存空间
        private var CACHE_SIZE = 1024 * 1024 * 5L

        //缓存地址
        private var CACHE_PATH = ""

        //占位资源
        private var placeHolderId: Any? = null
        private var errorId: Any? = null

        //缓存策略
        private var diskCacheMenu: DiskCacheMenu? = null

        //动画时间
        private var CROSS_TIME = 500

        /**
         * 获取实例
         */
        @JvmStatic
        fun getInstance(): ImageLoader {
            if (INSTANCE == null) {
                synchronized(GlideImageLoader::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = GlideImageLoader()
                    }
                }
            }
            return INSTANCE ?: GlideImageLoader()
        }

        /**
         * 设置 缓存策略
         */
        @JvmStatic
        fun apply(diskCacheMenu: DiskCacheMenu): Companion {
            this.diskCacheMenu = diskCacheMenu
            return GlideImageLoader
        }

        /**
         * 设置 placeholder error 占位图
         */
        @JvmStatic
        fun apply(placeholder: Any, error: Any): Companion {
            placeHolderId = placeholder
            errorId = error
            return GlideImageLoader
        }


        /**
         * 设置缓存大小
         */
        @JvmStatic
        fun apply(duration: Int): Companion {
            CROSS_TIME = duration
            return GlideImageLoader
        }

        /**
         * 设置缓存大小
         */
        @JvmStatic
        fun apply(cacheSize: Long): Companion {
            CACHE_SIZE = cacheSize
            return GlideImageLoader
        }

        /**
         * 获取缓存大小
         */
        @JvmStatic
        fun getCacheSize(): Long {
            return CACHE_SIZE
        }

        /**
         * 设置缓存路径
         */
        @JvmStatic
        fun apply(cachePath: String): Companion {
            CACHE_PATH = cachePath
            return GlideImageLoader
        }

        /**
         * 获取缓存路径
         */
        @JvmStatic
        fun getCachePath(): String {
            return CACHE_PATH
        }
    }


    /**
     * 获取glide with
     */
    private fun <CONTEXT> getGlideWith(context: CONTEXT): GlideRequests? {
        return when (context) {
            is Fragment -> {
                GlideApp.with(context)
            }
            is Activity -> {
                GlideApp.with(context)
            }
            is View -> {
                GlideApp.with(context)
            }
            is Context -> {
                GlideApp.with(context)
            }
            else -> null
        }
    }


    /**
     * drable 的方式获取资源
     */
    override fun <CONTEXT, RES> displayWithDrable(context: CONTEXT, url: RES?): ImageLoader? {
        REQUESTINSTANCE =
            getGlideWith(context)?.asDrawable()?.load(url)?.transition(drawableCrossFade)
        initDiskCacheMode(diskCacheMenu ?: DiskCacheMenu.AUTOMATIC)
        resetPlaceHolder(placeHolderId, errorId)
        resetScaleType(ScaleTypeMenu.Default)
        return INSTANCE
    }

    /**
     * bitmap 的方式获取资源
     */
    override fun <CONTEXT, RES> displayWithBitmap(context: CONTEXT, url: RES?): ImageLoader? {
        REQUESTINSTANCE = getGlideWith(context)?.asBitmap()?.load(url)?.transition(bitmapCrossFade)
        initDiskCacheMode(diskCacheMenu ?: DiskCacheMenu.AUTOMATIC)
        resetPlaceHolder(placeHolderId, errorId)
        resetScaleType(ScaleTypeMenu.Default)
        return INSTANCE
    }

    /**
     * bitmap 的方式获取资源
     */
    override fun <CONTEXT, RES> displayCircleWithBitmap(context: CONTEXT, url: RES?): ImageLoader? {
        displayWithBitmap(context, url)
        resetScaleType(ScaleTypeMenu.CircleCrop)
        return INSTANCE
    }

    /**
     * drable 的方式获取资源
     */
    override fun <CONTEXT, RES> displayCircleWithDrable(context: CONTEXT, url: RES?): ImageLoader? {
        displayWithDrable(context, url)
        resetScaleType(ScaleTypeMenu.CircleCrop)
        return INSTANCE
    }

    /**
     * drable 的方式获取资源
     */
    override fun <CONTEXT, RES> displayRoundWithDrable(
        context: CONTEXT,
        url: RES?,
        roundRadius: Int
    ): ImageLoader? {
        displayWithDrable(context, url)
        REQUESTINSTANCE = REQUESTINSTANCE?.transform(RoundedCornersTransformation(roundRadius, 0))
        return INSTANCE
    }

    /**
     * drable 的方式获取资源
     */
    override fun <CONTEXT, RES> displayRoundWithDrable(
        context: CONTEXT,
        url: RES?,
        roundRadius: Int,
        cornerTypeMenu: CornerTypeMenu
    ): ImageLoader? {
        displayWithDrable(context, url)
        var cornerType = when (cornerTypeMenu) {
            CornerTypeMenu.ALL -> RoundedCornersTransformation.CornerType.ALL
            CornerTypeMenu.TOP_LEFT -> RoundedCornersTransformation.CornerType.TOP_LEFT
            CornerTypeMenu.TOP_RIGHT -> RoundedCornersTransformation.CornerType.TOP_RIGHT
            CornerTypeMenu.BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            CornerTypeMenu.BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            CornerTypeMenu.TOP -> RoundedCornersTransformation.CornerType.TOP
            CornerTypeMenu.BOTTOM -> RoundedCornersTransformation.CornerType.BOTTOM
            CornerTypeMenu.LEFT -> RoundedCornersTransformation.CornerType.LEFT
            CornerTypeMenu.RIGHT -> RoundedCornersTransformation.CornerType.RIGHT
            CornerTypeMenu.OTHER_TOP_LEFT -> RoundedCornersTransformation.CornerType.OTHER_TOP_LEFT
            CornerTypeMenu.OTHER_TOP_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_TOP_RIGHT
            CornerTypeMenu.OTHER_BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_LEFT
            CornerTypeMenu.OTHER_BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_RIGHT
            CornerTypeMenu.DIAGONAL_FROM_TOP_LEFT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_LEFT
            CornerTypeMenu.DIAGONAL_FROM_TOP_RIGHT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT
        }
        REQUESTINSTANCE =
            REQUESTINSTANCE?.transform(RoundedCornersTransformation(roundRadius, 0, cornerType))
        return INSTANCE
    }

    /**
     * bitmpa 的方式获取资源
     */
    override fun <CONTEXT, RES> displayRoundWithBitmap(
        context: CONTEXT,
        url: RES?,
        roundRadius: Int
    ): ImageLoader? {
        displayWithBitmap(context, url)
        REQUESTINSTANCE = REQUESTINSTANCE?.transform(RoundedCornersTransformation(roundRadius, 0))
        return INSTANCE
    }

    /**
     * bitmap 方式获取
     */
    override fun <CONTEXT, RES> displayRoundWithBitmap(
        context: CONTEXT,
        url: RES?,
        roundRadius: Int,
        cornerTypeMenu: CornerTypeMenu
    ): ImageLoader? {
        displayWithBitmap(context, url)
        var cornerType = when (cornerTypeMenu) {
            CornerTypeMenu.ALL -> RoundedCornersTransformation.CornerType.ALL
            CornerTypeMenu.TOP_LEFT -> RoundedCornersTransformation.CornerType.TOP_LEFT
            CornerTypeMenu.TOP_RIGHT -> RoundedCornersTransformation.CornerType.TOP_RIGHT
            CornerTypeMenu.BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            CornerTypeMenu.BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            CornerTypeMenu.TOP -> RoundedCornersTransformation.CornerType.TOP
            CornerTypeMenu.BOTTOM -> RoundedCornersTransformation.CornerType.BOTTOM
            CornerTypeMenu.LEFT -> RoundedCornersTransformation.CornerType.LEFT
            CornerTypeMenu.RIGHT -> RoundedCornersTransformation.CornerType.RIGHT
            CornerTypeMenu.OTHER_TOP_LEFT -> RoundedCornersTransformation.CornerType.OTHER_TOP_LEFT
            CornerTypeMenu.OTHER_TOP_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_TOP_RIGHT
            CornerTypeMenu.OTHER_BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_LEFT
            CornerTypeMenu.OTHER_BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_RIGHT
            CornerTypeMenu.DIAGONAL_FROM_TOP_LEFT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_LEFT
            CornerTypeMenu.DIAGONAL_FROM_TOP_RIGHT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT
        }
        REQUESTINSTANCE =
            REQUESTINSTANCE?.transform(RoundedCornersTransformation(roundRadius, 0, cornerType))
        return INSTANCE
    }

    /**
     * 高斯模糊的方式加载
     */
    override fun <CONTEXT, RES> displayWithBlur(
        context: CONTEXT,
        url: RES?,
        blurRadius: Int
    ): ImageLoader? {
        displayWithBitmap(context, url)
        REQUESTINSTANCE = REQUESTINSTANCE?.transform(BlurTransformation(blurRadius))
        return INSTANCE
    }

    /**
     * 高斯模糊加圆角的方式加载
     */
    override fun <CONTEXT, RES> displayWithBlurRound(
        context: CONTEXT,
        url: RES?,
        blurRadius: Int,
        roundRadius: Int
    ): ImageLoader? {
        displayWithBitmap(context, url)
        REQUESTINSTANCE = REQUESTINSTANCE?.transform(
            MultiTransformation(
                BlurTransformation(blurRadius),
                RoundedCornersTransformation(roundRadius, 0)
            )
        )
        return INSTANCE
    }

    /**
     * 添加drable 加载监听
     */
    override fun <CONTEXT, RES> displayWithDrable(
        context: CONTEXT,
        url: RES?,
        imageListener: ImageLoaderListener<Drawable>?
    ): ImageLoader? {

        displayWithDrable(context, url)
        REQUESTINSTANCE = (REQUESTINSTANCE as GlideRequest<Drawable>)?.addListener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                imageListener?.onRequestFailed()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageListener?.onRequestSuccess(resource)
                return false
            }
        })
        return INSTANCE
    }

    /**
     * 添加drable 加载进度监听
     */
    override fun <CONTEXT, RES> displayWithDrable(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener<Drawable>?
    ): ImageLoader? {
        if (onProgressListener != null && url is String)
            ProgressManager.addListener(url, onProgressListener)
        displayWithDrable(context, url)
        REQUESTINSTANCE = (REQUESTINSTANCE as GlideRequest<Drawable>)?.addListener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
            ): Boolean {
                onProgressListener?.onComplete(resource)
                if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                return false
            }
        })
        return INSTANCE
    }

    /**
     * 添加bitmap 加载监听
     */
    override fun <CONTEXT, RES> displayWithBitmap(
        context: CONTEXT,
        url: RES?,
        imageListener: ImageLoaderListener<Bitmap>?
    ): ImageLoader? {
        displayWithBitmap(context, url)
        REQUESTINSTANCE = (REQUESTINSTANCE as GlideRequest<Bitmap>)?.addListener(object :
            RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                imageListener?.onRequestFailed()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageListener?.onRequestSuccess(resource)
                return false
            }
        })
        return INSTANCE
    }

    /**
     * 添加bitmap 加载进度监听
     */
    override fun <CONTEXT, RES> displayWithBitmap(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener<Bitmap>?
    ): ImageLoader? {
        if (onProgressListener != null && url is String)
            ProgressManager.addListener(url, onProgressListener)
        displayWithBitmap(context, url)
        REQUESTINSTANCE = (REQUESTINSTANCE as GlideRequest<Bitmap>)?.addListener(object :
            RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onProgressListener?.onComplete(resource)
                if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                return false
            }
        })
        return INSTANCE
    }

    /**
     * 设置展示方式
     */
    override fun resetScaleType(scaleType: ScaleTypeMenu): ImageLoader? {
        REQUESTINSTANCE = when (scaleType) {
            ScaleTypeMenu.Default -> REQUESTINSTANCE?.apply(defaultOptions)
            ScaleTypeMenu.CenterCrop -> REQUESTINSTANCE?.apply(centerCropOptions)
            ScaleTypeMenu.CenterInside -> REQUESTINSTANCE?.apply(centerInsideOptions)
            ScaleTypeMenu.FitCenter -> REQUESTINSTANCE?.apply(fitCenterOptions)
            ScaleTypeMenu.CircleCrop -> REQUESTINSTANCE?.apply(circleCropOptions)
        }
        return INSTANCE
    }

    /**
     * 指定大小
     */
    override fun resetTargetSize(width: Int, height: Int): ImageLoader? {
        REQUESTINSTANCE = REQUESTINSTANCE?.override(
            if (width == 0) defaultViewWidth else width,
            if (height == 0) defaultViewHeight else height
        )
        return INSTANCE
    }

    /**
     * 重置占位图
     */
    override fun <RESHOLDER, RESERROR> resetPlaceHolder(
        placeholder: RESHOLDER, error: RESERROR
    ): ImageLoader? {
        when (placeholder) {
            is Drawable -> REQUESTINSTANCE = REQUESTINSTANCE?.placeholder(placeholder)
            is Int -> REQUESTINSTANCE = REQUESTINSTANCE?.placeholder(placeholder)
        }
        when (error) {
            is Drawable -> REQUESTINSTANCE = REQUESTINSTANCE?.error(error)
            is Int -> REQUESTINSTANCE = REQUESTINSTANCE?.error(error)

        }
        return INSTANCE
    }

    /**
     * 展示数据 目标view
     */
    override fun intoTargetView(imageView: ImageView) {
        REQUESTINSTANCE?.into(imageView)
    }

    /**
     * 重置缓存策略
     */
    override fun resetDiskCacheStrategy(strategy: DiskCacheStrategy): ImageLoader? {
        REQUESTINSTANCE = REQUESTINSTANCE?.diskCacheStrategy(strategy)
        return INSTANCE
    }

    /**
     * 清楚下载监听
     */
    private fun removeProcessListener(url: String) {
        val onProgressListener = ProgressManager.getProgressListener(url)
        if (onProgressListener != null) {
            onProgressListener.onProgress(true, 100, 0, 0)
            ProgressManager.removeListener(url)
        }
    }

    /**
     * 设置缓存模式
     */
    private fun initDiskCacheMode(diskCacheMenu: DiskCacheMenu) {
        var diskcacheStrategy = when (diskCacheMenu) {
            DiskCacheMenu.ALL -> DiskCacheStrategy.ALL
            DiskCacheMenu.NONE -> DiskCacheStrategy.NONE
            DiskCacheMenu.RESOURCE -> DiskCacheStrategy.RESOURCE
            DiskCacheMenu.AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
            DiskCacheMenu.DATA -> DiskCacheStrategy.DATA
        }
        resetOptionsCacheMode(diskcacheStrategy)
    }

    /**
     * 设置options缓存模式
     */
    private fun resetOptionsCacheMode(diskcacheStrategy: DiskCacheStrategy) {
        defaultOptions?.diskCacheStrategy(diskcacheStrategy)
        centerCropOptions?.diskCacheStrategy(diskcacheStrategy)
        centerInsideOptions?.diskCacheStrategy(diskcacheStrategy)
        fitCenterOptions?.diskCacheStrategy(diskcacheStrategy)
        circleCropOptions?.diskCacheStrategy(diskcacheStrategy)
    }

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

    /**
     * 获取URL  bitmap资源
     */
    override fun <CONTEXT, RES> getUrlWithBitmap(
        context: CONTEXT,
        url: RES?,
        imageListener: ImageLoaderListener<Bitmap>?
    ) {
        getGlideWith(context)?.asBitmap()?.load(url)?.diskCacheStrategy(DiskCacheStrategy.NONE)
            ?.into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageListener?.onRequestSuccess(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    imageListener?.onRequestFailed()
                }
            })
    }

    /**
     * 获取URL  bitmap资源
     */
    override fun <CONTEXT, RES> getUrlWithBitmap(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener<Bitmap>?
    ) {
        if (onProgressListener != null && url is String)
            ProgressManager.addListener(url, onProgressListener)
        getGlideWith(context)?.asBitmap()?.load(url)?.diskCacheStrategy(DiskCacheStrategy.NONE)
            ?.into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onProgressListener?.onComplete(resource)
                    if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    if (onProgressListener != null && url is String)
                    removeProcessListener(url)
                }
            })
    }

    /**
     * 获取图片bitmap资源
     */
//    override fun <CONTEXT, RES> getImageUrlBitmap(context: CONTEXT, url: RES?, imageListener: ImageLoaderListener?, onProgressListener: OnProgressListener?) {
//        if (onProgressListener != null && url is String)
//            ProgressManager.addListener(url, onProgressListener)
//        getGlideRequest(context, url)
//        (REQUESTINSTANCE?.diskCacheStrategy(DiskCacheStrategy.RESOURCE) as GlideRequest<Drawable>)?.into(object : SimpleTarget<Drawable>() {
//            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                imageListener?.onRequestSuccess(resource)
//                if (onProgressListener != null && url is String)
//                    removeProcessListener(url)
//            }
//
//            override fun onLoadFailed(errorDrawable: Drawable?) {
//                super.onLoadFailed(errorDrawable)
//                imageListener?.onRequestFailed()
//                if (onProgressListener != null && url is String)
//                    removeProcessListener(url)
//            }
//        })

//    }
}