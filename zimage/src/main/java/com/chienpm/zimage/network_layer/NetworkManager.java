package com.chienpm.zimage.network_layer;

import android.content.Context;

import com.chienpm.zimage.utils.MsgDef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {
    static ExecutorService mPool = Executors.newFixedThreadPool(4);

    public static void downloadFileFromURL(Context context, String url, DownloadTaskCallback callback) throws Exception {
        if(NetworkUtils.isNetworkConnected(context)){

            DownloadTask task = new DownloadTask(url, callback);
            mPool.execute(task);

        }
        else{
            callback.onError(new Exception(MsgDef.ERR_NO_INTERNET_CONNECTION));
            throw new Exception(MsgDef.ERR_NO_INTERNET_CONNECTION);
        }
    }



        //for test
//        Drawable drawable =  context.getResources().getDrawable(R.drawable.ic_launcher_background);
//        Canvas canvas = new Canvas();
//        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        canvas.setBitmap(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
//        return bitmap;

}
