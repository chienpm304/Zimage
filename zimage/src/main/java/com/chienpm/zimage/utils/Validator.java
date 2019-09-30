package com.chienpm.zimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


/***
 *  Validator is a helper class providing validation methods
 */
public class Validator {
    public static void checkImageView(ImageView mImageView) throws Exception{
        if(mImageView == null)
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_VIEW);
        if(!(mImageView instanceof ImageView))
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_VIEW);

    }

    public static void checkUrl(String mUrl) throws Exception{
        if(!MyUtils.isValidUrlPattern(mUrl))
            throw new Exception(MsgDef.ERR_INVALID_IMAGE_URL);
        else{
            //Todo: need to request to http url to check invalid image here?
        }
    }

    public static void  checkContext(Context mContext) throws Exception{
        if(mContext == null)
            throw new Exception(MsgDef.ERR_INVALID_CONTEXT);
    }

    public static boolean checkBitmap(Bitmap bitmap) {
        //Todo: validate if bitmap is invalid
        return bitmap!=null;
    }
}
