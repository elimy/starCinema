package com.andy.free;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.andy.free.fragment.FilmFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/26.
 */

public class FilmActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private Unbinder mUnbinder;

    private ArrayList<FilmFragment> fragments=new ArrayList<>();
    private final String[] mTitles={"2345影视","爱奇艺","腾讯视频","优酷视频","PP聚力","哔哩哔哩","芒果TV","华数TV","乐视视频"};

    private FilmPagerAdapter mPagerAdapter;

    @BindView(R.id.film_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.filmTab)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.film_viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_film);
        mUnbinder= ButterKnife.bind(this);

        //设置显示toolbar
        mToolbar.setTitle("全网电影");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化Fragment
        initFragments();

        mPagerAdapter=new FilmPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        mSlidingTabLayout.setViewPager(mViewPager);

    }

    /*
    * 根据titles初始化Fragments
    * */
    private void initFragments() {

        for (int i=0;i<mTitles.length;i++){

            FilmFragment fragment=new FilmFragment();
            Bundle bundle=new Bundle();
            bundle.putString("type",mTitles[i]);
            fragment.setArguments(bundle);

            fragments.add(fragment);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /*
    * 电影fragment的pager适配器
    * */
    private class FilmPagerAdapter extends FragmentPagerAdapter {
        public FilmPagerAdapter(FragmentManager fm) {

            super(fm);
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
            return mTitles[position];
        }
    }
}
