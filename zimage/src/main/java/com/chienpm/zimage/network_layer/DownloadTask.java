package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.chienpm.zimage.controller.Validator;
import com.chienpm.zimage.disk_layer.DiskUtils;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.mapping.MappingManager;
import com.chienpm.zimage.exception.ErrorCode;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadTask implements Runnable {

    private static final String TAG = DownloadTask.class.getSimpleName();

    // The Url need to download image from
    private final String mUrlStr;

    // An interface to return to which component implementing @DownloadCallback interface
    private final DownloadCallback mCallback;

    // MainThread's looper handler: to pass to every DownloadTask instance (reducing the number of handler instances created), help return results in Mainthread
    private Handler mHandler = null;

    // The bitmap instance decoded from http stream
    private Bitmap mResultBitmap = null;

    // The file where image will the downloaded-image-file located on disk
//    private File mResultOutputFile = null;

    // Error
    private ZimageException mResultErr = null;


    /**
     * DownloadTask is a Runnable which is used to download bitmap from url and sent result when done (via DownloadCallback).
     *
     * @param url
     * @param handler which keeps MainThread's looper to make DownloadTask result callback will be invoked in MainThread
     * @param callback @DownloadCallback interface
     */
    public DownloadTask(String url, Handler handler, DownloadCallback callback) {
        this.mUrlStr = url;
        this.mCallback = callback;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "doInBackground: started");
        String filename = "";
        String ext = "";
        File outputFile;
        try{
            URL mUrl = new URL(mUrlStr);
            HttpURLConnection httpConn = (HttpURLConnection) mUrl.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setUseCaches(false);
            httpConn.setAllowUserInteraction(false);

            httpConn.setConnectTimeout(5000);
            httpConn.setReadTimeout(10000);

            httpConn.connect();

            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                String disposition = httpConn.getHeaderField("Content-Disposition");

                String contentType = httpConn.getContentType();

                if(!TextUtils.isEmpty(disposition)){
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");

                    if (index > 0) {

                        filename = disposition.substring(index + 10,
                                disposition.length() - 1);

                    }
                } else{

                    // extracts file name from URL
                    filename = mUrlStr.substring(mUrlStr.lastIndexOf("/") + 1);

                }

                ext = filename.substring(filename.lastIndexOf('.'));

                outputFile = MappingManager.getTemporaryFileFromUrl(mUrlStr, ext);

                Log.i(TAG, "outputPath: "+outputFile.getAbsolutePath());

                InputStream inputStream = httpConn.getInputStream();

                // Try to decode bitmap from inputstream first
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if(Validator.checkBitmap(bitmap)) {

                    mResultBitmap = bitmap;
                    mResultErr = null;

                } else {

                    NetworkUtils.writeStreamToFile(inputStream, outputFile);

                    bitmap = DiskUtils.loadBitmapFromFile(outputFile);

                    outputFile.delete();

                    if(Validator.checkBitmap(bitmap)) {
                        mResultBitmap = bitmap;
                        mResultErr = null;
                    }
                    else {
                        mResultBitmap = null;
                        mResultErr = new ZimageException(ErrorCode.ERR_INVALID_BITMAP);
                    }
                }
            }
            else{

                mResultErr = new ZimageException(ErrorCode.CANNOT_CONNECT_TO_SERVER);

            }

        } catch (Exception e) {
            e.printStackTrace();

            mResultBitmap = null;
            mResultErr = new ZimageException(
                    ErrorCode.ERR_WHEN_DOWNLOAD_IMAGE_FROM_NETWORK,
                    e.getMessage(),
                    e.getCause(),
                    e.getStackTrace());
        }
        finally {

            returnResults();

        }
    }



    /**
     * Return the values (bitmaps decoded or image downloaded (may not a bitmap) or ERROR
     * to any component which implement @DownloadTaskCall interface in MainThread.
     *
     * This function will throw a runtime error in these case:
     * - Case 1: DownloadTaslCallback instance passed is null or invalid.
     * - Case 2: No bitmap decoded (from HTTP connection inputstream), No image file downloaded from url, No error occurs while decoding and downloading.
     */
    private void returnResults() {

        mHandler.post(new Runnable() {

            @Override
            public void run() {

                if(mCallback !=null){

                    if(Validator.checkBitmap(mResultBitmap)){

                        mCallback.onSucceed(mResultBitmap);

                    }
                    else if(mResultErr != null){

                        mCallback.onFailed(mResultErr);

                    }
                    else{

                        throw new RuntimeException("All results were null!!!!");
                    }
                }
                else{

                    throw new RuntimeException("DownloadCallback must be passed to DownloadTask");

                }
            }
        });
    }

}
