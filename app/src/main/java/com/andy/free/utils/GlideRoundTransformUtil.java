package com.andy.free.utils;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Andy Lau on 2017/8/14.
 * description:glide转换圆角图片
 */
public class GlideRoundTransformUtil extends BitmapTransformation {
    public GlideRoundTransformUtil(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        //去最小的边
        int size = Math.min(source.getWidth(), source.getHeight());
        //设置中心点
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // 创建最小边的正方形
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        //创建画布
        Canvas canvas = new Canvas(result);

        //设置画笔
        Paint paint = new Paint();
        //CLAMP : 如果渲染器超出原始边界范围，会复制范围内边缘染色
        //REPEAT ：横向和纵向的重复渲染器图片，平铺。
        //MIRROR ：横向和纵向的重复渲染器图片，这个和REPEAT 重复方式不一样，他是以镜像方式平铺
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}