package com.example.magoapp;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfileAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public ProfileAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AboutMeFragment aboutMeFragment = new AboutMeFragment();
                return aboutMeFragment;
            case 1:
                MyStoryFragment myStoryFragment = new MyStoryFragment();
                return myStoryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
            return totalTabs;
    }
}
