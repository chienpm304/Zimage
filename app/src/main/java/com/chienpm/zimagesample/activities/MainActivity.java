package com.chienpm.zimagesample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimagesample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickSimple(View view) {
        startActivity(new Intent(this, DemoSingleImageActivity.class));
    }

    public void clickListview(View view) {
        startActivity(new Intent(this, DemoListViewActivity.class));
    }

    public void clickGridview(View view) {
        startActivity(new Intent(this, DemoGridviewActivity.class));
    }
}
