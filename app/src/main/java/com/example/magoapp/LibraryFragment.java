package com.example.magoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.magoapp.data.Story;
import com.example.magoapp.data.StoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class LibraryFragment extends Fragment {

    private ListView lvStory, lvReading;
    private String idStory, idReading, name, desc, image;
    List<String> keys = new ArrayList<>();
    String currentUser;

    DatabaseReference mLibraryRef, mStoryRef, mReadingRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1, mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //my story list
        mLibraryRef = FirebaseDatabase.getInstance().getReference("Library");
        mStoryRef = FirebaseDatabase.getInstance().getReference("Story");
        mReadingRef = FirebaseDatabase.getInstance().getReference("Reading");

        lvStory = (ListView) getView().findViewById(R.id.lvStory);
        lvReading = (ListView) getView().findViewById(R.id.lvReading);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        lvStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keys.get(position);
                Intent intent = new Intent(getActivity(), activity_story.class);
                intent.putExtra("idStory",idStory);
                ((Container)getActivity()).startActivity(intent);
            }
        });
//        readingStory();
        libStory();
    }

    private void readingStory() {
        ArrayList<Story> array = new ArrayList<Story>();
        StoryAdapter adapter = new StoryAdapter(getActivity(), array);
        lvReading.setAdapter(adapter);
        mReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (currentUser.equals(ds.child("user").getValue(String.class))){
                        idReading = ds.child("idStory").getValue(String.class);

                        mStoryRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    if (idReading.equals(ds.getKey())){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void libStory() {
        ArrayList<Story> array = new ArrayList<Story>();
        StoryAdapter adapter = new StoryAdapter(getActivity(), array);
        lvStory.setAdapter(adapter);
        mLibraryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (currentUser.equals(ds.child("user").getValue(String.class))){
                        idStory = ds.child("idStory").getValue(String.class);

                        mStoryRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    if (idStory.equals(ds.getKey())){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}