package com.amzi.cnews3;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.amzi.cnews3.adapters.wallpaper_adapterdownload;
import com.amzi.cnews3.utility.sharedData;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class wallpaperviewdownload extends AppCompatActivity {

    View decorView;
    AdRequest adIRequest = new AdRequest.Builder().build();
    private wallpaper_adapterdownload wallpaper_adapterdownload;
    private ViewPager viewPager;
    private ArrayList<String> pathlist = new ArrayList<>();
    private int currentpos = 0;
    private Dialog dw;
    private Uri furi;
    private File fFile;
    private ImageView imvset, imvshare, imvdelete, imvback;
    private String fName;
//    private InterstitialAd interstitial;
    private sharedData sharedData;
    private boolean isAdloaded = false;
    private boolean isVisible = true;
    private boolean istouched = false;
    private ConstraintLayout constraintLayoutfooter;

    private void hidebars() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            hidebars();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaperviewdownload);
        sharedData = new sharedData(wallpaperviewdownload.this);
        viewPager = findViewById(R.id.viewpagerWallpaperDownloaded);
        currentpos = getIntent().getIntExtra("pos", 0);
        dw = new Dialog(wallpaperviewdownload.this);
        dw.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        dw.setContentView(R.layout.dialouge_progress);
        dw.setCanceledOnTouchOutside(false);
        imvset = findViewById(R.id.imvsetd);
        imvshare = findViewById(R.id.imvshared);
        imvdelete = findViewById(R.id.imvdeleted);
        imvback = findViewById(R.id.imvbackd);
        constraintLayoutfooter = findViewById(R.id.clRight);
        decorView = getWindow().getDecorView();

        if(sharedData.getshowads()) {
            int m = sharedData.increasecounter();
            if (m % 4 == 0 && m != 0) {
                loadinterstitialad();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        displayInterstitial();
                    }
                }, 3500);
                sharedData.resetcounter();
            }
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int i) {
                    if (i == 0) {
                        hidebars();
                    }
                }
            });
        }
        File mydir = getFilesDir();
        File lister = mydir.getAbsoluteFile();

        for (String list : lister.list()) {
            String path = lister.getAbsolutePath() + "/" + list;
            Log.d("pathis:::: ", path + "\n" + lister.list());
            pathlist.add(path);

        }
        try {
            Bitmap b = BitmapFactory.decodeFile(pathlist.get(currentpos));
            imvback.setImageBitmap(b);
            String[] fnames = pathlist.get(currentpos).split("/");
            Log.d("Filenameis: ", fnames[fnames.length - 1]);
            fName = fnames[fnames.length - 1];
            wallpaper_adapterdownload = new wallpaper_adapterdownload(wallpaperviewdownload.this, pathlist);
            viewPager.setAdapter(wallpaper_adapterdownload);
            viewPager.setCurrentItem(currentpos);
        } catch (Exception exc) {

        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                currentpos = position;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap b = BitmapFactory.decodeFile(pathlist.get(position));
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               imvback.setImageBitmap(b);

                           }
                       });
                    }
                },450);

                String[] fnames = pathlist.get(position).split("/");
                Log.d("Filenameis: ", fnames[fnames.length - 1]);
                fName = fnames[fnames.length - 1];


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
     //   viewPager.setPageTransformer(false, new ParallaxTransformer());

        imvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedData.getLink())));
                } catch (android.content.ActivityNotFoundException anfe) {

                } catch (Exception e) {

                }
            }
        });

        imvset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWriteStoragePermissionGranted())
                    showWallpaperDialouge();
            }
        });

        imvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedData sharedData = new sharedData(wallpaperviewdownload.this);
                File myFile = new File(getFilesDir(), fName);
                Log.d("pathis", myFile.getAbsolutePath());
                boolean isDeleted = myFile.delete();
                if (isDeleted) {
                    pathlist.remove(currentpos);
                    String[] s = fName.split("\\.");
                    Log.d(fName, "hhhhh");
                    Log.d(s[0], "hhhhh");
                    Log.d(s[1], "hhhhh");
                    sharedData.removeDownloads(s[0]);
                    Toast.makeText(wallpaperviewdownload.this, "Wallpaper Deleted", Toast.LENGTH_SHORT).show();
                  //  File mydir = getFilesDir();
                    //File lister = mydir.getAbsoluteFile();

//                    for (String list : lister.list()) {
//                        String path = lister.getAbsolutePath() + "/" + list;
//                       // Bitmap myBitmap = BitmapFactory.decodeFile(lister.getAbsolutePath() + "/" + list);
//                        Log.d("pathis: ", path + "\n" + lister.list());
//                        pathlist.add(path);
//
//                    }

                    if (currentpos >= pathlist.size() - 1) {
                        currentpos = pathlist.size() - 1;
                    }

                    if (pathlist.size() == 0) {
                        finishac();
                    } else {
                        Bitmap b = BitmapFactory.decodeFile(pathlist.get(currentpos));
                        imvback.setImageBitmap(b);
                        String[] fnames = pathlist.get(currentpos).split("/");
                        Log.d("Filenameis: ", fnames[fnames.length - 1]);
                        fName = fnames[fnames.length - 1];
                    }
                    if (pathlist.size() == 0) {
                        finishac();
                    } else {
                        wallpaper_adapterdownload = new wallpaper_adapterdownload(wallpaperviewdownload.this, pathlist);
                        viewPager.setAdapter(wallpaper_adapterdownload);
                        viewPager.setCurrentItem(currentpos, true);
                    }

                } else {
                    Toast.makeText(wallpaperviewdownload.this, "Please Try again", Toast.LENGTH_SHORT).show();
                }


            }

            private void finishac() {
                finish();
            }
        });

