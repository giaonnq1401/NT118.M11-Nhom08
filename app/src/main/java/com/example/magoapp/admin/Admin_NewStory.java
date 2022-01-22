package com.example.magoapp.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.magoapp.R;
import com.example.magoapp.data.Story;
import com.example.magoapp.data.StoryAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_NewStory extends AppCompatActivity {

    ListView lv_newStory;
    private String idStory, name, desc, image;
    List<String> keys = new ArrayList<>();
    private ProgressBar progressBar;

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_story);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        lv_newStory = (ListView) findViewById(R.id.lv_admin_story);

        mRef = FirebaseDatabase.getInstance().getReference("Story");
        libStory();
    }

    private void libStory() {
        ArrayList<Story> array = new ArrayList<Story>();
        StoryAdapter adapter = new StoryAdapter(this, array);
        lv_newStory.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        if (ds.child("status").getValue(String.class).equals("0")){
                            keys.add(ds.getKey());
                            name = ds.child("sName").getValue(String.class);
                            desc = ds.child("sDesc").getValue(String.class);
                            image = ds.child("sImage").getValue(String.class);
                            Story newStory = new Story(name, desc, image);
                            adapter.add(newStory);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}