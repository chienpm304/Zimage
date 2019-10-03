package com.chienpm.zimage.mapping;

import android.os.Environment;
import android.util.Base64;

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
    public static String generateTemporaryFilePathFromUrl(String url) {
        String file_name = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);

        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/Zimage/123141";
    }

    public static File getLocalFileFromURL(String url) {
        return null;
    }

    public static File getBaseDir(){
        File base = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Zimage");
        return base;
    }
}
