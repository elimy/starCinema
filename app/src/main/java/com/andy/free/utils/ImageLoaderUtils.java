package com.andy.free.utils;

/**
 * Created by Andy Lau on 2017/8/14.
 * Description : 图片加载工具类 使用glide框架封装
 */

import android.content.Context;
import android.widget.ImageView;

import com.andy.free.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class ImageLoaderUtils {
    /*
    * thumbnail(0.5f)//设置播放比例
    * diskCacheStrategy(DiskCacheStrategy strategy)设置缓存策略 DiskCacheStrategy.SOURCE|DiskCacheStrategy.RESULT|DiskCacheStrategy.ALL|DiskCacheStrategy.NONE
    * priority(Priority priority)//设置优先级
    * dontAnimate()//移除所有动画
    * animate()//一步加载完成后执行的动画
    * placeholder()//设置占位图片
    * error()加载错误时显示的图片
    * allback(int resourceId)//设置model为空显示的图片，未设置显示error图片
    * skipMemoryCache(boolean skip)//设置是否跳过内存缓存，不保证一定不被缓存
    * into()设置将被加载的目标资源
    * asBitmap()//无论是gif还是普通图片都当做bitmap对待，动图将显示第一帧
    * asGif()//把资源当做gifDrawable对待，如果不是则显示error设置的图片
    * crossFade()//设置加载动画
    * transform()//图片转换
    * */
    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .crossFade().into(imageView);
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .thumbnail(0.5f)
                .into(imageView);
    }


    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .into(imageView);
    }


    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .crossFade()
                .into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_image_loading)
                .centerCrop()
                .transform(new GlideRoundTransformUtil(context)).into(imageView);
    }

}
