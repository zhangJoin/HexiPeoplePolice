package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zhong on 2018/5/5.
 */

public class AtMostViewPager extends ViewPager {
    public AtMostViewPager(Context context) {
        super(context);
    }

    public AtMostViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
}
