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
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.colin.library.progress.OnProgressListener
import com.colin.library.progress.ProgressManager
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Glide图片处理类
 */
@Suppress("DEPRECATION")
class GlideImageLoader : ImageLoader {
    //动画时间
    private val CROSS_TIME = 500


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
        RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    private val centerCropOptions: RequestOptions =
        RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    private val centerInsideOptions: RequestOptions =
        RequestOptions().centerInside().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    private val fitCenterOptions: RequestOptions =
        RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    private val circleCropOptions: RequestOptions =
        RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    private var REQUESTINSTANCE: GlideRequest<*>? = null


    /**
     * 单例方式获取
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GlideImageLoader? = null

        @JvmStatic
        fun getInstance(): GlideImageLoader {
            if (INSTANCE == null) {
                synchronized(GlideImageLoader::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = GlideImageLoader()
                    }
                }
            }
            return INSTANCE ?: GlideImageLoader()
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
        resetPlaceHolder(R.mipmap.item_default, R.mipmap.item_default)
        resetScaleType(ScaleTypeMenu.Default)
        return INSTANCE
    }

    /**
     * bitmap 的方式获取资源
     */
    override fun <CONTEXT, RES> displayWithBitmap(context: CONTEXT, url: RES?): ImageLoader? {
        REQUESTINSTANCE = getGlideWith(context)?.asBitmap()?.load(url)?.transition(bitmapCrossFade)
        resetPlaceHolder(R.mipmap.item_default, R.mipmap.item_default)
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
        cornerType: RoundedCornersTransformation.CornerType
    ): ImageLoader? {
        displayWithDrable(context, url)
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

    override fun <CONTEXT, RES> displayRoundWithBitmap(
        context: CONTEXT,
        url: RES?,
        roundRadius: Int,
        cornerType: RoundedCornersTransformation.CornerType
    ): ImageLoader? {
        displayWithBitmap(context, url)
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
    override fun <CONTEXT, RES> displayWithDrableAndLoaderListener(
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
    override fun <CONTEXT, RES> displayWithDrableAndProgressListener(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener?
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

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
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
    override fun <CONTEXT, RES> displayWithBitmapAndLoaderListener(
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
    override fun <CONTEXT, RES> displayWithBitmapAndProgressListener(
        context: CONTEXT,
        url: RES?,
        onProgressListener: OnProgressListener?
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
    @SuppressLint("CheckResult")
    override fun resetPlaceHolder(placeholder: Int, error: Int): ImageLoader? {
        REQUESTINSTANCE = REQUESTINSTANCE?.error(error)?.placeholder(placeholder)
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
        REQUESTINSTANCE=REQUESTINSTANCE?.diskCacheStrategy(strategy)
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

    //内存缓存清理（主线程）
    fun clearMemoryCache(context: Context?) {
        GlideApp.get(context!!).clearMemory()

    }

    //磁盘缓存清理（子线程）
    fun clearFileCache(context: Context?) {
        Thread(Runnable { GlideApp.get(context!!).clearDiskCache() }).start()
    }
}