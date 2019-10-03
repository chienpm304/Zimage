package com.chienpm.zimage.network_layer;

import android.text.TextUtils;
import android.util.Log;

import com.chienpm.zimage.mapping.MappingManager;

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

                NetworkUtils.writeStreamToFile(inputStream, outputFile);

                mCallback.onDownloadCompleted(outputFile);
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            mCallback.onError(e);
        }
    }
}
