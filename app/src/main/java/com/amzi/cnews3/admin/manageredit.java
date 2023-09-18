package com.amzi.cnews3.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manageredit extends AppCompatActivity {

    private EditText edavail,showads,edlink,edversion;
    private Button bshow,bupdate;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageredit);
        edavail = findViewById(R.id.edisavailable);
        showads = findViewById(R.id.edshowads);
        edlink = findViewById(R.id.edlink);
        edversion = findViewById(R.id.edversion);

        bupdate = findViewById(R.id.bsubmitmanager);
        bshow = findViewById(R.id.bshowmanager);

        db = FirebaseDatabase.getInstance().getReference().child("manager");

        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){


                        manager managerx = dataSnapshot.getValue(manager.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        edavail.setText(managerx.getAvailable().toString());
                        showads.setText(managerx.getShowads().toString());
                        edlink.setText(managerx.getLink().toString());
                        edversion.setText(String.valueOf(managerx.getVersion()));

                        db.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                db.removeEventListener(this);

            }


        };

        db.addValueEventListener(eventListener);

        bshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addValueEventListener(eventListener);
            }
        });

        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager managerx = new manager();
                managerx.setAvailable(edavail.getText().toString());
                managerx.setLink(edlink.getText().toString());
                managerx.setShowads(showads.getText().toString());
                managerx.setVersion(Integer.parseInt(String.valueOf(edversion.getText())));

                db.setValue(managerx).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(manageredit.this, "manager updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
