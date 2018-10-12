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
import com.xiante.jingwu.qingbao.Bean.Common.ContactEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ContactAdapter extends RecyclerView.Adapter {

    LayoutInflater inflater;
    Context context;
    List<ContactEntity> datalist;
    ContactInterface contactInterface;

    public ContactAdapter(Context context, List<ContactEntity> datalist) {
        this.context = context;
        this.datalist = datalist;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.contact_layout,parent,false);
        ModelOneHolder holder=new ModelOneHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
           ModelOneHolder modelOneHolder= (ModelOneHolder) holder;
           ContactEntity entity=datalist.get(position);
           modelOneHolder.topTitleView.setText(entity.getStrUnitName());
           modelOneHolder.bottomLeftView.setText(entity.getStrTelNum());
           modelOneHolder.rootview.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(contactInterface!=null){
                       contactInterface.contact(position,datalist.get(position).getStrTelNum());
                   }
               }
           });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class  ModelOneHolder extends  RecyclerView.ViewHolder{

        TextView topTitleView;
        TextView bottomLeftView;
        View rootview;
        public ModelOneHolder(View itemView) {
            super(itemView);
            rootview=itemView;
          topTitleView=itemView.findViewById(R.id.ModeloneTitle);
          bottomLeftView=itemView.findViewById(R.id.bottomLeftText);
        }
    }

    public void setContactInterface(ContactInterface contactInterface) {
        this.contactInterface = contactInterface;
    }

    public interface  ContactInterface{
        public void contact(int position,String telnumber);
    }

}
