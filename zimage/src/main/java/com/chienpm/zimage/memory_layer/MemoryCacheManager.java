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


	/* The LurCache for caching Bitmap images */
    private static LruCache<String, Bitmap> mBitmapCache;


	/* THe exclusively instance of MEmoryCachemanger*/
    private static MemoryCacheManager mInstance = null;


	/* Synchonize objecet */
    private static final Object mSync = new Object();


	/**
	 * Hidden constructor adaptive to Singleton pattern
	 * Determine the cache size of LruCache
	 */
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



	/**
	 * @return mInstance
	 */
    public static MemoryCacheManager getInstance() {

        synchronized (mSync){

            if(mInstance == null) {

                mInstance = new MemoryCacheManager();

                mSync.notifyAll();

            }

        }

        return  mInstance;
		
    }



	/**
	 * Loading bitmap instance mapped by url, width and height
	 * @return android.graphics.Bitmap or Null if not existed in Cache
	 */
    public Bitmap loadBitmap(@NonNull String url, int width, int height) {

        String key = MappingManager.getKeyFromUrl(url, width, height);

        return mBitmapCache.get(key);

    }


	
	/**
	 * Save bitmap instance on LruCache
	 */
    public void saveBitmap(String url, Bitmap bitmap, int width, int height) {

        if(loadBitmap(url, width, height) == null){

            String key = MappingManager.getKeyFromUrl(url, width, height);

            mBitmapCache.put(key, bitmap);

        }

    }

}
