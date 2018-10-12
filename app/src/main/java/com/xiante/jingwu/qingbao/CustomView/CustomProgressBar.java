package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xiante.jingwu.qingbao.Util.DisplayUtil;

public class CustomProgressBar extends View{
    Paint mPaint;
    private float progress=0.0f;
    private int bottomcolor,progressColor,topBottomPadding=5;
    private int progressWidth=5;
    private int leftRightPadding=5;
    private int minWidth=100;
    public CustomProgressBar(Context context) {
        super(context);
        init(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        progressWidth= DisplayUtil.dip2px(context,progressWidth);
        minWidth= DisplayUtil.dip2px(context,minWidth);
        mPaint.setStrokeWidth(progressWidth);
        topBottomPadding= DisplayUtil.dip2px(context,topBottomPadding);
        leftRightPadding= DisplayUtil.dip2px(context,leftRightPadding);
        bottomcolor= Color.parseColor("#eaeaea");
        progressColor=Color.parseColor("#6280f9");
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         int heightMode=MeasureSpec.getMode(heightMeasureSpec);
         int height=MeasureSpec.getSize(heightMeasureSpec);
         if(heightMode==MeasureSpec.EXACTLY){
             if(height<progressWidth+topBottomPadding*2){
                 height=progressWidth+topBottomPadding*2;
             }
         }else if(heightMode==MeasureSpec.AT_MOST){
             height=progressWidth+topBottomPadding*2;
         }
        int hsize=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
         setMeasuredDimension(widthMeasureSpec,hsize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(bottomcolor);
        canvas.drawLine(leftRightPadding,getMeasuredHeight()/2,getMeasuredWidth()-leftRightPadding,getMeasuredHeight()/2,mPaint);
        int p= (int) ((getMeasuredWidth()-leftRightPadding*2)*progress);
        mPaint.setColor(progressColor);
        canvas.drawLine(leftRightPadding,getMeasuredHeight()/2,p+leftRightPadding,getMeasuredHeight()/2,mPaint);
    }

    public void setProgress(float progress){
        this.progress=progress;
        invalidate();

    }
}
