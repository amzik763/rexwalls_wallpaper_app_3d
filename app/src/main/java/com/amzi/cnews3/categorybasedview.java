package com.amzi.cnews3;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;


import com.amzi.cnews3.firebase.Firebase_retrievefavouritewallpapersforgrid;
import com.amzi.cnews3.firebase.Firebase_retrievewallpapersforgrid;
import com.amzi.cnews3.model.model_wallpapers;
import com.amzi.cnews3.utility.sharedData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class categorybasedview extends AppCompatActivity {

    private AdView mxadview;
    private boolean scrollend = false;
    private String name, type;
    private DatabaseReference dbCategory;
    private Query q;
    private com.amzi.cnews3.adapters.adapter_gridviewforwallpapers adapter_gridviewforwallpapers;
    private GridView gvWallpapers;
    private ArrayList<model_wallpapers> wallpapersArrayList = new ArrayList<>();
    private Firebase_retrievewallpapersforgrid firebase_retrievewallpapersforgrid;
    private Firebase_retrievefavouritewallpapersforgrid firebase_retrievefavouritewallpapersforgrid;
    private sharedData sharedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorybasedview);
        sharedData = new sharedData(this);
        gvWallpapers = findViewById(R.id.gvwallpapersc);

        name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("type");
        dbCategory = FirebaseDatabase.getInstance().getReference().child("wallpapers");

        mxadview = findViewById(R.id.adView);
        if (type.equals("showOne"))
            q = dbCategory.orderByChild("category").equalTo(name).limitToFirst(15);
        else if (type.equals("showUnlockable"))
            q = dbCategory.orderByChild("isPremium").equalTo("Unlockable").limitToFirst(15);
        else if (type.equals("showFree"))
            q = dbCategory.orderByChild("isTop").equalTo("yes").limitToFirst(15);
        else if (type.equals("showFav"))
            q = dbCategory.orderByChild("wallpaperId").limitToFirst(50);
        else if (type.equals("search"))
            q = dbCategory.orderByChild("subCategory").equalTo(name).limitToFirst(15);


        if (type.equals("showFav")) {
            firebase_retrievefavouritewallpapersforgrid = new Firebase_retrievefavouritewallpapersforgrid(q, categorybasedview.this, adapter_gridviewforwallpapers, gvWallpapers, wallpapersArrayList);

        } else {
            firebase_retrievewallpapersforgrid = new Firebase_retrievewallpapersforgrid(q, categorybasedview.this, adapter_gridviewforwallpapers, gvWallpapers, wallpapersArrayList, 1, name, type);

        }


        gvWallpapers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (visibleItemCount > 0) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen == totalItemCount && !scrollend) {
                        scrollend = true;
                        Log.d("loadingfree: ", "loading");

                        // Toast.makeText(Home.this, "loading", Toast.LENGTH_SHORT).show();

                        if (type.equals("showOne")) {
                            q = dbCategory.orderByChild("wallpaperId");
                            firebase_retrievewallpapersforgrid.loadmorecategory(q, name);
                        } else if (type.equals("showUnlockable")) {
                            q = dbCategory.orderByChild("wallpaperId");
                            firebase_retrievewallpapersforgrid.loadmorepremium(q, "Unlockable");
                        } else if (type.equals("showFree")) {
                            q = dbCategory.orderByChild("wallpaperId");
                            firebase_retrievewallpapersforgrid.loadmorefree(q, "yes");

                        } else if (type.equals("showFav")) {
                            //q = dbCategory.orderByChild("wallpaperId");
                        } else if (type.equals("search")) {
                            q = dbCategory.orderByChild("wallpaperId");
                            firebase_retrievewallpapersforgrid.loadmoresearched(q, name);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollend = false;
                            }
                        }, 1500);
                    }
                }
            }
        });
        if (sharedData.getshowads()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            // Display Banner ad
            mxadview.loadAd(adRequest);

        }
    }

}
