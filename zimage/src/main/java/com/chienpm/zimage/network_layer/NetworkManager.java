package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;
import android.media.Image;

import com.chienpm.zimage.utils.ImageUtils;
import com.chienpm.zimage.utils.MsgDef;

public class NetworkManager {

    public static Bitmap downloadImageAndConvertToBitmap(String url) throws Exception {

        Bitmap bitmap = null;


        if(NetworkUtils.checkNetworkConnected()){

            Image downloadedImg = downloadImageFromUrl(url);

            bitmap = ImageUtils.produceBitmapWithCondition(downloadedImg);

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
