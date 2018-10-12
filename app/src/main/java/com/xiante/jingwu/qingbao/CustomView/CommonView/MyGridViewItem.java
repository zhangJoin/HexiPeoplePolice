package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 自定义GridViewItem，
 * 目的就是让item的宽高一样；
 * 我们无法确定高度是多少，但是我们可以根据GridView的列数得到宽度是多少，然后让高度等于宽度即可；
 */
public class MyGridViewItem extends FrameLayout{

	public MyGridViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyGridViewItem(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),getDefaultSize(0, heightMeasureSpec));

		// childWidthSize是自定义布局的宽
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();

		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
