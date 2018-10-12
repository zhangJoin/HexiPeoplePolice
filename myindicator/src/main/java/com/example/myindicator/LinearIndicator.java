package com.example.myindicator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */

public class LinearIndicator extends LinearLayout {

    ViewPager viewPager;
    int tabWidth;
    int choseColor= Color.BLUE,loseColor=Color.BLACK;
    private TabChangeListener listener;
    private int losetextColor=Color.parseColor("#000000");
    private int loseLineColor;
    private int TabbackgroundColor;
    private List<String> titleList;
    public LinearIndicator(Context context) {
        super(context);
        initView(context);
    }

    public LinearIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LinearIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
         setOrientation(HORIZONTAL);
        titleList=new ArrayList<>();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setTabChangeListener(TabChangeListener listener){
        this.listener=listener;
    }

    public void setChoseAndLoseColor(int choseColor,int loseColor){
        this.choseColor=choseColor;
        this.loseColor=loseColor;
    }
    public void setChoseAndLoseColor(int choseColor,int losetextColor,int loseLineColor,int tabbackgroundColor){
        this.choseColor=choseColor;
        this.losetextColor=losetextColor;
        this.loseLineColor=loseLineColor;
        this.TabbackgroundColor=tabbackgroundColor;

    }

    public void setViewPager(ViewPager viewPager) {
        titleList.clear();
        this.viewPager = viewPager;
        int count=viewPager.getAdapter().getCount();
        for(int i=0;i<count;i++){
            titleList.add(viewPager.getAdapter().getPageTitle(i).toString());
        }
        tabWidth=getContext().getResources().getDisplayMetrics().widthPixels/count;
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

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(listener!=null){
                    listener.onPageScrollStateChanged(state);
                }

            }
        });
          for(int i=0;i<count;i++){
              initOneItem(titleList.get(i),i);
          }
          setSelectItem(0);
    }

    public void initIndicatorByTitleList(List<String> titleList){
        this.titleList=titleList;
        int count=titleList.size();
        tabWidth=getContext().getResources().getDisplayMetrics().widthPixels/count;
        for(int i=0;i<count;i++){
            initOneItem(titleList.get(i),i);
        }
        setSelectItem(0);
    }


    public void initOneItem(String title,int index){
         final TabItem tabItem=new TabItem(getContext());
         LayoutParams params=new LayoutParams(tabWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        tabItem.setLayoutParams(params);
        tabItem.setTabBackground(TabbackgroundColor);
        tabItem.setBottoomLineColor(TabbackgroundColor);
        tabItem.setTitleTextColor(losetextColor);
        addView(tabItem);
        tabItem.setTabTitle(title);
        tabItem.setIndex(index);
         tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onPageSelected(tabItem.getIndex());
                }
                if(viewPager!=null){
                    viewPager.setCurrentItem(tabItem.getIndex());
                }
                setSelectItem(tabItem.getIndex());
            }
        });
    }

    public void setSelectItem(int index){
        int count=getChildCount();
        for(int i=0;i<count;i++){
            TabItem tabItem= (TabItem) getChildAt(i);
            tabItem.setTabColor(losetextColor,TabbackgroundColor,TabbackgroundColor);
        }
        TabItem selectItem= (TabItem) getChildAt(index);
        selectItem.setTabColor(choseColor);
    }

}
