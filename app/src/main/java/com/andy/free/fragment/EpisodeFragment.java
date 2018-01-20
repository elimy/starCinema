package com.andy.free.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andy.free.R;
import com.andy.free.X5PlayWebActivity;
import com.andy.free.adapter.EpisodeFragmentAdapter;
import com.andy.free.bean.TvSeries;
import com.andy.free.utils.ConstantUtil;
import com.aspsine.irecyclerview.IRecyclerView;

/**
 * Created by Administrator on 2017/11/9.
 */

public class EpisodeFragment extends Fragment {

    private TvSeries series;
    private View rootView;
    private IRecyclerView mIRecyclerView;
    private EpisodeFragmentAdapter episodeAdapter;

    public EpisodeFragment() {
    }

    public EpisodeFragment(TvSeries series) {
        this.series = series;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null==rootView){
            rootView=inflater.inflate(R.layout.fragment_episode,container,false);
        }

        mIRecyclerView= (IRecyclerView) rootView.findViewById(R.id.eqisode_recyclerView);

        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {

        episodeAdapter=new EpisodeFragmentAdapter(getActivity(),series.getPlayNumAndUrls());
        mIRecyclerView.setIAdapter(episodeAdapter);

        //设置剧集点击监听
        episodeAdapter.setOnItemClickListener(new EpisodeFragmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, String playUrl) {

                Intent intent=new Intent(getActivity(), X5PlayWebActivity.class);
                intent.putExtra("trueUrl",ConstantUtil.BASE_URL+playUrl);
                startActivity(intent);

                Log.d("trueUrl",ConstantUtil.BASE_URL+playUrl);
            }
        });

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);
        mIRecyclerView.setLayoutManager(manager);

    }
}
