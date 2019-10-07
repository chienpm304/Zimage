package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.File;

/**
 *  @DownloadTaskCallback interface which is used to return results from DownloadTask into Mainthread (for which component implement this interface)
 *  Expected: on of 3 methods in this interface will be invoked
 */
public interface DownloadTaskCallback {

    // Return the bitmap which decoded from URL's stream
    void onDecodedBitmap(@NonNull Bitmap bitmap);

    // The File where image was downloaded from url
    void onDownloadedImage(@NonNull File outputFile);

    // Error
    void onError(@NonNull Exception err);

}
