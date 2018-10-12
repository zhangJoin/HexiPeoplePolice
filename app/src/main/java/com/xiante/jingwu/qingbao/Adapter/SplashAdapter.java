package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/5/3.
 */

public class SplashAdapter extends PagerAdapter {
    private Context context;
    private List<ImageView> imageViews;
    public SplashAdapter(int[] imageRes, Context context) {
        this.context = context;
        imageViews=new ArrayList<>();
        for(int i=0;i<imageRes.length;i++){
            ImageView imageView=new ImageView(context);
            imageView.setImageResource(imageRes[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }
}
