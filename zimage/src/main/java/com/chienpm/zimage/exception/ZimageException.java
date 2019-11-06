package com.chienpm.zimage.exception;

import android.text.TextUtils;

import java.io.IOException;

public class ZimageException extends Exception {

    private Throwable mCause;
	
    private String mMessage;
    
	private ErrorCode mCode;

    public ZimageException(ErrorCode code) {

        mCode = code;

    }


    public ZimageException(ErrorCode code, String message, Throwable cause, StackTraceElement[] stackTrace) {
        this.mCode = code;
        this.mMessage = message;
        this.mCause = cause;
        this.setStackTrace(stackTrace.clone());
    }

    public String getMessage(){
        if(!TextUtils.isEmpty(mMessage))
            return mMessage;
        else{
            return "Error code "+mCode;
        }
    }



}
