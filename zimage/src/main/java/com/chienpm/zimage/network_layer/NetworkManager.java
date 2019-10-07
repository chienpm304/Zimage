package com.chienpm.zimage.network_layer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.chienpm.zimage.utils.MsgDef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {

    private static NetworkManager mInstance = null;

    private static ExecutorService mPool = null;

    private static Handler mHandler = null;

    private static Object mSync = new Object();


    private NetworkManager(){

        initFields();

    }

    private void initFields() {

        mPool = Executors.newFixedThreadPool(4);

        mHandler = new Handler(Looper.getMainLooper());

    }


    public static NetworkManager getInstance(){

        synchronized (mSync){

            if(mInstance==null) {

                mInstance = new NetworkManager();

            }

        }

        return  mInstance;
    }


    public void downloadFileFromURL(final Context context, String url, final DownloadTaskCallback callback) throws Exception {

        if(callback!=null) {

            if (NetworkUtils.isNetworkConnected(context)) {

                DownloadTask task = new DownloadTask(url, mHandler, callback);

                mPool.execute(task);

            } else {

                callback.onError(new Exception(MsgDef.ERR_NO_INTERNET_CONNECTION));

                throw new Exception(MsgDef.ERR_NO_INTERNET_CONNECTION);

            }
        }
    }

    public static void destroy(){
        synchronized (mSync) {
            if (mInstance != null) {

                mInstance = null;

            }
            if (mPool != null && !mPool.isShutdown()) {

                mPool.shutdown();

            }
            if (mHandler != null) {
                //TOdo: check how to release handler
                mHandler = null;
            }
        }
    }

//    public static void returnToCallback(final Bitmap bitmap, final Exception e) {
//        Message msg = new Message();
//        msg.what = ACTION_BITMAP_DOWNLOADED;
//        Bundle bundle = new Bundle();
//        msg.setData(bundle);
//        mResultHandler.sendMessage(msg);


//       Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if(e==null)
//                    mCallback.onDecodedBitmap(bitmap);
//                else
//                    mCallback.onError(e);
//            }
//        });
//    }

//
//    private static DownloadResultHandler mResultHandler = new DownloadResultHandler(Looper.getMainLooper());
//
//    class DownloadResultHandler extends Handler {
//        DownloadResultHandler(Looper looper) {
//            super(looper);
//        }
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case ACTION_BITMAP_DOWNLOADED:
//                    break;
//            }
//        }
//    }

}
