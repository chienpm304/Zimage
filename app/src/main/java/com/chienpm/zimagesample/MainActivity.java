package com.chienpm.zimagesample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimage.controller.Zimage.ZimageCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Context mContext;
    ImageView mImageView;
    EditText mEdtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mEdtUrl = findViewById(R.id.edtURL);
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
            .addListener(new ZimageCallback() {
                @Override
                public void onSucceed(@NonNull ImageView imageView, @NonNull String url) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                            Toast.makeText(getApplicationContext(), "Succeed: ", Toast.LENGTH_LONG).show();
//                        }
//                    });

                }

                @Override
                public void onError(@Nullable ImageView imageView, @NonNull final Exception e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });

                }

                @Override
                public boolean getResult() {
                    return false;
                }
            })
            .into(mImageView);


    }
}
