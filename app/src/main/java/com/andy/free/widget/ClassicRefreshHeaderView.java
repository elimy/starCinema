package com.andy.free.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.free.R;
import com.aspsine.irecyclerview.RefreshTrigger;


public class ClassicRefreshHeaderView extends RelativeLayout implements RefreshTrigger {

    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;

    private ProgressBar progressBar;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    private int mHeight;

    public ClassicRefreshHeaderView(Context context) {
        this(context, null);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //加载布局
        inflate(context, R.layout.irecyclerview_refresh_header_view, this);

        //初始化view
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);

        ivArrow = (ImageView) findViewById(R.id.ivArrow);

        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //向上旋转动画
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);

        //向下旋转
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        this.mHeight = headerHeight;
        progressBar.setIndeterminate(true);
    }

    @Override
    public void onMove(boolean isComplete, boolean automatic, int moved) {
        if (!isComplete) {

            //设置显示向下的箭头、隐藏进度条和成功图标
            ivArrow.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            ivSuccess.setVisibility(GONE);

            //根据临界高度，判断是否刷新
            if (moved <= mHeight) {
                if (rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }

                tvRefresh.setText("下拉刷新");
            } else {
                tvRefresh.setText("释放刷新");
                if (!rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            }
        }
    }

    @Override
    public void onRefresh() {

        //刷新时设置显示进度条和“正在刷新”提示
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("正在刷新");
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

        //刷新完成以后，显示刷新完成
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText("刷新完成");
    }

    @Override
    public void onReset() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }
}
