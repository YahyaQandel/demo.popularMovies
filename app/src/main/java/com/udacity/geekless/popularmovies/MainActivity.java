package com.udacity.geekless.popularmovies;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (Utils.isNetworkAvailable(this)) {
                fragmentTransaction
                        .replace(R.id.container, new MoviesGridFragment())
                        .commit();
            } else {
                fragmentTransaction
                        .replace(R.id.container, new OfflineFragment())
                        .commit();
            }
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

    }
}
