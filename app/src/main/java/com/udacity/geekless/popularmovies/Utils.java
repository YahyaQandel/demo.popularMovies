package com.udacity.geekless.popularmovies;

/**
 * Created by yahya on 18/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void showToast(Activity activity , String msg)
    {
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context activity , String msg)
    {
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

}