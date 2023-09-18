package com.amzi.cnews3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class wallpaper_adapter extends PagerAdapter {

    com.creative.dnas.rexwallnew.utility.sharedData sharedData;
    Context ctx;
    LayoutInflater layoutInflater;
    ImageView imv;
    TextView tvTitle, tvDescription, tvBrief, tvTimestamp;
    ArrayList<model_wallpapers> array_model_wallpapersx;


    public wallpaper_adapter(Context ctx, ArrayList<model_wallpapers> array_model_wallpapersx) {
        this.ctx = ctx;
        this.array_model_wallpapersx = array_model_wallpapersx;
        sharedData = new sharedData(ctx);

    }


    @Override
    public int getCount() {
        return array_model_wallpapersx.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.model_singlewallpaper, container, false);

        imv = view.findViewById(R.id.imvWallpaper);



        Picasso.get().load(array_model_wallpapersx.get(position).getDownloadlink()).into(imv, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("picasso", "done");

            }

            @Override
            public void onError(Exception e) {
                Log.d("picasso", "not done");

            }
        });


//tvBrief.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if (Objects.equals(sharedData.retrieveLanguage(), "Eng")) {
//            //dialouge = new Dialouge(ctx);
//            //dialouge.showFullArticle(ctx,array_model_wallpapersx.get(position).getDescription());
//        }
//        else if (Objects.equals(sharedData.retrieveLanguage(), "Hin")) {
//          //  dialouge = new Dialouge(ctx);
//            //dialouge.showFullArticle(ctx,array_model_wallpapersx.get(position).getDescriptionH());
//        }
//    }
//});
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }


}
