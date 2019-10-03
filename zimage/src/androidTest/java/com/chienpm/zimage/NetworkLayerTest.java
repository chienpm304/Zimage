package com.chienpm.zimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimage.network_layer.DownloadTaskCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.network_layer.NetworkUtils;
import com.chienpm.zimage.utils.MsgDef;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class NetworkLayerTest {

    private static final String TAG = NetworkLayerTest.class.getSimpleName();
    private Context mContext = getContext();
    private String mUrl = "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo-1024x576.jpg";


    public Context getContext(){
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }


    // Must set network state on Emulator for this test
    @Test
    public void testCheckInternetConnection(){

        assertTrue(NetworkUtils.isNetworkConnected(mContext));

    }


    // Turn of internet on emulator
    @Test
    public void testNoConnectThrowException(){
        ImageView mImageView = new ImageView(mContext);

        Zimage.ZimageCallback callback = new Zimage.ZimageCallback() {
            @Override
            public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {

            }

            @Override
            public void onError(@Nullable ImageView imageView, @NonNull Exception e) {
                assertEquals(MsgDef.ERR_NO_INTERNET_CONNECTION, e.getMessage());
            }

            @Override
            public boolean getResult() {
                return false;
            }
        };

        Zimage.getInstance().with(mContext).from(mUrl).addListener(callback).into(mImageView);
//        assertTrue(true);
    }



    @Test
    public void testDownloadImageFromURL(){
        Bitmap bitmap = null;
        // Download image from network
        try {
            NetworkManager.downloadFileFromURL(mContext, mUrl, new DownloadTaskCallback() {
                @Override
                public void onDownloadCompleted(@NonNull File targetFile) {
                    assertTrue(targetFile.exists());
                    Log.i(TAG, "onDownloadCompleted: "+targetFile.getAbsolutePath());
                }

                @Override
                public void onError(Exception err) {
                    assertNotEquals(MsgDef.ERR_NO_INTERNET_CONNECTION, err.getMessage());
                    Log.e(TAG, "onError: ", err);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
