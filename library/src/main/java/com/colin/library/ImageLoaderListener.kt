package com.ifenghui.imageloaderlibrary

interface ImageLoaderListener<T>  {
    fun onRequestSuccess(resource: T?)
    fun onRequestFailed()
}