package com.amzi.cnews3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.amzi.cnews3.firebase.Firebase_retrievecategoriesforgrid;
import com.amzi.cnews3.firebase.Firebase_retrievewallpapers;
import com.amzi.cnews3.firebase.Firebase_retrievewallpapersforgrid;
import com.amzi.cnews3.model.model_categories;
import com.amzi.cnews3.model.model_wallpapers;
import com.amzi.cnews3.utility.Dialouge;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.amzi.cnews3.utility.sharedData;
import com.amzi.cnews3.adapters.adapter_recycleviewforwallpapers;
import com.amzi.cnews3.adapters.adapter_gridviewforwallpapers;
import com.amzi.cnews3.adapters.adapter_gridviewforcategories;
import com.amzi.cnews3.adapters.adapter_recycleviewforwallpapers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements TextWatcher {


    boolean scrollend = false;
    boolean textmatched = false;

    long lastId = 0;

    private sharedData sharedData;
    private ImageView imvSearch;
    private TextView tvYourLibrary, tvYourFavourites, tvShowTop, tvShowPremium;
    private RecyclerView rvTopWallpapers, rvPremiumWallpapers;
    private AutoCompleteTextView edAutoSearch;
    private ImageView imvSearchWallpaper, imvRemoveAds;
    private  adapter_recycleviewforwallpapers adapter_recycleviewforwallpapers;
    private  adapter_gridviewforwallpapers adapter_gridviewforwallpapers;
    private  adapter_gridviewforcategories adapter_gridviewforcategories;
    private ArrayList<model_wallpapers> wallpapersArrayList = new ArrayList<>();
    private ArrayList<model_wallpapers> wallpapersArrayListPremium = new ArrayList<>();
    private ArrayList<model_wallpapers> wallpapersArrayListAll = new ArrayList<>();
    private ArrayList<model_categories> categoriesArrayList = new ArrayList<>();
    private DatabaseReference dbWallpapers, dbCategories;
    private Query qTop, qPremium, qAll, qCategories, qSearch;
    private GridView gvWallpapers, gvCategories;
    private Firebase_retrievewallpapers firebase_retrievewallpapers;
    private Firebase_retrievewallpapersforgrid firebase_retrievewallpapersforgrid;
    private Firebase_retrievecategoriesforgrid firebase_retrievecategoriesforgrid;
    private ArrayList<String> categories = new ArrayList<>();
    private AdView mxAdView;
    private String mfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        sharedData = new sharedData(this);
        mxAdView = findViewById(R.id.adView);
        getmanufacturer();
        init();
        retrieve_categories();
        rvTopWallpapers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        firebase_retrievewallpapers = new Firebase_retrievewallpapers(qTop, Home.this, adapter_recycleviewforwallpapers, rvTopWallpapers, wallpapersArrayList);
        firebase_retrievewallpapers = new Firebase_retrievewallpapers(qPremium, Home.this, adapter_recycleviewforwallpapers, rvPremiumWallpapers, wallpapersArrayListPremium);
        firebase_retrievewallpapersforgrid = new Firebase_retrievewallpapersforgrid(qAll, Home.this, adapter_gridviewforwallpapers, gvWallpapers, wallpapersArrayListAll, lastId, "gridview", "gridview");
        firebase_retrievecategoriesforgrid = new Firebase_retrievecategoriesforgrid(qCategories, Home.this, adapter_gridviewforcategories, gvCategories, categoriesArrayList);

        if(sharedData.getshowads()) {
              AdRequest adRequest = new AdRequest.Builder().build();
              mxAdView.loadAd(adRequest);
        }
    }


    private void getmanufacturer() {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                Log.d("xvx","xiaomi");


            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                Log.d("xvx","oppo");

            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                Log.d("xvx","vivo");
            }else if ("Honor".equalsIgnoreCase(manufacturer)) {
                Log.d("xvx","honor");
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                Log.d("xvx","size");

                for (int i =0;i<list.size();i++){
                    Log.d("xvx",list.get(i).resolvePackageName);

                }

            }
        } catch (Exception e) {
            Log.d("xvx",e.getMessage());

        }
    }

    private void retrieve_categories() {
        Query q = FirebaseDatabase.getInstance().getReference().child("subCategories");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String categoriesx = ds.getValue(String.class);
                        categories.add(categoriesx);
                    }

                    setmyAdapter(categories);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setmyAdapter(ArrayList<String> categories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.my_dropdown, categories);
        edAutoSearch.setAdapter(adapter);
        edAutoSearch.setThreshold(1);
        edAutoSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edAutoSearch.showDropDown();
                return false;

            }
        });

        edAutoSearch.addTextChangedListener(this);

    }

    public void init() {
        edAutoSearch = findViewById(R.id.edAutoSearchWallpaper);
        tvShowTop = findViewById(R.id.ButtonTopWallpaper);
        tvShowPremium = findViewById(R.id.ButtonrecentWallpaper);
        edAutoSearch = findViewById(R.id.edAutoSearchWallpaper);
        imvSearchWallpaper = findViewById(R.id.imvSearch);
        imvRemoveAds = findViewById(R.id.imvRemoveAds);
        rvTopWallpapers = findViewById(R.id.rvtopwall);
        rvPremiumWallpapers = findViewById(R.id.rvrecentwall);
        tvYourLibrary = findViewById(R.id.tvYourLibrary);
        tvYourFavourites = findViewById(R.id.tvYourFavourites);
//        tvShowPremium = findViewById(R.id.tvrecentWallpaper);
//        tvShowTop = findViewById(R.id.tvTopWallpaper);
        gvWallpapers = findViewById(R.id.gridviewWall);
        gvCategories = findViewById(R.id.gridviewWallCat);
        dbWallpapers = FirebaseDatabase.getInstance().getReference().child("wallpapers");
        dbCategories = FirebaseDatabase.getInstance().getReference().child("categories");
        qAll = dbWallpapers.orderByChild("wallpaperId").limitToFirst(25);
        qTop = dbWallpapers.orderByChild("isTop").equalTo("yes").limitToFirst(15);
        qPremium = dbWallpapers.orderByChild("isPremium").equalTo("Unlockable").limitToFirst(15);
        qCategories = dbCategories.orderByChild("name");


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
                        // Toast.makeText(Home.this, "loading", Toast.LENGTH_SHORT).show();

                        qAll = dbWallpapers.orderByChild("wallpaperId");
                        firebase_retrievewallpapersforgrid.loadmore(qAll);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollend = false;

                            }
                        }, 850);
                    }
                }
            }
        });

        imvSearchWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textmatched) {
                    String searchtext = edAutoSearch.getText().toString();
                    Intent cbw = new Intent(Home.this, categorybasedview.class);
                    cbw.putExtra("type", "search");
                    cbw.putExtra("name", searchtext);
                    startActivity(cbw);
                } else {
                    edAutoSearch.setError("Please Select correct option");
                    edAutoSearch.requestFocus();
                }
            }
        });

        imvRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "All Good", Toast.LENGTH_LONG).show();
            }
        });

        tvShowPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cbw = new Intent(Home.this, categorybasedview.class);
                cbw.putExtra("name", "showUnlockable");
                cbw.putExtra("type", "showUnlockable");
                startActivity(cbw);
            }
        });

        tvShowTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cbw = new Intent(Home.this, categorybasedview.class);
                cbw.putExtra("name", "showFree");
                cbw.putExtra("type", "showFree");
                startActivity(cbw);
            }
        });

        tvYourLibrary.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                File mydir = getFilesDir();
                File lister = mydir.getAbsoluteFile();
                int i = 0;
                for (String list : lister.list()) {
                    String path = lister.getAbsolutePath() + "/" + list;
                   // Bitmap myBitmap = BitmapFactory.decodeFile(lister.getAbsolutePath() + "/" + list);
                    Log.d("pathis: ", i+path);
                    i++;
                    if(i>0)
                        break;

                }

                if (i > 0) {
                    Log.d("pathis: ", "xloading");

                    Intent cbw = new Intent(Home.this, downloadedwallpapers.class);
                    startActivity(cbw);
                } else {
                    showdialog();
                }


            }
        });

        tvYourFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cbw = new Intent(Home.this, categorybasedview.class);
                cbw.putExtra("name", "showFav");
                cbw.putExtra("type", "showFav");
                startActivity(cbw);
            }
        });
    }

    public void showdialog() {
        Dialouge d = new Dialouge(Home.this);
        d.displaytext(Home.this, "No Wallpaper Downloaded yet!");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        for (int x = 0; x < categories.size(); x++) {
            Log.d("thisone", categories.size() + " " + edAutoSearch.getText().toString() + " " + categories.get(x).toString());
            if (edAutoSearch.getText().toString().equalsIgnoreCase(categories.get(x).toString())) {
                textmatched = true;
                edAutoSearch.setTextColor(getResources().getColor(R.color.white));
                break;
            } else {
                textmatched = false;
                edAutoSearch.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
//        if(edAutoSearch.getText().toString().equalsIgnoreCase(ca))
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }




}
