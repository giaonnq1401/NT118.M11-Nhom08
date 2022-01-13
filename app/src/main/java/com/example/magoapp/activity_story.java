package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.data.Library;
import com.example.magoapp.data.Reading;
import com.example.magoapp.data.Story;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class activity_story extends AppCompatActivity {

    TextView tv_storyName, tv_storyAuth, tv_storyDesc;
    ImageView imageStory;
    Button btn_Read, btn_Save;

    String idStory, authStory;

    DatabaseReference mStoryRef, mUserRef, mLibraryRef, mReadingRef;
    String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Intent intentGet = getIntent();
        idStory = intentGet.getStringExtra("idStory");

        tv_storyName = (TextView) findViewById(R.id.story_name);
        tv_storyAuth = (TextView) findViewById(R.id.story_author);
        tv_storyDesc = (TextView) findViewById(R.id.storyDesc);
        btn_Read = (Button) findViewById(R.id.btnRead);
        btn_Save = (Button) findViewById(R.id.btn_savetolibrary);
        imageStory = (ImageView) findViewById(R.id.story_image);

        tv_storyDesc.setMovementMethod( new ScrollingMovementMethod());

        mStoryRef = FirebaseDatabase.getInstance().getReference("Story");
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");
        mLibraryRef = FirebaseDatabase.getInstance().getReference("Library");
        mReadingRef = FirebaseDatabase.getInstance().getReference("Reading");

        getStory();

        btn_Read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readingStory();
                Intent intent = new Intent(activity_story.this, Chapter.class);
                intent.putExtra("idStory",idStory);
                startActivity(intent);
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStory();
            }
        });

    }

    //Lưu truyện vào danh sách truyện đang đọc
    private void readingStory() {
        Reading reading = new Reading(idStory, idUser);
        Query query = mReadingRef.orderByChild("user").equalTo(idUser);
        Query checkStory = mReadingRef.orderByChild("idStory").equalTo(idStory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    checkStory.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            if (!ds.exists()){
                                mReadingRef.push().setValue(reading, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {}
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                else {
                    mReadingRef.push().setValue(reading, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Lưu truyện vào thư viện
    private void saveStory() {
        Library library = new Library(idStory, idUser);
        Query query = mLibraryRef.orderByChild("user").equalTo(idUser);
        Query checkStory = mLibraryRef.orderByChild("idStory").equalTo(idStory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    checkStory.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            if (ds.exists()){
                                Toast.makeText(activity_story.this, "This story has been saved to the library before!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                mLibraryRef.push().setValue(library, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(activity_story.this, "Saved", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                else {
                    mLibraryRef.push().setValue(library, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Lấy thông tin của truyện
    private void getStory(){
        mStoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (idStory.equals(ds.getKey())){
                        tv_storyName.setText(ds.child("sName").getValue(String.class));
                        tv_storyDesc.setText(ds.child("sDesc").getValue(String.class));
                        Picasso.get().load(ds.child("sImage").getValue().toString()).into(imageStory);
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