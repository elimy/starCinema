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
import com.andy.free.VarietyActivity;
import com.andy.free.bean.Variety;
import com.andy.free.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */
public class VarietyRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Variety> mVarietyList;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public VarietyRecyclerViewAdapter(Context context, ArrayList<Variety> mVarietyList) {

        this.context=context;
        this.mVarietyList=mVarietyList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VarietyViewHolder(inflater.inflate(R.layout.item_variety_main,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        VarietyViewHolder varietyViewHolder= (VarietyViewHolder) holder;

        varietyViewHolder.varietyLastTextView.setText(mVarietyList.get(position).getLatestEpisode());
        varietyViewHolder.varietyNameTextView.setText(mVarietyList.get(position).getName());
        varietyViewHolder.varietySimpleDescTextView.setText(mVarietyList.get(position).getSimpleDesc());

        ImageLoaderUtils.display(context,varietyViewHolder.varietyPoster,mVarietyList.get(position).getPosterUrl());

        varietyViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(position,mVarietyList.get(position).getDescUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVarietyList.size();
    }

    class VarietyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rootLayout;
        private ImageView varietyPoster;
        private TextView varietyLastTextView;
        private TextView varietyNameTextView;
        private TextView varietySimpleDescTextView;

        public VarietyViewHolder(View itemView) {
            super(itemView);

            rootLayout= (LinearLayout) itemView.findViewById(R.id.variety_root_layout);
            varietyPoster= (ImageView) itemView.findViewById(R.id.variety_poster);
            varietyLastTextView= (TextView) itemView.findViewById(R.id.tv_last);
            varietyNameTextView= (TextView) itemView.findViewById(R.id.variety_name);
            varietySimpleDescTextView= (TextView) itemView.findViewById(R.id.variety_simple_desc);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos,String descUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
       mOnItemClickListener=listener;
    }
}
