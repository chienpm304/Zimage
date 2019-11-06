package com.chienpm.zimage.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.chienpm.zimage.R;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.network_layer.NetworkManager;

/**
 * Zimage is the master class to apply url image into a ImageView
 * Which is singleton and builder pattern:
 * @Usage:
 *          Zimage.getInstance()
 *              .with(Context)
 *              .from(String url)
 *              .reisze(witdth, height)
 *              ...
 *              .into(ImageView)
 */
public class Zimage {


    private static final String TAG = Zimage.class.getSimpleName();
	

    /* Zimage's exclusively instance*/
    private static Zimage mInstance = null;

    
	/* Temporary object for synchornize operation */
	private static final Object mSync = new Object();



	/* Request information holder*/
    private ZimageRequest mRequest = new ZimageRequest();



    /**
     * Hidden Zimage constructor to deny user creating Zimage instances, use only one.
	 * Get Zimage instance by: Zimage.getInstance();
     */
    private Zimage() {
        reset();
    }


	/**
	 * Get the mInstance - the only one instance of this class
	 *
	 */
    public static Zimage getInstance(){
        synchronized (mSync) {
            if (mInstance == null) {
                mInstance = new Zimage();
                mSync.notifyAll();
            }
        }
        return mInstance;
    }

	/**
	 * Reset request 
	 */
    private void reset() {
        mInstance = null;
        mRequest.reset();
    }


    /**
     *
     * @param context of Activity
     * @return Zimage instance for continuous builder
     */
    public Zimage with(Context context){
        mRequest.mContext = context;
        return this;
    }



    /***
     * @param url: Image string url need to display
     * @return Zimage instance for continuous builder
     */
    public Zimage from(@NonNull String url) {
        mRequest.mUrl = url;
        return mInstance;
    }



    /**
     * Add an @ZimageCallback to listen the callback result is succeed or failed
     * @param listener
     * @return
     */
    public Zimage addListener(@NonNull ZimageCallback listener){
        mRequest.mListener = listener;
        return mInstance;
    }



    /***
     *
     * @param resId is the Resource which will be render on ImageView while loading
     * @return Zimage instance for continuous builder
	 * 
     */
    public Zimage setLoadingResource(@IdRes int resId){
        if(Validator.checkResourceId(resId))
            resId = R.drawable.default_loading_drawable;

        mRequest.mLoadingResId = resId;

        return mInstance;
    }



    /***
     *
     * @param resId is the Resource which will be render on ImageView when loading failed
     * @return Zimage instance for continuous builder
	 * 
     */
    public Zimage setErrorResource(@IdRes int resId){

        if(Validator.checkResourceId(resId))
            resId = R.drawable.default_error_drawable;

        mRequest.mErrorResId = resId;

        return mInstance;
    }



    /***
     *
     * @param imageView: the ImageView which will be apply image from url on.
     * The ImageView with an image rendered on it.
     *         if any error occurs, an Error message will be render in ImageView
     *         and ErrorCallback will be called
     *
     */
    public void into(@NonNull ImageView imageView){

        mRequest.mImageView = imageView;

        new ZimageEngine(mRequest).execute();
    }

}
