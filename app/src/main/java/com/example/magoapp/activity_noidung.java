package com.example.magoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class activity_noidung extends AppCompatActivity {

    TextView txtTenTruyen, txtNoidung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noidung);

        txtNoidung = findViewById(R.id.Noidung);
        txtTenTruyen = findViewById(R.id.TenTruyen);

        //lay du lieu
        Intent intent = getIntent();
        String tentruyen = intent.getStringExtra(  "tentruyen");
        String noidung = intent.getStringExtra(  "noidung");

        txtTenTruyen.setText(tentruyen);
        txtNoidung.setText(noidung);

        //scroll noi dung truyen
        txtNoidung.setMovementMethod( new ScrollingMovementMethod());

    }
}