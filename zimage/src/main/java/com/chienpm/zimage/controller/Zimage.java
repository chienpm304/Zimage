package com.chienpm.zimage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.chienpm.zimage.R;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.network_layer.Downloader.DownloaderCallback;
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

    public static int VERSION = 1;

    /* Zimage's exclusively instance*/
    private static Zimage mInstance = null;

    private static Object ZSync = new Object();

    private Context mContext;
    private String mUrl;
    private int mLoadingResId;
    private int mErrorResId;
    private ImageView mImageView;
    private int mWidth;
    private int mHeight;
    private ZimageCallback mListener;

    private Zimage() {
        reset();
    }

    public static Zimage getInstance(){
        synchronized (ZSync) {
            if (mInstance == null) {
                mInstance = new Zimage();
                ZSync.notifyAll();
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
        mWidth = 0;
        mHeight = 0;
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
        this.mWidth = width;
        this.mHeight = height;
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
            if(mListener!=null)
                mListener.onError(mImageView, e);

            // Apply error image when error occurs
            applyErrorImage();
        }
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

        Bitmap bitmap = null;

        //Try to load image from memory cache
        bitmap = MemoryCacheManager.getBitmapFromMemory(mUrl);

        if(Validator.checkBitmap(bitmap)) {
            applyBitmapToImageView(bitmap);
            return;
        }


        // Try to load image from disk
        bitmap = DiskCacheManager.loadBitmap(mUrl);
        if(Validator.checkBitmap(bitmap)) {
            applyBitmapToImageView(bitmap);

            // Cached bitmap loaded on memory
            MemoryCacheManager.loadBitmapInMemory(mUrl, bitmap);

            return;
        }

        // Download image from network
        NetworkManager.downloadFileFromURL(mContext, mUrl, new DownloaderCallback() {
            @Override
            public void onDownloadCompleted(@NonNull File targetFile) {
//                Bitmap networkBitmap
//                if(Validator.checkBitmap(bitmap)) {
//                    applyBitmapToImageView(bitmap);

//                    DiskCacheManager.saveBitmapOnDisk(mUrl, bitmap);
//
//                    MemoryCacheManager.loadBitmapInMemory(mUrl, bitmap);
//
//                }
            }

            @Override
            public void onError(Exception err) {

            }
        });




    }


    private void applyLoadingImage() {
        ImageUtils.inflateDrawableOverImageView(mContext, mImageView, mLoadingResId);
    }


    private void applyErrorImage() {
        ImageUtils.inflateDrawableOverImageView(mContext, mImageView, mErrorResId);
    }


    private void applyBitmapToImageView(@NonNull Bitmap bitmap) throws Exception{
        try {
            mImageView.setImageBitmap(bitmap);
            if(mListener!=null)
                mListener.onSucceed(mImageView, mUrl);
        }
        catch (Exception e){
            throw e;
        }
    }


    public interface ZimageCallback {
        void onSucceed(@NonNull ImageView imageView, @NonNull String url);
        void onError(@Nullable ImageView imageView, @NonNull Exception e);

        @VisibleForTesting
        boolean getResult();
    }

}
