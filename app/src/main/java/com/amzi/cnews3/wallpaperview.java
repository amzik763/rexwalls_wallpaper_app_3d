package com.amzi.cnews3;

import static android.content.Intent.ACTION_ATTACH_DATA;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.content.Intent.createChooser;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.amzi.cnews3.adapters.adapter_gridviewforsimilarwallpapers;
import com.amzi.cnews3.adapters.wallpaper_adapter;
import com.amzi.cnews3.firebase.Firebase_retrievesimilarwallpapersforgrid;
import com.amzi.cnews3.model.model_wallpapers;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.amzi.cnews3.utility.sharedData;

public class wallpaperview extends AppCompatActivity implements View.OnClickListener {

    Uri furi;
    File fFile;
    sharedData sharedData;
    boolean isAdloaded = false;
    private ArrayList<model_wallpapers> wallpapersArrayList = new ArrayList<>();
    private ArrayList<model_wallpapers> similarWallpapersArrayList = new ArrayList<>();
    private myvp viewPagerWallpaper;
    private wallpaper_adapter wallpaper_adapter;
    private int currentPosition;
    private int wallpapercurrentposition;
    private ImageView imvFav, imvInfo, imvShare, imvLock, imvDownload, imvSet, imvFav2, imvInfo2, imvShare2;
    private ProgressBar pbWallDown;
    private TextView tvProgress;
    private ImageView imvbacknonblurred;
    private Bitmap wallpaperBitmap;
    private Dialog dw;
    private View decorView;
    private GridView gvSimilar;
    private Firebase_retrievesimilarwallpapersforgrid retrievesimilarwallpapersforgrid;
    private adapter_gridviewforsimilarwallpapers adapter_gridviewsimilarforwallpapers;

    private long lastId;
    private String name;
    private String type;

    private boolean dontload = false;

    private AdView mxAdview;
//    private InterstitialAd interstitial;
    AdRequest adIRequest = new AdRequest.Builder().build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaperview);
        sharedData = new sharedData(wallpaperview.this);
        decorView = getWindow().getDecorView();
        lastId = getIntent().getExtras().getLong("lastId", 0);
        name = getIntent().getExtras().getString("name", "n");
        type = getIntent().getExtras().getString("type", "t");
        mxAdview = findViewById(R.id.adView);
        init();

        if(sharedData.getshowads()) {
            int m = sharedData.increasecounter();
            if (m % 3 == 0 && m != 0) {
                Log.d("adadad","counter 4");
                loadinterstitialad();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        displayInterstitial();
                    }
                }, 3500);
                sharedData.resetcounter();
            }
        }

        dw = new Dialog(wallpaperview.this);
        dw.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        dw.setContentView(R.layout.dialouge_progress);
        dw.setCanceledOnTouchOutside(false);
        imvbacknonblurred.setDrawingCacheEnabled(true);
        currentPosition = getIntent().getIntExtra("position", 0);
        wallpapercurrentposition = currentPosition;
        wallpapersArrayList = getIntent().getParcelableArrayListExtra("data");
        checkWallpaperData(currentPosition);


        if (sharedData.getDownload(String.valueOf(wallpapersArrayList.get(currentPosition).getWallpaperId()))) {
            //imvDownload.setVisibility(View.GONE);
            imvDownload.setImageResource(R.drawable.ic_delete);
        } else {
            //imvDownload.setVisibility(View.VISIBLE);
            imvDownload.setImageResource(R.drawable.ic_download);
        }


//        Toast.makeText(this, wallpapersArrayList.size()+" ", Toast.LENGTH_SHORT).show();


        wallpaper_adapter = new wallpaper_adapter(wallpaperview.this, wallpapersArrayList);


        viewPagerWallpaper.setAdapter(wallpaper_adapter);
        viewPagerWallpaper.setOffscreenPageLimit(1);
        viewPagerWallpaper.setCurrentItem(currentPosition, true);

        ViewPager.OnPageChangeListener mviewpagelitsener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                // Toast.makeText(getContext(), position + " " + wallpapersArrayList.size() + " " + lastsize, Toast.LENGTH_SHORT).show();
                wallpapercurrentposition = position;
                checkWallpaperData(position);

                if (wallpapersArrayList.size() - position <= 3) {
                    //  Toast.makeText(wallpaperview.this, lastId+type+position+" "+wallpapersArrayList.size(), Toast.LENGTH_SHORT).show();
                    loadmorewallpapers();
                }


