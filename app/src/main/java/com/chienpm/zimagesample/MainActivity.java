package com.chienpm.zimagesample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimage.controller.Zimage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);

        Log.i(TAG, "onCreate: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);

    }

    public void buttonClick(View view) {
        Log.i(TAG, "onClick: "+mImageView.getLayoutParams().width+"x"+mImageView.getLayoutParams().height);
//        ImageUtils.drawTextOnImageView(this, mImageView, "Loading...");
//        ImageUtils.inflateDrawableOverImageView(this, mImageView, R.drawable.ic_launcher_background);
        Zimage
                .getInstance()
                .with(this)
                .from("http://fb.com/12 3")
                .addListener(new Zimage.ZimageCallback() {
                    @Override
                    public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {
                        Toast.makeText(getApplicationContext(), "Succeed: ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@Nullable ImageView imageView, @NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public boolean getResult() {
                        return false;
                    }
                })
                .into(mImageView);
    }
}
