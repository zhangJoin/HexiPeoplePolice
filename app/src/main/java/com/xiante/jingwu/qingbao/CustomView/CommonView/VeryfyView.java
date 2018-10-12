package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiante.jingwu.qingbao.Util.DisplayUtil;

import java.util.Random;

/**
 * Created by zhong on 2018/5/2.
 */

public class VeryfyView extends View {
    private String veryfyStr;
    private Paint paint;
    private int firnum,secnum;
    private int textsize=16;
    private int padding=10;
    private int distance;
    private Bitmap verifyBitmap;
    private String[] drawtext={"0","0","0","0"};
    public VeryfyView(Context context) {
        super(context);
        init(context);
    }

    public VeryfyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VeryfyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       distance=(getMeasuredWidth()-padding*2)/4;
    }

    private void init(Context context){
        textsize= DisplayUtil.dip2px(context,textsize);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(textsize);
        changeVerify();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVerify();
            }
        });
    }

    public void changeVerify(){
        firnum=new Random().nextInt(10);
        secnum=new Random().nextInt(10);
        drawtext[0]=firnum+"";
        drawtext[1]=new Random().nextInt(10)+"";
        drawtext[2]=secnum+"";
        drawtext[3]=new Random().nextInt(10)+"";
        createBitmap();
        invalidate();
    }

    public String getResult(){
        return new StringBuilder(drawtext[0]).append(drawtext[1]).append(drawtext[2]).append(drawtext[3]).toString();
    }

private void createBitmap(){
    int width=getMeasuredWidth();
    int height=getMeasuredHeight();
    if(width==0){
        width= DisplayUtil.dip2px(getContext(),150);
    }
    if(height==0){
        height= DisplayUtil.dip2px(getContext(),50);
    }
    if(distance==0){
        distance= DisplayUtil.dip2px(getContext(),30);
    }
    verifyBitmap=Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
    Canvas canvas=new Canvas(verifyBitmap);
    canvas.drawColor(Color.WHITE);
    float xp=0,yp=0;
    Random random=new Random();
    paint.setColor(Color.parseColor("#eaeaea"));
    paint.setTypeface(Typeface.DEFAULT_BOLD);
    canvas.drawRoundRect(new RectF(0,0,width,height),10,10,paint);
    paint.setTextSize(textsize);
    for(int i=0;i<drawtext.length;i++){
        int color=  Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256) );
        paint.setTextSkewX(new Random().nextFloat());
        paint.setColor(color);
        xp=padding+distance*i+random.nextInt(distance-10);
        yp=height/2+random.nextInt(20);
        canvas.drawText(drawtext[i],xp,yp,paint);
    }
}

    @Override
    protected void onDraw(Canvas canvas) {

       canvas.drawBitmap(verifyBitmap,0,0,paint);
    }



}
