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

    private static int pickRandomBackground() {
        return Color.GRAY;
    }

    public static void drawTextOnImageView(Context context, ImageView imageView, String msg) {
        int bgColor = pickRandomBackground();



        int width = imageView.getMeasuredWidth();
        int height = imageView.getMeasuredHeight();


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

    public static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
