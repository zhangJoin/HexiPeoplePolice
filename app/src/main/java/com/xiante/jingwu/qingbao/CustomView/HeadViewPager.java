package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by zhong on 2018/4/28.
 */

public class HeadViewPager extends ViewPager {
    public HeadViewPager(Context context) {
        super(context);
    }

    public HeadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
}
