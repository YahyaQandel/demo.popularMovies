package com.udacity.geekless.popularmovies;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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
import java.util.ArrayList;
import java.util.List;

public class MoviesGridFragment extends Fragment {

    View rootView ;
    GridView gridView;
    String MOVIES_FILTER_POPULAR = "popular";
    String MOVIES_FILTER_TOP_RATED = "top_rated";
    String MOVIES_FILTER_NOW_PLAYING = "now_playing";
    String MOVIES_FILTER_UPCOMING = "upcoming";
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
        weatherTask.execute(MOVIES_FILTER_POPULAR);
        return rootView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_display_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FetchMoviesTask weatherTask = new FetchMoviesTask();
        int id = item.getItemId();
        if (id == R.id.action_favourite) {
            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
            ArrayList<Movie> movies = db.getAllMovies();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            ImageAdapter imgAd = new ImageAdapter(getActivity(),fragmentManager,movies);
            gridView.setAdapter(imgAd);
        }
        if (id == R.id.action_popular) {
            weatherTask.execute(MOVIES_FILTER_POPULAR);
            return true;
        }
        else if(id == R.id.action_toprated)
        {
            weatherTask.execute(MOVIES_FILTER_TOP_RATED);
            return true;
        }
        else if(id == R.id.action_now_playing)
        {
            weatherTask.execute(MOVIES_FILTER_NOW_PLAYING);
            return true;
        }
        else if(id == R.id.action_upcoming)
        {
            weatherTask.execute(MOVIES_FILTER_UPCOMING);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Context Menu");
//        menu.add(0, v.getId(), 0, "Action 1");
//        menu.add(0, v.getId(), 0, "Action 2");
//    }
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if(item.getTitle()=="Action 1"){ Toast.makeText(getActivity(), "function 1 called", Toast.LENGTH_SHORT).show();}
//        else if(item.getTitle()=="Action 2"){Toast.makeText(getActivity(), "function 2 called", Toast.LENGTH_SHORT).show();}
//        else {return false;}
//        return true;
//    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        ProgressDialog pDialog;
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String popMoviesRequest = null;
            try {
                String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                final String API_FILTER_PARAM = params[0];
                final String MOVIES_API_KEY="5e44fc66144f9fc395dbce0ede660dfe";
                MOVIES_BASE_URL +=API_FILTER_PARAM +"?";


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

        private ArrayList<Movie> getMoviesArrayObjectsFromJson(String results)
        {
            ArrayList<String> moviesPosters = new ArrayList<String>();
            JSONArray moviesArray = null;
            JSONObject moviesJson = null;
            final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
            ArrayList<Movie> movies  = new ArrayList<Movie>();
            try {
                final String MOVIES_PAGE_RESULTS = "results";
                 moviesJson = new JSONObject(results);
                 moviesArray = moviesJson.getJSONArray(MOVIES_PAGE_RESULTS);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject oneMovieObject = moviesArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setID(Integer.parseInt(moviesArray.getJSONObject(i).getString("id")));
                    movie.setTitle(moviesArray.getJSONObject(i).getString("title"));
                    movie.setOverview(moviesArray.getJSONObject(i).getString("overview"));
                    movie.setReleaseDate(moviesArray.getJSONObject(i).getString("release_date"));
                    movie.setRate(moviesArray.getJSONObject(i).getString("vote_average"));
                    movie.setPoster(MOVIES_POSTER_BASE_URL+moviesArray.getJSONObject(i).getString("poster_path"));
                    movie.setBackpath(MOVIES_POSTER_BASE_URL+moviesArray.getJSONObject(i).getString("backdrop_path"));
                    movies.add(movie);
                }

            }catch (JSONException ex)
            {
                ex.printStackTrace();
                return null;
            }
            return movies;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ImageAdapter imgAd = new ImageAdapter(getActivity(),fragmentManager,result);
                gridView.setAdapter(imgAd);
                registerForContextMenu(gridView);
            }catch (Exception ex){
                ex.printStackTrace();

            }
        }

    }

}