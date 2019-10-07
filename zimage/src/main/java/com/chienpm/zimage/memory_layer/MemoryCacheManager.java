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


    private static MemoryCacheManager mInstance = null;

    private static final Object mSync = new Object();


    private MemoryCacheManager(){

    }

    public static MemoryCacheManager getInstance() {

        synchronized (mSync){

            if(mInstance == null) {

                mInstance = new MemoryCacheManager();

                mSync.notifyAll();

            }

        }

        return  mInstance;
    }



    public Bitmap loadBitmap(@NonNull String url) {

        String key = MappingManager.generateMemoryKeyFromUrl(url);

        return null;
    }

    public void saveBitmap(String url, Bitmap bitmap) {

        String key = MappingManager.generateMemoryKeyFromUrl(url);

        //load bitmap into memory with key access

    }

}