//


                if (position == wallpapersArrayList.size() - 1) {
                    Toast.makeText(wallpaperview.this, "End of List", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPagerWallpaper.setOnPageChangeListener(mviewpagelitsener);
        viewPagerWallpaper.setPageTransformer(true, new ParallaxTransformer());

        if(sharedData.getshowads()) {
             AdRequest adRequest = new AdRequest.Builder().build();
            // Display Banner ad
              mxAdview.loadAd(adRequest);
        }
    }

    private void loadinterstitialad() {
//        interstitial = new InterstitialAd(wallpaperview.this);
//        interstitial.setAdUnitId(getString(R.string.interstitial_full_screen));
//        interstitial.loadAd(adIRequest);
//        interstitial.setAdListener(new AdListener()
//                                   {
//                                       @Override
//                                       public void onAdClicked() {
//                                           super.onAdClicked();
//                                       }
//
//                                       @Override
//                                       public void onAdFailedToLoad(int i) {
//                                           super.onAdFailedToLoad(i);
//                                           loadinterstitialad();
//                                           Log.d("adadad","adfailed");
//                                       }
//
//                                       @Override
//                                       public void onAdLoaded() {
//                                           super.onAdLoaded();
//                                           isAdloaded = true;
//                                           displayInterstitial();
//                                           Log.d("adadad","adfloaded");
//
//                                       }
//
//                                       @Override
//                                       public void onAdImpression() {
//                                           super.onAdImpression();
//                                       }
//
//                                       @Override
//                                       public void onAdLeftApplication() {
//                                           super.onAdLeftApplication();
//                                       }
//
//                                       @Override
//                                       public void onAdOpened() {
//                                           super.onAdOpened();
//                                       }
//
//                                       @Override
//                                       public void onAdClosed() {
//                                           super.onAdClosed();
//                                       }
//                                   }
//
//        );
    }

    public void displayInterstitial() {
//         If Interstitial Ads are loaded then show else show nothing.

        if (sharedData.getshowads()) {
//            if (interstitial.isLoaded()) {
//                interstitial.show();
//            }
            //loadinterstitialad();
        }
    }
    private void loadmorewallpapers() {

        if (type.equalsIgnoreCase("gridview")) {


            loadmorenormal();

        }
        if (type.equalsIgnoreCase("showOne")) {
            // Toast.makeText(this, "showOne", Toast.LENGTH_SHORT).show();
            loadmorecategory();

        } else if (type.equals("showUnlockable")) {
            // Toast.makeText(this, "showUnlockable", Toast.LENGTH_SHORT).show();
            loadmoreunlockable();
        } else if (type.equals("showFree")) {
            // Toast.makeText(this, "showFree", Toast.LENGTH_SHORT).show();
            loadmorefree();
        } else if (type.equals("showFav")) {

        } else if (type.equals("search")) {
            Toast.makeText(this, "searching", Toast.LENGTH_SHORT).show();
            loadmoresearched();
        }
    }

    private void loadmoresearched() {

        final Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").startAt(lastId).limitToFirst(10);

        if (dontload) {
           // Toast.makeText(wallpaperview.this, "End", Toast.LENGTH_SHORT).show();

            return;
        }


        Log.d("FirebaseHelper", "Loading more searched");
        ValueEventListener listenerLoadMorescat = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHLoadMoreCate", name + " " + String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    Toast.makeText(wallpaperview.this, "Loading", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapers model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);

                        if (model_wallpapersx.getSubCategory().equals(name)) {
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                            count++;
                            wallpapersArrayList.add(model_wallpapersx);

                        }
                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));


                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    wallpaper_adapter.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                   // Toast.makeText(wallpaperview.this, "End of the list", Toast.LENGTH_SHORT).show();
                }

                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);
            }
        };
        q.addValueEventListener(listenerLoadMorescat);


    }

    private void loadmorefree() {

        if (dontload) {
          //  Toast.makeText(wallpaperview.this, "End of List", Toast.LENGTH_SHORT).show();
            return;
        }

        final Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").startAt(lastId).limitToFirst(10);
        ValueEventListener listenerLoadMorefre = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("firebasemore", name + " " + String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    Toast.makeText(wallpaperview.this, "Loading", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapers model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);

                        if (model_wallpapersx.getIsTop().equals("yes")) {
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                            count++;
                            wallpapersArrayList.add(model_wallpapersx);

                        }
                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));


                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    wallpaper_adapter.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                  //  Toast.makeText(wallpaperview.this, "End of the list", Toast.LENGTH_SHORT).show();
                }

                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);
            }
        };
        q.addValueEventListener(listenerLoadMorefre);

    }

    private void loadmoreunlockable() {

        if (dontload) {
           // Toast.makeText(wallpaperview.this, "End of List", Toast.LENGTH_SHORT).show();
            return;
        }

        final Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").startAt(lastId).limitToFirst(10);
        ValueEventListener listenerLoadMoreunl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("firebasemore", name + " " + String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    Toast.makeText(wallpaperview.this, "Loading", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapers model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);

                        if (model_wallpapersx.getIsPremium().equals("Unlockable")) {
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                            count++;
                            wallpapersArrayList.add(model_wallpapersx);

                        }
                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));


                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    wallpaper_adapter.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    //Toast.makeText(wallpaperview.this, "End of the list", Toast.LENGTH_SHORT).show();
                }

                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);
            }
        };
        q.addValueEventListener(listenerLoadMoreunl);

    }

    private void loadmorenormal() {
        if (dontload) {
           // Toast.makeText(wallpaperview.this, "End of List", Toast.LENGTH_SHORT).show();
            return;
        }

        final Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").startAt(lastId).limitToFirst(10);
        ValueEventListener listenerforgrid = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    Toast.makeText(wallpaperview.this, "Loading: ", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapers model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                        count++;

                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                        wallpapersArrayList.add(model_wallpapersx);

                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));

                    }


                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    wallpaper_adapter.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                   // Toast.makeText(wallpaperview.this, "End of list", Toast.LENGTH_SHORT).show();
                }
                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(wallpaperview.this, "Error", Toast.LENGTH_SHORT).show();
                q.removeEventListener(this);
            }
        };
        q.addValueEventListener(listenerforgrid);

    }

    public void loadmorecategory() {

        final Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").startAt(lastId).limitToFirst(10);

        if (dontload) {
          //  Toast.makeText(wallpaperview.this, "End", Toast.LENGTH_SHORT).show();

            return;
        }


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadMorecat = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHLoadMoreCate", name + " " + String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    Toast.makeText(wallpaperview.this, "Loading", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapers model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);

                        if (model_wallpapersx.getCategory().equals(name)) {
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                            count++;
                            wallpapersArrayList.add(model_wallpapersx);

                        }
                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));


                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        wallpapersArrayList.remove(wallpapersArrayList.size() - 1);
                    wallpaper_adapter.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                 //   Toast.makeText(wallpaperview.this, "End of the list", Toast.LENGTH_SHORT).show();
                }

                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);
            }
        };
        q.addValueEventListener(listenerLoadMorecat);


    }

    private void checkWallpaperData(int position) {
        //  Toast.makeText(this, " "+wallpapersArrayList.get(position).getWallpaperId(), Toast.LENGTH_SHORT).show();

        Query q = FirebaseDatabase.getInstance().getReference().child("wallpapers").orderByChild("wallpaperId").limitToFirst(350);
        similarWallpapersArrayList.clear();
        retrievesimilarwallpapersforgrid = new Firebase_retrievesimilarwallpapersforgrid(q, wallpaperview.this, adapter_gridviewsimilarforwallpapers, gvSimilar, similarWallpapersArrayList, wallpapersArrayList.get(position).getWallpaperId(),wallpapersArrayList.get(position).getTags().toString());
        gvSimilar.setAdapter(adapter_gridviewsimilarforwallpapers);


        Picasso.get().load(wallpapersArrayList.get(position).getLink()).into(imvbacknonblurred, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("picasso", "done");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BitmapDrawable drawable = (BitmapDrawable) imvbacknonblurred.getDrawable();
                        try {


                            Bitmap bitmap = drawable.getBitmap();

                            imvbacknonblurred.setImageBitmap(bitmap);
//                            Bitmap blurredbitmap = BlurBuilder.blur(wallpaperview.this, bitmap);

                        } catch (NullPointerException exc) {

                        }
                    }
                }, 180);

            }

            @Override
            public void onError(Exception e) {

                Log.d("picasso", "not done");

            }
        });


        if (sharedData.getDownload(String.valueOf(wallpapersArrayList.get(position).getWallpaperId()))) {
            //imvDownload.setVisibility(View.GONE);
            imvDownload.setImageResource(R.drawable.ic_delete);
        } else {
            //imvDownload.setVisibility(View.VISIBLE);
            imvDownload.setImageResource(R.drawable.ic_download);


        }
        if (sharedData.getFavourites(String.valueOf(wallpapersArrayList.get(position).getWallpaperId()))) {
            imvFav.setImageResource(R.drawable.ic_star);
            imvFav2.setImageResource(R.drawable.ic_star);
        } else {
            imvFav.setImageResource(R.drawable.ic_star_border);
            imvFav2.setImageResource(R.drawable.ic_star_border);

        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            hidebars();
        }
    }

    private void init() {
        gvSimilar = findViewById(R.id.gridviewWallSimilar);
        viewPagerWallpaper = findViewById(R.id.vpWallpaper);
        imvFav = findViewById(R.id.imvFav);
        imvbacknonblurred = findViewById(R.id.backnonblurred);
        imvInfo = findViewById(R.id.imvinfo);
        imvShare = findViewById(R.id.imvshare);
        imvFav2 = findViewById(R.id.imvFav2);
        imvInfo2 = findViewById(R.id.imvinfo2);
        imvShare2 = findViewById(R.id.imvshare2);
        imvLock = findViewById(R.id.imvunlcok);
        imvDownload = findViewById(R.id.imvdownload);
        imvSet = findViewById(R.id.imvset);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i == 0) {
                    hidebars();
                }
            }
        });


        imvFav.setOnClickListener(this);
        imvInfo.setOnClickListener(this);
        imvShare.setOnClickListener(this);
        imvFav2.setOnClickListener(this);
        imvInfo2.setOnClickListener(this);
        imvShare2.setOnClickListener(this);
        imvDownload.setOnClickListener(this);
        imvSet.setOnClickListener(this);

    }

    private void hidebars() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        imvbacknonblurred = findViewById(R.id.backnonblurred);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == imvFav.getId() | view.getId() == imvFav2.getId()) {
            //Toast.makeText(this, " "+wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId(), Toast.LENGTH_SHORT).show();
            if (sharedData.getFavourites(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()))) {
                sharedData.removeFavourites(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()));
                imvFav.setImageResource(R.drawable.ic_star_border);
                imvFav2.setImageResource(R.drawable.ic_star_border);
                Toast.makeText(this, "unfavourited", Toast.LENGTH_SHORT).show();

            } else {
                sharedData.setFavourites(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()));
                imvFav.setImageResource(R.drawable.ic_star);
                imvFav2.setImageResource(R.drawable.ic_star);
                Toast.makeText(this, "favourited", Toast.LENGTH_SHORT).show();

            }

        } else if (view.getId() == imvInfo.getId() | view.getId() == imvInfo2.getId()) {

            showinfoDialouge();

        } else if (view.getId() == imvShare.getId() | view.getId() == imvShare2.getId()) {
            try {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedData.getLink())));
            } catch (android.content.ActivityNotFoundException anfe) {

            } catch (Exception e) {

            }

        } else if (view.getId() == imvLock.getId()) {

        } else if (view.getId() == imvDownload.getId()) {
            if (sharedData.getDownload(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()))) {
                File myFile = new File(getFilesDir(), String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()).concat(".jpg"));
                Log.d("pathis", myFile.getAbsolutePath());
                boolean isDeleted = myFile.delete();
                if (isDeleted) {
                    sharedData.removeDownloads(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()));
                    Toast.makeText(this, "Wallpaper Deleted", Toast.LENGTH_SHORT).show();
                    imvDownload.setImageResource(R.drawable.ic_download);
                } else {
                    Toast.makeText(this, "Please Try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                startDownloading();
            }

        } else if (view.getId() == imvSet.getId()) {
            if (sharedData.getDownload(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()))) {
                if (isWriteStoragePermissionGranted()) {
                    showWallpaperDialouge();
                }
            } else {
                startDownloading();
            }
        }
    }

    private void startDownloading() {

        final ProgressBar pbDownload;
        final TextView tvProgress, tvMsg;
        final Dialog d = new Dialog(wallpaperview.this);
        d.setContentView(R.layout.dialouge_download);
        pbDownload = d.findViewById(R.id.pbDownload);
        tvProgress = d.findViewById(R.id.tvprogress);
        tvMsg = d.findViewById(R.id.tvdownloadmsg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvMsg.getText().equals("Set Wallpaper")) {
                    if (d.isShowing())
                        d.dismiss();
                    if (isWriteStoragePermissionGranted())
                        showWallpaperDialouge();
                } else if (tvMsg.getText().equals("Retry")) {
                    if (d.isShowing())
                        d.dismiss();
                    startDownloading();

                }

            }
        });
        d.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        d.setCanceledOnTouchOutside(false);
        d.show();


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
//                            Toast.makeText(ctx, "downloading", Toast.LENGTH_SHORT).show();
                    try {

                        URL url = new URL(wallpapersArrayList.get(wallpapercurrentposition).getDownloadlink());
                        Log.d("urlis", url.toString());
                        HttpURLConnection c = (HttpURLConnection) url.openConnection();
                        c.setRequestMethod("GET");
                        c.setReadTimeout(10000);
                        c.setConnectTimeout(10000);
//                                c.setDoOutput(true);
                        c.connect();
                        File myFile = new File(getFilesDir(), String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()).concat(".jpg"));
                        Log.d("pathis", myFile.getAbsolutePath());
                        Log.d("fileLength", c.getContentLength() + " ");
                        FileOutputStream fos = new FileOutputStream(myFile);//Get OutputStream for NewFile Location

                        InputStream is = c.getInputStream();//Get InputStream for connection

                        byte[] buffer = new byte[2048];//Set buffer type
                        int len1 = 0;//init length
                        long total = 0;
                        final int fileLength = c.getContentLength();
                        while ((len1 = is.read(buffer)) != -1) {
                            total += len1;
                            fos.write(buffer, 0, len1);//Write new file
                            Log.d("dwnld", String.valueOf(len1) + " showing " + fileLength + " prog: " + (total * 100) / fileLength);

                            final long finalTotal = total;
                            ((Activity) wallpaperview.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvProgress.setText("Downloaded: " + (finalTotal * 100) / fileLength + "%");
                                    pbDownload.setProgress((int) ((finalTotal * 100) / fileLength));
                                    //    Toast.makeText(wallpaperview.this, "Downloading: " + (finalTotal * 100) / fileLength + "%", Toast.LENGTH_SHORT).show();
                                    Log.d("Downloading: ", "Downloading:" + (finalTotal * 100) / fileLength + "%");
                                    if (fileLength == finalTotal) {
                                        //holder.pbDownload1.setVisibility(View.GONE);
                                        //holder.tvBookDownload1.setText("Downloaded Successfully");
                                        //holder.imvBookDownload1.setVisibility(View.GONE);
                                        //holder.imvBookOpen1.setVisibility(View.GONE);
                                        Toast.makeText(wallpaperview.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                        sharedData.setDownload(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()));
                                        imvDownload.setImageResource(R.drawable.ic_delete);
                                        tvMsg.setText("Set Wallpaper");
                                        tvMsg.setVisibility(View.VISIBLE);
                                    }

                                }
                            });


                        }

                        fos.close();
                        is.close();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        //d.dismiss();
                        tvMsg.setText("Retry");
                        tvMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(wallpaperview.this, "Network Problem!", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMsg.setText("Retry");
                                tvMsg.setVisibility(View.VISIBLE);
                                Toast.makeText(wallpaperview.this, "Network Problem!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        //d.dismiss();

                    }

                    //Your code goes here
                } catch (Exception e) {
                    e.printStackTrace();
                    tvMsg.setText("Retry");
                    tvMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(wallpaperview.this, "Network Problem!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        thread.start();
    }

    private void showWallpaperDialouge() {
        final Dialog dww;
        dww = new Dialog(wallpaperview.this);
        dww.setContentView(R.layout.dialouge_progress);
        dww.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        dww.setContentView(R.layout.dialouge_wpselect);
        dww.setCanceledOnTouchOutside(true);
        TextView tvwp1 = dww.findViewById(R.id.tvsetwp);
        TextView tvwp2 = dww.findViewById(R.id.tvsetwp2);
        TextView tvwpl = dww.findViewById(R.id.tvsetwpl);
        tvwp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dw.show();
                setWallpaper(false);
                if (dww.isShowing())
                    dww.dismiss();
            }
        });

        tvwp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaperUsingSystem();
                if (dww.isShowing())
                    dww.dismiss();
            }
        });

        tvwpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dw.show();
                setWallpaper(true);
                if (dww.isShowing())
                    dww.dismiss();
            }
        });
        dww.show();
    }

    private void showinfoDialouge() {
        final Dialog dww;
        dww = new Dialog(wallpaperview.this);
        dww.setContentView(R.layout.dialouge_info);
        dww.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        dww.setCanceledOnTouchOutside(true);
        dww.setCancelable(true);
        TextView tvwpid = dww.findViewById(R.id.tvwallpaperid);
        TextView tvwpcat = dww.findViewById(R.id.tvwallpapercat);
        TextView tvwptypel = dww.findViewById(R.id.tvwallpapertype);
        tvwpid.setText(String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()));
        tvwpcat.setText(wallpapersArrayList.get(wallpapercurrentposition).getCategory());
        tvwptypel.setText(wallpapersArrayList.get(wallpapercurrentposition).getSubCategory());
        dww.show();
    }


    private void setWallpaper(boolean lock) {
        final boolean islock = lock;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        File myFile = new File(getFilesDir(), String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()).concat(".jpg"));
                        BitmapDrawable drawable = (BitmapDrawable) imvbacknonblurred.getDrawable();
                      //  Bitmap bitmap = drawable.getBitmap();
                        Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());
                        wallpaperBitmap = bitmap;
                        imvbacknonblurred.setImageBitmap(wallpaperBitmap);

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

                        imvbacknonblurred.setDrawingCacheEnabled(true);
                        imvbacknonblurred.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        imvbacknonblurred.layout(0, 0, width, height);

                        imvbacknonblurred.buildDrawingCache(true);
                        wallpaperBitmap = Bitmap.createBitmap(imvbacknonblurred.getDrawingCache());
                        imvbacknonblurred.setDrawingCacheEnabled(false);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        wallpaperBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());

                        if (islock) {
                         //   Toast.makeText(wallpaperview.this, "Setting Lock Screen", Toast.LENGTH_SHORT).show();
                            manager.setStream(is, null, true, WallpaperManager.FLAG_LOCK);

                        } else
                            manager.setStream(is);

                        Toast.makeText(getApplicationContext(), "Wallpaper Changed", Toast.LENGTH_SHORT).show();
                        loadinterstitialad();

                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Cannot Change Wallpaper!", Toast.LENGTH_SHORT).show();
                }
                dw.dismiss();
            }
        }, 200);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.wq);


    }

    private void setWallpaperUsingSystem() {


        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                BitmapDrawable drawable = (BitmapDrawable) imvbacknonblurred.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();
                // Bitmap blurredbitmap = BlurBuilder.blur(wallpaperview.this, bitmap);
                File myFile = new File(getFilesDir(), String.valueOf(wallpapersArrayList.get(wallpapercurrentposition).getWallpaperId()).concat(".jpg"));
                Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());

                Toast.makeText(wallpaperview.this, "cp:" + wallpapercurrentposition, Toast.LENGTH_SHORT).show(); wallpaperBitmap = bitmap;
                imvbacknonblurred.setImageBitmap(wallpaperBitmap);


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                wallpaperBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), wallpaperBitmap, "mine", "mine");
                furi = Uri.parse(path);
                Log.d("path: ", path);
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                // Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                //intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
                 // Intent intent = new Intent("android.service.wallpaper.CROP_AND_SET_WALLPAPER");
                 Intent intent = new Intent(ACTION_ATTACH_DATA);
               // intent.setClassName("com.android.launcher", "com.android.launcher2.WallpaperChooser");
                intent.setDataAndType(furi, "image/jpeg");
                intent.putExtra("mimeType", "image/jpeg");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(createChooser(intent,"Select App"), 656);
                fFile = new File(path);
                Log.d("filepth: ", path + "\n" + fFile.getName());


                if (fFile.exists())
                    if (fFile.delete()) {

                    } else {

                    }

            }
        }, 200);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.wq);


    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisi", "Permission is granted2");
                return true;
            } else {

                Log.v("permisi", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisi", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("xpermisi", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("xpermisi", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    showWallpaperDialouge();

                    //resume tasks needing this permission
                } else {

                }
                break;

            case 3:
                Log.d("xpermisi", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("xpermisi", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                } else {
                }
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadinterstitialad();
        if (requestCode == 656) {
            // Toast.makeText(this, "wallpaper Applied", Toast.LENGTH_SHORT).show();
            getContentResolver().delete(furi, fFile.getName(), null);

        }
    }

    public class ParallaxTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position >= -1 && position <= 1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                // view.setAlpha(0f);
                view.findViewById(R.id.imvWallpaper).setTranslationX(-position * pageWidth / 2);

            } else
                view.setAlpha(1);


        }
    }
}
