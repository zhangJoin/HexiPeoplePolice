package com.xiante.jingwu.qingbao.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/4/19.
 */

abstract public class BaseActivity extends FragmentActivity {
    protected View backView;
    protected TextView titleTextView,rightTextView;
    protected ImageView rightImageView;
    protected View titlebarLineView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.texthintcolor));
        com.xiante.jingwu.qingbao.Manager.ActivityManager.getInstance().addActivity(this);
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    public void setStatusBarColor(int color){
        if(Build.VERSION.SDK_INT>21){
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        }else  if(Build.VERSION.SDK_INT>19){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = new View(window.getContext());
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarView);
        }
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    protected  void initTitlebar(String title,String rightText,String rightImage){
        backView=findViewById(R.id.titlebarBackView);
        titleTextView=findViewById(R.id.titlebarTitleTV);
        rightTextView=findViewById(R.id.titlebarRightTV);
        rightImageView=findViewById(R.id.titlebarRightIV);
        titlebarLineView=findViewById(R.id.titlebarLineView);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText(title);
        rightTextView.setText(rightText);
        if(!rightImage.equals("")){
            Glide.with(this).load(rightImage).into(rightImageView);
        }
    }


}
