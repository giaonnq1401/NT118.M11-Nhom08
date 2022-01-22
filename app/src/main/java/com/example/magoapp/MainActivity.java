package com.example.magoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magoapp.admin.Admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView imgLogo;
    TextView tvMago, tvSlogan, tvSince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        imgLogo = findViewById(R.id.img_logo);
        tvMago = findViewById(R.id.tv_Mago);
        tvSlogan = findViewById(R.id.tv_MakeYourStory);
        tvSince = findViewById(R.id.tv_since);

        imgLogo.setAnimation(topAnim);
        tvMago.setAnimation(topAnim);
        tvSlogan.setAnimation(bottomAnim);
        tvSince.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {nextActivity();}
        }, SPLASH_SCREEN);
    }
    private void nextActivity(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(MainActivity.this, dangnhap.class);
            startActivity(intent);
        }
        else {
            if (user.getEmail().equals("admin.mago@gmail.com")){
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, Container.class);
                startActivity(intent);
            }
        }
        finish();
    }
}