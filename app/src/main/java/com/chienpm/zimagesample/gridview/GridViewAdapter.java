package com.chienpm.zimagesample.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chienpm.zimage.controller.Zimage;
import com.chienpm.zimagesample.R;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final String[] gridViewImageUrl;

    public GridViewAdapter(Context context, String[] gridViewString, String[] gridViewImageId) {
        mContext = context;
        this.gridViewImageUrl = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.grid_item, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textViewAndroid.setText(gridViewString[i]);

//            imageViewAndroid.setImageResource(gridViewImageUrl[i]);

            Zimage.getInstance().with(mContext).from(gridViewImageUrl[i]).into(imageViewAndroid);

        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
