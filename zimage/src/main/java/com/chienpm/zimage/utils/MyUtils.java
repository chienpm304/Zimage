package com.chienpm.zimage.utils;

import android.content.Context;
import android.widget.Toast;

public class MyUtils {

    public static boolean isValidUrlPattern(String url) {
        return android.util.Patterns.WEB_URL.matcher(url).matches();
    }

    public static void showToast(Context mContext, String message, int length) {
        Toast.makeText(mContext, message, length).show();
    }
}
