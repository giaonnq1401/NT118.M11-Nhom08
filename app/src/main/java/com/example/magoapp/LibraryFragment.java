package com.example.magoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.magoapp.data.Library_StoryAdapter;
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
    private String idStory, idReading, name, desc, image, sID;
    List<String> keys = new ArrayList<>();
    List<String> keysReading = new ArrayList<>();
    String currentUser;

    ArrayList<Story> savedStory;
    StoryAdapter adapterSaved;

    ArrayList<Story> arrayReading;
    StoryAdapter adapterReading;

    DatabaseReference mLibraryRef, mStoryRef, mReadingRef;

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

        arrayReading = new ArrayList<Story>();
        adapterReading = new StoryAdapter(getActivity(), arrayReading);
        readingStory();
        lvReading.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keysReading.get(position);
                Intent intent = new Intent(getActivity(), activity_story.class);
                intent.putExtra("idStory",idStory);
                ((Container)getActivity()).startActivity(intent);
            }
        });
        lvReading.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeStory(mReadingRef, position, keysReading, adapterReading);
                return true;
            }
        });

        savedStory = new ArrayList<Story>();
        adapterSaved = new StoryAdapter(getActivity(), savedStory);
        libStory();
        lvStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idStory = keys.get(position);
                Intent intent = new Intent(getActivity(), activity_story.class);
                intent.putExtra("idStory",idStory);
                ((Container)getActivity()).startActivity(intent);
            }
        });
        lvStory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,final int position, long id) {
                removeStory(mLibraryRef, position, keys, adapterSaved);
                return true;
            }
        });
    }

    private void removeStory(DatabaseReference data, int position, List<String> list, StoryAdapter adapter){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setIcon(android.R.drawable.ic_delete).setTitle("Remove the story");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(adapter.getItem(position));

                String key = list.get(position);
                data.child(currentUser).child(key).removeValue();
                adapter.notifyDataSetChanged();

//                items.remove(position);
//                adapter.notifyDataSetChanged();
//                //new code below
//                data.child(currentUser).child(list.get(position)).removeValue();
//                keyList.remove(position);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Canceled"
                        , Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.create().show();
    }

    private void readingStory() {
//        ArrayList<Story> array = new ArrayList<Story>();
//        StoryAdapter adap = new StoryAdapter(getActivity(), array);
        lvReading.setAdapter(adapterReading);
        Query query = mReadingRef.orderByKey().equalTo(currentUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if (snapshot.exists()){
                    mReadingRef.child(currentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap) {
                            for (DataSnapshot ds : snap.getChildren()){
                                if (ds.exists()){
                                    mStoryRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dsnap : snapshot.getChildren()){
                                                if (ds.getKey().equals(dsnap.getKey())){
                                                    keysReading.add(dsnap.getKey());
                                                    name = dsnap.child("sName").getValue(String.class);
                                                    desc = dsnap.child("sDesc").getValue(String.class);
                                                    image = dsnap.child("sImage").getValue(String.class);
                                                    Story newStory = new Story(name, desc, image);
                                                    adapterReading.add(newStory);
                                                    adapterReading.setNotifyOnChange(true);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void libStory() {
//        ArrayList<Story> savedStory = new ArrayList<Story>();
//        Library_StoryAdapter adapter = new Library_StoryAdapter(getActivity(), savedStory);
        lvStory.setAdapter(adapterSaved);

        Query query = mLibraryRef.orderByKey().equalTo(currentUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if (snapshot.exists()){
                    mLibraryRef.child(currentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap) {
                            for (DataSnapshot ds : snap.getChildren()){
                                if (ds.exists()){
//                                    Toast.makeText(getActivity(), ds.getKey(), Toast.LENGTH_SHORT).show();
                                    mStoryRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dsnap : snapshot.getChildren()){
                                                if (ds.getKey().equals(dsnap.getKey())){
                                                    keys.add(dsnap.getKey());

                                                    name = dsnap.child("sName").getValue(String.class);
                                                    desc = dsnap.child("sDesc").getValue(String.class);
                                                    image = dsnap.child("sImage").getValue(String.class);
                                                    Story newStory = new Story(name, desc, image);
                                                    adapterSaved.add(newStory);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}