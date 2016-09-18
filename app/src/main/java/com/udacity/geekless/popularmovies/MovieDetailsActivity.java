package com.udacity.geekless.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class MovieDetailsActivity extends AppCompatActivity {

    List<Review> arrayOfReviews;
    ListView listView;
    Activity mContext ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_movie_details);

        String movieID = getIntent().getExtras().getString("id");
        String movieTitle = getIntent().getExtras().getString("title");
        String movieBackDropPath = getIntent().getExtras().getString("backdrop_path");
        String movieOverview = getIntent().getExtras().getString("overview");
        String movieReleaseYear = getReleaseYearFromFullDate(getIntent().getExtras().getString("release_date"));
        String movieRate = getIntent().getExtras().getString("vote_average");

        ImageView movie_backdrop_path_imgview = (ImageView) findViewById(R.id.movie_backdrop_path_imgview);
        TextView movie_title_txtview = (TextView) findViewById(R.id.movie_title_txtview);
        TextView movie_overview_txtview = (TextView) findViewById(R.id.movie_overview_txtview);
        TextView movie_release_year_txtview = (TextView) findViewById(R.id.movie_release_year_txtview);
        TextView movie_rate_txtview = (TextView) findViewById(R.id.movie_rate_txtview);
        listView = (ListView) findViewById(R.id.reviews_listview);

        try {
            if (Utils.isNetworkAvailable(this)) {
                Picasso.with(this).load(movieBackDropPath).into(movie_backdrop_path_imgview);
                new FetchMovieReview().execute(movieID);
            } else {
                Utils.showToast(this, "No Network Connection!!!");
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


    }

    class FetchMovieReview extends AsyncTask<String, Void, List<Review>> {
        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MovieDetailsActivity.this);
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
