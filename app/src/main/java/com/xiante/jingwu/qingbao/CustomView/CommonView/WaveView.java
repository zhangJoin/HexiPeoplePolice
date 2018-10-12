package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/24.
 */

public class WaveView extends View {
    private int lineColor;
    private int linewidth = 3;
    private Paint paint;
    private int heightlevel = 0;
    private int quarterWaveLength = 0;
    private int startX;
    Path path = new Path();

    public WaveView(Context context) {
        super(context);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        lineColor = context.getResources().getColor(R.color.colorPrimary);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(lineColor);
        paint.setStrokeWidth(linewidth);
        paint.setStyle(Paint.Style.STROKE);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                heightlevel = getMeasuredHeight() / 2;
                invalidate();
            }
        });
    }

    public void setWaveChange(int waveHeight) {
        heightlevel = waveHeight;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        quarterWaveLength = (getMeasuredWidth() / 5) / 2;
        startX = -quarterWaveLength * 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, paint);
        path.reset();
        path.lineTo(startX, getMeasuredHeight() / 2);
        int endx = startX;
        while (endx < getMeasuredWidth()) {
            endx += quarterWaveLength;
            path.quadTo(endx, getMeasuredHeight() / 2 - heightlevel, endx += quarterWaveLength, getMeasuredHeight() / 2);
            path.quadTo(endx += quarterWaveLength, getMeasuredHeight() / 2 + heightlevel, endx += quarterWaveLength, getMeasuredHeight() / 2);
        }
        canvas.drawPath(path, paint);
        if (heightlevel > 0 && startX < 0) {
            heightlevel -= 10;
            postInvalidateDelayed(100);
        }
        startX += 20;
        if (startX > 0) {
            startX = -quarterWaveLength;
        }
    }
}
