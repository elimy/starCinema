package com.andy.free;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.free.Jsoup.TvDetailJsoup;
import com.andy.free.adapter.TvDetailFragmentAdapter;
import com.andy.free.bean.TvSeries;
import com.andy.free.fragment.EpisodeFragment;
import com.andy.free.utils.ImageLoaderUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/6.
 */

public class TvDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tv_bg_poster)
    ImageView backPosterImage;
    @BindView(R.id.tv_poster)
    ImageView posterImage;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_time)
    TextView mTVTime;
    @BindView(R.id.tv_area)
    TextView mTvArea;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_actors)
    TextView mTvActors;
    @BindView(R.id.tv_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_detail_slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.tv_viewPager)
    ViewPager mViewPager;

    private String[] tabs={"剧集","概述"};
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private Unbinder mUnbinder;
    private final float radius=25;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tv_detail);
        mUnbinder=ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        Intent intent=getIntent();
        TvSeries series= (TvSeries) intent.getSerializableExtra("obj");

        Toast.makeText(this,"TvName="+series.getTvName(),Toast.LENGTH_SHORT).show();

        mToolbar.setTitle(series.getTvName());

        setSupportActionBar(mToolbar);
        //扩展是标题颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(-1);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取数据
        TvDetailJsoup detailJsoup=new TvDetailJsoup(series.getDescUrl());

        detailJsoup.setRequestListener(new TvDetailJsoup.requestEndListener() {
            @Override
            public void onRequestEnd(Object seriesObj, boolean suc) {
                TvSeries  series= (TvSeries) seriesObj;

                for (int i=0;i<series.getPlayNumAndUrls().size();i++){
                    Log.d("onRequestEnd",series.getPlayNumAndUrls().get(i).toString());
                }

                mTVTime.setText(series.getTime());
                mTvArea.setText(series.getArea());

                String actorStr="主演:";
                for (int i=0;i<series.getActors().length;i++){
                    if (i==0){
                        actorStr = actorStr + series.getActors()[i].getName();
                    }else {
                        actorStr = actorStr + "|" + series.getActors()[i].getName();
                    }
                }

                mTvActors.setText(actorStr);
                ImageLoaderUtils.display(TvDetailActivity.this,posterImage,series.getPosterUrl());
                new PathAsyncTask(backPosterImage,TvDetailActivity.this).execute(series.getPosterUrl());

                setFragment(series);

            }
        });

        detailJsoup.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    public void setFragment(TvSeries tvSeries) {

        EpisodeFragment episodeFragment=new EpisodeFragment(tvSeries);
        Fragment descFragment=new Fragment();
        fragments.add(episodeFragment);
        fragments.add(descFragment);

        //设置fragmentAdapter
        TvDetailFragmentAdapter fragmentAdapter=new TvDetailFragmentAdapter(getSupportFragmentManager(),fragments,tabs);
        mViewPager.setAdapter(fragmentAdapter);
        slidingTabLayout.setViewPager(mViewPager);

    }

    /*
   *下载缓存并设置歌单模糊背景
   * */
    private class PathAsyncTask extends AsyncTask<String,Void,String> {

        private ImageView mImageView;
        private String mPath;
        private FileInputStream mIs;
        private Context mContext;


        public PathAsyncTask(ImageView image,Context mContext){

            this.mImageView=image;
            this.mContext=mContext;
        }

        @Override
        protected String doInBackground(String... params) {

            //通过glide下载缓存歌单海报
            FutureTarget<File> future = Glide.with(mContext)
                    .load(params[0])
                    .downloadOnly(100,100);

            try {

                //获取到缓存路径
                File cacheFile=future.get();
                mPath=cacheFile.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return mPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Bitmap bitmap=null;
            try {

                //判断传回的线程返回的资源路径是否为空，为空设置默认的bitmap
                if (null==s) {
                    Drawable drawable=mContext.getResources().getDrawable(R.drawable.ic_image_loading);
                    BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
                    bitmap=bitmapDrawable.getBitmap();
                }else {
                    mIs=new FileInputStream(s);
                    bitmap= BitmapFactory.decodeStream(mIs);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            applyBlur(bitmap);
        }
    }



    /*设置海报的模糊背景*/
    private void applyBlur(Bitmap bitmap){
        RenderScript localRenderScript = RenderScript.create(this);
        Allocation localAllocation1 = Allocation.createFromBitmap(localRenderScript,bitmap);
        Allocation localAllocation2 = Allocation.createTyped(localRenderScript, localAllocation1.getType());
        if (Build.VERSION.SDK_INT >= 17)
        {
            ScriptIntrinsicBlur localScriptIntrinsicBlur = ScriptIntrinsicBlur.create(localRenderScript, Element.U8_4(localRenderScript));
            localScriptIntrinsicBlur.setInput(localAllocation1);
            localScriptIntrinsicBlur.setRadius(this.radius);
            localScriptIntrinsicBlur.forEach(localAllocation2);
            localAllocation2.copyTo(bitmap);
            localRenderScript.destroy();
        }

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

        if (Build.VERSION.SDK_INT >= 16) {
            this.backPosterImage.setBackground(bitmapDrawable);
        }
    }
}
