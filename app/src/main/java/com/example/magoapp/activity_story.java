package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_story extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView tv_storyName, tv_storyAuth;

    String idStory, authStory;

    DatabaseReference mStoryRef, mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Description"));
        tabLayout.addTab(tabLayout.newTab().setText("Chapter"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final StoryAdapter adapter = new StoryAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tv_storyName = (TextView) findViewById(R.id.story_name);
        tv_storyAuth = (TextView) findViewById(R.id.story_author);

        mStoryRef = FirebaseDatabase.getInstance().getReference("Story");
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");

        Intent intent = getIntent();
        idStory = intent.getStringExtra("idStory");

        getStory();
    }

    private void getStory(){
        mStoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (idStory.equals(ds.getKey())){
                        tv_storyName.setText(ds.child("sName").getValue(String.class));
                        authStory = ds.child("sAuthor").getValue(String.class);

                        mUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    if (authStory.equals(ds.getKey())){
                                        tv_storyAuth.setText(ds.child("nameUser").getValue(String.class));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}