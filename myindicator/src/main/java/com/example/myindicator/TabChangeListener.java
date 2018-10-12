package com.example.myindicator;

/**
 * Created by Administrator on 2017/6/12.
 */

public interface TabChangeListener {

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) ;

    public void onPageSelected(int position) ;

    public void onPageScrollStateChanged(int state);
}
