package com.udacity.geekless.popularmovies;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Utils.isNetworkAvailable(this)) {
            fragmentTransaction
                    .replace(R.id.container, new MoviesGridFragment())
                    .commit();
        }
        else
        {
            fragmentTransaction
                    .replace(R.id.container, new OfflineActivity())
                    .commit();
        }

    }
}
