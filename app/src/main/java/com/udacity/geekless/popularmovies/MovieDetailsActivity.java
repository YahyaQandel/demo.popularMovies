package com.udacity.geekless.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

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

        Picasso.with(this).load(movieBackDropPath).into(movie_backdrop_path_imgview);
        movie_title_txtview.setText(movieTitle);
        movie_overview_txtview.setText(movieOverview);
        movie_release_year_txtview.setText(movieReleaseYear);
        movie_rate_txtview.setText(movieRate+"/10");
//        setContentView(textView);

    }

    private String getReleaseYearFromFullDate(String fullDate) {
        return  fullDate.split("-")[0];
    }
}
