package com.chienpm.zimage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chienpm.zimage.R;
import com.chienpm.zimage.disk_layer.DiskCacheCallback;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.network_layer.DownloadCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.utils.ImageUtils;

import java.io.File;

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


    public static final String TAG = Zimage.class.getSimpleName();
    public static int VERSION = 1;

    /* Zimage's exclusively instance*/
    private static Zimage mInstance = null;

    private static NetworkManager mNetworkManager = null;

    private static DiskCacheManager mDiskCacheManager = null;

    private static MemoryCacheManager mMemoryCacheManager = null;

    private static Object mSync = new Object();


    // Inputs request fields
    private Context mContext;
    private String mUrl;
    private int mLoadingResId;
    private int mErrorResId;
    private ImageView mImageView;
//    private int mWidth;
//    private int mHeight;
    private ZimageCallback mListener;


    /**
     * Hidden Zimage constructor to deny user creating Zimage instances, use only one.
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
        mContext = null;
        mUrl = "";
        mImageView = null;
        mListener = null;
        mLoadingResId = R.drawable.default_loading_drawable;
        mErrorResId = R.drawable.default_error_drawable;
//        mWidth = 0;
//        mHeight = 0;
    }

    /**
     *
     * @param context of Activity
     * @return Zimage instance to continuous builder
     */
    public Zimage with(Context context){
        this.mContext = context;
        return this;
    }

    /***
     *
     * @param url: Image string url need to display
     * @return Zimage instance to continuous builder
     */
    public Zimage from(@NonNull String url) {
        this.mUrl = url;
        return mInstance;
    }

    /**
     * Add an @ZimageCallback to listen the callback result is succeed or failed
     * @param listener
     * @return
     */
    public Zimage addListener(@NonNull ZimageCallback listener){
        this.mListener = listener;
        return mInstance;
    }


    /***
     *
     * @param width is new width to scale
     * @param height is new height to scale
     * @return Zimage instance to continuous builder
     */
    public Zimage resize(int width, int height){
//        this.mWidth = width;
//        this.mHeight = height;
        return mInstance;
    }

    /***
     *
     * @param resId is the Resource which will be render on ImageView while loading
     * @return Zimage instance to continuous builder
     */
    public Zimage loadingResourceId(@NonNull int resId){
        if(Validator.checkResourceId(resId))
            resId = R.drawable.default_loading_drawable;

        this.mLoadingResId = resId;

        return mInstance;
    }


    /***
     *
     * @param resId is the Resource which will be render on ImageView when loading failed
     * @return Zimage instance to continuous builder
     */
    public Zimage errorResId(@NonNull int resId){

        if(Validator.checkResourceId(resId))
            resId = R.drawable.default_error_drawable;

        this.mErrorResId = resId;

        return mInstance;
    }

    /***
     *
     * @param imageView: the ImageView which will be apply image from url on.
     * @return the ImageView with an image rendered on it.
     *         if any error occurs, an Error message will be render in ImageView
     *         and ErrorCallback will be called
     *
     */
    public void into(@NonNull ImageView imageView){

        this.mImageView = imageView;

        try {
            // Apply loading image while fetch image
            applyLoadingImage();

            validateParameters();

            loadBitmapFromMemory();


        }
        catch (Exception e){

            handleErrors(e);

        }
    }

    /**
     * Handle Errors which occur when processing
     * Draw error bitmap on image
     * @param e is Exception instance which contain error message.
     */
    private void handleErrors(Exception e) {

        Log.i(TAG, "handleErrors: "+e.getMessage());

        if(mListener!=null) {

            mListener.onError(mImageView, mUrl, e);

        }

        // Apply error image when error occurs
        applyErrorImage();

    }


    /***
     * Validate parameters passed before running library
     * We will check: Context, Url, ImageView instances
     * @throws Exception if any of it is Invalid
     */
    private void validateParameters() throws Exception{
        try{
            Validator.checkContext(mContext);
            Validator.checkUrl(mUrl);
            Validator.checkImageView(mImageView);
        }
        catch (Exception e){
            throw e;
        }
    }



    /***
     *  Try to load bitmap from memory using url key
     */
    public void loadBitmapFromMemory() {

        Bitmap bitmap;

        //Try to load image from memory cache
        bitmap = mMemoryCacheManager.loadBitmap(mUrl);

        if(Validator.checkBitmap(bitmap)) {

            Log.i(TAG, "loadBitmapFromMemory: from MemoryCacheLayer");

            try {

                applyBitmapToImageView(bitmap);

            } catch (Exception e) {

                handleErrors(e);

            }

        }
        else {

            loadBitmapFromDisk();

        }

    }

    /**
     * Try to load bitmap from local disk storage using url key
     */
    private void loadBitmapFromDisk() {

        // Try to load image from disk
        mDiskCacheManager.loadBitmap(mUrl, new DiskCacheCallback() {

            @Override
            public void onSucceed(@Nullable Bitmap bm, @NonNull File ouputFile) {

                Log.i(TAG, "Load done: from DiskCacheLayer");

                try {

                    applyBitmapToImageView(bm);

                    // Cached bitmap loaded on memory
                    saveBitmapOnMemory(bm);

                } catch (Exception e) {

                    handleErrors(e);

                }



            }

            @Override
            public void onFailed(Exception err) {
                try {

                    fetchImageFromNetwork();

                } catch (Exception e) {

                    e.printStackTrace();

                    handleErrors(e);

                }
            }
        });
    }


    /**
     * Try to fetch image from network using DownloadTask to decoded image to bitmap
     * The bitmap result is call in DownloadTask
     */
    private void fetchImageFromNetwork() {

        // Fetch image from network (result in bitmap or image file)
        mNetworkManager.downloadFileFromURL(mContext, mUrl, new DownloadCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap bitmap) {

                Log.i(TAG, "Load done: from NetworkLayer");

                applyBitmapToImageView(bitmap);

                saveBitmapOnDisk(bitmap);

                saveBitmapOnMemory(bitmap);

            }

            @Override
            public void onFailed(@NonNull Exception err) {

                handleErrors(err);

            }

        });
    }

    private void saveBitmapOnMemory(Bitmap bitmap) {

        mMemoryCacheManager.saveBitmap(mUrl, bitmap);

    }

    private void saveBitmapOnDisk(Bitmap bitmap) {

        mDiskCacheManager.saveBitmap(mUrl, bitmap, new DiskCacheCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap bitmap, @NonNull File file) {

                Log.i(TAG, "onSucceed: DiskCached "+file.getAbsolutePath());

            }

            @Override
            public void onFailed(Exception err) {

                Log.e(TAG, "onFailed: DiskCached "+err.getMessage());
                handleErrors(err);
            }
        });
    }


    /**
     * Draw loading image on ImageView while Zimage processing
     */
    private void applyLoadingImage() {

        ImageUtils.inflateDrawableOverImageView(mContext, mImageView, mLoadingResId);

    }


    /**
     * Draw loading image on ImageView when Zimage process failed
     */
    private void applyErrorImage() {

        ImageUtils.inflateDrawableOverImageView(mContext, mImageView, mErrorResId);

    }


    /**
     * Draw bitmap fetched on ImageView
     * @param bitmap
     */
    private void applyBitmapToImageView(@NonNull Bitmap bitmap) {

        Log.i(TAG, "bitmap size: "+bitmap.getByteCount()/1024+" kB");

        //todo: scale and crop bitmap to adaptive with imageView
        mImageView.setImageBitmap(bitmap);

        if(mListener!=null)
            mListener.onSucceed(mImageView, mUrl);

    }


}
