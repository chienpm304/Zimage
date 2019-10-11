package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.chienpm.zimage.controller.Validator;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.mapping.MappingManager;
import com.chienpm.zimage.exception.ErrorCode;

import java.io.File;

class DiskLoadBitmapTask implements Runnable{
    public static final String TAG = DiskLoadBitmapTask.class.getSimpleName();
    private final String mUrl;
    private final Handler mHandler;
    private final DiskCacheCallback mCallback;

    public DiskLoadBitmapTask(String url, Handler handler, @NonNull DiskCacheCallback callback) {
        this.mUrl = url;
        this.mHandler = handler;
        this.mCallback = callback;
    }

    @Override
    public void run() {

        final File file = MappingManager.getFileFromURL(mUrl);

        // check if the image is existed on disk or not
        if(DiskUtils.checkFileIsExisted(file)){

            final Bitmap bitmap = DiskUtils.loadBitmapFromFile(file);

            if(Validator.checkBitmap(bitmap)) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSucceed(bitmap, file);
                    }
                });
            }
            else{
                // Unable to decode file because of permission denied (storage permission)
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onFailed(new ZimageException(ErrorCode.ERR_UNABLE_TO_DECODE_FILE_COZ_PERMISSION));
                    }
                });
            }
        }
        else{
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    mCallback.onFailed(new ZimageException(ErrorCode.ERR_FILE_CACHED_NOT_FOUND_OR_INVALID));
                }
            });
        }
    }
}
