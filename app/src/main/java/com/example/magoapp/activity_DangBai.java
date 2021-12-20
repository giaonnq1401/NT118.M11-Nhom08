package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.magoapp.data.Chapter;
import com.example.magoapp.data.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_DangBai extends AppCompatActivity {

    private EditText ed_nameStory, ed_descStory, ed_nameChapter, ed_contentChapter;
    private Button btn_upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_bai);

        ed_nameStory = (EditText) findViewById(R.id.dbTenTruyen);
        ed_descStory = (EditText) findViewById(R.id.storyDesc);
        ed_nameChapter = (EditText) findViewById(R.id.dbChapter);
        ed_contentChapter = (EditText) findViewById(R.id.dbnoidung);
        btn_upload = (Button) findViewById(R.id.btn_dangbai);



        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
            }
        });
    }

    private void onClickPushData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Story");
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
                Toast.makeText(activity_DangBai.this, "Upload successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}