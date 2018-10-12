package com.xiante.jingwu.qingbao.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ScrollView;

/**
 * Created by zhong on 2018/8/6.
 */

@SuppressLint("AppCompatCustomView")
public class ScrollEditText extends EditText {
    public ScrollEditText(Context context) {
        super(context);
    }

    public ScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        ViewParent parent=getParent();
        while (parent!=null){
            if(parent instanceof ScrollView){
                parent.requestDisallowInterceptTouchEvent(true);
             break;
            }
            parent=parent.getParent();
        }

        return super.onTouchEvent(event);
    }
}
