package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chienpm.zimage.exception.ErrorCode;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;
import java.io.IOException;

public class DiskSaveBitmapTask implements Runnable {
    public static final String TAG = DiskSaveBitmapTask.class.getSimpleName();
    private final Handler mHandler;
    private final DiskCacheCallback mCallback;
    private final String mUrl;
    private final Bitmap mBitmap;

    public DiskSaveBitmapTask(String url, Bitmap bitmap, Handler handler, @NonNull DiskCacheCallback callback) {
        this.mHandler = handler;
        this.mUrl = url;
        this.mBitmap = bitmap;
        this.mCallback = callback;
    }

    @Override
    public void run() {

        try {

            final File file = MappingManager.getFileFromURL(mUrl);

            if (!DiskUtils.checkFileIsExisted(file)) {

                //save bitmap to file
                DiskUtils.saveBitmap(mBitmap, file);

            }
            else{

                Log.i(TAG, "run: bitmap is already existed on disk");

            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSucceed(mBitmap, file);
                }
            });


        } catch (final IOException e) {

            e.printStackTrace();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ZimageException err = new ZimageException(
                            ErrorCode.ERR_WHEN_SAVE_BITMAP_ON_DISK_ASK_STORAGE_PERMISSION,
                            e.getMessage(),
                            e.getCause(),
                            e.getStackTrace()
                    );
                    mCallback.onFailed(err);
                }
            });

        }
    }
}
