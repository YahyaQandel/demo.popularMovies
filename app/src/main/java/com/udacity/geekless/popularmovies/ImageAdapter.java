package com.udacity.geekless.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    String ImageName = "iskara.jpg";

    String [] mThumbs ;

    // Constructor
    public ImageAdapter(Context c,String [] ImagesIds){
        mContext = c;
        mThumbs = ImagesIds;
    }

    @Override
    public int getCount() {
        return mThumbs.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbs[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(mThumbs[position]).centerCrop().into(imageView);
        Toast.makeText(mContext,"Test picasso",Toast.LENGTH_LONG).show();
        return imageView;
    }

}
