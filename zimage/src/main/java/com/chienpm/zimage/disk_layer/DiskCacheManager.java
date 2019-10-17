package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiskCacheManager {

    private static DiskCacheManager mInstance = null;

    private static ExecutorService mExecutor = null;

    private static Handler mHandler = null;

    private static final Object mSync = new Object();

    private DiskCacheManager(){
        initFields();
    }

    private void initFields() {

        mExecutor = Executors.newFixedThreadPool(4);

        mHandler = new Handler(Looper.getMainLooper());

    }

    public static DiskCacheManager getInstance() {

        synchronized (mSync){

            if(mInstance == null) {

                mInstance = new DiskCacheManager();

                mSync.notifyAll();
            }

        }

        return  mInstance;
    }



    /**
     * @param url: Cloud Image's URL string
     * @return  the Bitmap image which loaded from disk storage
     *          or NULL if the image has not cached on disk yet
     * @Note: do not throw exception for next request cache layer
     */
//    public Bitmap loadBitmap(String url) {
//
//        File file = MappingManager.getFileFromURL(url);
//
//        // check if the image is existed on disk or not
//        if(DiskUtils.checkFileIsExisted(file)){
//
//            return DiskUtils.loadBitmapFromFile(file);
//
//        }
//
//        return null;
//    }


    public void loadBitmap(final String url, @NonNull final DiskCacheCallback callback) {

        if(callback != null) {

            DiskLoadBitmapTask task = new DiskLoadBitmapTask(url, mHandler, callback);

            mExecutor.execute(task);

        }
        else{

            throw new RuntimeException("loadbitmap(): DiskCacheCallback must be not null!");

        }

    }


    /**
     * Save bitmap to local disk storage for next request, reducing request http request to fetch image
     * This class mapping url and system file location to save bitmap file in disk
     * @param url
     * @param bitmap
     */
    public void saveBitmap(final String url, final Bitmap bitmap, @NonNull final DiskCacheCallback callback) {

        if(callback != null) {

            DiskSaveBitmapTask task = new DiskSaveBitmapTask(url, bitmap, mHandler, callback);

            mExecutor.execute(task);
        }
        else{

            throw new RuntimeException("saveBitmap(): DiskCacheCallback must be not null!");

        }


    }
}
