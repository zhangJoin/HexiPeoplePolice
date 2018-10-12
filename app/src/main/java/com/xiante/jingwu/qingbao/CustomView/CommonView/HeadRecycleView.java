package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhong on 2018/4/28.
 */

public class HeadRecycleView extends RecyclerView {
    public HeadRecycleView(Context context) {
        super(context);
    }

    public HeadRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        LayoutManager manager=getLayoutManager();
        Log.i("sdjsklf",manager.getChildCount()+"---"+manager.getItemCount());
        if(state==RecyclerView.SCROLL_STATE_IDLE){

            if(manager instanceof LinearLayoutManager){
                int lastvisible=((LinearLayoutManager)manager).findLastVisibleItemPosition();
                if(manager.getChildCount()>0&&lastvisible>=manager.getItemCount()-1){
                    if(loadMoreListener!=null){
                        loadMoreListener.loadMore();
                    }
                }
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {

    }

public interface   LoadMoreListener{
      public void  loadMore();
}


}
