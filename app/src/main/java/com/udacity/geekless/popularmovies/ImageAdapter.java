package com.udacity.geekless.popularmovies;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
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
    enum MOVIE_IN_FAVOURITE_LIST {YES,NO};
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
                String MSG_ADD = "Add this movie to your favourites ?! ";
                String MSG_REMV = "Delete this movie from your favourites ?! ";
                String Dialgo_msg ;int choice = 0;
                final DatabaseHandler db = new DatabaseHandler(mContext.getApplicationContext());
                final Movie already_inserted = db.getMovie(allMoviesArray.get(position).getID());
                if(already_inserted!=null) {
                   Dialgo_msg = MSG_REMV;
                }
                else {
//                    db.addMovie(currentMovie);
//                    Utils.showToast(getActivity(), "Movie added to favourite !!!");
                    Dialgo_msg = MSG_ADD;
                }
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(already_inserted!=null) {
                                    db.deleteContact(allMoviesArray.get(position));
                                    Utils.showToast(mContext, "Movie deleted !!!");

                                }else{
                                    db.addMovie(allMoviesArray.get(position));
                                    Utils.showToast(mContext, "Movie added  !!!");
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
                builder.setTitle(Html.fromHtml("<font color='#FFFFFF'><u>Popular Movies<u></font>"));
                builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>"+Dialgo_msg+"</font>")).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
         }});
        return convertView;
    }

    @Override
    public int getCount() {
        return allMoviesArray.size();
    }

}
