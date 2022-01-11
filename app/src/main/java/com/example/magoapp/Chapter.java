package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.magoapp.data.Story;
import com.example.magoapp.data.StoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chapter extends AppCompatActivity {

    DatabaseReference mRef;
    String idStory, name, idChapter;
    private ListView lvChapter;
    List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        lvChapter = (ListView) findViewById(R.id.lvChapter);
        Intent intentGet = getIntent();
        idStory = intentGet.getStringExtra("idStory");

        mRef = FirebaseDatabase.getInstance().getReference("Chapter");
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getChapter(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.addListenerForSingleValueEvent(event);

        lvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idChapter = keys.get(position);
                Intent intent = new Intent(Chapter.this, activity_noidung.class);
                intent.putExtra("idChapter",idChapter);
                startActivity(intent);
            }
        });
    }

    private void getChapter(DataSnapshot snapshot) {
        ArrayList<String> chapterlist= new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                if (idStory.equals(ds.child("idStory").getValue(String.class))){
                    keys.add(ds.getKey());
                    name = ds.child("nameChapter").getValue(String.class);
                    chapterlist.add(name);
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(this,R.layout.item_chapter, R.id.tvchapter, chapterlist);
            lvChapter.setAdapter(adapter);

        }

    }
}