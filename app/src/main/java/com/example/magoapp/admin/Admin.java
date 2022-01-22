package com.example.magoapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.magoapp.R;
import com.example.magoapp.dangnhap;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    ImageView new_story, new_chapter, manage_user, mago, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        new_chapter = (ImageView) findViewById(R.id.imgNewChapter);
        new_story = (ImageView) findViewById(R.id.imgNewStory);
        manage_user = (ImageView) findViewById(R.id.imgUser);
        mago = (ImageView) findViewById(R.id.imgMago);
        logout = (ImageView) findViewById(R.id.imgLogout);

        new_story.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, dangnhap.class));
                break;
            case R.id.imgNewStory:
                startActivity(new Intent(this, Admin_NewStory.class));
                break;
        }
    }
}