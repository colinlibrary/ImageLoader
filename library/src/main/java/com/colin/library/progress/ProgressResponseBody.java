package com.colin.library.progress;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 *
 */
public class ProgressResponseBody extends ResponseBody {

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private String url;
    private InternalProgressListener internalProgressListener;

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    ProgressResponseBody(String url, InternalProgressListener internalProgressListener, ResponseBody responseBody) {
        this.url = url;
        this.internalProgressListener = internalProgressListener;
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead;
            long lastTotalBytesRead;

            @Override
            public long read( Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;

                if (internalProgressListener != null && lastTotalBytesRead != totalBytesRead) {
                    lastTotalBytesRead = totalBytesRead;
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            internalProgressListener.onProgress(url, totalBytesRead, contentLength());
                        }
                    });
                }
                return bytesRead;
            }
        };
    }

    interface InternalProgressListener {
        void onProgress(String url, long bytesRead, long totalBytes);
    }
}
