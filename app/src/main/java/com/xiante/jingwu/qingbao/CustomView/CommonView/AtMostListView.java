package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zhong on 2018/5/5.
 */

public class AtMostListView extends ListView {
    public AtMostListView(Context context) {
        super(context);
    }

    public AtMostListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AtMostListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
