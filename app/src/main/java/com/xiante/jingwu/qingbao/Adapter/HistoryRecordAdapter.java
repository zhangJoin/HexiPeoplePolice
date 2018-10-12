package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Activity.SearchMemberActivity;
import com.xiante.jingwu.qingbao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class HistoryRecordAdapter extends BaseAdapter {
    private List<String> listData;
    private Context ctx;

    public HistoryRecordAdapter(List<String> listData, Context ctx) {
        this.listData = listData;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listData.size() > 10 ? 10 : listData.size();
    }

    @Override
    public String getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.history_adapter_item, null);
            holder.mImageView = convertView.findViewById(R.id.iv_history_delete);
            holder.mTextView = convertView.findViewById(R.id.tv_history_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.mTextView.setText(listData.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();
                SharedPreferences history = ctx.getSharedPreferences("history", Context.MODE_PRIVATE);
                String historyStr = history.getString("history", "");
                String[] split = historyStr.split(",");
                for (int i = 0; i < split.length; i++) {
                    tempList.add(split[i]);
                }
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).contains(listData.get(position))) {
                        tempList.remove(i);
                    }
                }
                if(tempList.size()==0){
                    history.edit().putString("history","").commit();
                }else{
                    history.edit().putString("history", tempList.toString().substring(1,tempList.toString().length()-1)).commit();
                }
                listData.remove(position);
                notifyDataSetChanged();
                if(listData.size()==0){
                    SearchMemberActivity.mTextViewHistory.setText("-暂无历史搜索-");
                    SearchMemberActivity.mTextViewHistory.setTextColor(ctx.getResources().getColor(R.color.textnormalcolor));
                }
            }
        });
        return convertView;
    }

    class Holder {
        TextView mTextView;
        ImageView mImageView;
    }
}
