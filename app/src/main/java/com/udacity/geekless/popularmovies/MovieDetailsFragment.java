package com.udacity.geekless.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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

public class MovieDetailsFragment extends Fragment {

    FragmentTransaction fragmentTransaction ;
    List<Review> arrayOfReviews;
    List<Trailer> arrayofTrailers;
    ListView review_listView , trailers_listview;
    Activity mContext ;
    View rootView ;
    ShareActionProvider mShareActionProvider;
    // movie details
    Movie currentMovie ,already_inserted;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
//
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


    }
    private Intent createShareMovieIntent() {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    String movieShareString = currentMovie.getTitle() + " \n#popularMovies\n";
                    movieShareString+="https://www.youtube.com/embed/"+arrayofTrailers.get(0).getKey();
                    try {
                        shareIntent.putExtra(Intent.EXTRA_TEXT,movieShareString);
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    return shareIntent;
                }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_movie_details, container, false);
        String movieID =getArguments().getString("id");
        String movieTitle = getArguments().getString("title");
        String movieOverview = getArguments().getString("overview");
        String movieReleaseYear = getReleaseYearFromFullDate(getArguments().getString("release_date"));
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
        review_listView = (ListView) rootView.findViewById(R.id.reviews_listview);
        trailers_listview = (ListView) rootView.findViewById(R.id.trailers_listview);
        try {
            if (Utils.isNetworkAvailable(getActivity())) {
                Picasso.with(getActivity()).load(movieBackDropPath).into(movie_backdrop_path_imgview);
                new FetchMovieReview().execute(movieID);
                new FetchMovieTrailer().execute(movieID);
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

        review_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Review oneReview = arrayOfReviews.get(position);
                Uri adress= Uri.parse(oneReview.getLink());
                Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                startActivity(browser);
            }

        });
        trailers_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Trailer oneReview = arrayofTrailers.get(position);
                Uri adress= Uri.parse("https://www.youtube.com/embed/"+oneReview.getKey());
                Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                startActivity(browser);
            }

        });
        // scroll views
        ScrollView parentScroll = (ScrollView) rootView.findViewById(R.id.parent_scrollview);
        parentScroll.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                rootView.findViewById(R.id.reviews_listview).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        review_listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        trailers_listview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        movie_overview_txtview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return rootView;
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
                  TextView reviewTXT = (TextView) rootView.findViewById(R.id.movie_reviews_separator);
                  reviewTXT.setVisibility(View.GONE);
            } else {

                ReviewAdapter objAdapter = new ReviewAdapter(mContext,
                R.layout.review_item, result);
                review_listView.setAdapter(objAdapter);

            }

        }
    }
    class FetchMovieTrailer extends AsyncTask<String, Void, List<Trailer>> {
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
        protected List<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieTrailers = null;
            try {
                String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                final String MOVIE_ID_PARAM = params[0];
                final String MOVIES_API_KEY="5e44fc66144f9fc395dbce0ede660dfe";
                MOVIES_BASE_URL +=MOVIE_ID_PARAM +"/videos?";

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
                movieTrailers = buffer.toString();
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
            List<Trailer> temp ;
            try {
                temp =  getMovieArrayTrailersFromJson(movieTrailers);
                return  temp;
            }
            catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }


        }

        private List<Trailer> getMovieArrayTrailersFromJson(String results)
        {
            List<Trailer> moviesTrailersList  = new ArrayList<Trailer>();
            JSONArray moviesArray = null;
            JSONObject moviesJson = null;
            try {
                final String MOVIES_PAGE_RESULTS = "results";
                moviesJson = new JSONObject(results);
                moviesArray = moviesJson.getJSONArray(MOVIES_PAGE_RESULTS);
                for (int i=0;i<moviesArray.length();i++) {
                    Trailer trailerObj = new Trailer();
                    trailerObj.setId(moviesArray.getJSONObject(i).getString("id"));
                    trailerObj.setKey(moviesArray.getJSONObject(i).getString("key"));
                    trailerObj.setName(moviesArray.getJSONObject(i).getString("name"));
                    trailerObj.setSize(moviesArray.getJSONObject(i).getString("size"));
                    trailerObj.setSite(moviesArray.getJSONObject(i).getString("site"));
                    moviesTrailersList.add(trailerObj);
                }
            }catch (JSONException ex)
            {
                ex.printStackTrace();
                return null;
            }
            return moviesTrailersList;
        }

        @Override
        protected void onPostExecute(List<Trailer> result) {
//            super.onPostExecute();
            arrayofTrailers = result;
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.size() == 0) {
                Utils.showToast(mContext,"There are no trailers for this movie !!!");
                TextView trailerTXT = (TextView) rootView.findViewById(R.id.movie_trailer_separator);
                trailerTXT.setVisibility(View.GONE);
            } else {

                TrailerAdapter objAdapter = new TrailerAdapter(mContext,
                        R.layout.trailer__item, result);
                try {
                    trailers_listview.setAdapter(objAdapter);
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            } else {
                Utils.showToast(getActivity(),"Share Action Provider is null?");
            }

        }
    }


    private String getReleaseYearFromFullDate(String fullDate) {
        return  fullDate.split("-")[0];
    }
}
