package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;
import java.io.IOException;

public class DiskSaveBitmapTask implements Runnable {
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

            if (DiskUtils.checkFileIsExisted(file))
                file.delete();

            //save bitmap to file
            DiskUtils.saveBitmap(mBitmap, file);

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
                    mCallback.onFailed(e);
                }
            });

        }
    }
}
