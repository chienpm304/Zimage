package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;
import android.media.Image;

import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.mapping.MappingManager;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.utils.ImageUtils;
import com.chienpm.zimage.utils.MsgDef;

public class NetworkManager {

    public static Bitmap downloadImageAndConvertToBitmap(String url) throws Exception {
        Bitmap bitmap = null;
        String key = MappingManager.generateKeyFromUrl(url);
        if(NetworkUtils.checkNetworkConnected()){
            Image downloadedImg = downloadImageFromUrl(url);
            bitmap = ImageUtils.produceBitmapWithCondition(downloadedImg);

            DiskCacheManager.saveBitmap(key, bitmap);
            MemoryCacheManager.saveBitmap(key, bitmap);
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
