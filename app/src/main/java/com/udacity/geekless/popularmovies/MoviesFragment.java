package com.udacity.geekless.popularmovies;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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


/**
 * Created by geekless on 13/08/16.
 */
public class MoviesFragment extends Fragment {

    public MoviesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add this line in order for this fragment to handle menu events.
            setHasOptionsMenu(true);
//        Toast.makeText(getActivity(),"Test ",Toast.LENGTH_LONG).show();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
//        FetchMoviesTask weatherTask = new FetchMoviesTask();
//        weatherTask.execute();
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
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main, container, false);
//        GridView gridView = (GridView) rootView.findViewById(R.id.poster_grid_view);
        Toast.makeText(getActivity(),"Test ",Toast.LENGTH_LONG).show();
        return rootView;
    }

//
//    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
//
//        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
//
//        @Override
//        protected String[] doInBackground(String... params) {
//
////             If there's no zip code, there's nothing to look up.  Verify size of params.
////            if (params.length == 0) {
////                return null;
////            }
////            HttpURLConnection urlConnection = null;
////            BufferedReader reader = null;
////            String MoviesJson = null;
////            try {
////                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
////                final String APPID_PARAM = "api_key";
////                final String APPID_VALUE = "5e44fc66144f9fc395dbce0ede660dfe";
////
////                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
////                        .appendQueryParameter(APPID_PARAM, APPID_VALUE)
////                        .build();
////
////                URL url = new URL(builtUri.toString());
////
////                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
////                urlConnection = (HttpURLConnection) url.openConnection();
////                urlConnection.setRequestMethod("GET");
////                urlConnection.connect();
////                InputStream inputStream = urlConnection.getInputStream();
////                StringBuffer buffer = new StringBuffer();
////                if (inputStream == null) {
////                    return null;
////                }
////                reader = new BufferedReader(new InputStreamReader(inputStream));
////
////                String line;
////                while ((line = reader.readLine()) != null) {
////                    buffer.append(line + "\n");
////                }
////
////                if (buffer.length() == 0) {
////                    return null;
////                }
////                MoviesJson = buffer.toString();
////
////                Log.v(LOG_TAG, "Forecast string: " + MoviesJson);
////            } catch (IOException e) {
////                Log.e(LOG_TAG, "Error ", e);
////                return null;
////            } finally {
////                if (urlConnection != null) {
////                    urlConnection.disconnect();
////                }
////                if (reader != null) {
////                    try {
////                        reader.close();
////                    } catch (final IOException e) {
////                        Log.e(LOG_TAG, "Error closing stream", e);
////                    }
////                }
////            }
////
//             String[] mThumbs  = {
//                    "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
//                    "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
//                    "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
//                    "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
//                    "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
//                    "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
//                    "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
//                    "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
//                    "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg"
//            };
//            return mThumbs;
//
//        }
//        @Override
//        protected void onPostExecute(String[] result) {
//
//            Toast.makeText(getActivity(),"Test ",Toast.LENGTH_LONG).show();
//
////            try {
////                GridView gridView = (GridView) rootView.findViewById(R.id.poster_grid_view);
////                gridView.setAdapter(new ImageAdapter(getActivity(),result));
////            }catch (Exception ex){
////                ex.printStackTrace();
////            }
//        }
//    }

}
