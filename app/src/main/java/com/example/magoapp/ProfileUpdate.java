package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.magoapp.data.Chapter;
import com.example.magoapp.data.Story;
import com.example.magoapp.data.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Map;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText ed_username, ed_birthday, ed_zodiac, ed_hobbies, ed_quotes;
    private ImageView img_profile;
    private Button btn_Update;
    private String username, birthday, zodiac, hobbies, quotes;
    private ProgressBar progressBar;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 1;

    StorageReference storageRef;
    DatabaseReference mRef;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        ed_username = (TextInputEditText) findViewById(R.id.user_name);
        ed_birthday = (TextInputEditText) findViewById(R.id.birthday);
        ed_zodiac = (TextInputEditText) findViewById(R.id.zodiac);
        ed_hobbies = (TextInputEditText) findViewById(R.id.hobbies);
        ed_quotes = (TextInputEditText) findViewById(R.id.quotes);
        img_profile = (ImageView) findViewById(R.id.profile_image);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        btn_Update = (Button) findViewById(R.id.btnUpdate);

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        storageRef = FirebaseStorage.getInstance().getReference("Users");
        getInfo();

        ed_birthday.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        btn_Update.setOnClickListener(this);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(img_profile);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 5000);

                    Toast.makeText(ProfileUpdate.this, "Upload Successful", Toast.LENGTH_LONG).show();

                    username = ed_username.getText().toString().trim();
                    birthday= ed_birthday.getText().toString().trim();
                    zodiac = ed_zodiac.getText().toString().trim();
                    hobbies = ed_hobbies.getText().toString().trim();
                    quotes = ed_quotes.getText().toString().trim();

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Users user = new Users(username, birthday, uri.toString(), zodiac, hobbies, quotes);
                            Map<String, Object> postValues = user.toMap();
                            mRef.child(currentUser).updateChildren(postValues);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {}
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void getInfo() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (currentUser.equals(ds.getKey())){
                        if (!ds.child("mImageUrl").getValue(String.class).isEmpty()){
                            Picasso.get().load(ds.child("mImageUrl").getValue(String.class)).placeholder(R.drawable.user__2_).into(img_profile);
                        }
                        ed_username.setText(ds.child("nameUser").getValue(String.class));
                        ed_birthday.setText(ds.child("doBUser").getValue(String.class));
                        ed_zodiac.setText(ds.child("zodiac").getValue(String.class));
                        ed_hobbies.setText(ds.child("hobbies").getValue(String.class));
                        ed_quotes.setText(ds.child("quotes").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.birthday:
                chonngay();
                break;
            case R.id.profile_image:
                openFileChooser();
            case R.id.btnUpdate:
                uploadFile();
        }
    }

    private void chonngay() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i: năm, i1:tháng, i2: ngày
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ed_birthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
}