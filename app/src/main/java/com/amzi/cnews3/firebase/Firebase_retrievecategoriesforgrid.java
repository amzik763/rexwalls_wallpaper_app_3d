package com.amzi.cnews3.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import com.amzi.cnews3.model.model_categories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.amzi.cnews3.adapters.adapter_gridviewforcategories;
import com.amzi.cnews3.utility.sharedData;
import com.amzi.cnews3.model.model_categories;

import java.util.ArrayList;

public class Firebase_retrievecategoriesforgrid {

    Query db;
    model_categories model_categoriesx;

    ArrayList<model_categories> arrayList_categories;

    GridView gridView;

    Context c;

    adapter_gridviewforcategories adapter_gridviewforcategories;

    com.amzi.cnews3.utility.sharedData sharedData;

    public Firebase_retrievecategoriesforgrid(Query db, Context context, adapter_gridviewforcategories adapter_gridviewforcategories, GridView gridView, ArrayList<model_categories> model_categoriesx) {
        this.db = db;
        this.c = context;
        this.adapter_gridviewforcategories = adapter_gridviewforcategories;
        this.gridView = gridView;
        this.arrayList_categories = model_categoriesx;
        sharedData = new sharedData(c);
        this.retrievecategories();
    }


    public void retrievecategories() {
        Log.d("FirebaseHelper", "start retrieved");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_categoriesx = ds.getValue(model_categories.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        arrayList_categories.add(model_categoriesx);


                        Log.d("categores", model_categoriesx.getName());


                    }
                    adapter_gridviewforcategories = new adapter_gridviewforcategories(c, arrayList_categories, gridView);
                    gridView.setAdapter(adapter_gridviewforcategories);
                    gridView.setHorizontalScrollBarEnabled(false);
                    gridView.setVerticalScrollBarEnabled(false);
                    gridView.setHorizontalSpacing(8);
                    gridView.setVerticalSpacing(8);
                    // categories_viewPager.setPageTransformer(true,new DepthPageTransformer());
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

//    private boolean checkDownload(String docId) {
//
//        return sharedData.getDocument(docId);
//
//    }
}