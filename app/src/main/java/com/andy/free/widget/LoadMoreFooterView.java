package com.andy.free.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.andy.free.R;

/**
 * Created by Administrator on 2017/8/17.
 */

public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;
    private View mLoadingView;
    private View mErrorView;
    private View mTheEndView;
    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.irecyclerview_load_more_footer_view,this,true);

        mLoadingView=findViewById(R.id.loadingView);
        mErrorView=findViewById(R.id.errorView);
        mTheEndView=findViewById(R.id.theEndView);

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener!=null){
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(Status.GONE);
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status){
        this.mStatus=status;
        change();
    }

    private void change(){
        switch (mStatus){
            case GONE:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;
            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;
            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                break;
            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }

    public boolean canLoadMore(){
        return mStatus== Status.GONE||mStatus== Status.ERROR;
    }

    public enum Status{
        GONE,LOADING,ERROR,THE_END
    }

    public interface OnRetryListener{
        void onRetry(LoadMoreFooterView view);
    }
}