viewPager.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!istouched) {
            istouched = true;
            if (isVisible) {
                isVisible = false;
                constraintLayoutfooter.setVisibility(View.GONE);
            } else {
                isVisible = true;
                constraintLayoutfooter.setVisibility(View.VISIBLE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    istouched = false;
                }
            },500);

        }
        return false;
    }
});



    }

    private void loadinterstitialad() {
//        interstitial = new InterstitialAd(wallpaperviewdownload.this);
//        interstitial.setAdUnitId(getString(R.string.interstitial_video));
//        interstitial.loadAd(adIRequest);
//        interstitial.setAdListener(new AdListener() {
//                                       @Override
//                                       public void onAdClicked() {
//                                           super.onAdClicked();
//                                       }
//
//                                       @Override
//                                       public void onAdFailedToLoad(int i) {
//                                           super.onAdFailedToLoad(i);
//                                           loadinterstitialad();
//                                           Log.d("adadad","failed");
//                                       }
//
//                                       @Override
//                                       public void onAdLoaded() {
//                                           super.onAdLoaded();
//                                           isAdloaded = true;
//                                           Log.d("adadad","success");
//                                           displayInterstitial();
//
//                                       }
//
//                                       @Override
//                                       public void onAdImpression() {
//                                           super.onAdImpression();
//                                       }
//
//                                       @Override
//                                       public void onAdLeftApplication() {
//                                           super.onAdLeftApplication();
//                                       }
//
//                                       @Override
//                                       public void onAdOpened() {
//                                           super.onAdOpened();
//                                       }
//
//                                       @Override
//                                       public void onAdClosed() {
//                                           super.onAdClosed();
//                                       }
//                                   }
//
//        );
    }

    public void displayInterstitial() {
//         If Interstitial Ads are loaded then show else show nothing.
//        if (sharedData.getshowads()) {
//            if (interstitial.isLoaded()) {
//                interstitial.show();
//            }
//           // loadinterstitialad();
//        }
    }
    private void showWallpaperDialouge() {
        final Dialog dww;
        dww = new Dialog(wallpaperviewdownload.this);
        dww.setContentView(R.layout.dialouge_progress);
        dww.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        dww.setContentView(R.layout.dialouge_wpselect);
        dww.setCanceledOnTouchOutside(true);
        final TextView tvwp1 = dww.findViewById(R.id.tvsetwp);
        TextView tvwp2 = dww.findViewById(R.id.tvsetwp2);
        TextView tvwp3 = dww.findViewById(R.id.tvsetwpl);
        tvwp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dw.show();
                setWallpaper(false);
                if (dww.isShowing())
                    dww.dismiss();
            }
        });

        tvwp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaperUsingSystem();
                if (dww.isShowing())
                    dww.dismiss();
            }
        });

        tvwp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dw.show();
                setWallpaper(true);
                if (dww.isShowing())
                    dww.dismiss();
            }
        });
        dww.show();
    }

    private void setWallpaper(boolean lock) {
        final boolean isLock = lock;
        loadinterstitialad();


        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;

                    imvback.setDrawingCacheEnabled(true);
                    imvback.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    imvback.layout(0, 0, width, height);

                    imvback.buildDrawingCache(true);
                    Bitmap wallpaperBitmap = Bitmap.createBitmap(imvback.getDrawingCache());
                    imvback.setDrawingCacheEnabled(false);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    wallpaperBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    InputStream is = new ByteArrayInputStream(baos.toByteArray());

                    if (isLock)
                        manager.setStream(is, null, true, WallpaperManager.FLAG_LOCK);
                    else
                        manager.setStream(is);

                    Toast.makeText(getApplicationContext(), "Wallpaper Changed", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Cannot Change Wallpaper!", Toast.LENGTH_SHORT).show();
                }
                dw.dismiss();
            }
        }, 200);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.wq);


    }

    private void setWallpaperUsingSystem() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                imvback.setDrawingCacheEnabled(true);
                imvback.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                imvback.layout(0, 0, width, height);

                imvback.buildDrawingCache(true);
                Bitmap wallpaperBitmap = Bitmap.createBitmap(imvback.getDrawingCache());
                imvback.setDrawingCacheEnabled(false);


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                wallpaperBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), wallpaperBitmap, "mine", "mine");
                furi = Uri.parse(path);
                Log.d("path: ", path);
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                // Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER,uri);
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(furi, "image/jpeg");
                intent.putExtra("mimeType", "image/jpeg");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, 656);
                fFile = new File(path);
                Log.d("filepth: ", path + "\n" + fFile.getName());
                if (fFile.exists())
                    if (fFile.delete()) {

                    } else {
                    }

            }
        }, 200);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.wq);


    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisi", "Permission is granted2");
                return true;
            } else {

                Log.v("permisi", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisi", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("xpermisi", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("xpermisi", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    showWallpaperDialouge();

                    //resume tasks needing this permission
                } else {

                }
                break;

            case 3:
                Log.d("xpermisi", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("xpermisi", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                } else {
                }
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       loadinterstitialad();
        if (requestCode == 656) {
            Toast.makeText(this, "wallpaper Applied", Toast.LENGTH_SHORT).show();
            getContentResolver().delete(furi, fFile.getName(), null);

        }
    }

//    public class ParallaxTransformer implements ViewPager.PageTransformer {
//        private static final float MIN_SCALE = 0.75f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//
//            if (position >= -1 && position <= 1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                // view.setAlpha(0f);
//                view.findViewById(R.id.imvWallpaper).setTranslationX(-position * pageWidth / 2);
//
//            } else
//                view.setAlpha(1);
//
//
//        }
//    }
}
