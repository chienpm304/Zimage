package com.chienpm.zimage.controller;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chienpm.zimage.disk_layer.DiskCacheCallback;
import com.chienpm.zimage.disk_layer.DiskCacheManager;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.memory_layer.MemoryCacheManager;
import com.chienpm.zimage.network_layer.DownloadCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.utils.ImageUtils;

import java.io.File;

/**
 * 
 */
class ZimageEngine {

    private static final String TAG = ZimageEngine.class.getSimpleName();

 	/* NetworkManager's instance */
    private static NetworkManager mNetworkManager = null;


	/* DiskCacheManager's instance */
    private static DiskCacheManager mDiskCacheManager = null;


	/* MemoryCacheManager's instance */
    private static MemoryCacheManager mMemoryCacheManager = null;
	
	/* Synchonize object */
    private static Object mSync = new Object();

	
	/* Request information holder*/
    private ZimageRequest mRequest;


    //init Manager instance
    static {
        mNetworkManager = NetworkManager.getInstance();
        mDiskCacheManager = DiskCacheManager.getInstance();
        mMemoryCacheManager = MemoryCacheManager.getInstance();
    }
	
	
    /**
     * Hidden ZimageEngine constructor to deny user creating ZimageEngine instances, use only one.
     * @param request the copy version of the built Request through builder functions
     */
    ZimageEngine(ZimageRequest request) {
        this.mRequest = request.copy();
        Log.i(TAG, "request after set: "+mRequest.toString());
    }



	/**
	 * Initialize nescessary data for next processes
	 */
    private void initParameters() {

        mRequest.updateMeasuredSize();
        Log.i(TAG, "initParameters: "+mRequest.mWidth+"x"+mRequest.mHeight);

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
     * Draw error resource on image
     * @param e is ZimageException instance which contain error message.
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

            Log.i(TAG, "Load done: from MemoryCacheLayer");

            applyBitmapToImageView(bitmap);

        }
        else {

            loadBitmapFromDisk();

        }

    }

    /**
     * Try to load bitmap from local disk storage using url key.
	 * If load succeed: draw bitmap on ImageView and save bitmap on Memory.
	 * If load failed : try to fetch image from network.
     */
    private void loadBitmapFromDisk() {

        // Try to load image from disk
        mDiskCacheManager.loadBitmap(mRequest.mUrl, new DiskCacheCallback() {

            @Override
            public void onSucceed(@Nullable Bitmap originBitmap, @NonNull File ouputFile) {

                Log.i(TAG, "Load done: from DiskCacheLayer");
                Log.i(TAG, "Request size: "+mRequest.mWidth+"x"+mRequest.mHeight);
                Log.i(TAG, "Bitmap loaded from disk: " + originBitmap.getWidth() +"x"+originBitmap.getHeight() + " size: "+originBitmap.getByteCount()/1024+" kb");

                Bitmap scaledBitmap = ImageUtils.resizeBitmap(originBitmap, mRequest.mWidth, mRequest.mHeight);

                Log.i(TAG, "Bitmap scaled: " + scaledBitmap.getWidth() +"x"+scaledBitmap.getHeight() + " size: "+scaledBitmap.getByteCount()/1024+" kb");

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
     * The bitmap result is call in DownloadTask:
	 * fetch succeed: draw bitmap on ImageView, save it on Disk and Memory
	 * fetch failed : handleErrors (draw errror resource)
     */
    private void fetchImageFromNetwork() {

        Log.i(TAG, "fetchImageFromNetwor: "+mRequest.mUrl);
        // Fetch image from network (result in bitmap or image file)
        mNetworkManager.downloadFileFromURL(mRequest.mContext, mRequest.mUrl, new DownloadCallback() {

            @Override
            public void onSucceed(@NonNull Bitmap originBitmap) {

                Log.i(TAG, "Load done: from NetworkLayer");
                Log.i(TAG, "Request size: "+mRequest.mWidth+"x"+mRequest.mHeight);
                Log.i(TAG, "Bitmap fetched from network: " + originBitmap.getWidth() +"x"+originBitmap.getHeight() + " size: "+originBitmap.getByteCount()+"  bytes");

                Bitmap scaledBitmap = ImageUtils.resizeBitmap(originBitmap, mRequest.mWidth, mRequest.mHeight);

                Log.i(TAG, "Bitmap scaled: " + scaledBitmap.getWidth() +"x"+scaledBitmap.getHeight() + " size: "+scaledBitmap.getByteCount()/1024+" kb");

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



	/**
	 * Save the bitmap instance in Memory Cache with mRequest.url
	 */
    private void saveBitmapOnMemory(Bitmap bitmap) {

        //Resize bitmap before cached it on memory

        mMemoryCacheManager.saveBitmap(mRequest.mUrl, bitmap, mRequest.mWidth, mRequest.mHeight);

    }


	/**
	 * Save the bitmap instance on Disk with mRequest.url for next cache access
	 * The results are returned in DiskCacheCallback
	 */
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


	/**
	 * Try to excute the request by flows:
	 * - Apply loading resource
	 * - Validate request parameters
	 * - Initialize request data
	 * - Try to loading bitmap from Memory (with url)
	 * If there is any error occurs while processing, process will be stop and render Error resource
	 */
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