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
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.data.Story;
import com.example.magoapp.data.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private String idStory, name, desc;
    List<String> keys = new ArrayList<>();

    DatabaseReference mRef;
    // instance for firebase storage and StorageReference
    FirebaseStorage fStore;
    StorageReference storageRef, profileRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        //my story list
        mRef = FirebaseDatabase.getInstance().getReference("Story");

        lvStory = (ListView) getView().findViewById(R.id.lvStory);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                libStory(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.addListenerForSingleValueEvent(event);

        lvStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keys.get(position);
                Intent intent = new Intent(getActivity(), activity_story.class);
                intent.putExtra("idStory",idStory);
                ((Container)getActivity()).startActivity(intent);
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();
        profileRef = storageRef.child("story/" + mRef.getKey() + "/avatar.jpg");

    }


    private void libStory(DataSnapshot snapshot) {
        ArrayList<String> storylist_name = new ArrayList<>();
//        ArrayList<String> storylist_desc = new ArrayList<>();
        if (snapshot.exists()) {

            for (DataSnapshot ds : snapshot.getChildren()) {
                keys.add(ds.getKey());
                name = ds.child("sName").getValue(String.class);
                storylist_name.add(name);
//                desc = ds.child("sDesc").getValue(String.class);
//                storylist_desc.add(desc);
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.my_story_list, R.id.name_story, storylist_name);
            lvStory.setAdapter(adapter);
//            ArrayAdapter desc_adap = new ArrayAdapter(getActivity(),R.layout.my_story_list, R.id.desc_story, storylist_desc);
//            lvStory.setAdapter(desc_adap);
        }
    }

}