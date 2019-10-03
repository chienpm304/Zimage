package com.chienpm.zimage.network_layer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

import com.chienpm.zimage.mapping.MappingManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null &&
                cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }

    public static void writeStreamToFile(InputStream inputStream, File targetFile) throws IOException {

        try {
            FileOutputStream outStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outStream.close();


//            BitmapFactory.de
        }catch (Exception e){
            throw e;
        }
    }
}
