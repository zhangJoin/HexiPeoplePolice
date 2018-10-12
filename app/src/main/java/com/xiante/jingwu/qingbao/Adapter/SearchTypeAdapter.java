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
 * 选择标签页
 */
public class SearchTypeAdapter extends DefaultAdapter<SearchTypeBean> {
    private Context ctx;
    private List<SearchTypeBean>dates;
    private HashMap<Integer, Boolean> isSelected;
    @SuppressLint("UseSparseArrays")
    public SearchTypeAdapter(List<SearchTypeBean> datas, Context context) {
        super(datas, context);
        this.ctx=context;
        this.dates=datas;
        isSelected = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView==null){
            mHolder=new Holder();
            convertView= LayoutInflater.from(ctx).inflate(R.layout.search_type_item,null);
            mHolder.mTextView=convertView.findViewById(R.id.tv_type_item);
            convertView.setTag(mHolder);
        }else{
            mHolder= (Holder) convertView.getTag();
        }
        mHolder.mTextView.setText(dates.get(position).getStrValue());
        if(getIsSelected().get(position)){
            mHolder.mTextView.setTextColor(Color.parseColor("#ffffff"));
            mHolder.mTextView.setBackgroundResource(R.drawable.send_power);
        }else{
            mHolder.mTextView.setBackgroundResource(R.drawable.send_gray);
            mHolder.mTextView.setTextColor(Color.parseColor("#646464"));
        }
        mHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected.put(position,!isSelected.get(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class Holder{
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

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
         notifyDataSetChanged();
    }

    public List<SearchTypeBean> getSelectList(){
        List<SearchTypeBean> templist=new ArrayList<>();
        Set<Integer> ketset=isSelected.keySet();
        for (Integer key:ketset) {
            if(isSelected.get(key)){
                templist.add(dates.get(key));
            }
        }
        return templist;
    }



}
