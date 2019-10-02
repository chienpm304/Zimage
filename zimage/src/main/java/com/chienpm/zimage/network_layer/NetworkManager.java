package com.chienpm.zimage.network_layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;

import com.chienpm.zimage.R;

public class NetworkManager {

    public static Bitmap downloadImageAndConvertToBitmap(Context context, String url) throws Exception {

        Bitmap bitmap = null;

        //for test
        Drawable drawable =  context.getResources().getDrawable(R.drawable.ic_launcher_background);
        Canvas canvas = new Canvas();
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

//
//        if(NetworkUtils.checkNetworkConnected()){
//
//            Image downloadedImg = downloadImageFromUrl(url);
//
//            bitmap = ImageUtils.produceBitmapWithCondition(downloadedImg);
//
//        }
//        else{
//
//            throw new Exception(MsgDef.ERR_DISCONNECTED_FROM_NETWORK);
//
//        }
//        return null;
    }

    private static Image downloadImageFromUrl(String url) {

        return null;

    }

}
