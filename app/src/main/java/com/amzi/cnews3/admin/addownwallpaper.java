package com.amzi.cnews3.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amzi.cnews3.R;
import com.amzi.cnews3.model.model_categories;
import com.amzi.cnews3.model.model_wallpapers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class addownwallpaper extends AppCompatActivity {

    ProgressBar pb;
    String[] isPremium = {"Select Premiumship", "Premium", "Unlockable", "Free"};
    String[] isTop = {"Select Trend", "yes", "no"};
    EditText edTags, edwallpaper, edlink, eddownloadlink;
    Button b_choose1, b_choose2, b_upload1, b_upload2;
    model_wallpapers model_wallpapersx;
    Spinner spincat, spinsubcat, spinispremium, spinistop;
    String selectedcat, selectedsubcat, selectedpremium, selectedtop, tags, imgurl, imgurl2;
    ValueEventListener eventListener;
    model_wallpapers model_wallpaperstemp = new model_wallpapers();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> subCategories = new ArrayList<>();
    Button b_add, b_launch;
    private ImageView imv1main, imv1thumb;
    private int PICK_IMAGE_REQUEST = 786;
    private Uri filePath, filePath2;
    private DatabaseReference userDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private boolean isPhotoUploaded1 = false;
    private boolean isPhotoUploaded2 = false;
    private boolean isPhotoChoosen1 = false;
    private boolean isPhotoChoosen2 = false;
    private boolean isWallpaperAdded = false;
    private boolean isurlRetrieved1 = false;
    private boolean isurlRetrieved2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addownwallpaper);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        imv1main = findViewById(R.id.imvmain);
        imv1thumb = findViewById(R.id.imvthumb);

        pb = findViewById(R.id.pb);
        pb.setVisibility(View.GONE);

        edTags = findViewById(R.id.edTags);
        edlink = findViewById(R.id.edlink);
        eddownloadlink = findViewById(R.id.eddownloadlink);
        edwallpaper = findViewById(R.id.ed_wallpaperid);

        b_choose1 = findViewById(R.id.tvChoosethumb);
        b_choose2 = findViewById(R.id.tvchoosemain);

        b_upload1 = findViewById(R.id.tvUploadthumb);
        b_upload2 = findViewById(R.id.tvuploadmain);

        spincat = findViewById(R.id.spinCat);
        spinsubcat = findViewById(R.id.spinSubCat);
        spinispremium = findViewById(R.id.spinIsPremium);
        spinistop = findViewById(R.id.spinIsTop);
        categories.add("Select Category");
        subCategories.add("Select Sub Category");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        b_add = findViewById(R.id.b_add);
        b_launch = findViewById(R.id.b_launch);
        retrieve_Categories();
        retrieve_subCategories();

        ArrayAdapter<String> premiumAdapter = new ArrayAdapter<String>(addownwallpaper.this, android.R.layout.simple_spinner_item, isPremium);
        premiumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinispremium.setAdapter(premiumAdapter);

        ArrayAdapter<String> topAdapter = new ArrayAdapter<String>(addownwallpaper.this, android.R.layout.simple_spinner_item, isTop);
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinistop.setAdapter(topAdapter);

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedcat = categories.get(position);
                model_wallpaperstemp.setCategory(selectedcat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinsubcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedsubcat = subCategories.get(position);
                model_wallpaperstemp.setSubCategory(selectedsubcat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinistop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedtop = isTop[position];
                model_wallpaperstemp.setIsTop(selectedtop);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinispremium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedpremium = isPremium[position];
                model_wallpaperstemp.setIsPremium(selectedpremium);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        b_choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(addownwallpaper.this, "clicek", Toast.LENGTH_SHORT).show();
                chooseImage1();

            }
        });

        b_choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage2();

            }
        });

        b_upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage1();
            }
        });
        b_upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage2();
            }
        });


        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPhotoChoosen1){
                    Toast.makeText(addownwallpaper.this, "please choose left image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPhotoChoosen2){
                    Toast.makeText(addownwallpaper.this, "please choose right image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPhotoChoosen1){
                    Toast.makeText(addownwallpaper.this, "please upload left image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPhotoChoosen1){
                    Toast.makeText(addownwallpaper.this, "please upload right image", Toast.LENGTH_SHORT).show();
                    return;
                }

                addWallpaper();





            }
        });


    }

    private void retrieve_subCategories() {
        Query q = FirebaseDatabase.getInstance().getReference().child("subCategories");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String categoriesx = ds.getValue(String.class);
                        subCategories.add(categoriesx);
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(addownwallpaper.this, android.R.layout.simple_spinner_item, subCategories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinsubcat.setAdapter(dataAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieve_Categories() {
        Query q = FirebaseDatabase.getInstance().getReference().child("categories");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_categories model_categories = ds.getValue(model_categories.class);
                        categories.add(model_categories.getName());
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(addownwallpaper.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spincat.setAdapter(dataAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addWallpaper() {
        String tags = edTags.getText().toString();
        String thumblink = edlink.getText().toString();
        String link = eddownloadlink.getText().toString();
        if (tags.isEmpty()) {
            Toast.makeText(this, "Please Fill tags", Toast.LENGTH_SHORT).show();
            return;
        }

        if (thumblink.isEmpty()) {
            Toast.makeText(this, "Please provide Thumbnail Link", Toast.LENGTH_SHORT).show();
            return;
        }

        if (link.isEmpty()) {
            Toast.makeText(this, "Please provide Wallpaper Link", Toast.LENGTH_SHORT).show();
            return;
        }

        Picasso.get().load(link).into(imv1main);
        Picasso.get().load(thumblink).into(imv1thumb);
        //model_wallpaperstemp.setDownloadlink(link);
        //model_wallpaperstemp.setLink(thumblink);
//        if (!isPhotoUploaded) {
//            Toast.makeText(this, "Upload Photo first", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (!isurlRetrieved) {
//            Toast.makeText(this, "Retrieving Url! Try Again after 5 seconds", Toast.LENGTH_LONG).show();
//            return;
//        }

        if (selectedpremium.equalsIgnoreCase("Select Premiumship")) {
            Toast.makeText(this, "Select Premiumship", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedtop.equalsIgnoreCase("Select Trend")) {
            Toast.makeText(this, "Select Trend", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedcat.equalsIgnoreCase("Select Category")) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedsubcat.equalsIgnoreCase("Select Sub Category")) {
            Toast.makeText(this, "Select Sub Category", Toast.LENGTH_LONG).show();
            return;
        }


        model_wallpaperstemp.setWallpaperId(Long.parseLong(edwallpaper.getText().toString()));
        model_wallpaperstemp.setTags(tags);

        showDialouge();


    }

    private void showDialouge() {

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialouge_data);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTrans)));
        Button b = d.findViewById(R.id.bdadd);
        TextView tvid, tvcat, tvsubcat, tvprem, tvtop, tvtag, tvlink;
        tvid = d.findViewById(R.id.tvId);
        tvcat = d.findViewById(R.id.tvcat);
        tvsubcat = d.findViewById(R.id.tvsubcat);
        tvprem = d.findViewById(R.id.tvpremium);
        tvtop = d.findViewById(R.id.tvtop);
        tvtag = d.findViewById(R.id.tvtag);
        //tvlink = d.findViewById(R.id.tvlink);



        tvid.setText("Wallpaper Id: " + String.valueOf(model_wallpaperstemp.getWallpaperId()));
        tvcat.setText("Category is: " + model_wallpaperstemp.getCategory());
        tvsubcat.setText("Sub Category is: " + model_wallpaperstemp.getSubCategory());
        tvprem.setText("Premiumship is: " + model_wallpaperstemp.getIsPremium());
        tvtag.setText("Tags are: " + model_wallpaperstemp.getTags());
        tvtop.setText("Is Top: " + model_wallpaperstemp.getIsTop());
        // tvlink.setText("Link: "+model_wallpaperstemp.getLink());



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDatabaseReference.child("wallpapers").child(String.valueOf(edwallpaper.getText().toString())).setValue(model_wallpaperstemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(addownwallpaper.this, "wallpaper Added", Toast.LENGTH_SHORT).show();
//                            isPhotoChoosen = false;
//                            isPhotoUploaded = false;
//                            isurlRetrieved = false;
                            imv1main.setImageDrawable(getResources().getDrawable(R.drawable.fullrounded_grey));
                            imv1thumb.setImageDrawable(getResources().getDrawable(R.drawable.fullrounded_grey));
                        } else {

                            Toast.makeText(addownwallpaper.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            task.getException();

                        }
                    }
                });
            }
        });
        d.show();

    }

    private void chooseImage1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
    }

    private void chooseImage2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 222);
    }

    private void uploadImage1() {

        if (!isPhotoChoosen1) {
            Toast.makeText(this, "Please Choose Phtot First!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("wallpapers/" + String.valueOf(edwallpaper.getText().toString()));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(addownwallpaper.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            isPhotoUploaded1 = true;
                            GetDownloadURL1(ref);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(addownwallpaper.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void uploadImage2() {

        if (!isPhotoChoosen2) {
            Toast.makeText(this, "Please Choose Phtot First!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (filePath2 != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("wallpapers/" + String.valueOf(edwallpaper.getText().toString()+"t"));
            ref.putFile(filePath2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(addownwallpaper.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            isPhotoUploaded2 = true;
                            GetDownloadURL2(ref);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(addownwallpaper.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void GetDownloadURL1(StorageReference ref) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imgurl = "https://firebasestorage.googleapis.com" + uri.getEncodedPath() + "?" + uri.getQuery();
                Log.d("imageee", imgurl);
                edlink.setText(imgurl);

                model_wallpaperstemp.setLink(imgurl);
                isurlRetrieved1 = true;
            }


        });
    }

    private void GetDownloadURL2(StorageReference ref) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imgurl2 = "https://firebasestorage.googleapis.com" + uri.getEncodedPath() + "?" + uri.getQuery();
                Log.d("imageee", imgurl2);
                eddownloadlink.setText(imgurl2);
                model_wallpaperstemp.setDownloadlink(imgurl2);
                isurlRetrieved2 = true;
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imv1thumb.setImageBitmap(bitmap);
                isPhotoChoosen1 = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 222 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imv1main.setImageBitmap(bitmap);
                isPhotoChoosen2 = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
