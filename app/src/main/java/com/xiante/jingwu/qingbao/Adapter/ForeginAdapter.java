package com.xiante.jingwu.qingbao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.RegionBean;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

public class ForeginAdapter extends DefaultAdapter<RegionBean>{
    private Context ctx;
    private List<RegionBean> dateList;
    private int location=-1;
    public ForeginAdapter(List<RegionBean> datas, Context context) {
        super(datas, context);
        this.ctx=context;
        this.dateList=datas;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView==null){
            mHolder=new Holder();
            convertView= LayoutInflater.from(ctx).inflate(R.layout.gv_foregin_item, null);
            mHolder.mTextView=convertView.findViewById(R.id.tv_foregin_item);
            convertView.setTag(mHolder);
        }else{
            mHolder=(Holder)convertView.getTag();
        }
        mHolder.mTextView.setText(dateList.get(position).getStrRegionName());
        if(location==position){
            mHolder.mTextView.setTextColor(Color.parseColor("#ffffff"));
            mHolder.mTextView.setBackgroundResource(R.drawable.send_power);
        }else{
            mHolder.mTextView.setBackgroundResource(R.drawable.send_white);
            mHolder.mTextView.setTextColor(Color.parseColor("#6280f9"));
        }
        return convertView;
    }
    class Holder{
        TextView mTextView;
    }
    public void setSeclection(int position) {
        location = position;
    }
}

