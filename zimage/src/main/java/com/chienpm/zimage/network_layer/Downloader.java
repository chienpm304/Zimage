package com.chienpm.zimage.network_layer;

import android.os.AsyncTask;

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

        String outputPath = MappingManager.generateFilePathFromUrl(url);

        new HttpDownloaderAsync(callback).execute(url, outputPath);
    }


    public static class HttpDownloaderAsync extends AsyncTask<String, Void, File>{

        private DownloaderCallback mDownloadCallback=null;

        public HttpDownloaderAsync(@NonNull DownloaderCallback callback){
            this.mDownloadCallback = callback;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0];
            String outputPath = params[1];

            try{
                URL mUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(10000);

                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
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
