package com.chienpm.zimage.network_layer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.chienpm.zimage.exception.ErrorCode;
import com.chienpm.zimage.exception.ZimageException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetworkManager {

    public static final String TAG = NetworkManager.class.getSimpleName();
	
	
    private static NetworkManager mInstance = null;


    private static ExecutorService mExecutor = null;


    private static Handler mHandler = null;

	
    private static final Object mSync = new Object();


    private NetworkManager(){

        initFields();

    }

	
    private void initFields() {

        mExecutor = Executors.newFixedThreadPool(4);

        mHandler = new Handler(Looper.getMainLooper());

    }

	
	
    public static NetworkManager getInstance(){

        synchronized (mSync){

            if(mInstance == null) {

                mInstance = new NetworkManager();

                mSync.notifyAll();

            }

        }

        return  mInstance;
    }

	
	
    public void downloadFileFromURL(final Context context, String url, final DownloadCallback callback)  {

        if(callback!=null) {

            if (NetworkUtils.isNetworkConnected(context)) {

                DownloadTask task = new DownloadTask(url, mHandler, callback);

                mExecutor.execute(task);

            } else {

                callback.onFailed(new ZimageException(ErrorCode.ERR_NO_INTERNET_CONNECTION));

            }
			
        }
        else{

            throw new RuntimeException("DownloadCallback must be not null!");

        }
    }

	
    //Todo: destroy function must be call when render request queue is empty (Top Layer)
    public static void destroy(){

        synchronized (mSync) {
			
            if (mInstance != null) {

                mInstance = null;

            }
            if (mExecutor != null && !mExecutor.isShutdown()) {

                mExecutor.shutdown();

            }
			
            if (mHandler != null) {
				
                mHandler = null;

            }
			
        }
		
    }

}
