package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.chienpm.zimage.mapping.MappingManager;
import com.chienpm.zimage.utils.MsgDef;

import java.io.File;

class DiskLoadBitmapTask implements Runnable{
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

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mCallback.onSucceed(bitmap, file);

                }
            });

        }
        else{

            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    mCallback.onFailed(new Exception(MsgDef.ERR_INVALID_FILE_CACHED));

                }
            });
        }


    }
}
