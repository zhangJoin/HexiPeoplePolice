package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiante.jingwu.qingbao.Activity.AlbumsActivity;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by zhong on 2018/4/23.
 * 录入页图片上传控件的adapter
 *
 */

public class ImageInputAdapter extends BaseAdapter {
    private  ImageLoader imageLoader;
    private  DisplayImageOptions options;
    private LayoutInflater inflater;
    List<String> selectImageList;
    Context context;
    private int maxSelect;
    private String inputkey;
    public ImageInputAdapter(List<String> selectImageList, Context context,int maxSelect,String inputkey) {
        this.selectImageList = selectImageList;
        this.context = context;
        this.maxSelect=maxSelect;
        this.inputkey=inputkey;
        inflater=LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        // 使用DisplayImageOption.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build(); // 创建配置过的DisplayImageOption对象
    }

    public List<String> getSelectImageList() {
        return selectImageList;
    }

    public void setSelectImageList(List<String> selectImageList) {
        this.selectImageList = selectImageList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return selectImageList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder=new Holder();
           convertView=inflater.inflate(R.layout.image_input_item,null);
            holder.imageView=convertView.findViewById(R.id.itemimageIV);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(position<getCount()-1){
            imageLoader.displayImage("file://"+selectImageList.get(position), holder.imageView, options);
            holder.imageView.setOnClickListener(null);
        }else {
           holder.imageView.setImageResource(R.drawable.upimage_location);
           holder.imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context, AlbumsActivity.class);
                   intent.putExtra("maxSelect",maxSelect+"");
                   intent.putExtra(Global.INPUTKEY,inputkey);
                   context.startActivity(intent);
               }
           });
        }
        return convertView;
    }
    class Holder{
        ImageView imageView;
    }

    public String getUploadString(){
       return com.alibaba.fastjson.JSONArray.toJSONString(selectImageList);
    }

}
