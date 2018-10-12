package com.example.myindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhong on 2018/5/15.
 */

public class CircleIndicator  extends View{
    private ViewPager viewPager;
    private TabChangeListener tabChangeListener;
    private int selectIndex=0;
    private float radius=10;
    private int radiusMargin=30;
    private int choseColor,normalColor;
    private int circleCount=0;
    private Paint paint;
    public CircleIndicator(Context context) {
        super(context);
        init(context);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
           choseColor= Color.parseColor("#ff0000");
           choseColor=Color.parseColor("#eaeaea");
           paint=new Paint();
           paint.setAntiAlias(true);
           paint.setDither(true);
    }

   public void setChoseAndNormalColor(int choseColor,int normalColor){
        this.choseColor=choseColor;
        this.normalColor=normalColor;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


    public void setTabChangeListener(TabChangeListener tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerWidth= (int) (circleCount*radius*2+(circleCount-1)*radiusMargin);
        int curx=(getMeasuredWidth()-centerWidth)/2;
        int cury=getMeasuredHeight()/2;
        for(int i=0;i<circleCount;i++){
            int tempx= (int) (curx+radius*2*i+radiusMargin*i+radius);
            if(i==selectIndex){
                paint.setColor(choseColor);
            }else {
                paint.setColor(normalColor);
            }
            canvas.drawCircle(tempx,cury,radius,paint);
        }
    }




    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        circleCount=viewPager.getAdapter().getCount();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(tabChangeListener!=null){
                    tabChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                selectIndex=position;
                invalidate();
                if(tabChangeListener!=null){
                    tabChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(tabChangeListener!=null){
                    tabChangeListener.onPageScrollStateChanged(state);
                }
            }
        });



    }


}
