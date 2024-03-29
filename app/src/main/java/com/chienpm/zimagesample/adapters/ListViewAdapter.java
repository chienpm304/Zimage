package com.chienpm.zimagesample.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimagesample.R;
import com.chienpm.zimagesample.viewholders.ListViewSubjectData;

import java.util.ArrayList;

public class ListViewAdapter implements ListAdapter {
    ArrayList<ListViewSubjectData> arrayList;
    Context context;
    public ListViewAdapter(Context context, ArrayList<ListViewSubjectData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListViewSubjectData subjectData=arrayList.get(position);
      if(convertView==null){
          LayoutInflater layoutInflater = LayoutInflater.from(context);
          convertView=layoutInflater.inflate(R.layout.listview_item, null);
          convertView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse(subjectData.Link));
                  context.startActivity(i);
                  //Toast.makeText(context,subjectData.Link,Toast.LENGTH_LONG).show();
              }
          });
          TextView tittle=convertView.findViewById(R.id.title);
          ImageView imag=convertView.findViewById(R.id.list_image);
          tittle.setText(subjectData.SubjectName);

          //todo: change ZimageEngine here
          Zimage.getInstance()
                  .with(context)
                  .from(subjectData.Image)
                  .into(imag);

//          Picasso.with(context)
//                  .load(subjectData.Image)
//                  .into(imag);

      }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }


    @Override
    public boolean isEmpty() {
        return false;
    }
}
