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
 * ZimageEngine is the master class to apply url image into a ImageView
 * Which is singleton and builder pattern:
 * @Usage:
 *          ZimageEngine.getInstance()
 *              .with(Context)
 *              .from(String url)
 *              .reisze(witdth, height)
 *              ...
 *              .into(ImageView)
 */
public class Zimage {


    private static final String TAG = ZimageEngine.class.getSimpleName();
    private static final String TAG_ERROR = "Zimage_ERROR";
    public static int VERSION = 1;

    /* ZimageEngine's exclusively instance*/
    private static Zimage mInstance = null;

    private static NetworkManager mNetworkManager = null;

    private static DiskCacheManager mDiskCacheManager = null;

    private static MemoryCacheManager mMemoryCacheManager = null;

    private static final Object mSync = new Object();


    // Inputs request fields
//    private Context mContext;
//    private String mUrl;
//    private int mLoadingResId;
//    private int mErrorResId;
//    private ImageView mImageView;
//    private int mWidth;
//    private int mHeight;
//    private ZimageCallback mListener;

    private ZimageRequest mRequest = new ZimageRequest();

    /**
     * Hidden ZimageEngine constructor to deny user creating ZimageEngine instances, use only one.
     */
    private Zimage() {
        initManagers();
        reset();
    }


    private void initManagers() {
        mNetworkManager = NetworkManager.getInstance();
        mDiskCacheManager = DiskCacheManager.getInstance();
        mMemoryCacheManager = MemoryCacheManager.getInstance();
    }


    public static Zimage getInstance(){
        synchronized (mSync) {
            if (mInstance == null) {
                mInstance = new Zimage();
                mSync.notifyAll();
            }
        }
        return mInstance;
    }

    public void reset() {
        mInstance = null;
        mRequest.reset();
//        mContext = null;
//        mUrl = "";
//        mImageView = null;
//        mListener = null;
//        mLoadingResId = R.drawable.default_loading_drawable;
//        mErrorResId = R.drawable.default_error_drawable;
//        mWidth = 0;
//        mHeight = 0;
    }

    /**
     *
     * @param context of Activity
     * @return ZimageEngine instance to continuous builder
     */
    public Zimage with(Context context){
        mRequest.mContext = context;
        return this;
    }

    /***
     *
     * @param url: Image string url need to display
     * @return ZimageEngine instance to continuous builder
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


    /**
     * @param width is new width of the bitmap to be cached on Disk
     * @param height is new height of the bitmap to be cached on Disk
     * @return ZimageEngine instance to continuous builder
     */
//    public Zimage resize(int width, int height){
//        this.mWidth = width;
//        this.mHeight = height;
//        return mInstance;
//    }

    /***
     *
     * @param resId is the Resource which will be render on ImageView while loading
     * @return ZimageEngine instance to continuous builder
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
     * @return ZimageEngine instance to continuous builder
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
