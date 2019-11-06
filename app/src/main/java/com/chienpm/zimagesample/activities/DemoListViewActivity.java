package com.chienpm.zimagesample.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.chienpm.zimagesample.R;
import com.chienpm.zimagesample.adapters.ListViewAdapter;
import com.chienpm.zimagesample.viewholders.ListViewSubjectData;

import java.util.ArrayList;


public class DemoListViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        final ListView list = findViewById(R.id.list);

        ArrayList<ListViewSubjectData> arrayList = new ArrayList<ListViewSubjectData>();
        arrayList.add(new ListViewSubjectData("JAVA", "https://www.tutorialspoint.com/java/", "https://www.tutorialspoint.com/java/images/java-mini-logo.jpg"));
        arrayList.add(new ListViewSubjectData("Python", "https://www.tutorialspoint.com/python/", "https://www.tutorialspoint.com/python/images/python-mini.jpg"));
        arrayList.add(new ListViewSubjectData("Android", "https://www.tutorialspoint.com/android/", "https://www.tutorialspoint.com/android/images/android-mini-logo.jpg"));
        arrayList.add(new ListViewSubjectData("Javascript", "https://www.tutorialspoint.com/javascript/", "https://www.tutorialspoint.com/javascript/images/javascript-mini-logo.jpg"));
        arrayList.add(new ListViewSubjectData("Cprogramming", "https://www.tutorialspoint.com/cprogramming/", "https://www.tutorialspoint.com/cprogramming/images/c-mini-logo.jpg"));
        arrayList.add(new ListViewSubjectData("Cplusplus", "https://www.tutorialspoint.com/cplusplus/", "https://www.tutorialspoint.com/cplusplus/images/cpp-mini-logo.jpg"));

        ListViewAdapter customAdapter = new ListViewAdapter(this, arrayList);

        list.setAdapter(customAdapter);
    }
}
