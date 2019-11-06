package com.chienpm.zimagesample.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimage.controller.ZimageCallback;
import com.chienpm.zimage.exception.ZimageException;
import com.chienpm.zimagesample.R;

public class DemoSingleImageActivity extends AppCompatActivity {

    private static final String TAG = DemoSingleImageActivity.class.getSimpleName();

    Context mContext;
    ImageView mImageView;
    EditText mEdtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        mContext = getApplicationContext();
        mEdtUrl = findViewById(R.id.edtURL);

        mEdtUrl.setText("https://cdn.wallpaper.com/main/styles/fp_820x503/s3/2019/10/_l_goldsmith-street_5627ctim-crocker.jpg");
        mImageView = findViewById(R.id.imageView);

        Log.i(TAG, "onCreate: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);

    }

    public void buttonClick(View view) {
        Log.i(TAG, "onClick: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);

        String url = mEdtUrl.getText().toString();

        Zimage
            .getInstance()
            .with(this)
            .from(url)
//            .setLoadingResource(R.id.bottom)
//            .setErrorResource(1123)
            .addListener(new ZimageCallback() {
                @Override
                public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {
                    Log.i(TAG, "onSucceed: "+url);
                }

                @Override
                public void onFailed(@Nullable ImageView imageView, String url, @NonNull final ZimageException e) {
                    Log.e(TAG, "onFailed: ", e);
                }
            })
            .into(mImageView);


    }
}
