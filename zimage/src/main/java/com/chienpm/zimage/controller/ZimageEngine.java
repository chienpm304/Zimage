package com.chienpm.zimage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chienpm.zimage.R;
import com.chienpm.zimage.controller.Zimage.ZimageRequest;
import com.chienpm.zimage.disk_layer.DiskCacheCallback;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.network_layer.DownloadCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.utils.ImageUtils;

import java.io.File;

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
public class ZimageEngine {


    public static final String TAG = ZimageEngine.class.getSimpleName();
    public static final String TAG_ERROR = "Zimage_ERROR";
    public static int VERSION = 1;

    /* ZimageEngine's exclusively instance*/
//    private static ZimageEngine mInstance = null;

    private static NetworkManager mNetworkManager = null;

    private static DiskCacheManager mDiskCacheManager = null;

    private static MemoryCacheManager mMemoryCacheManager = null;

    private static Object mSync = new Object();

    private ZimageRequest mRequest;


    //init Manager instance
    static {
        mNetworkManager = NetworkManager.getInstance();
        mDiskCacheManager = DiskCacheManager.getInstance();
        mMemoryCacheManager = MemoryCacheManager.getInstance();
    }
    /**
     * Hidden ZimageEngine constructor to deny user creating ZimageEngine instances, use only one.
     * @param request
     */
    ZimageEngine(ZimageRequest request) {
        this.mRequest = request.copy();
        Log.i(TAG, "request after set: "+mRequest.toString());
    }




//    public static ZimageEngine getInstance(){
//        synchronized (mSync) {
//            if (mInstance == null) {
//                mInstance = new ZimageEngine();
//                mSync.notifyAll();
//            }
//        }
//        return mInstance;
//    }

    private void initParameters() {
        mRequest.mWidth = mRequest.mImageView.getMeasuredWidth();
        mRequest.mHeight = mRequest.mImageView.getMeasuredHeight();

//        mContext.createConfigurationContext(new Configuration())

//        Log.i(TAG, "ImageView: "+mWidth+"x"+mHeight);
    }


    /***
     * Validate parameters passed before running library
     * We will check: Context, Url, ImageView instances
     * @throws ZimageException if any of it is Invalid
     */
    private void validateParameters() throws ZimageException {
        try{

            Validator.checkContext(mRequest.mContext);
            Validator.checkUrl(mRequest.mUrl);
            Validator.checkImageView(mRequest.mImageView);

        }
        catch (ZimageException e){
            throw e;
        }
    }

    /**
     * Handle Errors which occur when processing
     * Draw error bitmap on image
     * @param e is Exception instance which contain error message.
     */
    private void handleErrors(ZimageException e) {

        Log.i(TAG, "handleErrors: "+e.getMessage());

        if(mRequest.mListener!=null) {

            mRequest.mListener.onFailed(mRequest.mImageView, mRequest.mUrl, e);

        }

        // Apply error image when error occurs
        applyErrorImage();

    }



