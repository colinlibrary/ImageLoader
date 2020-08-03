package com.colin.library.progress;

/**
 *
 */
public interface OnProgressListener<T> {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
    void onComplete(T resource);
    void onFailed();
}
