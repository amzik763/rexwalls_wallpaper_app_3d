package com.amzi.cnews3.utility;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.firebase.client.Firebase;

public class prerequisites extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
       // MobileAds.initialize(this, getString(R.string.admob_app_id));

    }

    public  boolean checknetwork(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if((networkInfo != null) && networkInfo.isConnectedOrConnecting())
        {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobiledata = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobiledata!=null && mobiledata.isConnectedOrConnecting())||(wifi!=null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        }
        else return  false;
    }
}
