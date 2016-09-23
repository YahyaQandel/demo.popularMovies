package com.udacity.geekless.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener {

    FragmentTransaction fragmentTransaction ;
    List<Review> arrayOfReviews;
    ListView listView;
    Activity mContext ;
    View rootView ;
    // movie details
    Movie currentMovie ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

//        fragmentTransaction  = getActivity().getSupportFragmentManager().beginTransaction();

//        getView().setOnKeyListener( new View.OnKeyListener(){
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event ){
//                if( keyCode == KeyEvent.KEYCODE_BACK ){
//                    if(getFragmentManager().getBackStackEntryCount() != 0) {
//                        getFragmentManager().popBackStack();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        } );
//    }

//    @Override
//    public void onBackPressed() {
//
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                if(getFragmentManager().getBackStackEntryCount() != 0) {
//                    getFragmentManager().popBackStack();
//                    return true;
//                }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_movie_details, container, false);
        String movieID =getArguments().getString("id");
        String movieTitle = getArguments().getString("title");
        String movieOverview = getArguments().getString("overview");
        String movieReleaseYear = getArguments().getString("release_date");
        String movieRate = getArguments().getString("vote_average");
        String movieBackDropPath =getArguments().getString("backdrop_path");
        String moviePoster =getArguments().getString("poster_path");

        currentMovie = new Movie();
        currentMovie.setID(Integer.parseInt(movieID));
        currentMovie.setTitle(movieTitle);
        currentMovie.setOverview(movieOverview);
        currentMovie.setReleaseDate(movieReleaseYear);
        currentMovie.setRate(movieRate);
        currentMovie.setPoster(moviePoster);
        currentMovie.setBackpath(movieBackDropPath);

        ImageView movie_backdrop_path_imgview = (ImageView) rootView.findViewById(R.id.movie_backdrop_path_imgview);
        TextView movie_title_txtview = (TextView) rootView.findViewById(R.id.movie_title_txtview);
        TextView movie_overview_txtview = (TextView) rootView.findViewById(R.id.movie_overview_txtview);
        TextView movie_release_year_txtview = (TextView) rootView.findViewById(R.id.movie_release_year_txtview);
        TextView movie_rate_txtview = (TextView) rootView.findViewById(R.id.movie_rate_txtview);
        listView = (ListView) rootView.findViewById(R.id.reviews_listview);
        ImageView addToFavouriteImg = (ImageView) rootView.findViewById(R.id.addToFav_imgview);
        addToFavouriteImg.setOnClickListener(this);

        try {
            if (Utils.isNetworkAvailable(getActivity())) {
                Picasso.with(getActivity()).load(movieBackDropPath).into(movie_backdrop_path_imgview);
                new FetchMovieReview().execute(movieID);
            } else {
                Utils.showToast(getActivity(), "No Network Connection!!!");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        movie_title_txtview.setText(movieTitle);
        movie_overview_txtview.setText(movieOverview);
        movie_release_year_txtview.setText(movieReleaseYear);
        movie_rate_txtview.setText(movieRate+"/10");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Review oneReview = arrayOfReviews.get(position);
                Uri adress= Uri.parse(oneReview.getLink());
                Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                startActivity(browser);
            }

        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        try {
            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
            Movie already_inserted = db.getMovie(currentMovie.getID());
            if(already_inserted!=null) {
                Utils.showToast(getActivity(), "This movie already in your favourite list");
            }
            else {
                db.addMovie(currentMovie);
                Utils.showToast(getActivity(), "Movie added to favourite !!!");
            }
//            List<Movie> movies = db.getAllMovies();
//            for (Movie mv : movies) {
//                String toast_string = "Id: " + mv.getID() + " ,Title: " + mv.getTitle();
//                Utils.showToast(getActivity(), toast_string);
//            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
            // display movies in a toast

    }

    class FetchMovieReview extends AsyncTask<String, Void, List<Review>> {
        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Loading...");
            pDialog.show();

        }

        @Override
        protected List<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieReviews = null;
            try {
                String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                final String MOVIE_ID_PARAM = params[0];
                final String MOVIES_API_KEY="5e44fc66144f9fc395dbce0ede660dfe";
                MOVIES_BASE_URL +=MOVIE_ID_PARAM +"/reviews?";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, MOVIES_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieReviews = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            List<Review> temp ;
            try {
                temp =  getMovieArrayReviewsFromJson(movieReviews);
                return  temp;
            }
            catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }


        }

        private List<Review> getMovieArrayReviewsFromJson(String results)
        {
            List<Review> moviesReviesList  = new ArrayList<Review>();
            JSONArray moviesArray = null;
            JSONObject moviesJson = null;
            try {
                final String MOVIES_PAGE_RESULTS = "results";
                moviesJson = new JSONObject(results);
                moviesArray = moviesJson.getJSONArray(MOVIES_PAGE_RESULTS);
                for (int i=0;i<moviesArray.length();i++) {
                    Review reviewObj = new Review();
                    reviewObj.setId(moviesArray.getJSONObject(i).getString("id"));
                    reviewObj.setDesc(moviesArray.getJSONObject(i).getString("content"));
                    reviewObj.setLink(moviesArray.getJSONObject(i).getString("url"));
                    reviewObj.setTitle(moviesArray.getJSONObject(i).getString("author"));
                    moviesReviesList.add(reviewObj);
                }
            }catch (JSONException ex)
            {
                ex.printStackTrace();
                return null;
            }
            return moviesReviesList;
        }

        @Override
        protected void onPostExecute(List<Review> result) {
//            super.onPostExecute();
            arrayOfReviews = result;
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.size() == 0) {
                  Utils.showToast(mContext,"There are no reviews for this movie !!!");
            } else {

                ReviewAdapter objAdapter = new ReviewAdapter(mContext,
                R.layout.review_item, result);
                listView.setAdapter(objAdapter);

            }

        }
    }



    private String getReleaseYearFromFullDate(String fullDate) {
        return  fullDate.split("-")[0];
    }
}
