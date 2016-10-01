package com.udacity.geekless.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by yahya on 01/10/16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            if (!Utils.isNetworkAvailable(MainActivity.MAIN_APP_ACTIVITY)) {
                Utils.showToast(MainActivity.MAIN_APP_ACTIVITY, "Your connection has been disabled !!");
            }
            
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}