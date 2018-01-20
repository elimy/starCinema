package com.andy.free.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.free.R;
import com.andy.free.TvActivity;
import com.andy.free.bean.TvSeries;
import com.andy.free.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/1.
 */
public class TvContentRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<TvSeries> mTvList;
    private LayoutInflater inflater;
    private OnTvItemClickListener mOnTvItemClickListener;

    public TvContentRecyclerViewAdapter(Context mContext, ArrayList<TvSeries> mTvList) {

        this.mContext=mContext;
        this.mTvList=mTvList;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvViewHolder(inflater.inflate(R.layout.item_tv_main,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        TvViewHolder tvViewHolder= (TvViewHolder) holder;

        TvSeries series=mTvList.get(position);
        tvViewHolder.tvName.setText(series.getTvName());
        tvViewHolder.tvNumber.setText(series.getTvNumber());
        tvViewHolder.tvScore.setText(series.getScore());
        TvSeries.Actor[] actors = series.getActors();

        String actorStr="";
        for (int i=0;i<actors.length;i++){

            if (null!=actors[i]) {
                if (i == 0) {
                    actorStr = actors[i].getName();
                }
                {
                    actorStr = actorStr + "|" + actors[i].getName();
                }
            }
        }


        tvViewHolder.tvActor.setText(actorStr);

        ImageLoaderUtils.display(mContext,tvViewHolder.mPosterImage,mTvList.get(position).getPosterUrl());

        tvViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTvItemClickListener.onClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTvList.size();
    }

    private  class TvViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvScore;
        private TextView tvNumber;
        private TextView tvActor;
        private ImageView mPosterImage;
        private LinearLayout rootLayout;

        public TvViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tv_name);
            tvScore= (TextView) itemView.findViewById(R.id.tv_score);
            tvNumber= (TextView) itemView.findViewById(R.id.tv_Number);
            tvActor= (TextView) itemView.findViewById(R.id.tv_actor);
            mPosterImage= (ImageView) itemView.findViewById(R.id.tv_poster);
            rootLayout= (LinearLayout) itemView.findViewById(R.id.tv_root_layout);

        }
    }

    public interface OnTvItemClickListener{

        void onClickListener(int position);
    }

    public void setOnTvItemClickListener(OnTvItemClickListener listener){

        this.mOnTvItemClickListener=listener;
    }
}
