package com.example.myindicator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/6/12.
 */

public class TabItem extends LinearLayout {

    TextView titleView;
    View bottoomLine;
    int index;
    String title;
    LinearLayout rootView;
    public TabItem(Context context) {
        super(context);
        initView(context);
    }

    public TabItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       initView(context);
    }

    public TabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context){
        View.inflate(context,R.layout.tablayout,this);
        titleView= (TextView) findViewById(R.id.tabTitle);
        bottoomLine=findViewById(R.id.bottomLine);
        rootView= (LinearLayout) findViewById(R.id.tabview);
    }

    public void setTabBackground(int color){
        rootView.setBackgroundColor(color);
    }

    public void setTabTitle(String title){
        this.title=title;
        titleView.setText(title);
    }

    public void setBottoomLineColor(int color){
        bottoomLine.setBackgroundColor(color);
    }
    public void setTitleTextColor(int color){
        titleView.setTextColor(color);
    }

    public void setTabColor(int color){
        setBottoomLineColor(color);
        setTitleTextColor(color);
    }

    public void setTabColor(int textcolor,int backgroudColor,int bottomlineColor){

        titleView.setTextColor(textcolor);
        setTabBackground(backgroudColor);
        bottoomLine.setBackgroundColor(bottomlineColor);

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
