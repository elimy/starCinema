package com.andy.free.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andy.free.Jsoup.IqiyiJsoupThread;
import com.andy.free.Jsoup.OverAllJsoup;
import com.andy.free.R;
import com.andy.free.X5PlayWebActivity;
import com.andy.free.adapter.FilmFragmentAdapter;
import com.andy.free.adapter.MainRecyclerViewAdapter;
import com.andy.free.bean.FullMovie;
import com.andy.free.bean.Movie;
import com.andy.free.utils.ConstantUtil;
import com.andy.free.widget.LoadMoreFooterView;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/26.
 */
public class FilmFragment extends Fragment implements OnRefreshListener,OnLoadMoreListener {

    private IRecyclerView mIRecyclerView;
    
    private View rootView;
    private String filmType;
    private FilmFragmentAdapter filmFragmentAdapter;
    private String url;
    private int pageNum=1;
    private Handler handler;
    private ArrayList<Object> mList=new ArrayList<>();
    private LoadMoreFooterView loadMoreView;

    public FilmFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null==rootView){
            rootView=inflater.inflate(R.layout.fragment_film,container,false);
        }
        mIRecyclerView= (IRecyclerView) rootView.findViewById(R.id.film_Irec);
        initView();

        return rootView;

    }

    private void initView() {

        if (null!=getArguments()){
            filmType=getArguments().getString("type");
        }

        //获取数据
        requestData();

        //初始化IRecyclerView
        initRecyclerView();


    }

    private void initRecyclerView() {
        
        //设置适配器
        //Log.d("initRecyclerView",""+mList.size());

        filmFragmentAdapter=new FilmFragmentAdapter(getContext(),mList);
        if (null==mIRecyclerView){
            Log.d("initRecyclerView","mIRecyclerView is null");
        }

        mIRecyclerView.setIAdapter(filmFragmentAdapter);

        filmFragmentAdapter.setOnFilmItemClickListener(new MainRecyclerViewAdapter.onFilmItemClickListener() {
            @Override
            public void onItemClick(int position, String playUrl) {
                String trueUrl=playUrl;

                if (!filmType.equals("2345影视")){
                    trueUrl=ConstantUtil.BASE_URL+playUrl;
                }

                Intent intent=new Intent(getActivity(), X5PlayWebActivity.class);
                intent.putExtra("trueUrl", trueUrl);
                startActivity(intent);
            }
        });

        //设置布局
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        manager.setItemPrefetchEnabled(false);
        mIRecyclerView.setLayoutManager(manager);
        
        //设置监听
        mIRecyclerView.setOnRefreshListener(this);
        mIRecyclerView.setOnLoadMoreListener(this);
        
        //设置滑动动画
       // mIRecyclerView.setItemAnimator(new LandingAnimator());
        
        loadMoreView= (LoadMoreFooterView) mIRecyclerView.getLoadMoreFooterView();
        
    }

    @Override
    public void onLoadMore() {

        //请求下一页数据
        pageNum=pageNum+1;
        //启动线程请求
        requestData();
        mIRecyclerView.setRefreshing(true);
        loadMoreView.setStatus(LoadMoreFooterView.Status.LOADING);
    }

    @Override
    public void onRefresh() {

    }

    /*
    * 根据电影类型获取数据
    * */
    private void requestData(){

        if (filmType.equals("2345影视")){
            OverAllJsoup allJsoup=new OverAllJsoup("2345影视","","","","vip",pageNum);

            allJsoup.setRequestListener(new OverAllJsoup.requestEndListener() {
                @Override
                public void onRequestEnd(Object movieList, boolean suc) {

                    List<FullMovie> list= (List<FullMovie>) movieList;
                    for (FullMovie movie:list){
                        Log.d("initView",movie.toString());
                    }

                    mList.addAll(list);
                    mIRecyclerView.setRefreshing(false);
                    filmFragmentAdapter.notifyDataSetChanged();
                }

            });

            allJsoup.start();


        }else if (filmType.equals("腾讯视频")){
            OverAllJsoup allJsoup=new OverAllJsoup("tencent","","","","",pageNum);
            allJsoup.setRequestListener(new OverAllJsoup.requestEndListener() {
                @Override
                public void onRequestEnd(Object movieList, boolean suc) {

                    List<FullMovie> list= (List<FullMovie>) movieList;
                    for (FullMovie movie:list){
                        Log.d("initView",movie.toString());
                    }

                    mList.addAll(list);
                    mIRecyclerView.setRefreshing(false);
                    filmFragmentAdapter.notifyDataSetChanged();
                }

            });

            allJsoup.start();
        }else {

            OverAllJsoup allJsoup=new OverAllJsoup("iqiyi","","","","",pageNum);

            allJsoup.setRequestListener(new OverAllJsoup.requestEndListener() {
                @Override
                public void onRequestEnd(Object movieList, boolean suc) {

                    List<Movie> list= (List<Movie>) movieList;
                    for (Movie movie:list){
                        Log.d("initView",movie.toString());
                    }

                    mList.addAll(list);

                    mIRecyclerView.setRefreshing(false);
                    filmFragmentAdapter.notifyDataSetChanged();

                }

            });

            allJsoup.start();

            //根据类型开启线程获取数据
/*            url = "http://list.iqiyi.com/www/1/------27815----2---4-1-" + pageNum + "-iqiyi--.html";
            IqiyiJsoupThread thread = new IqiyiJsoupThread(handler, url);
            thread.start();*/
        }
    }
}
