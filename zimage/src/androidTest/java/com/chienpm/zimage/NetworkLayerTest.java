package com.chienpm.zimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.chienpm.zimage.controller.ZimageEngine;
import com.chienpm.zimage.controller.ZimageCallback;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimage.network_layer.DownloadCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.network_layer.NetworkUtils;
import com.chienpm.zimage.exception.ErrorCode;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NetworkLayerTest {

    private static final String TAG = NetworkLayerTest.class.getSimpleName();
    private Context mContext = getContext();
    public static String mUrl = "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo-1024x576.jpg";


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

        ZimageCallback callback = new ZimageCallback() {
            @Override
            public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {

            }

            @Override
            public void onFailed(@Nullable ImageView imageView, String url, @NonNull ZimageException e) {
                assertEquals(ErrorCode.ERR_NO_INTERNET_CONNECTION, e.getMessage());
            }

        };

        ZimageEngine.getInstance().with(mContext).from(mUrl).addListener(callback).into(mImageView);
//        assertTrue(true);
    }



    @Test
    public void testDownloadImageFromURL(){
        String mUrl = "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo-1024x576.jpg";
        String mUrl2 = "https://lh3.googleusercontent.com/t0QbD0qO1PTeBhyDlJRZHySdTb8pjBRolWpy6h6mdPEDTTaSrSPxF_q7HPoZTmyuiJJXlvVhFbUt1ZYNNEvAHInuqasSWyO8mH-yQg9n4ji3bpTQ42QCyXWZ9HOUfqB8VqZHCsBFUtc2hB8HF343I2Ljlu7gLQiAFE-bPUXSoiLz9W4Ws1DNQXZfWGWryYp_vc5HdtXH3nkblkMwzwjhcZByE6h1Nego0z_OnVnkewAfJEL9-GrEgZcHhbW6vvlc988q-BPSkd40Tm4tTiRMuHp-iDYf55J7DLyA7n49dZ4QXbkPsBukzuy8FWaIlJwAJ5bOBucQWOPVOA7npSP7O9eKnaPQ4XHOSFukC3Xu3iXhUe1q0NeWr2GRpYG0_8LUaTIvj9bKpaC943JNzrit5RtgDB35Sk9rnDWXrKUi8r7NR1QbN63ReEPurrGbDqXSZVkFscTnXd3oLWQ_6Ur-ZY8B22h_Rili_EBLBJUVcRa_DwYhZxrUR7z-hyNFl_6eQD1rgp39moJlluGVLyrcmaLPAe-v_r7t0_4ukR912g3JL0YLIJn_JRDDgHE3BaVtPbCrOGCE1847mLbmtZ1QnBiysElwgdYT8A-o5EwNmv_Rb0D8pCaKGN-VASyenqaA_XnC2WI8sjy4sy6jfLigzc8BfHewHp8gM2sjQxeKd9OFVeR8Q17-XwU=w1200-h901-no";
        String mURLGPhoto = "https://photos.google.com/share/AF1QipPV_CHWctkrXTjHoI7nMri_GvXwewyLlKcvSufSJVCTha13WN1ckUXxcj9BbBq4Uw?key=VnNjRTkyaE1mR0NKTWoyUXJWX29SYTFWeDJiQ29B";
        String url3 = "https://www.upsieutoc.com/images/2019/10/04/Zimage-Architecture.png";
        String url4 = "data:image/png;base64,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";

        try {
            NetworkManager.getInstance().
                    downloadFileFromURL(mContext, url3, new DownloadCallback() {
                @Override
                public void onFailed(@NonNull final ZimageException err) {
                    Log.e(TAG, "onFailed: ",err);
                    System.out.println("onFailed: "+err.getMessage());
                }

                @Override
                public void onSucceed(@NonNull final Bitmap bitmap) {
                    Log.i(TAG, "onSucceed");
                    System.out.println("onSucceed from stream - data: "+bitmap.getByteCount() +" bytes");
                }


            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
