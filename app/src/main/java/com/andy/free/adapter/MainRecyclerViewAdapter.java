package com.andy.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.free.MainActivity;
import com.andy.free.R;
import com.andy.free.bean.Movie;
import com.andy.free.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by Andy Lau on 2017/10/18.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter {
    private ArrayList<Movie> movieList;
    private Context mContext;
    private LayoutInflater inflater;

    private final static int TYPE_TOP_BANNER=0;
    private final static int TYPE_GRID_FUN=1;
    private final static int TYPE_RECO_TITLE=2;
    private final static int TYPR_CONTENT_ITEM=3;
    private final static int TYPE_BOTTOM_TIP=4;
    private onFilmItemClickListener mOnFilmItemClickListener;
    private onFuncItemClickListener mOnFuncItemClickListener;

    public MainRecyclerViewAdapter( Context mContext,ArrayList<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = mContext;
        this.inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_TOP_BANNER;
        }else if (position==1){
            return TYPE_GRID_FUN;
        }else if (position==2){
            return TYPE_RECO_TITLE;
        }else if (position==(movieList.size()+3)){
            return TYPE_BOTTOM_TIP;
        }else {
            return TYPR_CONTENT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TOP_BANNER:
                return new TopBannerViewHolder(inflater.inflate(R.layout.item_main_banner,parent,false));
            case TYPR_CONTENT_ITEM:

                return new FilmItemViewHolder(inflater.inflate(R.layout.item_main_movie,parent,false));
            case TYPE_RECO_TITLE:

                return new TitleViewHolder(inflater.inflate(R.layout.item_main_title,parent,false));
            case TYPE_GRID_FUN:

                return new FunViewHolder(inflater.inflate(R.layout.item_main_fun,parent,false));
            default:
                return new TopBannerViewHolder(inflater.inflate(R.layout.item_main_banner,parent,false));

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof FilmItemViewHolder){

            Log.d("onBindViewHolder","position="+position);

            String filmTime=movieList.get(position-3).getTime();
            String filmPosterUrl=movieList.get(position-3).getPosterUrl();
            String filmName=movieList.get(position-3).getName();
            final String playUrl=movieList.get(position-3).getUrl();
            Movie.Role[] filmActors=movieList.get(position-3).getRoles();

            if (null!=filmActors) {
                String actor = "";
                for (int i = 0; i < filmActors.length; i++) {

                    if (i == 0) {
                        actor = filmActors[i].getName();
                    }else {
                        actor = actor + "|" + filmActors[i].getName();
                    }

                    ((FilmItemViewHolder) holder).filmActor.setText(actor);
                }
            }

            ((FilmItemViewHolder)holder).filmTime.setText(filmTime);
            ((FilmItemViewHolder)holder).filmName.setText(filmName);

            ImageLoaderUtils.display(mContext,((FilmItemViewHolder)holder).filmPoster,filmPosterUrl);

            Log.d("onBindViewHolder","filmPosterUrl="+filmPosterUrl);

            ((FilmItemViewHolder) holder).movieRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnFilmItemClickListener.onItemClick(position-3,playUrl);
                }
            });
        }else if(holder instanceof  TitleViewHolder){

        }else if (holder instanceof FunViewHolder){
            ((FunViewHolder)holder).filmLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(0);
                }
            });

            ((FunViewHolder)holder).tvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(1);
                }
            });

            ((FunViewHolder)holder).liveLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(2);
                }
            });

            ((FunViewHolder)holder).zyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(3);
                }
            });

            ((FunViewHolder)holder).dmLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(4);
                }
            });

            ((FunViewHolder)holder).duanZiLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFuncItemClickListener.onItemClick(5);
                }
            });


        }else{
            ((TopBannerViewHolder)holder).bannerImage.setImageResource(R.drawable.banner);
        }

    }

    @Override
    public int getItemCount() {
        return movieList.size()+4;
    }

    /*
    * 顶部banner viewHolder
    * */
    private class TopBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView bannerImage;

         TopBannerViewHolder(View itemView) {
            super(itemView);

            bannerImage= (ImageView) itemView.findViewById(R.id.banner_image);
        }
    }

    /*
    * 推荐标题viewHolder
    * */
    private class TitleViewHolder extends RecyclerView.ViewHolder{

        private ImageView mIcon;
        private TextView mTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mIcon= (ImageView) itemView.findViewById(R.id.rec_ico);
            mTitle= (TextView) itemView.findViewById(R.id.rec_text);

        }
    }

    /*
    * 电影item的viewHolder
    * */
    private class FilmItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView filmPoster;
        private TextView filmTime;
        private TextView filmName;
        private TextView filmActor;
        private LinearLayout movieRootLayout;

        public FilmItemViewHolder(View itemView) {
            super(itemView);

            filmPoster= (ImageView) itemView.findViewById(R.id.film_poster);
            filmName= (TextView) itemView.findViewById(R.id.film_name);
            filmActor= (TextView) itemView.findViewById(R.id.film_actor);
            filmTime= (TextView) itemView.findViewById(R.id.film_time);
            movieRootLayout= (LinearLayout) itemView.findViewById(R.id.movie_root_layout);
        }
    }

    /*
    * 分类viewHolder
    * */
    private class FunViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout filmLayout;
        RelativeLayout tvLayout;
        RelativeLayout liveLayout;
        RelativeLayout zyLayout;
        RelativeLayout dmLayout;
        RelativeLayout duanZiLayout;

        public FunViewHolder(View itemView) {
            super(itemView);

            filmLayout= (RelativeLayout) itemView.findViewById(R.id.film_layout);
            tvLayout= (RelativeLayout) itemView.findViewById(R.id.tv_layout);
            liveLayout= (RelativeLayout) itemView.findViewById(R.id.live_layout);
            zyLayout= (RelativeLayout) itemView.findViewById(R.id.zy_layout);
            dmLayout= (RelativeLayout) itemView.findViewById(R.id.dm_layout);
            duanZiLayout= (RelativeLayout) itemView.findViewById(R.id.duanzi_layout);

        }
    }

    public interface onFilmItemClickListener{
        void onItemClick(int position,String playUrl);
    }

    public void setOnFilmItemClickListener(onFilmItemClickListener listener){
        this.mOnFilmItemClickListener=listener;
    }


    public interface onFuncItemClickListener{

        void onItemClick(int position);
    }

    public void setOnFuncItemClickListener(onFuncItemClickListener listener){
        this.mOnFuncItemClickListener=listener;
    }

}
