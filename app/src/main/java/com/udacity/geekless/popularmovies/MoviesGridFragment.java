package com.udacity.geekless.popularmovies;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesGridFragment extends Fragment {

    View rootView ;
    GridView gridView;

    public MoviesGridFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.movie_poster_grid_layout, container, false);
        gridView  = (GridView) rootView.findViewById(R.id.poster_grid_view);
        FetchMoviesTask weatherTask = new FetchMoviesTask();
        weatherTask.execute();
        return rootView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
//            FetchMoviesTask weatherTask = new FetchMoviesTask();
//            weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String popMoviesRequest = null;
            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular?";
                final String API_KEY_PARAM = "api_key";
                final String MOVIES_API_KEY="5e44fc66144f9fc395dbce0ede660dfe";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, MOVIES_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popMoviesRequest = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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

            try {
//                Toast.makeText(getActivity(),popMoviesRequest,Toast.LENGTH_LONG).show();
                ArrayList<String> moviesPostersList =  getMoviesArrayObjectsFromJson(popMoviesRequest);
                String[] moviesPostersArray = new String[moviesPostersList.size()];
                moviesPostersArray = moviesPostersList.toArray(moviesPostersArray);
                return moviesPostersArray;
            }
            catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;


        }

        private ArrayList<String> getMoviesArrayObjectsFromJson(String results)
        {
            ArrayList<String> moviesPosters = new ArrayList<String>();
            JSONArray moviesArray = null;
            JSONObject moviesJson = null;
            try {
                final String MOVIES_PAGE_RESULTS = "results";
                final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
                 moviesJson = new JSONObject(results);
                 moviesArray = moviesJson.getJSONArray(MOVIES_PAGE_RESULTS);
                for(int i=0;i<moviesArray.length();i++)
                {
                    JSONObject oneMovieObject = moviesArray.getJSONObject(i);
                    moviesPosters.add(MOVIES_POSTER_BASE_URL+oneMovieObject.getString("poster_path"));
                }
            }catch (JSONException ex)
            {
                ex.printStackTrace();
                return null;
            }
//            return moviesArray;
            return moviesPosters;
        }
        @Override
        protected void onPostExecute(String[] result) {
            try {
                ImageAdapter imgAd = new ImageAdapter(getActivity(),result);
//                int count = imgAd.getCount();
                gridView.setAdapter(imgAd);
            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_LONG);
            }
        }
    }

}