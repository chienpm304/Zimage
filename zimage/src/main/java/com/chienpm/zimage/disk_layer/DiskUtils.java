package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskUtils {

    private static final String TAG = DiskUtils.class.getSimpleName();

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

//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inBitmap;

            return BitmapFactory.decodeFile(localFile.getAbsolutePath());

        }
        catch (Exception e){
            Log.e(TAG, "loadBitmapFromFile: ",e );
            return null;

        }
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void saveBitmap(Bitmap bitmap, File file) throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
//            e.printStackTrace();
            throw e;
        }
    }
}
