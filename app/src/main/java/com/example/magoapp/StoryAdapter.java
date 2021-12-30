package com.example.magoapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StoryAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public StoryAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DescriptionFragment descriptionFragment = new DescriptionFragment();
                return descriptionFragment;
            case 1:
                ChapterFragment chapterFragment = new ChapterFragment();
                return chapterFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
