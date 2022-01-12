package com.example.magoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutMeFragment extends Fragment {

    private TextView tv_zodiac, tv_hobbies, tv_quotes;
    DatabaseReference mRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public AboutMeFragment() {
        // Required empty public constructor
    }

    public static AboutMeFragment newInstance(String param1, String param2) {
        AboutMeFragment fragment = new AboutMeFragment();
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
        return inflater.inflate(R.layout.fragment_about_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_zodiac = (TextView) getView().findViewById(R.id.tv_zodiac);
        tv_hobbies = (TextView) getView().findViewById(R.id.tv_hobbies);
        tv_quotes = (TextView) getView().findViewById(R.id.tv_quotes);

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        getInfo();
    }

    private void getInfo() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (currentUser.equals(ds.getKey())) {
                        tv_zodiac.setText(ds.child("zodiac").getValue(String.class));
                        tv_hobbies.setText(ds.child("hobbies").getValue(String.class));
                        tv_quotes.setText(ds.child("quotes").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}