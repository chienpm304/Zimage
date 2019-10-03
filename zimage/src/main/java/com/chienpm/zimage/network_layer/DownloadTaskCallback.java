package com.chienpm.zimage.network_layer;

import androidx.annotation.NonNull;

import java.io.File;

public interface DownloadTaskCallback {
    void onDownloadCompleted(@NonNull File targetFile);
    void onError(@NonNull Exception err);
}
