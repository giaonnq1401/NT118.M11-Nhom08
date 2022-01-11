package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.magoapp.data.Story;
import com.example.magoapp.data.StoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseStoryUpdate extends AppCompatActivity {

    ListView lvMyStory;
    private String idStory, name, desc, image;
    List<String> keys = new ArrayList<>();
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_story_update);

        //my story list
        mRef = FirebaseDatabase.getInstance().getReference("Story");

        lvMyStory = (ListView) findViewById(R.id.lvStory);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myStory(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.addListenerForSingleValueEvent(event);

        lvMyStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keys.get(position);
                Intent intent = new Intent(ChooseStoryUpdate.this, UpdateStory.class);
                intent.putExtra("idStory",idStory);
                startActivity(intent);
            }
        });
    }

    private void myStory(DataSnapshot snapshot) {
        ArrayList<Story> arrayOfStories = new ArrayList<Story>();

        StoryAdapter adapter = new StoryAdapter(this, arrayOfStories);
        lvMyStory.setAdapter(adapter);
        Query query = mRef.orderByChild("sAuthor").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        keys.add(ds.getKey());
                        name = ds.child("sName").getValue(String.class);
                        desc = ds.child("sDesc").getValue(String.class);
                        image = ds.child("sImage").getValue(String.class);
                        Story newStory = new Story(name, desc, image);
                        adapter.add(newStory);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}