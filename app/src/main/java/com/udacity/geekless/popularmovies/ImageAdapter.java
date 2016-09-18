package com.udacity.geekless.popularmovies;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater = null;
    final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    JSONArray allMoviesArray = null;
    String [] moviesPostersArray ;
    // Constructor
    public ImageAdapter(Context c,JSONArray moviesArray){
        mContext = c;
        allMoviesArray = moviesArray;
        ArrayList<String> moviesPostersList  = new ArrayList<String>();
//      fetching json data here in imageAdapater is important cuz we need to send Intent variables with movie attributes.
        try {

            for (int i = 0; i < allMoviesArray.length(); i++) {
                JSONObject oneMovieObject = allMoviesArray.getJSONObject(i);
                moviesPostersList.add(MOVIES_POSTER_BASE_URL + oneMovieObject.getString("poster_path"));
            }
             moviesPostersArray = new String[moviesPostersList.size()];
            moviesPostersArray = moviesPostersList.toArray(moviesPostersArray);
        }catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        inflater = ( LayoutInflater )c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return moviesPostersArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.movie_trailer_image, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        try{
            Picasso.with(mContext).load(moviesPostersArray[position]).into(imageView);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    String output =  allMoviesArray.getJSONObject(position).getString("title");
//                    Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
                    Intent detailedMovieFragment = new Intent(mContext, MovieDetailsActivity.class);
                    detailedMovieFragment.putExtra("id", allMoviesArray.getJSONObject(position).getString("id"));
                    detailedMovieFragment.putExtra("title", allMoviesArray.getJSONObject(position).getString("title"));
                    detailedMovieFragment.putExtra("backdrop_path", MOVIES_POSTER_BASE_URL+allMoviesArray.getJSONObject(position).getString("backdrop_path"));
                    detailedMovieFragment.putExtra("overview", allMoviesArray.getJSONObject(position).getString("overview"));
                    detailedMovieFragment.putExtra("release_date", allMoviesArray.getJSONObject(position).getString("release_date"));
                    detailedMovieFragment.putExtra("vote_average", allMoviesArray.getJSONObject(position).getString("vote_average"));

                    mContext.startActivity(detailedMovieFragment);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return moviesPostersArray.length;
    }

}
