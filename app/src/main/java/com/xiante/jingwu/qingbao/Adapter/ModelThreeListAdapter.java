package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.ModeTwoEntity;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ModelThreeListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<ModeTwoEntity> datalist;

    public ModelThreeListAdapter(Context context, List<ModeTwoEntity> datalist) {
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
            convertView=inflater.inflate(R.layout.model_three,null);
            modelOneHolder=new ModelOneHolder(convertView);
            convertView.setTag(modelOneHolder);
        }else {
            modelOneHolder= (ModelOneHolder) convertView.getTag();
        }
            ModeTwoEntity entity=datalist.get(position);
        if(!IsNullOrEmpty.isEmpty(entity.getTopRightColor())){
            modelOneHolder.topRighttv.setTextColor(Color.parseColor(entity.getTopRightColor()));
        }
            modelOneHolder.toplefttv.setText(entity.getTopLeftText().replaceAll("\\\\n","\n"));
            modelOneHolder.topRighttv.setText(entity.getTopRightText().replaceAll("\\\\n","\n"));
            modelOneHolder.middletv.setText(entity.getMiddleText().replaceAll("\\\\n","\n"));
            modelOneHolder.bottomLeftView.setText(entity.getBottomLeftText().replaceAll("\\\\n","\n"));
            modelOneHolder.bottomRightView.setText(entity.getBottomRightText().replaceAll("\\\\n","\n"));
        return convertView;
    }

    class  ModelOneHolder {

        TextView toplefttv,topRighttv,middletv;
        TextView bottomLeftView,bottomRightView;
        public ModelOneHolder(View itemView) {
            toplefttv=itemView.findViewById(R.id.topleftTV);
            topRighttv=itemView.findViewById(R.id.topRightTV);
             middletv=itemView.findViewById(R.id.middleTV);
           bottomLeftView=itemView.findViewById(R.id.bottomLeftText);
           bottomRightView=itemView.findViewById(R.id.bottomRightText);
        }
    }

}
