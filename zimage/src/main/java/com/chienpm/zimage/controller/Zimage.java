package com.chienpm.zimage.controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chienpm.zimage.utils.MyUtils;
import com.chienpm.zimage.utils.MsgDef;

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
    private static Zimage mInstance = null;

    private static Object ZSync = new Object();

    private Context mContext;
    private String mUrl;
    private String mLoadingMsg;
    private String mErrorMsg;
    private ImageView mImageView;
    private int mWidth;
    private int mHeight;

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
        mLoadingMsg = MsgDef.MSG_IMG_LOADING;
        mErrorMsg = MsgDef.MSG_IMG_ERROR;
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
    public Zimage from(@NonNull String url) throws Exception{
        this.mUrl = url;
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
     * @param loadingMsg is the text which will be render on ImageView while loading
     * @return Zimage instance to continuous builder
     */
    public Zimage loadingMsg(@NonNull String loadingMsg){
        if(TextUtils.isEmpty(loadingMsg))
            loadingMsg = MsgDef.MSG_IMG_LOADING;

        this.mLoadingMsg = loadingMsg;

        return mInstance;
    }


    /***
     *
     * @param errorMsg is the text which will be render on ImageView while loading
     * @return Zimage instance to continuous builder
     */
    public Zimage errorMsg(@NonNull String errorMsg){
        if(TextUtils.isEmpty(errorMsg))
            errorMsg = MsgDef.MSG_IMG_ERROR;

        this.mErrorMsg = errorMsg;

        return mInstance;
    }

    /***
     *
     * @param imageView: the ImageView which will be apply image from url on.
     * @return the ImageView with an image rendered on it.
     *         if any error occurs, an Eroor message will be render in ImageView.
     * @throws Exception if the parameters passed before is INVALID
     */
    public void into(@NonNull ImageView imageView) throws Exception{
        this.mImageView = imageView;

        try {
            validateParameters();

            loadImage();
        }
        catch (Exception e){
            throw e;
        }
    }


    /***
     * Validate parameters passed before running library
     * We will check: Context, Url, ImageView instances
     * @throws Exception if any of it is Invalid
     */
    private void validateParameters() throws Exception{
        try{
            checkContext();
            checkUrl();
            checkImageView();
        }
        catch (Exception e){
            throw e;
        }
    }

    private void checkImageView() throws Exception{
        if(mImageView == null)
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_VIEW);
        if(!(mImageView instanceof ImageView))
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_VIEW);

    }

    private void checkUrl() throws Exception{
        if(!MyUtils.isValidUrlPattern(mUrl))
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_URL);
        else{
            //Todo: need to request to http url to check invalid image here?
        }
    }

    private void checkContext() throws Exception{
        if(mContext == null)
            throw new Exception(MsgDef.ERR_INVALID_CONTEXT);
    }


    /***
     *  Start to loading image processes
     */
    private void loadImage() {

    }

}
