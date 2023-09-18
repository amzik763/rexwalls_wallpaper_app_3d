package com.amzi.cnews3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.amzi.cnews3.R;
import com.amzi.cnews3.categorybasedview;
import com.amzi.cnews3.model.model_categories;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_gridviewforcategories extends BaseAdapter {


    Context ctx;
    ArrayList<model_categories> model_categoriess;
    GridView gridView;
    ConstraintLayout constraintLayoutItem;
    ImageView imvcart;
    ImageView imvWallpaper;
    TextView txvcategoryname;
    TextView txvproductprice;
    TextView txvproductoriginalprice;
    TextView txvproductdiscount;
    TextView txvproductcategory;



    public adapter_gridviewforcategories(final Context ctx, ArrayList<model_categories> model_categoriess, GridView gridView) {
        this.ctx = ctx;
        this.gridView = gridView;
        this.model_categoriess = model_categoriess;

    }


    @Override
    public int getCount() {

        return model_categoriess.size();
    }

    @Override
    public Object getItem(int position) {
        return model_categoriess.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_categories, null);
        } else {
            view = convertView;
        }

        imvWallpaper = view.findViewById(R.id.imvCategories);
        txvcategoryname = view.findViewById(R.id.tvcategory);
        txvcategoryname.setText(model_categoriess.get(position).getName());

        imvWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cbw = new Intent(ctx, categorybasedview.class);
                cbw.putExtra("name",model_categoriess.get(position).getName());
                cbw.putExtra("type","showOne");
                ctx.startActivity(cbw);
            }
        });

        Picasso.get().load(model_categoriess.get(position).getDisplaylink()).resize(320,220).centerCrop().into(imvWallpaper, new Callback() {
            @Override
            public void onSuccess() {
                // shimmerFrameLayout2.stopShimmerAnimation();
            }

            @Override
            public void onError(Exception e) {
                // Toast.makeText(getApplicationContext(),"oor Network",Toast.LENGTH_LONG).show();
            }
        });




        return view;
    }


}
