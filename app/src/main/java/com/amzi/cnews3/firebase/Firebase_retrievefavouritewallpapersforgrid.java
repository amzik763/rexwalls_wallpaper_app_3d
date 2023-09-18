package com.amzi.cnews3.firebase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase_retrievefavouritewallpapersforgrid {

    Query db;
    model_wallpapers model_wallpapersx;
    ArrayList<model_wallpapers> arrayList_wallpapers;
    GridView gridView;
    Context c;
    Dialog d;
    com.creative.dnas.rexwallnew.adapters.adapter_gridviewforwallpapers adapter_gridviewforwallpapers;
     com.creative.dnas.rexwallnew.utility.sharedData sharedData;

    public Firebase_retrievefavouritewallpapersforgrid(Query db, Context context, adapter_gridviewforwallpapers adapter_gridviewforwallpapers, GridView gridView, ArrayList<model_wallpapers> model_wallpapersx) {
        this.db = db;
        this.c = context;
        this.adapter_gridviewforwallpapers = adapter_gridviewforwallpapers;
        this.gridView = gridView;
        this.arrayList_wallpapers = model_wallpapersx;
        sharedData = new sharedData(c);
        d = new Dialog(c);
        this.retrievewallpapers();

    }

    public void showdialog() {
        Dialouge d = new Dialouge(c);
        d.displaytext(c, "No Favourites Added yet!");
    }

    public void showdialogp(){
        d.getWindow().setBackgroundDrawable(new ColorDrawable(c.getResources().getColor(R.color.colorTrans)));
        d.setContentView(R.layout.dialouge_progressload);
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public void dismissdialog(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                d.dismiss();
            }
        },500);
    }

    public void retrievewallpapers() {
        showdialogp();
        Log.d("FirebaseHelper", "start retrieved");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        if (sharedData.getFavourites(String.valueOf(model_wallpapersx.getWallpaperId()))) {
                            arrayList_wallpapers.add(model_wallpapersx);
                            Log.d("book", model_wallpapersx.toString());
                        }

                    }

                    if (arrayList_wallpapers.size() > 0) {
                        adapter_gridviewforwallpapers = new adapter_gridviewforwallpapers(c, arrayList_wallpapers, gridView,0,"n","t");
                        gridView.setAdapter(adapter_gridviewforwallpapers);
                        gridView.setHorizontalScrollBarEnabled(false);
                        gridView.setVerticalScrollBarEnabled(false);
                        gridView.setHorizontalSpacing(8);
                        gridView.setVerticalSpacing(8);
                    } else {
                         showdialog();
                     }
dismissdialog();
                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                }


                db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                db.removeEventListener(this);
                dismissdialog();
            }
        };
        db.addValueEventListener(listener);

    }

//    private boolean checkDownload(String docId) {
//
//        return sharedData.getDocument(docId);
//
//    }
}