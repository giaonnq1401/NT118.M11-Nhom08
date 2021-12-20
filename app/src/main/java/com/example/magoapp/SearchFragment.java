package com.example.magoapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.magoapp.data.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private AutoCompleteTextView mSearchField;
    private ListView mResultList;

    private DatabaseReference mUserDatabase, mStoryDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mStoryDatabase = FirebaseDatabase.getInstance().getReference("Story");

        mSearchField = (AutoCompleteTextView) getView().findViewById(R.id.search_field);
        mResultList = (ListView) getView().findViewById(R.id.result_list);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        //mUserDatabase.addListenerForSingleValueEvent(event);
        mStoryDatabase.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names = new ArrayList<>();
        if (snapshot.exists()){

            for (DataSnapshot ds:snapshot.getChildren()){
                String name = ds.child("sName").getValue(String.class);
                names.add(name);
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, names);
            mSearchField.setAdapter(adapter);

            mSearchField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String name = mSearchField.getText().toString();
                    searchStory(name);
                }
            });
        }else {
            Log.d("Story", "no data found");
        }
    }

    private void searchStory(String name) {

        Query query = mStoryDatabase.orderByChild("sName").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    ArrayList<String> listStory = new ArrayList<>();
                    for (DataSnapshot ds:snapshot.getChildren()){
                        Story story = new Story(ds.child("sName").getValue(String.class), ds.child("sAuthor").getValue(String.class));
                        listStory.add(story.getsName() + story.getsAuthor());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_layout, R.id.name_story, listStory);
                    mResultList.setAdapter(adapter);

                }else {
                    Log.d("Story", "no data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class Story{

        public Story(String sName, String sAuthor) {
            this.sName = sName;
            this.sAuthor = sAuthor;
        }

        public String getsName() {
            return sName;
        }

        public String getsAuthor() {
            return sAuthor;
        }

        public String sName, sAuthor;
    }

}