package com.example.magoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.magoapp.data.Story;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyStoryFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton fabAdd, fabStory, fabChaper;
    TextView tvAddStory, tvUpdateStory;
    Boolean isAllFabsVisible;
    ListView lvMyStory;
    ArrayAdapter<String> arrayAdapter;

    private DatabaseReference mRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyStoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyStoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyStoryFragment newInstance(String param1, String param2) {
        MyStoryFragment fragment = new MyStoryFragment();
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
        return inflater.inflate(R.layout.fragment_my_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fabAdd = getView().findViewById(R.id.fabAdd);
        fabStory = getView().findViewById(R.id.fabStory);
        fabChaper = getView().findViewById(R.id.fabChapter);


        // Also register the action name text, of all the FABs.
        tvAddStory = getView().findViewById(R.id.add_story_action_text);
        tvUpdateStory = getView().findViewById(R.id.update_story_action_text);

        // texts as GONE
        fabStory.setVisibility(View.GONE);
        fabChaper.setVisibility(View.GONE);
        tvUpdateStory.setVisibility(View.GONE);
        tvAddStory.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false;

        fabAdd.setOnClickListener(this);
        fabStory.setOnClickListener(this);


        //my story list
        mRef = FirebaseDatabase.getInstance().getReference("Story");

        lvMyStory = (ListView) getView().findViewById(R.id.lvStory);

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
    }

    private void myStory(DataSnapshot snapshot) {
        ArrayList<String> storylist = new ArrayList<>();
        if (snapshot.exists()) {

            for (DataSnapshot ds : snapshot.getChildren()) {
                String name = ds.child("sName").getValue(String.class);
                storylist.add(name);
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.my_story_list, R.id.name_story, storylist);
            lvMyStory.setAdapter(adapter);

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fabAdd:
            {
                if (!isAllFabsVisible) {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs VISIBLE.
                    fabStory.show();
                    fabChaper.show();
                    tvAddStory.setVisibility(View.VISIBLE);
                    tvUpdateStory.setVisibility(View.VISIBLE);

                    // make the boolean variable true as
                    // we have set the sub FABs
                    // visibility to GONE
                    isAllFabsVisible = true;
                } else {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs GONE.
                    fabStory.hide();
                    fabChaper.hide();
                    tvAddStory.setVisibility(View.GONE);
                    tvUpdateStory.setVisibility(View.GONE);

                    // make the boolean variable false
                    // as we have set the sub FABs
                    // visibility to GONE
                    isAllFabsVisible = false;
                }
                break;
            }

            case R.id.fabStory:
            {
                startActivity(new Intent(getActivity(), activity_DangBai.class));
                break;
            }

        }
    }
}