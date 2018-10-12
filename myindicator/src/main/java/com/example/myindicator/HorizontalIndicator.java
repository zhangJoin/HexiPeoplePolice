package com.example.myindicator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/6/12.
 */

public class HorizontalIndicator extends LinearLayout {

    ViewPager viewPager;
    int tabWidth;
    int choseColor= Color.BLUE,losetextColor=Color.BLACK,loseLineColor=Color.BLACK,TabbackgroundColor=Color.GRAY;
    private TabChangeListener listener;
    private HorizontalScrollView horizontalScrollView;
    public HorizontalIndicator(Context context) {
        super(context);
        initView(context);
    }

    public HorizontalIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HorizontalIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
   private LinearLayout rootView;
    public void initView(Context context){
             View.inflate(context,R.layout.horizontalindicatorlayout,this);
             rootView= (LinearLayout) findViewById(R.id.indicatorRootView);
             horizontalScrollView= (HorizontalScrollView) findViewById(R.id.horizontalView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setTabChangeListener(TabChangeListener listener){
        this.listener=listener;
    }

    public void setChoseAndLoseColor(int choseColor,int losetextColor,int loseLineColor,int tabbackgroundColor){
        this.choseColor=choseColor;
        this.losetextColor=losetextColor;
        this.loseLineColor=loseLineColor;
        this.TabbackgroundColor=tabbackgroundColor;

    }

    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        int count=viewPager.getAdapter().getCount();
        tabWidth=getContext().getResources().getDisplayMetrics().widthPixels/5;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(listener!=null){
                    listener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }

            }

            @Override
            public void onPageSelected(int position) {
                setSelectItem(position);
                if(listener!=null){
                    listener.onPageSelected(position);
                }
                if(viewPager.getAdapter().getCount()>5){
                    dealScroollState(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(listener!=null){
                    listener.onPageScrollStateChanged(state);
                }

            }
        });
          for(int i=0;i<count;i++){
              initOneItem(viewPager.getAdapter().getPageTitle(i).toString(),i);
          }
          setSelectItem(0);
    }

    private void dealScroollState(int index){
        horizontalScrollView.smoothScrollTo(tabWidth*(index-1),0);
    }

    public void initOneItem(String title,int index){
         final TabItem tabItem=new TabItem(getContext());
         LayoutParams params=new LayoutParams(tabWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
         params.setMargins(15,0,0,0);
         tabItem.setLayoutParams(params);
         rootView.addView(tabItem);
          tabItem.setTabTitle(title);
           tabItem.setIndex(index);
         tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(tabItem.getIndex());
                setSelectItem(tabItem.getIndex());
                dealScroollState(tabItem.getIndex());
            }
        });
    }

    public void setSelectItem(int index){
        int count=rootView.getChildCount();
        for(int i=0;i<count;i++){
            TabItem tabItem= (TabItem) rootView.getChildAt(i);
            tabItem.setTabColor(losetextColor,TabbackgroundColor,loseLineColor);
        }
        TabItem selectItem= (TabItem) rootView.getChildAt(index);
        selectItem.setTabColor(choseColor,TabbackgroundColor,choseColor);
    }

}
