package com.chienpm.zimage.disk_layer;

import java.io.File;

public interface DiskSaveBitmapCallback {

    void onSucceed(File file);

    void onFailed(Exception err);

}