    /***
     *  Try to load bitmap from memory using url key
     */
    private void loadBitmapFromMemory() {

        Bitmap bitmap;

        //Try to load image from memory cache
        bitmap = mMemoryCacheManager.loadBitmap(mRequest.mUrl, mRequest.mWidth, mRequest.mHeight);

        if(Validator.checkBitmap(bitmap)) {

            Log.i(TAG, "loadBitmapFromMemory: from MemoryCacheLayer");

            applyBitmapToImageView(bitmap);

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
        mDiskCacheManager.loadBitmap(mRequest.mUrl, new DiskCacheCallback() {

            @Override
            public void onSucceed(@Nullable Bitmap originBitmap, @NonNull File ouputFile) {

                Log.i(TAG, "Load done: from DiskCacheLayer");

                Log.i(TAG, "Bitmap loaded from disk: " + originBitmap.getWidth() +"x"+originBitmap.getHeight() + " size: "+originBitmap.getByteCount()/1024+" kb");

                Bitmap scaledBitmap = ImageUtils.resizeBitmap(originBitmap, mRequest.mWidth, mRequest.mHeight);

                //render scaled bitmap on ImageView
                applyBitmapToImageView(scaledBitmap);

                // Cached bitmap loaded on memory
                saveBitmapOnMemory(scaledBitmap);

            }

            @Override
            public void onFailed(ZimageException err) {

                Log.e(TAG, "DiskCache load Bitmap onFailed: ", err);

                fetchImageFromNetwork();

            }
        });
    }


    /**
     * Try to fetch image from network using DownloadTask to decoded image to bitmap
     * The bitmap result is call in DownloadTask
     */
    private void fetchImageFromNetwork() {

        Log.i(TAG, "fetchImageFromNetwor: "+mRequest.mUrl);
        // Fetch image from network (result in bitmap or image file)
        mNetworkManager.downloadFileFromURL(mRequest.mContext, mRequest.mUrl, new DownloadCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap originBitmap) {

                Log.i(TAG, "Load done: from NetworkLayer");
                Log.i(TAG, "Bitmap fetched from network: " + originBitmap.getWidth() +"x"+originBitmap.getHeight() + " size: "+originBitmap.getByteCount()+"  bytes");

                Bitmap scaledBitmap = ImageUtils.resizeBitmap(originBitmap, mRequest.mWidth, mRequest.mHeight);

                // render scaled bitmap on ImageView
                applyBitmapToImageView(scaledBitmap);

                // save original bitmap on Disk
                saveBitmapOnDisk(originBitmap);

                // save scaled bitmap in Memory
                saveBitmapOnMemory(scaledBitmap);

            }

            @Override
            public void onFailed(@NonNull ZimageException err) {

                Log.e(TAG_ERROR, "NetworkLayer fetch image Failed: ", err);

                handleErrors(err);

            }

        });
    }

    private void saveBitmapOnMemory(Bitmap bitmap) {

        //Resize bitmap before cached it on memory

        mMemoryCacheManager.saveBitmap(mRequest.mUrl, bitmap, mRequest.mWidth, mRequest.mHeight);

    }

    private void saveBitmapOnDisk(Bitmap bitmap) {

        mDiskCacheManager.saveBitmap(mRequest.mUrl, bitmap, new DiskCacheCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap bitmap, @NonNull File file) {

                Log.i(TAG, "onSucceed: DiskCached "+file.getAbsolutePath());

            }

            @Override
            public void onFailed(ZimageException err) {

                Log.e(TAG_ERROR, "DiskCache saveBitmapOnDisk Failed" + err.getMessage());

            }
        });
    }


    /**
     * Draw loading image on ImageView while ZimageEngine processing
     */
    private void applyLoadingImage() {

        ImageUtils.inflateDrawableOverImageView(mRequest.mContext, mRequest.mImageView, mRequest.mLoadingResId);

    }


    /**
     * Draw loading image on ImageView when ZimageEngine process failed
     */
    private void applyErrorImage() {

        ImageUtils.inflateDrawableOverImageView(mRequest.mContext, mRequest.mImageView, mRequest.mErrorResId);

    }


    /**
     * Draw bitmap fetched on ImageView
     * @param bitmap
     */
    private void applyBitmapToImageView(@NonNull Bitmap bitmap) {

        Log.i(TAG, "bitmap rendered: " + bitmap.getWidth() +"x"+bitmap.getHeight() + " size: "+bitmap.getByteCount()/1024+"kb");

        //todo: scale and crop bitmap to adaptive with imageView
        mRequest.mImageView.setImageBitmap(bitmap);

        if(mRequest.mListener!=null)
            mRequest.mListener.onSucceed(mRequest.mImageView, mRequest.mUrl);

    }


    void execute(){
        try {

            applyLoadingImage();

            validateParameters();

            initParameters();

            loadBitmapFromMemory();

        }
        catch (ZimageException e){
            Log.e(TAG_ERROR, "ZimageEngine summary error: ", e);
            e.printStackTrace();
            handleErrors(e);

        }

    }
}