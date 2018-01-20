package com.andy.free.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/18.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
