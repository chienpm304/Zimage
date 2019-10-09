package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskUtils {

    public static boolean checkExternalStorageAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.isExternalStorageRemovable() || Environment.isExternalStorageEmulated();
        } else
            return false;
    }

    public static boolean checkFileIsExisted(File file) {
        return file.exists() && file.isFile() && file.length() > 0;
    }


    /**
     *
     * @param localFile
     * @return bitmap instance loaded from file or null if not exist
     */
    public static Bitmap loadBitmapFromFile(File localFile) {

        try{

            return BitmapFactory.decodeFile(localFile.getAbsolutePath());

        }
        catch (Exception e){

            return null;

        }
    }



    public static void saveBitmap(Bitmap bitmap, File file) throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
