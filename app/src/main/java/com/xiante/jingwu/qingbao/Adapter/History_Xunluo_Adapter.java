package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Activity.HistoryMapActivity;
import com.xiante.jingwu.qingbao.Bean.Common.HistoryXunluoEntity;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class History_Xunluo_Adapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<HistoryXunluoEntity> datalist;

    public History_Xunluo_Adapter(Context context, List<HistoryXunluoEntity> datalist) {
        this.context = context;
        this.datalist = datalist;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HistoryHold historyHold=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.history_xunluo_item,null);
            historyHold=new HistoryHold(convertView);
            convertView.setTag(historyHold);
        }else {
            historyHold= (HistoryHold) convertView.getTag();
        }
        HistoryXunluoEntity entity=datalist.get(position);
        historyHold.createTimeTV.setText(entity.getStrCreateTime());
        historyHold.totalMileTV.setText(entity.getStrDistance().equals("")?entity.getStrDistance():entity.getStrDistance()+"km");
        historyHold.totalTimeTV.setText(entity.getStrDuration());
        historyHold.speedTV.setText(entity.getStrSpeed().equals("")?entity.getStrSpeed():entity.getStrSpeed()+"m/s");
        historyHold.goMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,HistoryMapActivity.class);
                intent.putExtra("entity",datalist.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class  HistoryHold extends  RecyclerView.ViewHolder{

        TextView createTimeTV,totalMileTV,totalTimeTV,speedTV;
        View goMapView;
        public HistoryHold(View itemView) {
            super(itemView);
            createTimeTV=itemView.findViewById(R.id.his_timeTV);
            totalMileTV=itemView.findViewById(R.id.his_totalMileTV);
            totalTimeTV=itemView.findViewById(R.id.his_totaltimeTV);
            speedTV=itemView.findViewById(R.id.his_speedTV);
            goMapView=itemView.findViewById(R.id.go_mapTV);
        }
    }

}
