package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.chienpm.zimage.controller.Validator;
import com.chienpm.zimage.disk_layer.StorageUtils;
import com.chienpm.zimage.mapping.MappingManager;
import com.chienpm.zimage.utils.MsgDef;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadTask implements Runnable{

    private static final String TAG = DownloadTask.class.getSimpleName();
    private final String mUrlStr;
    private final DownloadTaskCallback mCallback;

    public DownloadTask(String url, DownloadTaskCallback callback) {
        this.mUrlStr = url;
        this.mCallback = callback;
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

                outputFile = MappingManager.generateTemporaryFileFromUrl(mUrlStr, ext);

                Log.i(TAG, "outputPath: "+outputFile.getAbsolutePath());

                InputStream inputStream = httpConn.getInputStream();

                // Try to decode bitmap from inputstream first
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if(Validator.checkBitmap(bitmap)) {

                    mCallback.onDecodedBitmap(bitmap);

                } else {

                    NetworkUtils.writeStreamToFile(inputStream, outputFile);

                    if(StorageUtils.checkOutputImageFile(outputFile))
                        mCallback.onDownloadedImage(outputFile);
                    else
                        mCallback.onError(new Exception(MsgDef.ERR_INVALID_BITMAP));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mCallback.onError(e);
        }
    }

}
