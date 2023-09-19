package com.amzi.cnews3.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amzi.cnews3.R;
import com.amzi.cnews3.model.model_wallpapers;
import com.amzi.cnews3.wallpaperview;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.amzi.cnews3.utility.sharedData;

import java.util.ArrayList;


public class adapter_recycleviewforwallpapers extends RecyclerView.Adapter<adapter_recycleviewforwallpapers.ViewHolder> {

    private ArrayList<model_wallpapers> arraymodel_wallpapers = new ArrayList<>();

    private sharedData sharedData;
    private Context ctx;
    private RecyclerView recyclerViewforbookitems;
    private int images[] = {
            R.drawable.ic_check,
            R.drawable.ic_lock_open,
            R.drawable.ic_lock
    };

    public adapter_recycleviewforwallpapers(final Context ctx, ArrayList<model_wallpapers> arraymodel_wallpapers, RecyclerView recyclerViewforbookitems) {
        this.ctx = ctx;
        this.arraymodel_wallpapers = arraymodel_wallpapers;
        this.recyclerViewforbookitems = recyclerViewforbookitems;
        sharedData = new sharedData(ctx);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.model_wallpapersmall, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //Toast.makeText(ctx, discount+"", Toast.LENGTH_SHORT).show();
        model_wallpapers model_wallpapersx = arraymodel_wallpapers.get(position);

        Log.d("wallpaperid: ", String.valueOf(arraymodel_wallpapers.get(position).getWallpaperId()));
        if (sharedData.getDownload(String.valueOf(arraymodel_wallpapers.get(position).getWallpaperId()))) {
            arraymodel_wallpapers.get(position).setIsDownloaded("yes");

        } else {
            arraymodel_wallpapers.get(position).setIsDownloaded("no");

        }


        Picasso.get().load(arraymodel_wallpapers.get(position).getLink()).resize(200, 320).centerCrop().into(holder.imvWall, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.imvWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wallpaperActivit = new Intent(ctx, wallpaperview.class);
                wallpaperActivit.putParcelableArrayListExtra("data", arraymodel_wallpapers);
                wallpaperActivit.putExtra("position", position);
                //Toast.makeText(ctx, " "+ arraymodel_wallpapers.get(position), Toast.LENGTH_SHORT).show();
                ctx.startActivity(wallpaperActivit);
            }
        });


    }


    @Override
    public int getItemCount() {
        return arraymodel_wallpapers.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvWall;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imvWall = itemView.findViewById(R.id.imvWallpaper);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

    }
}


