package com.chienpm.zimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimage.network_layer.NetworkUtils;
import com.chienpm.zimage.utils.MsgDef;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NetworkLayerTest {

    private Context mContext = getContext();
    private String mUrl = "www.fb.com/1234";


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
//            bitmap = NetworkManager.downloadFileFromURL(mContext, mUrl);


        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(bitmap);
    }
}
