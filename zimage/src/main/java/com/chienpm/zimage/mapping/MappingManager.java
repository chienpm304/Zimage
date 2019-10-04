package com.chienpm.zimage.mapping;

import android.os.Environment;

import com.chienpm.zimage.disk_layer.StorageUtils;

import java.io.File;

/***
 *  MappingManager is a helper class with mapping image URL to @DiskCacheManager path and @MemoryCacheManager
 */
public class MappingManager {

    public static String generateMemoryKeyFromUrl(String url) {
        return "111";
    }

    /**
     *
     * @param url
     * @return the temporary file path to download temporary image without extension
     */
    public static File generateTemporaryFileFromUrl(String url, String extension) {
        String file_name = String.valueOf(url.hashCode()) + extension;

        return new File(getBaseDir(), file_name);
    }

    public static File getLocalFileFromURL(String url) {
        return null;
    }


    public static File getBaseDir(){

        File root;
        if(StorageUtils.checkExternalStorageAvailable()){
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
