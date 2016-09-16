package com.udacity.geekless.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater = null;
    // Keep all Images in array
    String ImageName = "iskara.jpg";

    String [] mThumbs ;
    int [] FixedThunbs ;
    // Constructor
    public ImageAdapter(Context c,String [] ImagesIds){
        mContext = c;
        mThumbs = ImagesIds;
        inflater = ( LayoutInflater )c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return mThumbs[position];
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.movie_trailer_image, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        try{

        Picasso.with(mContext).load(mThumbs[position]).into(imageView);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mThumbs.length;
    }

}
