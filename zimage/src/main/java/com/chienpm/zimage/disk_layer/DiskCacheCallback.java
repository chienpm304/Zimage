package com.chienpm.zimage.disk_layer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chienpm.zimage.exception.ZimageException;

import java.io.File;

public interface DiskCacheCallback {

    void onSucceed(@Nullable Bitmap bitmap, @NonNull File output_file);

    void onFailed(@NonNull ZimageException err);

}
