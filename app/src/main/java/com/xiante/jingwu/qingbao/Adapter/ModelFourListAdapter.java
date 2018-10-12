package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.Bean.Common.ModeFourEntity;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ModelFourListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<ModeFourEntity> datalist;

    public ModelFourListAdapter(Context context, List<ModeFourEntity> datalist) {
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
            convertView=inflater.inflate(R.layout.model_four,null);
            modelOneHolder=new ModelOneHolder(convertView);
            convertView.setTag(modelOneHolder);
        }else {
            modelOneHolder= (ModelOneHolder) convertView.getTag();
        }
            ModeFourEntity entity=datalist.get(position);
            modelOneHolder.toptv.setText(entity.getTopText());
            modelOneHolder.middletv.setText(entity.getMiddleText());
            modelOneHolder.bottomtv.setText(entity.getBottomText());
            if(!entity.getRightImage().equals("")){
                modelOneHolder.topRightIV.setVisibility(View.VISIBLE);
                Glide.with(context).load(entity.getRightImage()).error(R.drawable.modeplace).placeholder(R.drawable.modeplace).into(modelOneHolder.topRightIV);
            }else {
                modelOneHolder.topRightIV.setVisibility(View.GONE);
            }
        return convertView;
    }

    class  ModelOneHolder {

        TextView toptv,bottomtv,middletv;
        ImageView topRightIV;
        public ModelOneHolder(View itemView) {
               toptv=itemView.findViewById(R.id.topTV);
               middletv=itemView.findViewById(R.id.middletv);
               bottomtv=itemView.findViewById(R.id.bottomtv);
               topRightIV=itemView.findViewById(R.id.topRightIV);
        }
    }

}
