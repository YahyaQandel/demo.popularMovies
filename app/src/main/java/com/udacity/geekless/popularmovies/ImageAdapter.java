package com.udacity.geekless.popularmovies;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private FragmentManager frgmntManager ;
    private static LayoutInflater inflater = null;
    final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    ArrayList<Movie> allMoviesArray = null;
    String [] moviesPostersArray ;
    // Constructor
    public ImageAdapter(Context c,FragmentManager f,ArrayList<Movie> moviesArray){
        mContext = c;
        frgmntManager = f;
        allMoviesArray = moviesArray;
        inflater = ( LayoutInflater )c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return allMoviesArray.get(position);
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
            Picasso.with(mContext).load(allMoviesArray.get(position).getPoster()).into(imageView);
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

//
                    Bundle detailedMovieFragment = new Bundle();
                    detailedMovieFragment.putString("id", String.valueOf(allMoviesArray.get(position).getID()));
                    detailedMovieFragment.putString("title",allMoviesArray.get(position).getTitle());
                    detailedMovieFragment.putString("backdrop_path",allMoviesArray.get(position).getBackpath());
                    detailedMovieFragment.putString("overview",allMoviesArray.get(position).getOverview());
                    detailedMovieFragment.putString("release_date", allMoviesArray.get(position).getReleasedate());
                    detailedMovieFragment.putString("vote_average", allMoviesArray.get(position).getRate());
                    detailedMovieFragment.putString("poster_path",allMoviesArray.get(position).getPoster());

                    MovieDetailsFragment mvDetailsFrgment = new MovieDetailsFragment();
                    mvDetailsFrgment.setArguments(detailedMovieFragment);
                    FragmentManager fragmentManager = frgmntManager;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.container, mvDetailsFrgment)
                            .commit();
//                    mContext.startActivity(detailedMovieFragment);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.showToast(mContext,"LOOOOOOOOOOOOOONG press");
                return true;
            } });
        return convertView;
    }

    @Override
    public int getCount() {
        return allMoviesArray.size();
    }

}
