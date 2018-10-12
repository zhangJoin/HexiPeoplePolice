package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;



public class BothWayProgressBar extends View {
    private static final String TAG = "BothWayProgressBar";
    private int maxProgresTime=0;
    private int currentTime=0;
    //取消状态为红色bar, 反之为绿色bar
    private boolean isCancel = false;
    private Context mContext;
    //正在录制的画笔
    private Paint mRecordPaint;
    //上滑取消时的画笔
    private Paint mCancelPaint;
    //是否显示
    private int mVisibility;
    // 当前进度
    private int progress;
    //进度条结束的监听
    private OnProgressEndListener mOnProgressEndListener;
    private Handler timeProgressHandler;

    public BothWayProgressBar(Context context) {
        super(context, null);
    }
    public BothWayProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
    private void init() {
        mVisibility = INVISIBLE;
        mRecordPaint = new Paint();
        mRecordPaint.setColor(Color.GREEN);
        mCancelPaint = new Paint();
        mCancelPaint.setColor(Color.RED);
        timeProgressHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                currentTime+=50;
                if(currentTime<=maxProgresTime){
                    setProgressByPercent(currentTime*1.0f/maxProgresTime);
                    timeProgressHandler.sendEmptyMessageDelayed(0,50);
                }

            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mVisibility == View.VISIBLE) {
            int height = getHeight();
            int width = getWidth();
            int mid = width / 2;
            //画出进度条
            if (progress < mid){
                canvas.drawRect(progress, 0, width-progress, height, isCancel ? mCancelPaint : mRecordPaint);
            } else {
                if (mOnProgressEndListener != null) {
                    mOnProgressEndListener.onProgressEndListener();
                }
            }
        } else {
            canvas.drawColor(Color.argb(0, 0, 0, 0));
        }
    }






    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setProgressByPercent(float progressPercet){
        this.progress= (int) ((getWidth()/2)*progressPercet);
        invalidate();
    }

    public void setMaxProgresTime(int maxTime){
        this.maxProgresTime=maxTime;
    }

    public void startCaculateProgress(){
        timeProgressHandler.sendEmptyMessageDelayed(0,50);
    }

    public void stopCaculateProgress(){
        timeProgressHandler.removeMessages(0);
        timeProgressHandler=null;
    }

    /**
     * 设置录制状态 是否为取消状态
     * @param isCancel
     */
    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
        invalidate();
    }
    /**
     * 重写是否可见方法
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        mVisibility = visibility;
        //重新绘制
        invalidate();
    }
    /**
     * 当进度条结束后的 监听
     * @param onProgressEndListener
     */
    public void setOnProgressEndListener(OnProgressEndListener onProgressEndListener) {
        mOnProgressEndListener = onProgressEndListener;
    }

    public interface OnProgressEndListener{
        void onProgressEndListener();
    }
    public int getCurrentTime() {
        return currentTime;
    }

}
