package com.amzi.cnews3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amzi.cnews3.model.manager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.amzi.cnews3.utility.sharedData;

public class splash extends AppCompatActivity {

    manager mmanager = new manager();
    sharedData sharedData;
    ImageView imv;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedData = new sharedData(this);
        imv = findViewById(R.id.logo);
        textView = findViewById(R.id.textlogo);
        YoYo.with(Techniques.Shake)
                .duration(1000)
                .repeat(0)
                .playOn(imv);
        YoYo.with(Techniques.SlideInDown)
                .duration(1000)
                .repeat(0)
                .playOn(textView);
        retrievemanager();
    }

    private void retrievemanager() {
        DatabaseReference dbmanager = FirebaseDatabase.getInstance().getReference().child("manager");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {


                    mmanager = dataSnapshot.getValue(manager.class);
                    sharedData.setLink(mmanager.getLink());
                    sharedData.setVersion(mmanager.getVersion());
                    sharedData.setavailable(mmanager.getAvailable());
                    sharedData.setshowads(mmanager.getShowads());


                    if (!sharedData.getavailable()) {
                        showMaintenanceDialouge();
                    } else if (getVersionInfo() < sharedData.getVersion()) {
                        showUpdateDialouge();
                    } else {
                        startac();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbmanager.addValueEventListener(eventListener);
    }


    private void showUpdateDialouge() {

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialouge_update);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        TextView b = d.findViewById(R.id.tvupdate);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedData.getLink())));
                } catch (android.content.ActivityNotFoundException anfe) {

                } catch (Exception e) {

                }
            }
        });

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                startac();
            }

        });
        d.show();


    }

    private void showMaintenanceDialouge() {

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialouge_server);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        d.setCancelable(false);
        TextView b = d.findViewById(R.id.tvexittext);
        ImageView imv = d.findViewById(R.id.imvsetser);
        imv.startAnimation(
                AnimationUtils.loadAnimation(splash.this, R.anim.rotateclock) );
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finishAffinity();
                } catch (android.content.ActivityNotFoundException anfe) {

                } catch (Exception e) {

                }
            }
        });


        d.show();


    }

    private void startac() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent a = new Intent(getApplicationContext(), Home.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                finish();
            }
        }, 500);
    }


    private int getVersionInfo() {
        String versionName = "";
        int versionCode = 1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //   textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));

        return versionCode;
    }
}
