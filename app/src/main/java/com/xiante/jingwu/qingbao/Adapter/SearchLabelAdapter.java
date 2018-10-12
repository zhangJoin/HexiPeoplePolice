package com.xiante.jingwu.qingbao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.SearchTypeBean;
import com.xiante.jingwu.qingbao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class SearchLabelAdapter extends DefaultAdapter<SearchTypeBean> {
    private Context ctx;
    private List<SearchTypeBean> dates;
    private HashMap<Integer, Boolean> isSelected;

    @SuppressLint("UseSparseArrays")
    public SearchLabelAdapter(List<SearchTypeBean> datas, Context context) {
        super(datas, context);
        this.ctx = context;
        this.dates = datas;
        isSelected = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.search_label_item, null);
            mHolder.mTextView = convertView.findViewById(R.id.tv_label_item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        mHolder.mTextView.setText(dates.get(position).getStrValue());
        if (getIsSelected().get(position)) {
            mHolder.mTextView.setTextColor(Color.parseColor("#ffffff"));
            mHolder.mTextView.setBackgroundResource(R.drawable.send_power);
        } else {
            mHolder.mTextView.setBackgroundResource(R.drawable.send_gray);
            mHolder.mTextView.setTextColor(Color.parseColor("#646464"));
        }
        mHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected.put(position, !isSelected.get(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class Holder {
        TextView mTextView;
    }
    public void reset(){
        Set<Integer> keyset= isSelected.keySet();
        for (Integer key:keyset
                ) {
            isSelected.put(key,false);
        }
        notifyDataSetChanged();

    }
    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public List<SearchTypeBean> getSelectList() {
        List<SearchTypeBean> templist = new ArrayList<>();
        Set<Integer> ketset = isSelected.keySet();
        for (Integer key : ketset) {
            if (isSelected.get(key)) {
                templist.add(dates.get(key));
            }
        }
        return templist;
    }

}
