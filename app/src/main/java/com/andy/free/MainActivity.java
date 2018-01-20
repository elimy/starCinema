package com.andy.free;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.andy.free.adapter.MainViewPagerAdapter;
import com.andy.free.fragment.MainFragment;
import com.andy.free.fragment.MeFragment;
import com.andy.free.fragment.SaleFragment;
import com.andy.free.fragment.SearchFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_viewPager)
    ViewPager mViewPager;

    @BindView(R.id.radio_main)
    RadioButton mMainRadio;
    @BindView(R.id.radio_search)
    RadioButton mSearchRadio;
    @BindView(R.id.radio_sale)
    RadioButton mSaleRadio;
    @BindView(R.id.radio_me)
    RadioButton mMeRadio;

    ArrayList<Fragment> fragments;

    private Unbinder mUnbinder;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder= ButterKnife.bind(this);

        initFragment();

        mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mainViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mMainRadio.setOnClickListener(this);
        mSearchRadio.setOnClickListener(this);
        mSaleRadio.setOnClickListener(this);
        mMeRadio.setOnClickListener(this);

    }

    private void initFragment() {
        fragments=new ArrayList<>();
        MainFragment mainFragment=new MainFragment();
        SearchFragment searchFragment=new SearchFragment();
        SaleFragment saleFragment=new SaleFragment();
        MeFragment meFragment=new MeFragment();

        fragments.add(mainFragment);
        fragments.add(searchFragment);
        fragments.add(saleFragment);
        fragments.add(meFragment);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        resetCheckedState();

        switch (position){
            case 0:
                mMainRadio.setChecked(true);
                break;
            case 1:
                mSearchRadio.setChecked(true);
                break;
            case 2:
                mSaleRadio.setChecked(true);
                break;
            case 3:
                mMeRadio.setChecked(true);
                break;
            default:
                break;
        }

    }

    private void resetCheckedState() {
        mMainRadio.setChecked(false);
        mSearchRadio.setChecked(false);
        mSaleRadio.setChecked(false);
        mMeRadio.setChecked(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_main:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.radio_search:
                mViewPager.setCurrentItem(1);

                break;
            case R.id.radio_sale:
                mViewPager.setCurrentItem(2);

                break;
            case R.id.radio_me:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }
}
