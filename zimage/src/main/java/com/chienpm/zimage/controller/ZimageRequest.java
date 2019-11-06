package com.chienpm.zimage.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chienpm.zimage.R;

class ZimageRequest {

    
	private static final String TAG = ZimageRequest.class.getSimpleName();
    
	Context mContext;
    
	String mUrl;
    
	ImageView mImageView;
    
	ZimageCallback mListener;
    
	int mWidth;
    
	int mHeight;
    
	int mLoadingResId;
    
	int mErrorResId;

    ZimageRequest() {
    
		reset();

    }

    void reset() {
        mContext = null;
        mUrl = "";
        mImageView = null;
        mListener = null;
        mLoadingResId = R.drawable.default_loading_drawable;
        mErrorResId = R.drawable.default_error_drawable;
        mWidth = 0;
        mHeight = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ZimageRequest{" +
                "mContext=" + mContext +
                ", mUrl='" + mUrl + '\'' +
                ", mImageView=" + mImageView +
                ", mListener=" + mListener +
                '}';
    }

    ZimageRequest copy() {
		
        ZimageRequest req = new ZimageRequest();
        req.mContext = mContext;
        req.mUrl = mUrl;
        req.mImageView = mImageView;
        req.mListener = mListener;
        req.mWidth = mWidth;
        req.mHeight = mHeight;
        req.mErrorResId = mErrorResId;
        req.mLoadingResId = mLoadingResId;
        return req;
    }

	/**
	 * Get absolutated size of ImageView instance into width and heigh fields.
	 */
    void updateMeasuredSize() {
        int w = mImageView.getLayoutParams().width;
        int h = mImageView.getLayoutParams().height;

        if(w < 1){
            w = mImageView.getMeasuredWidth();
        }

        if(h<1){
            h = mImageView.getMeasuredHeight();
        }

        if(w < 1 || h < 1)
            Log.w(TAG, "updateMeasuredSize: cannot get correct image size");
        mWidth = w;
        mHeight = h;

    }
}
