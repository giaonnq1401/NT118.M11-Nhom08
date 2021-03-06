package com.example.magoapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.magoapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoryAdapter extends ArrayAdapter<Story> {
    public StoryAdapter(Context context, ArrayList<Story> stories){
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Story story = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_story_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.name_story);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.desc_story);
        ImageView imgStory = (ImageView) convertView.findViewById(R.id.img_story);

        String desc = story.getsDesc();
        String arr[] = desc.split("\\s");
        String description = "";
        if (arr.length > 10){
            for (int i = 0; i < 10; i++){
                description = description + " " + arr[i];
            }
            tvDesc.setText(description + "...");
        }
        else {
            tvDesc.setText(desc);
        }

        tvName.setText(story.getsName());
        Picasso.get().load(story.getsImage()).placeholder(R.drawable.ic_image).fit().centerCrop().into(imgStory);
        return convertView;
    }

}
