package com.chienpm.zimage.network_layer;

import android.content.Context;
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

    public static File writeStreamToFile(InputStream inputStream, String outputPath) throws IOException {

//        File targetFile = new File(outputPath);
        FileOutputStream outStream = new FileOutputStream(MappingManager.getBaseDir());

        byte[] buffer = new byte[8 * 1024];
        int bytesRead=-1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outStream.close();
        return new File(outputPath);
    }
}
