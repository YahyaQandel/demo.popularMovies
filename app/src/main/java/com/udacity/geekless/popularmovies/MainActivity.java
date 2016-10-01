package com.udacity.geekless.popularmovies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GridItemListener{
    public static boolean two_panels = false;
    public static String FILTER_TYPE = null;
    FragmentManager fragmentManager;
    public static Activity MAIN_APP_ACTIVITY ;
    public static final String MY_PREFS_VAR = "localPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrameLayout flpanel2 = (FrameLayout) findViewById(R.id.fl2);
            if(flpanel2!=null){
                two_panels=true;
            }
            if (Utils.isNetworkAvailable(this)) {
                fragmentTransaction
                        .replace(R.id.container, new MoviesGridFragment())
                        .commit();
            } else {
                fragmentTransaction
                        .replace(R.id.container, new OfflineFragment())
                        .commit();
            }
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_VAR, MODE_WORLD_READABLE).edit();
            editor.putString("filter_type", null);
            editor.apply();
            MAIN_APP_ACTIVITY = this;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {
        List<android.support.v4.app.Fragment> fragments = fragmentManager.getFragments();
        android.support.v4.app.Fragment lastFragment = fragments.get(fragments.size() - 1);
        if(lastFragment instanceof MovieDetailsFragment) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MoviesGridFragment())
                        .addToBackStack(null)
                        .commit();
        }
        else if (lastFragment instanceof MoviesGridFragment)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        BroadcastReceiver receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("CONNECTIVITY_CHANGE");
        registerReceiver(receiver,filter);
    }

    @Override
    public void onItemClicked(Movie movie) {
        if(two_panels){

            Bundle detailedMovieFragment = new Bundle();
            detailedMovieFragment.putString("id", String.valueOf(movie.getID()));
            detailedMovieFragment.putString("title",movie.getTitle());
            detailedMovieFragment.putString("backdrop_path",movie.getBackpath());
            detailedMovieFragment.putString("overview",movie.getOverview());
            detailedMovieFragment.putString("release_date", movie.getReleasedate());
            detailedMovieFragment.putString("vote_average", movie.getRate());
            detailedMovieFragment.putString("poster_path",movie.getPoster());
            FragmentManager fragmentManager = getSupportFragmentManager();
            MovieDetailsFragment detailed_fragment = new MovieDetailsFragment();
            detailed_fragment.setArguments(detailedMovieFragment);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                            .replace(R.id.fl2, detailed_fragment)
                            .commit();

        }
        else {
            Intent detailedMovie = new Intent(this,MovieDetailsActivity.class);
            detailedMovie.putExtra("id", String.valueOf(movie.getID()));
            detailedMovie.putExtra("title",movie.getTitle());
            detailedMovie.putExtra("backdrop_path",movie.getBackpath());
            detailedMovie.putExtra("overview",movie.getOverview());
            detailedMovie.putExtra("vote_average",movie.getRate());
            detailedMovie.putExtra("release_date", movie.getReleasedate());
            detailedMovie.putExtra("poster_path",movie.getPoster());
            startActivity(detailedMovie);
        }
    }
}
