package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUpdate extends AppCompatActivity {

    private TextInputEditText ed_username, ed_birthday, ed_hobbies;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        ed_username = (TextInputEditText) findViewById(R.id.user_name);
        ed_birthday = (TextInputEditText) findViewById(R.id.birthday);

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        getInfo();
    }

    private void getInfo() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (currentUser.equals(ds.getKey())){
                        ed_username.setText(ds.child("nameUser").getValue(String.class));
                        ed_birthday.setText(ds.child("doBUser").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}