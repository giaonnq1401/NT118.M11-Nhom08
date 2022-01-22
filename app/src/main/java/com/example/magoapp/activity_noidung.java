package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_noidung extends AppCompatActivity {

    TextView txtTenTruyen, txtNoidung;
    String idChapter;
    DatabaseReference mRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noidung);

        txtNoidung = findViewById(R.id.Noidung);
        txtTenTruyen = findViewById(R.id.TenTruyen);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //lay du lieu
        Intent intent = getIntent();
        idChapter = intent.getStringExtra(  "idChapter");

        //scroll noi dung truyen
        txtNoidung.setMovementMethod( new ScrollingMovementMethod());

        mRef = FirebaseDatabase.getInstance().getReference("Chapter");
        getContent();

    }

    private void getContent() {
        progressBar.setVisibility(View.VISIBLE);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (idChapter.equals(ds.getKey())){
                        txtTenTruyen.setText(ds.child("nameChapter").getValue(String.class));
                        String content = ds.child("contentChapter").getValue().toString().replace("\\n", "\n");
                        txtNoidung.setText(content);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}