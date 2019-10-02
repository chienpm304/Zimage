package com.chienpm.zimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.widget.ImageView;

public class ImageUtils {
    public static Bitmap produceBitmapWithCondition(Image downloadedImg) {
        return null;
    }

    public static int pickRandomBackground() {
        return Color.GRAY;
    }

    public static void drawTextOnImageView(Context context, ImageView imageView, String msg) {
        int bgColor = pickRandomBackground();



        int width = imageView.getLayoutParams().width;
        int height = imageView.getLayoutParams().height;


        Bitmap newImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(newImage, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        c.drawText(msg, 0, height, paint);

        imageView.setImageBitmap(newImage);
    }

    public static void inflateDrawableOverImageView(Context context, ImageView imageView, int drawable) {
        imageView.setImageResource(drawable);
    }
}
