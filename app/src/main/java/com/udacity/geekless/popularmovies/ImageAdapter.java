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
    int [] FixedThunbs ;
    // Constructor
    public ImageAdapter(Context c,String [] ImagesIds){
        mContext = c;
        mThumbs = ImagesIds;
        FixedThunbs[0]=R.drawable.iskara;
        FixedThunbs[1]=R.drawable.iskara;
        FixedThunbs[2]=R.drawable.iskara;
        FixedThunbs[3]=R.drawable.iskara;
        FixedThunbs[4]=R.drawable.iskara;
        FixedThunbs[5]=R.drawable.iskara;
        FixedThunbs[6]=R.drawable.iskara;
        FixedThunbs[7]=R.drawable.iskara;
        FixedThunbs[8]=R.drawable.iskara;
        FixedThunbs[9]=R.drawable.iskara;

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
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
//        imageView.setImageResource(mThumbIds[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(mThumbs[position]).centerCrop().into(imageView);
        Toast.makeText(mContext,"Test picasso",Toast.LENGTH_LONG).show();
//        imageView.setImageResource(FixedThunbs[position]);
        return imageView;
    }

    @Override
    public int getCount() {
        return mThumbs.length;
    }

}
