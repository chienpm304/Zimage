package com.chienpm.zimage.network_layer;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    public interface DownloaderCallback{
        void onDownloadCompleted(@NonNull File targetFile);
        void onError(Exception err);
    }

    public static void downloadFile(String url, DownloaderCallback callback) {

        String outputPath = MappingManager.generateTemporaryFilePathFromUrl(url);

        new HttpDownloaderAsync(callback).execute(url, outputPath);
    }


    public static class HttpDownloaderAsync extends AsyncTask<String, Void, File>{
        public static final String TAG = HttpDownloaderAsync.class.getSimpleName();
        private DownloaderCallback mDownloadCallback=null;

        public HttpDownloaderAsync(@NonNull DownloaderCallback callback){
            this.mDownloadCallback = callback;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0];
            String outputPath = params[1];
            String filename = "";
            String ext = "";
            try{
                URL mUrl = new URL(url);
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
                        filename = url.substring(url.lastIndexOf("/") + 1);
                    }

                    ext = filename.substring(filename.lastIndexOf('.'));
                    if(!TextUtils.isEmpty(ext))
                        outputPath+=ext; //append extension

                    Log.i(TAG, "outputPath: "+outputPath);
                    InputStream inputStream = httpConn.getInputStream();
                    return NetworkUtils.writeStreamToFile(inputStream, outputPath);

                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if(mDownloadCallback!=null){
                if(file !=null && file.isFile() && file.exists()){
                    mDownloadCallback.onDownloadCompleted(file);
                }
                else {
                    mDownloadCallback.onError(new IOException());
                }
            }
            else{
                throw new RuntimeException("Downloader callback must be assigned");
            }
        }
    }
}
