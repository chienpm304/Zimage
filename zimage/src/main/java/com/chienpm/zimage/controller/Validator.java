package com.chienpm.zimage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.exception.ErrorCode;
import com.chienpm.zimage.utils.MyUtils;


/**
 *  Validator is a helper class providing validation methods
 */
public class Validator {


    /***
     * Check if the ImageView passed is correct to apply bitmap on or not.
     * @param mImageView the ImageView instance which need to be render on
     * @throws ZimageException if the ImageView passed is NULL or not instance of ImageView
     *         Exception return message: @ErrorCode.ERR_INVALID_IMAGE_VIEW
     */
    static void checkImageView(ImageView mImageView) throws ZimageException{
        if(mImageView == null)
            throw new ZimageException(ErrorCode.ERR_INVALID_IMAGE_VIEW);

        if(!(mImageView instanceof ImageView))
            throw new ZimageException(ErrorCode.ERR_INVALID_IMAGE_VIEW);

    }



    /**
     * Check if the Image URL passed is matching with standard url pattern or not
     * Do not check if it is a real image'url or not, we do in Network Layer later (if need)
     * @param mUrl string
     * @throws ZimageException code @ErrorCode.ERR_INVALID_IMAGE_URL
     */
    static void checkUrl(String mUrl) throws ZimageException{
        if(!MyUtils.isValidUrlPattern(mUrl))
            throw new ZimageException(ErrorCode.ERR_INVALID_IMAGE_URL);

    }



    /**
     * Check if the context passed is valid or not, context is necessary for system accession
     * @param mContext is the context of the ImageView to be render
     * @throws ZimageException code @ErrorCode.ERR_INVALID_CONTEXT
     */
    static void checkContext(Context mContext) throws ZimageException{
        if(mContext == null)
            throw new ZimageException(ErrorCode.ERR_INVALID_CONTEXT);
    }



    /**
     * Check the input bitmap is valid and ready to use or not
     * @param bitmap the bitmap image which might be null of damaged
     * @return True or False
     */
    public static boolean checkBitmap(Bitmap bitmap) {
		
        //Todo: validate if bitmap is invalid
        return bitmap!=null && bitmap.getByteCount() > 0;
		
    }

	/**
	 * Check if the resource is is invalied to render or not
	 * @return true if valid and false if not
	 */
    static boolean checkResourceId(int resId) {

        return true;

    }
}
