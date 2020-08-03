package com.colin.imageloader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.colin.library.CornerTypeMenu
import com.colin.library.progress.OnProgressListener
import com.colin.library.GlideImageLoader
import com.colin.library.ImageLoaderListener
import com.colin.library.ScaleTypeMenu
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var defaultUrl="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2249871602,59311995&fm=11&gp=0.jpg"
    private var defaultUrl2="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596176909598&di=6acb306592ba8f5d14b353a4793b827a&imgtype=0&src=http%3A%2F%2Fimg1.touxiang.cn%2Fuploads%2F20131121%2F21-074918_36.jpg"
    private var defaultUrl3="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596177054579&di=18d88b391aa5ed7f58318c640fc4be8e&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ffront%2F435%2Fw640h595%2F20181130%2FErE8-hpevhcm5473942.jpg"
    private var defaultUrl4="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596188810464&di=69def7b3d4deac127c2ac47e110c55db&imgtype=0&src=http%3A%2F%2Ff.mgame.netease.com%2Fforum%2Fmonth_1505%2F150529124521aac0a2a5ca6192.gif"
    private var defaultUrl5="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596188810462&di=f31ae97156adb3470e3e8af40230b9a7&imgtype=0&src=http%3A%2F%2Fimg2.100bt.com%2Fupload%2Fttq%2F20121029%2F1351501394920.gif"
    private var defaultUrl6="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2853553659,1775735885&fm=26&gp=0.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadImage()
        bindListener()
    }

    private fun loadImage(){
        GlideImageLoader.getInstance().displayWithDrawable(this,defaultUrl)?.resetDiskCacheStrategy(
            DiskCacheStrategy.DATA)?.intoTargetView(iv_defalult)
        GlideImageLoader.getInstance().displayWithBitmap(this,defaultUrl2)?.intoTargetView(iv_defalult_bitmap)
        GlideImageLoader.getInstance().displayRoundWithDrawable(this,defaultUrl3,30,
            CornerTypeMenu.ALL)?.resetTargetSize(400,400)?.intoTargetView(iv_defalult_round)
        GlideImageLoader.getInstance().displayRoundWithBitmap(this,defaultUrl3,30,
            CornerTypeMenu.TOP)?.resetTargetSize(400,400)?.intoTargetView(iv_defalult_bitmap_round)
        GlideImageLoader.getInstance().displayRoundWithDrawable(this,defaultUrl4,30)?.resetTargetSize(400,400)?.intoTargetView(iv_defalult_gif)
        GlideImageLoader.getInstance().displayCircleWithDrawable(this,defaultUrl4)?.intoTargetView(iv_defalult_git_round)
        GlideImageLoader.getInstance().displayWithBlur(this,defaultUrl,20)?.intoTargetView(iv_defalult_blur)
        GlideImageLoader.getInstance().displayWithBlurRound(this,defaultUrl,20,10)?.intoTargetView(iv_defalult_blur_round)
        GlideImageLoader.getInstance().displayWithBlurRound(this,defaultUrl,20,1000)?.resetScaleType(ScaleTypeMenu.CenterCrop)?.intoTargetView(iv_defalult_blur_circle)
    }


    @SuppressLint("SetTextI18n")
    private fun bindListener(){
        tv_listener?.setOnClickListener {
            tv_listener?.text="图片加载中。。。"
            GlideImageLoader.getInstance().displayWithBitmap(this,defaultUrl2,object :
                ImageLoaderListener<Bitmap> {
                override fun onRequestSuccess(resource: Bitmap?) {
                    tv_listener?.visibility= View.INVISIBLE
                }

                override fun onRequestFailed() {
                    tv_listener?.text="图片加载失败～"
                }
            })?.intoTargetView(iv_defalult_listener)
        }
        tv_process?.setOnClickListener {
            tv_process?.text="0%"
            GlideImageLoader.getInstance().displayWithDrawable(this,defaultUrl5,object : OnProgressListener<Drawable>{
                override fun onProgress(
                    isComplete: Boolean,
                    percentage: Int,
                    bytesRead: Long,
                    totalBytes: Long
                ) {
                    tv_process?.text=percentage?.toString()+"%"
                }

                override fun onComplete(resource: Drawable?) {
                    tv_process?.visibility=View.INVISIBLE
                }

                override fun onFailed() {

                }
            } )?.intoTargetView(iv_defalult_process)
        }

        tv_getUrl_process?.setOnClickListener {
            tv_getUrl_process?.text="0%"
            GlideImageLoader.getInstance().getUrlWithBitmap(this,defaultUrl6,object :OnProgressListener<Bitmap>{
                override fun onProgress(
                    isComplete: Boolean,
                    percentage: Int,
                    bytesRead: Long,
                    totalBytes: Long
                ) {
                    tv_getUrl_process?.text=percentage?.toString()+"%"
                }

                override fun onComplete(resource: Bitmap?) {
                    tv_getUrl_process?.visibility=View.INVISIBLE
                    iv_getUrl?.setImageBitmap(resource)
                }

                override fun onFailed() {

                }
            })
        }
    }
}