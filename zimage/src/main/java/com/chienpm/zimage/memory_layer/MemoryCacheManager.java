package com.chienpm.zimage.memory_layer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.chienpm.zimage.mapping.MappingManager;

/***
 *  MemoryCacheManager is the master class of Memory Caching layer of library
 *  This class will holding a cache-memory which store bitmaps
 *  Get bitmap from key which mapping to image url view @MappingManager
 */
public class MemoryCacheManager {

    public static Bitmap getBitmap(@NonNull String url) {
        String key = MappingManager.generateKeyFromUrl(url);

        return null;
    }

    public static void saveBitmap(String url, Bitmap bitmap) {

    }
}
