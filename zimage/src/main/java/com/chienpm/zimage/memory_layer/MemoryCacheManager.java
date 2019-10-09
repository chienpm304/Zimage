package com.chienpm.zimage.memory_layer;

import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.chienpm.zimage.mapping.MappingManager;

/***
 *  MemoryCacheManager is the master class of Memory Caching layer of library
 *  This class will holding a cache-memory which store bitmaps
 *  Get bitmap from key which mapping to image url view @MappingManager
 */
public class MemoryCacheManager {


    private static LruCache<String, Bitmap> mBitmapCache;

    private static MemoryCacheManager mInstance = null;

    private static final Object mSync = new Object();



    private MemoryCacheManager(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mBitmapCache = new LruCache<String, Bitmap>(cacheSize){

            @Override
          protected int sizeOf(String key, Bitmap bitmap){
              return bitmap.getByteCount()/1024;
          }

        };
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

        String key = MappingManager.getKeyFromUrl(url);

        return mBitmapCache.get(key);

    }

    public void saveBitmap(String url, Bitmap bitmap) {

        if(loadBitmap(url)==null){

            String key = MappingManager.getKeyFromUrl(url);
            mBitmapCache.put(key, bitmap);

        }

    }

}
