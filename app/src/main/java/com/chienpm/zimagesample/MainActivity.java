package com.chienpm.zimagesample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimagesample.gridview.DemoGridviewActivity;
import com.chienpm.zimagesample.listview.DemoListViewActivity;
import com.chienpm.zimagesample.singleimage.DemoSingleImageActivity;

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
