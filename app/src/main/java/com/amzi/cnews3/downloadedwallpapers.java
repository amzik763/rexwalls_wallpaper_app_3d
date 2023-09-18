package com.amzi.cnews3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amzi.cnews3.utility.Dialouge;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class downloadedwallpapers extends AppCompatActivity {
    private ImageView imv;
    private GridView gv;
    private ArrayList<String> pathlist = new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onresume:","onresumecalled");
        pathlist.clear();
        File mydir = getFilesDir();
        File lister = mydir.getAbsoluteFile();

        for (String list : lister.list()) {
            String path = lister.getAbsolutePath() + "/" + list;
            Log.d("pathis: ", path + "\n" + lister.list());

            pathlist.add(path);
        }
            simpleAdapter = new SimpleAdapter();
            if(pathlist.size()>0) {
                gv.setAdapter(simpleAdapter);
                gv.setHorizontalScrollBarEnabled(false);
                gv.setVerticalScrollBarEnabled(false);
                gv.setHorizontalSpacing(8);
                gv.setVerticalSpacing(8);
            }else {
                finish();
            }


        Log.d("pathis: ", "size:"+pathlist.size());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadedwallpapers);
        gv = findViewById(R.id.gvwallpaperdwn);
        simpleAdapter = new SimpleAdapter();

        File mydir = getFilesDir();
        File lister = mydir.getAbsoluteFile();

        for (String list : lister.list()) {
            String path = lister.getAbsolutePath() + "/" + list;
            Log.d("pathis--: ", path);
            pathlist.add(path);

        }

if(pathlist.size()>0) {
    gv.setAdapter(simpleAdapter);
    gv.setHorizontalScrollBarEnabled(false);
    gv.setVerticalScrollBarEnabled(false);
    gv.setHorizontalSpacing(8);
    gv.setVerticalSpacing(8);
}else {
   showdialog();
}



    }
    public void showdialog(){
        Dialouge d = new Dialouge(downloadedwallpapers.this);
        d.displaytext(downloadedwallpapers.this,"No Wallpaper Downloaded yet!");
    }

    public class SimpleAdapter extends BaseAdapter {
        ImageView imageView;

        @Override
        public int getCount() {
            return pathlist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            final View view;
            if (convertView == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_wallpaperdownloaded, null);
            } else {
                view = convertView;
            }

            imageView = view.findViewById(R.id.imvWallpaperd);
//
            File f = new File(pathlist.get(i));
            Picasso.get().load(f).centerCrop().resize(250, 400).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("picassomessage: ", "can load");

                }

                @Override
                public void onError(Exception e) {
                    Log.d("picassoerrror: ", e.getMessage() + "\n" + e.getLocalizedMessage());
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent a = new Intent(downloadedwallpapers.this, wallpaperviewdownload.class);
                    a.putExtra("pos",i);
                    startActivity(a);
                }
            });

            return view;
        }
    }
}
