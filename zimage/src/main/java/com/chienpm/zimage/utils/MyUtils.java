package com.chienpm.zimage.utils;

public class MyUtils {

    public static boolean isValidUrlPattern(String url) {
        return android.util.Patterns.WEB_URL.matcher(url).matches();
    }

}
