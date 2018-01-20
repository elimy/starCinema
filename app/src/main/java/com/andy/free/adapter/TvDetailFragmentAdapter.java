package com.andy.free.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Andy Lau on 2017/11/9.
 */

public class TvDetailFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private String[] tabs;

    public TvDetailFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public TvDetailFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments,String[] tabs) {
        super(fm);
        this.fragments = fragments;
        this.tabs=tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
