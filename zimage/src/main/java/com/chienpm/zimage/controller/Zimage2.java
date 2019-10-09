package com.chienpm.zimage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chienpm.zimage.R;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.disk_layer.DiskCacheCallback;
import com.chienpm.zimage.disk_layer.DiskUtils;
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

            //Todo: queue up the requests
            loadImage();


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
     *  Start to loading image processes
     */
    public void loadImage() throws Exception {

        //Todo: recycle this bitmap
        final Bitmap[] bitmap = {null};

        //Try to load image from memory cache
        bitmap[0] = mMemoryCacheManager.loadBitmap(mUrl);

        if(Validator.checkBitmap(bitmap[0])) {

            Log.i(TAG, "loadImage: from MemoryCacheLayer");
            applyBitmapToImageView(bitmap[0]);

            return;
        }


        // Try to load image from disk
        mDiskCacheManager.loadBitmap(mUrl, new DiskCacheCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap bm, @NonNull File ouput_file) {
                bitmap[0] = bm.copy(bm.getConfig(), true);
                bm.recycle();
            }

            @Override
            public void onFailed(Exception err) {

            }
        });
        
        if(Validator.checkBitmap(bitmap[0])) {

            Log.i(TAG, "loadImage: from DiskCacheLayer");
            applyBitmapToImageView(bitmap[0]);

            // Cached bitmap loaded on memory
            mMemoryCacheManager.saveBitmap(mUrl, bitmap[0]);

            return;
        }

        // Fetch image from network (result in bitmap or image file)
        mNetworkManager.downloadFileFromURL(mContext, mUrl, new DownloadCallback() {

            @Override
            public void onDecodedBitmap(@NonNull Bitmap bitmap) {
                try {
                    Log.i(TAG, "loadImage from NetworkLayer (from STREAM)");

                    applyBitmapToImageView(bitmap);

                    processCacheOnDiskAndMemory(bitmap);

                } catch (Exception e) {

                    handleErrors(e);
                }
            }

            @Override
            public void onDownloadedImage(@NonNull File outputFile) {

                try{

                    Bitmap bitmap = DiskUtils.loadBitmapFromFile(outputFile);

                    if(Validator.checkBitmap(bitmap)) {

                        Log.i(TAG, "loadImage from NetworkLayer (from FILE DOWNLOADED)");

                        applyBitmapToImageView(bitmap);

                        processCacheOnDiskAndMemory(bitmap);

                    }
                    else{

                        handleErrors(new Exception("Cannot decode bitmap from image downloaded"));

                    }

                    outputFile.delete();

                }
                catch (Exception e){

                    handleErrors(e);
                }
            }

            @Override
            public void onError(@NonNull Exception err) {
                handleErrors(err);
            }

        });

    }

    /**
     * Try to cached bitmap fetched from NetworkLayer to DiskCache and MemoryCache
     * These 2 task are run in seperately threads
     * @param bitmap
     */
    private void processCacheOnDiskAndMemory(Bitmap bitmap) {

        try {
            // Todo: storage on diskCacheLayer
            mDiskCacheManager.saveBitmap(mUrl, bitmap, new DiskCacheCallback() {

                @Override
                public void onSucceed(@NonNull Bitmap bitmap, @NonNull File file) {

                    Log.i(TAG, "onSucceed: DiskCached "+file.getAbsolutePath());

                }

                @Override
                public void onFailed(Exception err) {

                    Log.e(TAG, "onError: DiskCached "+err.getMessage());

                }
            });

            // Todo: save bitmap on memoryCacheLayer
            mMemoryCacheManager.saveBitmap(mUrl, bitmap);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

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
        try {

            //todo: scale and crop bitmap to adaptive with imageView
            mImageView.setImageBitmap(bitmap);

            if(mListener!=null)
                mListener.onSucceed(mImageView, mUrl);
        }
        catch (Exception e){
            throw e;
        }
    }


}
