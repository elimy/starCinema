package com.andy.free.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andy.free.FilmActivity;
import com.andy.free.Jsoup.IqiyiJsoupThread;
import com.andy.free.LiveActivity;
import com.andy.free.MainActivity;
import com.andy.free.R;
import com.andy.free.TvActivity;
import com.andy.free.VarietyActivity;
import com.andy.free.X5PlayWebActivity;
import com.andy.free.adapter.MainRecyclerViewAdapter;
import com.andy.free.bean.Movie;
import com.andy.free.utils.ConstantUtil;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/18.
 */

public class MainFragment extends Fragment implements OnRefreshListener {

    private IRecyclerView mIRecyclerView;

    private ArrayList<Movie> mList=new ArrayList<>();
    private MainRecyclerViewAdapter mMainAdapter;

    private Boolean isGet=true;
    private String url = "http://list.iqiyi.com/www/1/------27815----2---4-1-1-iqiyi--.html";
    private int pageNum=1;
    private Handler handler=null;

    public MainFragment() {

         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                List<Movie> movies = (List<Movie>) msg.obj;
                mList.addAll(movies);

                for (int i = 0; i < movies.size(); i++) {
                    Log.d("Element", "movie=" + movies.get(i).toString());
                }
                mMainAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null);

        mIRecyclerView = (IRecyclerView) view.findViewById(R.id.main_IRecyclerView);

        //通过线程获取数据
        if (mList.size()==0&&isGet){

            url="http://list.iqiyi.com/www/1/------27815----2---4-1-"+pageNum+"-iqiyi--.html";
            IqiyiJsoupThread thread = new IqiyiJsoupThread(handler,url);
            thread.start();
            isGet=false;
        }

        initIRecyclerView();

        return view;
    }

    private void initIRecyclerView() {

        mMainAdapter=new MainRecyclerViewAdapter(getContext(),mList);
        mIRecyclerView.setIAdapter(mMainAdapter);

        GridLayoutManager manager=new GridLayoutManager(getActivity(),3, LinearLayoutManager.VERTICAL,false);

        //设置item跨度
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==2||position==3||position==4||position==(mList.size()+5)){
                    return 3;
                }
                return 1;
            }
        });

        mIRecyclerView.setLayoutManager(manager);
        mIRecyclerView.setOnRefreshListener(this);

        mMainAdapter.setOnFilmItemClickListener(new MainRecyclerViewAdapter.onFilmItemClickListener() {
            @Override
            public void onItemClick(int position, String playUrl) {

                String trueUrl= ConstantUtil.BASE_URL+playUrl;

                //TbsVideo.openVideo(getActivity(),trueUrl);

                Intent intent=new Intent(getActivity(), X5PlayWebActivity.class);
                intent.putExtra("trueUrl",trueUrl);
                startActivity(intent);
            }
        });

        mMainAdapter.setOnFuncItemClickListener(new MainRecyclerViewAdapter.onFuncItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //Toast.makeText(getActivity(),position+"",Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent filmIntent=new Intent(getActivity(), FilmActivity.class);
                        startActivity(filmIntent);
                        break;
                    case 1:
                        Intent tvIntent=new Intent(getActivity(), TvActivity.class);
                        startActivity(tvIntent);
                        break;
                    case 2:
                         Intent intent=new Intent(getActivity(), LiveActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent varietyIntent=new Intent(getActivity(), VarietyActivity.class);
                        startActivity(varietyIntent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mIRecyclerView.setRefreshing(true);

        pageNum=pageNum+1;

        url="http://list.iqiyi.com/www/1/------27815----2---4-"+pageNum+"-1-iqiyi--.html";

        Log.d("url",url);
        IqiyiJsoupThread thread = new IqiyiJsoupThread(handler,url);
        thread.start();

        mIRecyclerView.setRefreshing(false);

    }
}
