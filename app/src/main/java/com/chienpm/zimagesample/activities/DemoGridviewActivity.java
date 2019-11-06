package com.chienpm.zimagesample.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimagesample.R;
import com.chienpm.zimagesample.adapters.GridViewAdapter;

public class DemoGridviewActivity extends AppCompatActivity {

    GridView androidGridView;

    String[] gridViewString = {
            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",
            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",
            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",

    } ;
    String[] gridViewImageUrl = {
            "https://i.imgur.com/CK0NNEc.jpg",
            "https://i.imgur.com/HZuzDdb.png",
            "https://i.imgur.com/q3nfXaE.jpg",
            "https://i.imgur.com/VdSk7AT.jpg",
            "https://i.imgur.com/dFaomZv.jpg",
            "https://i.imgur.com/WewC2vz.jpg",
            "https://i.imgur.com/uKam60W.jpg",
            "https://i.imgur.com/KfDTuQN.jpg",
            "https://i.imgur.com/AO5xufT.jpg",
            "https://i.imgur.com/7RDgnBz.jpg",
            "https://i.imgur.com/CqSewOw.jpg",
            "https://i.imgur.com/rYmsJ61.jpg",
            "https://i.imgur.com/slicZ3I.jpg",
            "https://i.imgur.com/SuHBSTF.jpg",
            "https://i.imgur.com/gMnRWbS.jpg",
            "https://i.imgur.com/5nMFGhX.jpg",
            "https://i.imgur.com/ffwByWw.jpg",
            "https://i.imgur.com/dd04Dcy.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        GridViewAdapter adapterViewAndroid = new GridViewAdapter(DemoGridviewActivity.this, gridViewString, gridViewImageUrl);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Toast.makeText(DemoGridviewActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
            }
        });

    }
}
