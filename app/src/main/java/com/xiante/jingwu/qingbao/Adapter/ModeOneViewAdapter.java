package com.xiante.jingwu.qingbao.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.CustomView.CommonView.AtMostListView;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/5/3.
 */

public class ModeOneViewAdapter extends PagerAdapter {
    private Context context;
    private List<View> imageViews;
    private List<String> titleList;
    List<List<ModelOneEntity>> datalist;
    LayoutInflater inflater;
    public ModeOneViewAdapter(final Context context, List<String> titleList, List<List<ModelOneEntity>> datalist) {
        this.context = context;
        inflater=LayoutInflater.from(context);
        this.titleList=titleList;
        this.datalist=datalist;
        imageViews=new ArrayList<>();
        for(int i=0;i<datalist.size();i++){
            View view=inflater.inflate(R.layout.shouye_news_list_layout,null);
            AtMostListView listView=view.findViewById(R.id.atmostlistview);
            ModelOneListAdapter adapter=new ModelOneListAdapter(context,datalist.get(i));
            listView.setAdapter(adapter);
            imageViews.add(view);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context,""+position,Toast.LENGTH_LONG).show();
                }
            });
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.i("listviewontouch",""+v.getMeasuredHeight());
                    EventBus.getDefault().post(new Integer(v.getMeasuredHeight()));
                    return false;
                }
            });
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
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
