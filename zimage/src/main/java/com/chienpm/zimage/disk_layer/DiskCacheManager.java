package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;

import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;

public class DiskCacheManager {

    private static DiskCacheManager mInstance = null;

    private static final Object mSync = new Object();

    private DiskCacheManager(){

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
     */
    public static Bitmap loadBitmap(String url) {
        File localFile = MappingManager.getLocalFileFromURL(url);

        // check if the image is existed on disk or not
        if(DiskHelper.checkFileIsExisted(localFile)){
            Bitmap bitmap = DiskHelper.loadBitmapImage(localFile);
            return bitmap;
        }

        return null;
    }

    public static void saveBitmapOnDisk(String url, Bitmap bitmap) {

        File file = MappingManager.getLocalFileFromURL(url);

        //save bitmap to file

    }


}
