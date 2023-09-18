package com.amzi.cnews3.utility;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class notificat extends AppCompatActivity {

    DatabaseReference databaseReference;
    Query q;
    String not;
    TextView tvmsg;
    EditText ednot;
    Button bnot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_notificat);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notification");
       // tvmsg = findViewById(R.id.tvmsg);
       // ednot = findViewById(R.id.ednot);
       // bnot = findViewById(R.id.buttonsendnot);

        bnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue(ednot.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            tvmsg.setText("Notification Sent");
                        }
                        else{
                            tvmsg.setText(task.getException().getMessage().toString());
                        }
                    }
                });
            }
        });

    }
}
