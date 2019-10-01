package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;

import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;

public class DiskCacheManager {

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

    public static void saveBitmap(String key, Bitmap bitmap) {

    }
}
