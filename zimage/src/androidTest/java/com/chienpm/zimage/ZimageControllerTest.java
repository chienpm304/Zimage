package com.chienpm.zimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chienpm.zimage.controller.ZimageEngine;
import com.chienpm.zimage.controller.ZimageCallback;
import com.chienpm.zimage.exception.ErrorCode;
import com.chienpm.zimage.exception.ZimageException;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ZimageControllerTest {

    public Context getContext(){
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(appContext);
    }

//    @Test
//    public void testContextName(){
//        Context appContext = getContext();
//        assertEquals("com.chienpm.zimage", appContext.getPackageName());
//    }

    @Test
    public void testZimageSingletonInstance(){
        ZimageEngine.getInstance().reset();
        assertSame(ZimageEngine.getInstance(), ZimageEngine.getInstance());
        assertSame(ZimageEngine.getInstance(), ZimageEngine.getInstance());
        assertSame(ZimageEngine.getInstance(), ZimageEngine.getInstance());
        assertSame(ZimageEngine.getInstance(), ZimageEngine.getInstance());
        assertSame(ZimageEngine.getInstance(), ZimageEngine.getInstance());
    }


    @Test
    public void testZimageBuilderContextNull(){
        ZimageEngine.getInstance().reset();

        Exception err = null;
        Context context = getContext();
        ImageView imgView = new ImageView(context);

        try {
            ZimageEngine.getInstance()
                    .from("https://1234124")
                    .into(imgView);
        } catch (Exception e) {
            err = e;
        }


        assertNotNull(err);
        assertEquals(ErrorCode.ERR_INVALID_CONTEXT, err.getMessage());

    }

    @Test
    public void testZimageBuilderContextTrue(){
        ZimageEngine.getInstance().reset();

        Context context = getContext();
        ImageView imgView = new ImageView(context);

        Exception err = null;
        try {
            ZimageEngine.getInstance()
                    .with(context)
                    .from("https://1234124.com")
                    .into(imgView);
        } catch (Exception e) {
            err  = e;
        }

        assertNull(err);
    }


    @Test
    public void testZimageBuilderWrongImageView(){
        ZimageEngine.getInstance().reset();

        Exception err = null;
        Context context = getContext();
        ImageView imgView = null;

        try {
            ZimageEngine.getInstance()
                    .with(context)
                    .from("https://abc.com/asf.jpg")
                    .into(imgView);
        } catch (Exception e) {
            err = e;
        }

        assertNotNull(err);
        assertEquals(err.getMessage(), ErrorCode.ERR_INVALID_IMAGE_VIEW);

    }

    @Test
    public void testZimageBuilderWrongURLpattern(){
        ZimageEngine.getInstance().reset();

        Exception err = null;
        Context context = getContext();
        ImageView imgView = new ImageView(context);

        try {
            ZimageEngine.getInstance()
                    .with(context)
                    .from("https://abccom")
                    .into(imgView);
        } catch (Exception e) {
            err = e;
        }

        assertNotNull(err);
        assertEquals(err.getMessage(), ErrorCode.ERR_INVALID_IMAGE_URL);

    }


    @Test
    public void testLoadNullBitmap(){
        ZimageEngine.getInstance().reset();

        Context context = getContext();
        ImageView imgView = new ImageView(context);
        Exception err = null;

        try{
            ZimageEngine.getInstance()
                    .with(context)
                    .from("http://fb.com/chienpm.jpg")
//                    .resize(100, 150)
                    .into(imgView);
        }
        catch (Exception e){
            err = e;
            e.printStackTrace();
        }

        assertNull(err);
    }

    @Test
    public void testPerfectFlow(){
        ZimageEngine.getInstance().reset();

        Context context = getContext();
        ImageView imgView = new ImageView(context);
        Exception err = null;

        ZimageCallback callback = new ZimageCallback() {
            Boolean result = null;

            @Override
            public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {
                result = Boolean.TRUE;
            }

            @Override
            public void onFailed(@Nullable ImageView imageView, String url, @NonNull ZimageException e) {

            }

        };


        try{
            ZimageEngine.getInstance()
                    .with(context)
                    .from("http://fb.com/chienpm.jpg")
//                    .resize(100, 150)
                    .addListener(callback)
                    .into(imgView);
        }
        catch (Exception e){
            err = e;
        }

    }


    @Test
    public void playWithGlide(){
        Context ctx = getContext();
        ImageView imageView = new ImageView(ctx);

        Glide
            .with(ctx)
            .load("asf").addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            })
            .into(imageView);
    }
}
