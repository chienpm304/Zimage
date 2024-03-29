package com.chienpm.zimage.controller;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chienpm.zimage.exception.ZimageException;

/**
 * ZimageEngine listener is an optional while building
 * Invoke to 2 methods: @onSucceed and @onFailed
 */
public interface ZimageCallback {

    void onSucceed(@NonNull ImageView imageView, @NonNull String url);

    void onFailed(@Nullable ImageView imageView, String url, @NonNull ZimageException e);

}