package com.amzi.cnews3.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.amzi.cnews3.R;
import com.amzi.cnews3.model.model_wallpapers;
import com.amzi.cnews3.utility.sharedData;
import com.amzi.cnews3.wallpaperview;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_gridviewforsimilarwallpapers extends BaseAdapter {


    Context ctx;
    ArrayList<model_wallpapers> model_wallpaperss;
    GridView gridView;
    ImageView imvWallpaper;
    sharedData sharedData;


    public adapter_gridviewforsimilarwallpapers(final Context ctx, ArrayList<model_wallpapers> model_wallpaperss, GridView gridView) {
        this.ctx = ctx;
        this.gridView = gridView;
        this.model_wallpaperss = model_wallpaperss;
        sharedData = new sharedData(ctx);

    }


    @Override
    public int getCount() {

        return model_wallpaperss.size();
    }

    @Override
    public Object getItem(int position) {
        return model_wallpaperss.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_wallpapermedium, null);
        } else {
            view = convertView;
        }

        imvWallpaper = view.findViewById(R.id.imvWallpaperm);



        Log.d("checkpremium", model_wallpaperss.get(position).getIsPremium());

        Picasso.get().load(model_wallpaperss.get(position).getLink()).resize(220, 320).centerCrop().into(imvWallpaper, new Callback() {
            @Override
            public void onSuccess() {
                // shimmerFrameLayout2.stopShimmerAnimation();
            }

            @Override
            public void onError(Exception e) {
                // Toast.makeText(getApplicationContext(),"oor Network",Toast.LENGTH_LONG).show();
            }
        });

        if (sharedData.getDownload(String.valueOf(model_wallpaperss.get(position).getWallpaperId()))) {
            model_wallpaperss.get(position).setIsDownloaded("yes");
        } else
            model_wallpaperss.get(position).setIsDownloaded("no");


        imvWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(ctx, "size: " + model_wallpaperss.size(), Toast.LENGTH_SHORT).show();
                Intent wallpaperActivit = new Intent(ctx, wallpaperview.class);
                wallpaperActivit.putParcelableArrayListExtra("data", model_wallpaperss);
                wallpaperActivit.putExtra("position", position);

                //Toast.makeText(ctx, " "+ arraymodel_wallpapers.get(position), Toast.LENGTH_SHORT).show();
                ctx.startActivity(wallpaperActivit);
            }
        });


        return view;
    }


}
