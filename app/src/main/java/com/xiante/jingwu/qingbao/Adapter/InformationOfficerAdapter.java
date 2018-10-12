package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.Bean.Common.InforOfficeBean;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class InformationOfficerAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private Context context;
    private List<InforOfficeBean> datalist;
    private InformationOfficerAdapter.ContactInterface contactInterface;
    private onItemClickListener itemClickListener;
    public InformationOfficerAdapter(Context context, List<InforOfficeBean> datalist) {
        this.context = context;
        this.datalist = datalist;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.information_adapter_item,parent,false);
        InformationOfficerAdapter.InformationHolder holder=new InformationOfficerAdapter.InformationHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        InformationOfficerAdapter.InformationHolder modelOneHolder= (InformationOfficerAdapter.InformationHolder) holder;
        InforOfficeBean entity=datalist.get(position);
        modelOneHolder.tvOfficer.setText(entity.getStrName());
        modelOneHolder.tvOfficerType.setText(entity.getStrLabelName());
        Glide.with(context).load(entity.getStrPortrait()).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(modelOneHolder.ivOfficeritem);
        modelOneHolder.rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactInterface!=null){
//                    contactInterface.contact(position,datalist.get(position).getStrTelNum());
                }
            }
        });
       modelOneHolder.rootview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(itemClickListener!=null){
                   itemClickListener.onItemClick(position);
               }
           }
       });
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class  InformationHolder extends  RecyclerView.ViewHolder{

        TextView tvOfficer;
        TextView tvOfficerType;
        ImageView ivOfficeritem;
        View rootview;
        public InformationHolder(View itemView) {
            super(itemView);
            rootview=itemView;
            tvOfficer=itemView.findViewById(R.id.tv_officer_item);
            tvOfficerType=itemView.findViewById(R.id.tv_officer_type_item);
            ivOfficeritem=itemView.findViewById(R.id.iv_officer_item);
        }
    }


    public interface  onItemClickListener{
        public void onItemClick(int position);
    }

    public void setContactInterface(InformationOfficerAdapter.ContactInterface contactInterface) {
        this.contactInterface = contactInterface;
    }

    public interface  ContactInterface{
        public void contact(int position,String telnumber);
    }

    public void setDatalist(List<InforOfficeBean> datalist) {
        this.datalist = datalist;
    }
}
