package com.chienpm.zimage.mapping;

import android.os.Environment;
import android.util.Log;

import com.chienpm.zimage.disk_layer.DiskUtils;

import java.io.File;

/***
 *  MappingManager is a helper class with mapping image URL to @DiskCacheManager path and @MemoryCacheManager
 */
public class MappingManager {

    public static final String TAG = MappingManager.class.getSimpleName();

    public static String getKeyFromUrl(String url) {
        return String.valueOf(Math.abs(url.hashCode()));
    }

    public static String getKeyFromUrl(String url, int width, int height) {
        return String.valueOf(Math.abs(new String(url+width+height).hashCode()));
    }
    /**
     *
     * @param url
     * @return the temporary file path to download temporary image without extension
     */
    public static File getTemporaryFileFromUrl(String url, String extension) {

        String file_name = String.valueOf(Math.abs(url.hashCode())) + extension;

        File root = new File(getBaseDir(), "tmp");

        if(!root.exists())
            root.mkdirs();

        File file = new File(root, file_name);

        Log.i(TAG, "getTemporaryFileFromUrl: "+file.getAbsolutePath());
        return file;

    }


    public static File getFileFromURL(String url) {

        String file_name = String.valueOf(Math.abs(url.hashCode())) + ".jpg";

        File root = new File(getBaseDir(), "cache"+File.separator+"images");

        if(!root.exists())
            root.mkdirs();

        File file = new File(root, file_name);

        Log.i(TAG, "getFileFromURL: "+file.getAbsolutePath());

        return file;

    }


    private static File getBaseDir(){

        File root;

        if(DiskUtils.checkExternalStorageAvailable()){

            root = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Zimage");

        }
        else{

            root = new File(Environment.getDataDirectory(), "Zimage");

        }

        if(!root.exists()) {

            root.mkdirs();

        }


        return root;
    }

}
