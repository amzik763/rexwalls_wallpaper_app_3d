package com.amzi.cnews3.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amzi.cnews3.R;


public class Dialouge extends Dialog {

    Dialouge m_dialouge;
    Context m_context;
    Button b_retry,b_update,b_go;
    ImageView imageView;
    String imageLink;
    TextView tv;
    prerequisites prerequisites;



    public Dialouge(Context ctx) {
        super(ctx);
        this.m_context = ctx;
    }


//
//    public void showPopUp(final Context ctx) {
//        prerequisites = new prerequisites();
//        m_dialouge = new Dialouge(ctx);
//        m_dialouge.setContentView(R.layout.dialouge_no_network);
//        b_retry = m_dialouge.findViewById(R.id.b_retrynetwork);
//        b_retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (prerequisites.checknetwork(ctx)) {
//                    m_dialouge.dismiss();
//
//                } else {
//                    Toast.makeText(ctx, "No Network!", Toast.LENGTH_SHORT).show();
//                    m_dialouge.dismiss();
//                }
//
//            }
//        });
//        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        m_dialouge.setCanceledOnTouchOutside(true);
//        m_dialouge.setCancelable(true);
//        m_dialouge.show();
//
//    }

    public void simpleProgress(Context ctx){
        m_dialouge = new Dialouge(ctx);
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(ctx.getResources().getColor(R.color.colorTrans)));
        m_dialouge.setContentView(R.layout.dialouge_progress);
        m_dialouge.setCanceledOnTouchOutside(true);
        m_dialouge.show();
    }

    public void simpleLoading(Context ctx){
        m_dialouge = new Dialouge(ctx);
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(ctx.getResources().getColor(R.color.colorTrans)));
        m_dialouge.setContentView(R.layout.dialouge_progressload);
        m_dialouge.setCanceledOnTouchOutside(false);
        m_dialouge.show();
    }

    public void displaytext(Context ctx,String message){
        m_dialouge = new Dialouge(ctx);

        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(ctx.getResources().getColor(R.color.colorTrans)));
        m_dialouge.setContentView(R.layout.dialouge_text);
        TextView tv = m_dialouge.findViewById(R.id.tvdisplaytext);
        tv.setText(message);
        m_dialouge.setCanceledOnTouchOutside(true);
        m_dialouge.show();
    }

//    public void showUpdate(final Context ctx) {
//        prerequisites = new prerequisites();
//        m_dialouge = new Dialouge(ctx);
//        m_dialouge.setContentView(R.layout.dialouge_update);
//        b_update = m_dialouge.findViewById(R.id.b_update);
//        b_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (prerequisites.checknetwork(ctx)) {
//                   // m_dialouge.dismiss();
//
//
//                } else {
//                    //ZToast.makeText(ctx, "No Network!", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        m_dialouge.setCanceledOnTouchOutside(true);
//        m_dialouge.setCancelable(true);
//        m_dialouge.show();
//
//    }
//    public void showImage(final Context ctx, String imageLink) {
//        m_dialouge = new Dialouge(ctx);
//        m_dialouge.setContentView(R.layout.dialouge_image);
//        imageView = m_dialouge.findViewById(R.id.imvdialouge);
//        Picasso.get().load(imageLink).into(imageView);
//        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        m_dialouge.show();
//
//    }
//    public void showFullArticle(final Context ctx, String news) {
//        m_dialouge = new Dialouge(ctx);
//        m_dialouge.setContentView(R.layout.dialouge_full_article);
//        tv = m_dialouge.findViewById(R.id.tvArticle);
//        tv.setText(news);
//        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        m_dialouge.show();
//
//    }
//    public void showAutoStart(final Context ctx, String news) {
//        m_dialouge = new Dialouge(ctx);
//        m_dialouge.setContentView(R.layout.dialouge_autostat);
//        b_go = m_dialouge.findViewById(R.id.b_GO);
//        b_go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                sharedData sharedData = new sharedData(ctx);
//                if(AutoStartPermissionHelper.getInstance().getAutoStartPermission(ctx)){
//                    sharedData.updateAutostart("Yes");
//                    m_dialouge.dismiss();
//                }
//            }
//        });
//        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        m_dialouge.show();
//
//    }
}
