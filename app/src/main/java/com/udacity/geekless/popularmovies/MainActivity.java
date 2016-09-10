package com.udacity.geekless.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//        public Integer[] mThumbIds = {
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle, R.drawable.eagle,
//            R.drawable.eagle
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_poster_grid_layout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesFragment())
                    .commit();
        }
//        Toast.makeText(this,"Test ",Toast.LENGTH_LONG).show();
//        try {
//            GridView gridView = (GridView) findViewById(R.id.poster_grid_view);
//            gridView.setAdapter(new ImageAdapter(this,mThumbs));
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }
}
