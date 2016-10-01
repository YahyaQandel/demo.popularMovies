package com.udacity.geekless.popularmovies;

import android.content.DialogInterface;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    GridItemListener gridItemListener ;
    private static LayoutInflater inflater = null;
    final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    ArrayList<Movie> allMoviesArray = null;
    enum MOVIE_IN_FAVOURITE_LIST {YES,NO};
    String [] moviesPostersArray ;
    // Constructor
    public ImageAdapter(Context c,ArrayList<Movie> moviesArray){
        mContext = c;
        gridItemListener = (GridItemListener) c;
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
                gridItemListener.onItemClicked(allMoviesArray.get(position));
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
                                    allMoviesArray = db.getAllMovies();
                                    notifyDataSetChanged();

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
        try {
            return allMoviesArray.size();
        }catch (NullPointerException nex)
        {
            Utils.showToast(mContext,"No data retrieved");
            return 0;
        }
        catch (Exception ex)
        {
            Utils.showToast(mContext,"Something wrong happened");
            return 0;
        }
    }
}
