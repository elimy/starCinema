package com.andy.free.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.free.R;
import com.andy.free.bean.FullMovie;
import com.andy.free.bean.Movie;
import com.andy.free.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lau on 2017/10/26.
 */
public class FilmFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private ArrayList<Object> mList;
    private LayoutInflater mInflater;
    private MainRecyclerViewAdapter.onFilmItemClickListener mOnFilmItemClickListener;
    private Movie mMovie;
    private FullMovie mFullMovie;

    public FilmFragmentAdapter(Context mContext, ArrayList<Object> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public FilmItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilmItemViewHolder(mInflater.inflate(R.layout.item_main_movie,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FilmItemViewHolder filmItemViewHolder= (FilmItemViewHolder) holder;

        //Log.d("onBindViewHolder","position="+position);

        String filmTime="";
        String filmPosterUrl="";
        String filmName="";
        String playUrl="";

        
        if (mList.get(position) instanceof Movie){

            mMovie= (Movie) mList.get(position);

            filmTime=mMovie.getTime();
            filmPosterUrl=mMovie.getPosterUrl();
            filmName=mMovie.getName();
            playUrl=mMovie.getUrl();
            Movie.Role[] filmActors=mMovie.getRoles();

            if (null!=filmActors) {
                String actor = "";
                for (int i = 0; i < filmActors.length; i++) {

                    if (i == 0) {
                        actor = filmActors[i].getName();
                    }else {
                        actor = actor + "|" + filmActors[i].getName();
                    }

                    filmItemViewHolder.filmActor.setText(actor);
                }
            }
        }else {

            mFullMovie= (FullMovie) mList.get(position);
            filmTime=mFullMovie.getScore();
            filmName=mFullMovie.getName();
            filmPosterUrl=mFullMovie.getPosterUrl();

            if (null==mFullMovie.getDescUrl()){
                playUrl=mFullMovie.getUrls().get(0).get("tencentUrl");
            }else {
                playUrl=mFullMovie.getDescUrl();
            }

            FullMovie.Actors[] movieActors=mFullMovie.getActors();

            if (null!=movieActors) {
                String actor = "";
                for (int i = 0; i < movieActors.length; i++) {

                    if (i == 0) {
                        actor = movieActors[i].getName();
                    }else {
                        actor = actor + "|" + movieActors[i].getName();
                    }

                    filmItemViewHolder.filmActor.setText(actor);
                }
            }
        }

        if (null==filmTime||filmTime.equals("")){
            filmTime="00:00";
        }

        filmItemViewHolder.filmTime.setText(filmTime);
        filmItemViewHolder.filmName.setText(filmName);

        ImageLoaderUtils.display(mContext,filmItemViewHolder.filmPoster,filmPosterUrl);

        //Log.d("onBindViewHolder","filmPosterUrl="+filmPosterUrl);

        final String finalPlayUrl = playUrl;
        filmItemViewHolder.movieRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnFilmItemClickListener.onItemClick(position, finalPlayUrl);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
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

    public interface onFilmItemClickListener{
        void onItemClick(int position,String playUrl);
    }

    public void setOnFilmItemClickListener(MainRecyclerViewAdapter.onFilmItemClickListener listener){
        this.mOnFilmItemClickListener=listener;
    }
}
