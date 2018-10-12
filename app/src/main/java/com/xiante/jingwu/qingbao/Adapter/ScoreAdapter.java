package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ScoreEntity;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ScoreAdapter extends RecyclerView.Adapter {

    LayoutInflater inflater;
    Context context;
    List<ScoreEntity> datalist;

    public ScoreAdapter(Context context, List<ScoreEntity> datalist) {
        this.context = context;
        this.datalist = datalist;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.score_item_layout,parent,false);
        ModelOneHolder holder=new ModelOneHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           ModelOneHolder modelOneHolder= (ModelOneHolder) holder;
            ScoreEntity entity=datalist.get(position);
           Glide.with(context).load(entity.getStrIco()).into(modelOneHolder.score_item_image);
           modelOneHolder.score_item_content.setText(entity.getStrRuleName());
           float score=Float.parseFloat(entity.getIntScore());
           if(score>0){
               modelOneHolder.score_item_costScore.setText("+"+score);
           }else {
               modelOneHolder.score_item_costScore.setText(entity.getIntScore());
           }

           modelOneHolder.scoreTimeTV.setText(entity.getDtCeateTime());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class  ModelOneHolder extends  RecyclerView.ViewHolder{
        ImageView score_item_image;
        TextView score_item_content,score_item_costScore,scoreTimeTV;

        public ModelOneHolder(View itemView) {
            super(itemView);
            score_item_image=itemView.findViewById(R.id.score_item_image);
            score_item_content=itemView.findViewById(R.id.score_item_content);
            score_item_costScore=itemView.findViewById(R.id.score_item_costScore);
            scoreTimeTV=itemView.findViewById(R.id.scoreTimeTV);
        }
    }

}
