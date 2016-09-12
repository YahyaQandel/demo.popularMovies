package com.udacity.geekless.popularmovies;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction
                    .add(R.id.container, new MoviesGridFragment())
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
