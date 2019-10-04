package com.chienpm.zimage.disk_layer;

import android.os.Environment;

import java.io.File;

public class StorageUtils {
    public static boolean checkExternalStorageAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.isExternalStorageRemovable() || Environment.isExternalStorageEmulated();
        } else
            return false;
    }

    public static boolean checkOutputImageFile(File file) {
        return file.exists() && file.canRead() && file.length()> 0;
    }
}
