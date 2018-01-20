package com.andy.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andy.free.Jsoup.TvJsoup;
import com.andy.free.adapter.TvContentRecyclerViewAdapter;
import com.andy.free.bean.TvSeries;
import com.andy.free.widget.LoadMoreFooterView;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lau on 2017/11/1.
 */

public class TvActivity extends AppCompatActivity implements OnRefreshListener,OnLoadMoreListener {


    private IRecyclerView mainRecyclerView;
    private Toolbar mToolbar;

    private ArrayList<TvSeries> mTVlist=new ArrayList<>();

    private SlidingTabLayout areaSlidingTab;
    private SlidingTabLayout typeSlidingTab;
    private ViewPager emptyViewPager;
    private ViewPager sourceViewPager;
    private EmptyPageAdapter emptyAdapter;
    private SourcePageAdapter sourceAdapter;

    private ArrayList<Fragment> fragments=new ArrayList<>();
    private ArrayList<Fragment> sourceFragments=new ArrayList<>();

    private TvContentRecyclerViewAdapter mainAdapter;

    private int pageNum=1;
    private String checked_area="";
    private String checked_type="";

    //private String checked_sourceType="全部资源";

    private final String[] tvType={"全部","言情","偶像","都市","战争","悬疑","伦理","惊悚","网络剧","警匪",
            "古装","武侠","神话","历史","动作","谍战","喜剧","抗日","家庭","剧情","励志","农村","科幻","军旅"};

    private final String[] tvTypeKey={"","yanqing","ouxiang","dushi","zhanzheng","xuanyi","lunli","jingsong","wangluoju","jingfei",
    "guzhuang","wuxia","shenhua","lishi","dongzuo","diezhan","xiju","kangri","jiatong","juqing","lizhi","nongcun","kehuan","junlv"};


    private final String[] areas={"全部地区","内地","香港","台湾","韩国","日本","泰国","新加坡","美国","英国","其他"};

    private final String[] areasKey={"","neidi","xianggang","taiwan","hangguo","riben","taiguo","xinjiapo","meiguo","yingguo","qita"};

    //private final String[] sourceTypes={"全部资源","免费","付费"};
    private LoadMoreFooterView loadMoreView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);

        initView();

        initRecyclerView();
    }

    private void initView() {

        Log.d("initView", "initView");
        areaSlidingTab = (SlidingTabLayout) findViewById(R.id.area_slidingTab);
        typeSlidingTab= (SlidingTabLayout) findViewById(R.id.type_slidingTab);
        mainRecyclerView = (IRecyclerView) findViewById(R.id.tv_main_recyclerView);
        emptyViewPager= (ViewPager) findViewById(R.id.empty_viewPager);
        sourceViewPager= (ViewPager) findViewById(R.id.source_viewPager);
        mToolbar = (Toolbar) findViewById(R.id.tv_mainToolbar);

        mToolbar.setTitle("电视剧");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i=0;i<areas.length;i++){
            fragments.add(new Fragment());
        }

        emptyAdapter=new EmptyPageAdapter(getSupportFragmentManager());
        emptyViewPager.setAdapter(emptyAdapter);
        areaSlidingTab.setViewPager(emptyViewPager);


        areaSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Toast.makeText(TvActivity.this, "选择" + areas[position], Toast.LENGTH_SHORT).show();

                checked_area=areasKey[position];
                pageNum=1;
                mTVlist.clear();
                requestData(checked_area, checked_type);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        for (int i=0;i<tvTypeKey.length;i++){
            sourceFragments.add(new Fragment());
        }
        sourceAdapter=new SourcePageAdapter(getSupportFragmentManager());
        sourceViewPager.setAdapter(sourceAdapter);
        typeSlidingTab.setViewPager(sourceViewPager);

        typeSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Toast.makeText(TvActivity.this, "选择" + tvType[position], Toast.LENGTH_SHORT).show();

                checked_type=tvTypeKey[position];

                pageNum=1;
                mTVlist.clear();
                requestData(checked_area, checked_type);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        //获得数据
        requestData(checked_area, checked_type);


    }
    /*
    * 获取数据
    * */
    private void requestData(String areaKey,String tvTypeKey) {

        TvJsoup tvJsoup=new TvJsoup(areaKey,tvTypeKey,pageNum);

        tvJsoup.setRequestListener(new TvJsoup.requestEndListener() {
            @Override
            public void onRequestEnd(Object tvList, boolean suc) {

                List<TvSeries> list= (List<TvSeries>) tvList;
                for (int i=0;i<list.size();i++){

                    Log.d("requestData",list.get(i).toString());
                }

                mTVlist.addAll(list);
                mainRecyclerView.setRefreshing(false);
                mainAdapter.notifyDataSetChanged();

            }
        });

        tvJsoup.start();
    }

    private void initRecyclerView() {

        Log.d("initRecyclerView","initRecyclerView");

        mainAdapter=new TvContentRecyclerViewAdapter(this,mTVlist);
        mainRecyclerView.setIAdapter(mainAdapter);

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        manager.setItemPrefetchEnabled(false);

        mainRecyclerView.setLayoutManager(manager);
        mainAdapter.setOnTvItemClickListener(new TvContentRecyclerViewAdapter.OnTvItemClickListener() {
            @Override
            public void onClickListener(int position) {
                //Toast.makeText(TvActivity.this,"position="+position,Toast.LENGTH_SHORT).show();

                TvSeries series=mTVlist.get(position);

                Intent intent=new Intent(TvActivity.this,TvDetailActivity.class);
                intent.putExtra("obj",series);
                startActivity(intent);
            }
        });

        mainRecyclerView.setOnRefreshListener(this);
        mainRecyclerView.setOnLoadMoreListener(this);

        //设置滑动动画
        // mIRecyclerView.setItemAnimator(new LandingAnimator());

        loadMoreView= (LoadMoreFooterView) mainRecyclerView.getLoadMoreFooterView();       
    }


    @Override
    public void onLoadMore() {

        pageNum=pageNum+1;
        requestData(checked_area,checked_type);

        mainRecyclerView.setRefreshing(true);
        loadMoreView.setStatus(LoadMoreFooterView.Status.LOADING);
    }

    @Override
    public void onRefresh() {

        pageNum=1;
        mTVlist.clear();
        requestData(checked_area,checked_type);
        mainRecyclerView.setRefreshing(true);
    }

    private class EmptyPageAdapter extends FragmentPagerAdapter{

        public EmptyPageAdapter(FragmentManager fm) {
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
            return areas[position];
        }
    }

    private class   SourcePageAdapter extends  FragmentPagerAdapter {
        public SourcePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return sourceFragments.get(position);
        }

        @Override
        public int getCount() {
            return sourceFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tvType[position];
        }
    }
}
