package com.chienpm.zimagesample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimage.network_layer.DownloadTaskCallback;
import com.chienpm.zimage.network_layer.NetworkManager;
import com.chienpm.zimage.utils.MyUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Context mContex;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContex = getApplicationContext();

        mImageView = findViewById(R.id.imageView);

        Log.i(TAG, "onCreate: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);

    }

    public void buttonClick(View view) {
        Log.i(TAG, "onClick: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);
//        ImageUtils.drawTextOnImageView(this, mImageView, "Loading...");
//        ImageUtils.inflateDrawableOverImageView(this, mImageView, R.drawable.ic_launcher_background);
//        Zimage
//                .getInstance()
//                .with(this)
//                .from("http://fb.com/12 3")
//                .addListener(new Zimage.ZimageCallback() {
//                    @Override
//                    public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {
//                        Toast.makeText(getApplicationContext(), "Succeed: ", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(@Nullable ImageView imageView, @NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public boolean getResult() {
//                        return false;
//                    }
//                })
//                .into(mImageView);

        String mUrl = "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo-1024x576.jpg";

        try {
            NetworkManager.downloadFileFromURL(mContex, mUrl, new DownloadTaskCallback() {
                @Override
                public void onDownloadCompleted(@NonNull final File targetFile) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyUtils.showToast(mContex, "Download completed"+targetFile.getAbsolutePath(), Toast.LENGTH_LONG);
                        }
                    });

                    Log.i(TAG, "onDownloadCompleted: "+targetFile.getAbsolutePath());
                }

                @Override
                public void onError(@NonNull final Exception err) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyUtils.showToast(mContex, "Download err:"+err.getMessage(), Toast.LENGTH_LONG);
                        }
                    });

                    Log.e(TAG, "onError: ",err);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
