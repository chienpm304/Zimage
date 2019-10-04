package com.chienpm.zimage.disk_layer;

import android.os.Environment;

public class StorageUtils {
    public static boolean checkExternalStorageAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.isExternalStorageRemovable() || Environment.isExternalStorageEmulated();
        } else
            return false;
    }

}
