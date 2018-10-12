package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/4/28.
 */

public class NoDataAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    public NoDataAdapter(Context context) {
   inflater=LayoutInflater.from(context);
   this.context=context;
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.nodata,null);
        }
        return convertView;
    }
}
