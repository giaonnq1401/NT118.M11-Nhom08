package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.magoapp.data.Chapter;
import com.example.magoapp.data.Story;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
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

import java.io.IOException;
import java.util.UUID;

public class activity_DangBai extends AppCompatActivity {

    private EditText ed_nameStory, ed_descStory, ed_nameChapter, ed_contentChapter;
    private Button btn_upload;
    private ImageView img_story;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage fStore;
    StorageReference storageRef;

    DatabaseReference mRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_bai);

        img_story = (ImageView) findViewById(R.id.img_story);
        ed_nameStory = (EditText) findViewById(R.id.dbTenTruyen);
        ed_descStory = (EditText) findViewById(R.id.storyDesc);
        ed_nameChapter = (EditText) findViewById(R.id.dbChapter);
        ed_contentChapter = (EditText) findViewById(R.id.dbnoidung);
        btn_upload = (Button) findViewById(R.id.btn_dangbai);


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Story");
        fStore = FirebaseStorage.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageRef.child("story/" + mRef.getKey() + "/avatar.jpg");

        img_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
            }
        });
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                img_story.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Uri filePath, String idStory) {
        //upload image to Firebase Storage
        final StorageReference fileRef = storageRef.child("story/" + idStory + "/avatar.jpg");
        fileRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity_DangBai.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_DangBai.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onClickPushData(){
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String nameStory = ed_nameStory.getText().toString().trim();
        String descStory = ed_descStory.getText().toString().trim();
        String nameChapter = ed_nameChapter.getText().toString().trim();
        String contentChapter = ed_contentChapter.getText().toString().trim();

        Chapter chapter = new Chapter(nameChapter, contentChapter);
        Story story = new Story(nameStory, currentuser, descStory, chapter);

        mRef.push().setValue(story, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                uploadImageToFirebase(filePath, ref.getKey());
                Toast.makeText(activity_DangBai.this, "Upload successfully", Toast.LENGTH_LONG).show();
            }
        });

    }
}