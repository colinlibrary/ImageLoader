package com.colin.library

interface ImageLoaderListener<T>  {
    fun onRequestSuccess(resource: T?)
    fun onRequestFailed()
}