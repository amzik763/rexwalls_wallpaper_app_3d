package com.amzi.cnews3.firebase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase_retrievewallpapers {

    Query db;
    model_wallpapers model_wallpapersx;
    ArrayList<model_wallpapers> arrayList_wallpapers;
    RecyclerView recyclerView;
    Context c;
    adapter_recycleviewforwallpapers adapter_recycleviewforwallpapersx;
    com.creative.dnas.rexwallnew.utility.sharedData sharedData;
    Dialog d;

    public Firebase_retrievewallpapers(Query db, Context context, adapter_recycleviewforwallpapers adapter_recycleviewforwallpapersx, RecyclerView recyclerView, ArrayList<model_wallpapers> model_wallpapersx) {
        this.db = db;
        this.c = context;
        this.adapter_recycleviewforwallpapersx = adapter_recycleviewforwallpapersx;
        this.recyclerView = recyclerView;
        this.arrayList_wallpapers = model_wallpapersx;
        sharedData = new sharedData(c);
        d = new Dialog(c);
        this.retrievewallpapers();

    }

    public void showdialog(){
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
        showdialog();
        Log.d("FirebaseHelper", "start retrieved");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_wallpapersx = ds.getValue(model_wallpapers.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        arrayList_wallpapers.add(model_wallpapersx);

                        Log.d("book", model_wallpapersx.toString());


                    }
                    adapter_recycleviewforwallpapersx = new adapter_recycleviewforwallpapers(c, arrayList_wallpapers, recyclerView);
                    recyclerView.setAdapter(adapter_recycleviewforwallpapersx);
                    dismissdialog();
                    // wallpapers_viewPager.setPageTransformer(true,new DepthPageTransformer());
                }
                db.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dismissdialog();
                db.removeEventListener(this);
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