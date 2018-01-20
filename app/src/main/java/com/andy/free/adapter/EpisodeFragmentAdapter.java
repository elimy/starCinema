package com.andy.free.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andy.free.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */

public class EpisodeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Map<Integer,String>> playNumAndUrls;
    private onItemClickListener mOnItemClickListener;
    private LayoutInflater inflater;

    public EpisodeFragmentAdapter(Context context, ArrayList<Map<Integer, String>> playNumAndUrls) {
    
        this.context=context;
        this.playNumAndUrls=playNumAndUrls;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TvEpisodeViewHolder(inflater.inflate(R.layout.item_tv_episode,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        TvEpisodeViewHolder episodeHolder= (TvEpisodeViewHolder) holder;

        Log.d("onBindViewHolder",playNumAndUrls.get(position).get(position));

        episodeHolder.episodeBtn.setText("第"+(position+1)+"集");

        episodeHolder.episodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position,playNumAndUrls.get(position).get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return playNumAndUrls.size();
    }
    
    public interface onItemClickListener{
        void onItemClick(int pos,String playUrl);
    }
    
    public void setOnItemClickListener(onItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    /*
    * 剧集播放按钮的viewHolder
    * */
    private class TvEpisodeViewHolder extends RecyclerView.ViewHolder {

        private Button episodeBtn;

        public TvEpisodeViewHolder(View itemView) {
            super(itemView);

            episodeBtn= (Button) itemView.findViewById(R.id.episode_btn);
        }
    }
}
