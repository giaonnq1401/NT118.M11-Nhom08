package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.data.Chapter;
import com.example.magoapp.data.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateStory extends AppCompatActivity {

    private String nameStory, nameChapter, contentChapter, idStory;
    private EditText ed_nameChapter, ed_contentChapter;
    private TextView tv_nameStory;
    private Button btn_upload;
    private ProgressBar progressBar;


    DatabaseReference mStoryRef, mChapterRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_story);

        Intent intent = getIntent();
        idStory = intent.getStringExtra(  "idStory");

        tv_nameStory = (TextView) findViewById(R.id.dbTenTruyen);
        ed_nameChapter = (EditText) findViewById(R.id.dbChapter);
        ed_contentChapter = (EditText) findViewById(R.id.dbnoidung);
        btn_upload = (Button) findViewById(R.id.btn_dangbai);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        database = FirebaseDatabase.getInstance();
        mStoryRef = database.getReference("Story");
        mChapterRef = database.getReference("Chapter");

        getStoryName();
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    private void getStoryName(){
        mStoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (idStory.equals(ds.getKey())){
                        tv_nameStory.setText(ds.child("sName").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadFile() {
        nameChapter = ed_nameChapter.getText().toString().trim();
        contentChapter = ed_contentChapter.getText().toString().trim();

        Chapter chapter = new Chapter(nameChapter, contentChapter, idStory);

        mChapterRef.push().setValue(chapter, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(UpdateStory.this, "Upload successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}