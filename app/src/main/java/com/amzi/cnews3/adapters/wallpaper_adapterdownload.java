package com.amzi.cnews3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class wallpaper_adapterdownload extends PagerAdapter{

    sharedData sharedData;
    Context ctx;
    LayoutInflater layoutInflater;
    ImageView imv;
    ArrayList<String> array_Stringx;




    public wallpaper_adapterdownload(Context ctx, ArrayList<String> array_Stringx) {
        this.ctx = ctx;
        this.array_Stringx = array_Stringx;
        sharedData = new sharedData(ctx);
        this.array_Stringx = array_Stringx;



    }


    @Override
    public int getCount() {
        return array_Stringx.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.model_singlewallpaperdownloaded, container, false);


        imv = view.findViewById(R.id.imvWallpaper);
        File f = new File(array_Stringx.get(position));


        Picasso.get().load(f).into(imv, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("picasso", "done");


            }

            @Override
            public void onError(Exception e) {

                Log.d("picasso", "not done");

            }
        });



        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }




}
