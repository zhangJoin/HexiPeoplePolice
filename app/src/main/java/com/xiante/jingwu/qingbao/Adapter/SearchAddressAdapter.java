package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.SearchAddressBean;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/5/22.
 */

public class SearchAddressAdapter extends BaseAdapter {

    private List<SearchAddressBean> searchAddressBeanList;
    private Context context;
    private LayoutInflater inflater;

    public SearchAddressAdapter(List<SearchAddressBean> searchAddressBeanList, Context context) {
        this.searchAddressBeanList = searchAddressBeanList;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchAddressBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchAddressBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressHolder holder;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.search_address_item, null);
            holder = new AddressHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (AddressHolder) convertView.getTag();
        }
        holder.firstName.setText(searchAddressBeanList.get(position).getFirstName());
        holder.detailName.setText(searchAddressBeanList.get(position).getDetailName());
        if(searchAddressBeanList.get(position).getSelectState()){
            holder.stateView.setVisibility(View.VISIBLE);
        }else {
            holder.stateView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class  AddressHolder{
        TextView firstName,detailName;
        View stateView;
        public  AddressHolder(View rootview){
            firstName=rootview.findViewById(R.id.firstName);
            detailName=rootview.findViewById(R.id.detailName);
            stateView=rootview.findViewById(R.id.stateView);
        }
    }
}
