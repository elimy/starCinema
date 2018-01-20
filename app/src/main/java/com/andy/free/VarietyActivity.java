package com.andy.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andy.free.Jsoup.VarietyJsoup;
import com.andy.free.adapter.VarietyRecyclerViewAdapter;
import com.andy.free.bean.Variety;
import com.andy.free.widget.LoadMoreFooterView;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public class VarietyActivity extends AppCompatActivity implements OnRefreshListener,OnLoadMoreListener {

    private IRecyclerView mIRecyclerView;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SlidingTabLayout areaSlidingTab;
    private LoadMoreFooterView loadMoreFooterView;

/*    private final String[] varietyArea={"全部","大陆","香港","台湾","韩国","日本","欧美"};
    private final String[] varietyAreaKey={"all","10","15","11","12","13","14"};*/

    private final String[] varietyArea={"全部","大陆","港台","日韩","欧美","其他"};
    private final String[] varietyAreaKey={"","dqdalu","dqgangtai","dqrihan","dqoumei","dqqita"};

    private int pageNum=1;
    private String checked_area="all";
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private EmptyAdapter emptyAdapter;
    private ArrayList<Variety> mVarietyList=new ArrayList<>();
    private VarietyRecyclerViewAdapter varietyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_variety);

        //初始化布局
        initView();

        //初始化recyclerView
        initRecyclerView();
    }

    private void initView() {

        mToolbar= (Toolbar) findViewById(R.id.variety_toolbar);
        mIRecyclerView= (IRecyclerView) findViewById(R.id.variety_IrecyclerView);
        mViewPager= (ViewPager) findViewById(R.id.variety_empty_viewPager);
        areaSlidingTab= (SlidingTabLayout) findViewById(R.id.variety_area_slidingTab);

        mToolbar.setTitle("综艺");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化fragments
        for (int i=0;i<varietyArea.length;i++){
            fragments.add(new Fragment());
        }

        //viewPager的适配器
        emptyAdapter=new EmptyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(emptyAdapter);
        areaSlidingTab.setViewPager(mViewPager);

        areaSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Toast.makeText(VarietyActivity.this, "选择" + varietyArea[position], Toast.LENGTH_SHORT).show();

                checked_area=varietyAreaKey[position];
                pageNum=1;
                mVarietyList.clear();
                requestData(checked_area);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        requestData(checked_area);
    }

    /*
    * 获取对应的综艺节目数据
    * */
    private void requestData(String checked_area) {

        VarietyJsoup jsoup=new VarietyJsoup(checked_area,pageNum);
        jsoup.setRequestListener(new VarietyJsoup.requestEndListener() {
            @Override
            public void onRequestEnd(Object varietyList, boolean suc) {

                List<Variety> list= (List<Variety>) varietyList;
                for (int i=0;i<list.size();i++){

                    Log.d("requestData",list.get(i).toString());
                }

                mVarietyList.addAll(list);
                mIRecyclerView.setRefreshing(false);

                varietyAdapter.notifyDataSetChanged();
            }
        });

        jsoup.start();

    }

    private void initRecyclerView() {

        varietyAdapter=new VarietyRecyclerViewAdapter(this,mVarietyList);
        mIRecyclerView.setIAdapter(varietyAdapter);

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        manager.setItemPrefetchEnabled(false);

        mIRecyclerView.setLayoutManager(manager);

        varietyAdapter.setOnItemClickListener(new VarietyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos, String descUrl) {

                Variety variety=mVarietyList.get(pos);

                Intent intent=new Intent(VarietyActivity.this,VarietyDetailActivity.class);
                intent.putExtra("variety",variety);

                startActivity(intent);
            }
        });

        mIRecyclerView.setOnRefreshListener(this);
        mIRecyclerView.setOnLoadMoreListener(this);

        loadMoreFooterView= (LoadMoreFooterView) mIRecyclerView.getLoadMoreFooterView();
    }

    @Override
    public void onLoadMore() {
        pageNum=pageNum+1;
        requestData(checked_area);

        mIRecyclerView.setRefreshing(true);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
    }

    @Override
    public void onRefresh() {
        pageNum=1;
        mVarietyList.clear();
        requestData(checked_area);
        mIRecyclerView.setRefreshing(false);
    }


    /*
    * 选项卡与fragment的适配器
    * */
    private class EmptyAdapter extends FragmentPagerAdapter {

        public EmptyAdapter(FragmentManager fm) {
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
            return varietyArea[position];
        }
    }
}
