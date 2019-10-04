package com.chienpm.zimage.network_layer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.File;

public interface DownloadTaskCallback {

    void onError(@NonNull Exception err);

    void onDecodedBitmap(@NonNull Bitmap bitmap);

    void onDownloadedImage(@NonNull File outputFile);
}
