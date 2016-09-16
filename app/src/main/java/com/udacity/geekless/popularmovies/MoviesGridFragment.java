package com.udacity.geekless.popularmovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesGridFragment extends Fragment {

    View rootView ;
    GridView gridView;
    String FILTER_POPULAR = "popular";
    String FILTER_TOP_RATED = "top_rated";
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
        weatherTask.execute(FILTER_POPULAR);
        return rootView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_display_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_popular) {
            FetchMoviesTask weatherTask = new FetchMoviesTask();
            weatherTask.execute(FILTER_POPULAR);
            return true;
        }
        else if(id == R.id.action_toprated)
        {
            FetchMoviesTask weatherTask = new FetchMoviesTask();
            weatherTask.execute(FILTER_TOP_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, JSONArray> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected JSONArray doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String popMoviesRequest = null;
            try {
                String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                MOVIES_BASE_URL +=params[0]+"?";
                final String API_KEY_PARAM = "api_key";
                final String API_FILTER_PARAM = params[0];
                final String MOVIES_API_KEY="5e44fc66144f9fc395dbce0ede660dfe";

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
                popMoviesRequest = buffer.toString();
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

            try {
                return getMoviesArrayObjectsFromJson(popMoviesRequest);
            }
            catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;


        }

        private JSONArray getMoviesArrayObjectsFromJson(String results)
        {
            ArrayList<String> moviesPosters = new ArrayList<String>();
            JSONArray moviesArray = null;
            JSONObject moviesJson = null;
            try {
                final String MOVIES_PAGE_RESULTS = "results";
                 moviesJson = new JSONObject(results);
                 moviesArray = moviesJson.getJSONArray(MOVIES_PAGE_RESULTS);
            }catch (JSONException ex)
            {
                ex.printStackTrace();
                return null;
            }
            return moviesArray;
        }
        @Override
        protected void onPostExecute(JSONArray result) {
            try {
                ImageAdapter imgAd = new ImageAdapter(getActivity(),result);
                gridView.setAdapter(imgAd);
            }catch (Exception ex){
                ex.printStackTrace();

            }
        }
    }

}