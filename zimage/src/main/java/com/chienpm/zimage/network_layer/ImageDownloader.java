package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;
import android.media.Image;

import com.chienpm.zimage.disk_layer.DiskCache;
import com.chienpm.zimage.memory_layer.MemoryCache;
import com.chienpm.zimage.utils.ImageUtils;
import com.chienpm.zimage.utils.MsgDef;

public class ImageDownloader {

    public static Bitmap downloadImageAndConvertToBitmap(String key, String url) throws Exception {
        Bitmap bitmap = null;
        if(NetworkUtils.checkNetworkConnected()){
            Image downloadedImg = downloadImageFromUrl(url);
            bitmap = ImageUtils.produceBitmapWithCondition(downloadedImg);

            DiskCache.saveBitmap(key, bitmap);
            MemoryCache.saveBitmap(key, bitmap);
        }
        else{
            throw new Exception(MsgDef.ERR_DISCONNECTED_FROM_NETWORK);
        }
        return null;
    }

    private static Image downloadImageFromUrl(String url) {
        return null;
    }
}
