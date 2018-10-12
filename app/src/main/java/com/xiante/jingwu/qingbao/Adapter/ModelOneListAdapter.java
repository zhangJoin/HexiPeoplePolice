package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ModelOneListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<ModelOneEntity> datalist;

    public ModelOneListAdapter(Context context, List<ModelOneEntity> datalist) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ModelOneHolder modelOneHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.model_one,null);
            modelOneHolder=new ModelOneHolder(convertView);
            convertView.setTag(modelOneHolder);
        }else {
            modelOneHolder= (ModelOneHolder) convertView.getTag();
        }
            ModelOneEntity entity=datalist.get(position);
            modelOneHolder.topTitleView.setText(entity.getTopText());
            if(entity.getMiddlePic().equals("")){
                modelOneHolder.middleImageview.setVisibility(View.GONE);
            }else {
                modelOneHolder.middleImageview.setVisibility(View.VISIBLE);
                Glide.with(context).load(entity.getMiddlePic()).error(R.drawable.modeplace).placeholder(R.drawable.modeplace).into(modelOneHolder.middleImageview);
            }
        if(!IsNullOrEmpty.isEmpty(entity.getTopTextColor())){
            modelOneHolder.topTitleView.setTextColor(Color.parseColor(entity.getTopTextColor()));
        }else {
            modelOneHolder.topTitleView.setTextColor(Color.parseColor("#333333"));
        }
            modelOneHolder.bottomLeftView.setText(entity.getBottomLeftText());
            modelOneHolder.bottomRightView.setText(entity.getBottomRightText());
        return convertView;
    }

    class  ModelOneHolder extends  RecyclerView.ViewHolder{

        TextView topTitleView;
        ImageView middleImageview;
        TextView bottomLeftView,bottomRightView;

        public ModelOneHolder(View itemView) {
            super(itemView);
          topTitleView=itemView.findViewById(R.id.ModeloneTitle);
          middleImageview=itemView.findViewById(R.id.ModeloneImageview);
          bottomLeftView=itemView.findViewById(R.id.bottomLeftText);
          bottomRightView=itemView.findViewById(R.id.bottomRightText);
        }
    }

}
