package com.example.magoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.data.Story;
import com.example.magoapp.data.StoryAdapter;
import com.example.magoapp.data.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ListView lvStory;
    private String idStory, name, desc, image;
    List<String> keys = new ArrayList<>();
    private ProgressBar progressBar;


    DatabaseReference mRef;
    // instance for firebase storage and StorageReference
    FirebaseStorage fStore;
    StorageReference storageRef, profileRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRef = FirebaseDatabase.getInstance().getReference("Story");

        progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
        lvStory = (ListView) getView().findViewById(R.id.lvStory);
        lvStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keys.get(position);
                Intent intent = new Intent(getActivity(), activity_story.class);
                intent.putExtra("idStory", idStory);
                ((Container) getActivity()).startActivity(intent);
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();
        libStory();
    }

    private void libStory() {
        ArrayList<Story> array = new ArrayList<Story>();
        StoryAdapter adapter = new StoryAdapter(getActivity(), array);
        lvStory.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        mRef.orderByValue().limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.exists()) {
//                    if (dataSnapshot.child("status").getValue(String.class).equals("1")){
                        keys.add(dataSnapshot.getKey());
                        name = dataSnapshot.child("sName").getValue(String.class);
                        desc = dataSnapshot.child("sDesc").getValue(String.class);
                        image = dataSnapshot.child("sImage").getValue(String.class);
                        Story newStory = new Story(name, desc, image);
                        adapter.add(newStory);
                        progressBar.setVisibility(View.GONE);
//                    }
                }
                else {
                    Toast.makeText(getActivity(), "Can not load data. Please try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });
    }
}