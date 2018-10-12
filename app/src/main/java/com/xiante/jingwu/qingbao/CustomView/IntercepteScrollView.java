package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by zhong on 2018/5/1.
 */

public class IntercepteScrollView extends ScrollView {
    public IntercepteScrollView(Context context) {
        super(context);
    }

    public IntercepteScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntercepteScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
   //     if(ev.getAction()==MotionEvent.ACTION_DOWN){
          return   super.onInterceptTouchEvent(ev);
//        }else if(ev.getAction()==MotionEvent.ACTION_MOVE){
//            return true;
//        }else {
//            return   super.onInterceptTouchEvent(ev);
//        }
    }
}
