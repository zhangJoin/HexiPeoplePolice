package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.RegionBean;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class RegionAdapter extends DefaultAdapter<RegionBean>{
    Context ctx;
    List<RegionBean> dateList;
    private int location=-1;
    public RegionAdapter(List<RegionBean> datas, Context context) {
        super(datas, context);
        this.ctx=context;
        this.dateList=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         Holder mHolder=null;
         if (convertView==null){
             mHolder=new Holder();
             convertView= LayoutInflater.from(ctx).inflate(R.layout.gv_region_item, null);
             mHolder.mTextView=convertView.findViewById(R.id.tv_region_item);
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
