package com.example.magoapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Library_StoryAdapter extends ArrayAdapter<Story> {
    ArrayList<Story> arrayList;
    itemClickListener mItemClickListener;
    public Library_StoryAdapter(Context context, ArrayList<Story> stories){
        super(context, 0, stories);
        this.arrayList = stories;
    }

    public interface itemClickListener {
        void onClickDelete(Story story);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Story story = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.library_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.name_story);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.desc_story);
        ImageView imgStory = (ImageView) convertView.findViewById(R.id.img_story);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);

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

        delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
