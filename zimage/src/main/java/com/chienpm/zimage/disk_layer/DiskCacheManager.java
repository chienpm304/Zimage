package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class DiskCacheManager {

	/* The exclusively instance of class */
    private static DiskCacheManager mInstance = null;

	/* Executor service to execute Runables on limited number of threads */
    private static ExecutorService mExecutor = null;

	/* Mainthread handler for posting result in Mainthread */
    private static Handler mHandler = null;

	/* Synchronize object*/
    private static final Object mSync = new Object();

	/* Hidden constructor for singleton design pattern*/
    private DiskCacheManager(){
        
		initFields();
		
    }


	/* Intit Executor service and handler once*/
    private void initFields() {

        mExecutor = Executors.newFixedThreadPool(4);

        mHandler = new Handler(Looper.getMainLooper());

    }


	/**
	 * Get the DiskCacheManager's instance
	 * @return mInstance
	 */ 
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
     * Try to loadbitmap from disk storage by using url to get file location 
	 * @param 
	 *	url: Cloud Image's URL string
	 *  @NotNull callback: DiskCacheCallback interface
	 * 
     */
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
