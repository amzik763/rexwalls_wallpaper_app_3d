package com.amzi.cnews3.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.amzi.cnews3.model.model_wallpapers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.amzi.cnews3.adapters.adapter_gridviewforwallpapers;
import java.util.ArrayList;
import com.amzi.cnews3.utility.sharedData;

public class Firebase_retrievewallpapersforgrid {

    Query db;
    model_wallpapers model_wallpapersx;
    ArrayList<model_wallpapers> arrayList_wallpapers;
    GridView gridView;
    Context c;
    String wid = "no";
    adapter_gridviewforwallpapers adapter_gridviewforwallpapers;
    sharedData sharedData;
    String category = null;
    boolean success = false;
    boolean dontload = false;
    long lastId = 0;
    String name;
    String type;

    public Firebase_retrievewallpapersforgrid(Query db, Context context, adapter_gridviewforwallpapers adapter_gridviewforwallpapers, GridView gridView, ArrayList<model_wallpapers> model_wallpapersx, long lastId,String name, String type) {
        this.db = db;
        this.c = context;
        this.adapter_gridviewforwallpapers = adapter_gridviewforwallpapers;
        this.arrayList_wallpapers = model_wallpapersx;
        sharedData = new sharedData(c);
        this.gridView = gridView;
        this.lastId = lastId;
        this.name = name;
        this.type = type;
        this.retrievewallpapers();
    }




    public void retrievewallpapers() {
        Log.d("FirebaseHelper", "start retrieved");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        if (String.valueOf(model_wallpapersx.getWallpaperId()).equals(wid)) {

                        } else
                        {
                            arrayList_wallpapers.add(model_wallpapersx);
                            lastId = model_wallpapersx.getWallpaperId();
                            //category = model_wallpapersx.getCategory();
                            Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()) + "  " + lastId);
                        }

                    }
                    arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers = new adapter_gridviewforwallpapers(c, arrayList_wallpapers, gridView,lastId,name,type);
                    gridView.setAdapter(adapter_gridviewforwallpapers);
                    gridView.setHorizontalScrollBarEnabled(false);
                    gridView.setVerticalScrollBarEnabled(false);
                    gridView.setHorizontalSpacing(8);
                    gridView.setVerticalSpacing(8);

                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    Toast.makeText(c, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
            }
        };
        db.addValueEventListener(listener);

    }



    public void loadmore(Query q) {


        if (dontload) {
            return;
        }

        db = q.startAt(lastId).limitToFirst(25);


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadMore = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelperLoadMore", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    if (dataSnapshot.getChildrenCount() == 1) {
                        Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();
                        dontload = true;
                        return;
                    }
                    Toast.makeText(c, "Loading", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("loadmorewaaallpper", String.valueOf(model_wallpapersx.getWallpaperId()) + "  " + lastId);

                        if (String.valueOf(model_wallpapersx.getWallpaperId()).equals(wid)) {

                        } else {
                            arrayList_wallpapers.add(model_wallpapersx);
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));
                        }

                    }
                    success = true;
                    arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers.notifyDataSetChanged();

                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    success = true;
                    Toast.makeText(c, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                success = true;
            }
        };
        db.addValueEventListener(listenerLoadMore);


    }

    public void loadmorecategory(Query q, final String categoryName) {

        if (dontload) {
            Toast.makeText(c, "End of List", Toast.LENGTH_SHORT).show();

            return;
        }

        db = q.startAt(lastId).limitToFirst(300);


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadMoreval = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelperLoadMore", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;

                    Toast.makeText(c, "Loading", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("wallpaperfoundnot", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);

                        if (model_wallpapersx.getCategory().equals(categoryName)) {
                            lastId = model_wallpapersx.getWallpaperId();
                            Log.d("wallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()) + " " + lastId);
                            count++;
                            arrayList_wallpapers.add(model_wallpapersx);

                        }
                        lastId = model_wallpapersx.getWallpaperId();
                        Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));


                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers.notifyDataSetChanged();


                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();
                }

                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                success = true;
            }
        };
        db.addValueEventListener(listenerLoadMoreval);


    }

    public void loadmorefree(Query q, final String categoryName) {


        if (dontload) {
            Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();

            return;
        }

        db = q.startAt(lastId).limitToFirst(30);


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadMore = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelperLoadMore", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;

                    Toast.makeText(c, "Loading", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("loadmorewaaallpper", String.valueOf(model_wallpapersx.getWallpaperId()) + "  " + lastId);

                        if (model_wallpapersx.getIsTop().equals("yes")) {
                            arrayList_wallpapers.add(model_wallpapersx);
                            lastId = model_wallpapersx.getWallpaperId();
                            count++;
                            Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));
                        }

                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers.notifyDataSetChanged();

                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();
                }

                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                success = true;
            }
        };
        db.addValueEventListener(listenerLoadMore);


    }

    public void loadmorepremium(Query q, final String categoryName) {


        if (dontload) {
            Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();

            return;
        }

        db = q.startAt(lastId).limitToFirst(100);


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadMore = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelperLoadMore", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;

                    Toast.makeText(c, "Loading", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("loadmorewaaallpper", String.valueOf(model_wallpapersx.getWallpaperId()) + "  " + lastId);

                        if (model_wallpapersx.getIsPremium().equals("Unlockable")) {
                            arrayList_wallpapers.add(model_wallpapersx);
                            lastId = model_wallpapersx.getWallpaperId();
                            count++;
                            Log.d("wallpaper", String.valueOf(model_wallpapersx.getWallpaperId()));
                        }

                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers.notifyDataSetChanged();

                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();
                }

                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                success = true;
            }
        };
        db.addValueEventListener(listenerLoadMore);


    }

    public void loadmoresearched(Query q, final String categoryName) {


        if (dontload) {
            Toast.makeText(c, "End of list", Toast.LENGTH_SHORT).show();

            return;
        }

        db = q.startAt(lastId).limitToFirst(200);


        Log.d("FirebaseHelper", "Loading more");
        ValueEventListener listenerLoadsearchMore = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelperLoadMore", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;

                    Toast.makeText(c, "Loading", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("loadmorewaaallpper", String.valueOf(model_wallpapersx.getWallpaperId()) + "  " + lastId);
                        lastId = model_wallpapersx.getWallpaperId();

                        if (model_wallpapersx.getSubCategory().equalsIgnoreCase(categoryName)) {
                            arrayList_wallpapers.add(model_wallpapersx);
                            lastId = model_wallpapersx.getWallpaperId();
                            count++;
                            Log.d("loadwallpaperfound", String.valueOf(model_wallpapersx.getWallpaperId()));
                        }

                    }
                    if (count == 0 || count == 1) {
                        dontload = true;
                    }

                    if (count > 1)
                        arrayList_wallpapers.remove(arrayList_wallpapers.size() - 1);
                    adapter_gridviewforwallpapers.notifyDataSetChanged();

                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                } else {
                    Toast.makeText(c, "End of the list", Toast.LENGTH_SHORT).show();
                }

                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                success = true;
            }
        };
        db.addValueEventListener(listenerLoadsearchMore);


    }




//    private boolean checkDownload(String docId) {
//
//        return sharedData.getDocument(docId);
//
//    }
}