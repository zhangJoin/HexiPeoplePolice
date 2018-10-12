package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiante.jingwu.qingbao.Activity.PublishTypeActivity;
import com.xiante.jingwu.qingbao.Bean.Common.SearchTypeBean;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/4/19.
 */

public class PublishTypeView extends LinearLayout implements InputView{
     private TextView nameView;
     private TextView valueTV;
     private InputItemBean inputItemBean;
     private View goSelectView,rootview;
    private List<SearchTypeBean> tradelist;
    private List<SearchTypeBean> labellist;
    private String selectStr="";
    public PublishTypeView(Context context) {
        super(context);
        initView(context);
    }

    public PublishTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PublishTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context){
        View.inflate(context, R.layout.publish_input_view,this);
        nameView=findViewById(R.id.inputNameTV);
        valueTV=findViewById(R.id.inputValueTV);
        rootview=findViewById(R.id.rootview);
        goSelectView=findViewById(R.id.goSelectView);
        rootview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PublishTypeActivity.class);
                intent.putExtra(Global.INPUTKEY,inputItemBean.getStrField());
                intent.putExtra("url",inputItemBean.getStrUrl());
                intent.putExtra("select",selectStr);
                context.startActivity(intent);
            }
        });
    }

    public void setInputName(String name){
        nameView.setText(name);
    }
    //value={strRangeTrade:[],strRangeLabel:[]}
    @Override
    public UploadBean getUploadValue() {

        List<String> tradetemp=new ArrayList<>();
        List<String> labeltemp=new ArrayList<>();
        if(tradelist!=null){
            for (SearchTypeBean b:tradelist
                 ) {
                tradetemp.add(b.getStrGuid());
            }
        }
        if(labellist!=null){
            for (SearchTypeBean b:labellist
                    ) {
                labeltemp.add(b.getStrGuid());
            }
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("strRangeTrade",JSON.toJSONString(tradetemp));
            jsonObject.put("strRangeLabel",JSON.toJSONString(labeltemp));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String value=jsonObject.toString();
        try {
            value= URLEncoder.encode(value,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(Global.PUBLISH_TYPE,value);
    }

    @Override
    public boolean checkUploadValue() {
        boolean flag=true;
        if(inputItemBean.getIsMust().equals("1")){
         if(tradelist==null||tradelist.size()==0){
           flag=false;
           Toast.makeText(getContext(),"未选择类型",Toast.LENGTH_SHORT).show();
         }
         if(labellist==null||labellist.size()==0){
             Toast.makeText(getContext(),"未选择标签",Toast.LENGTH_SHORT).show();
             flag=false;
         }
        }
        return  flag;
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        nameView.setText(inputItemBean.getStrFieldName());
    }
//jsonObject.put("strRangeTrade",tradeStr);
                  //  jsonObject.put("strRangeLabel ",labelStr);
    //value={strRangeTrade:[],strRangeLabel:[]}
    @Override
    public void updateInputView(String string) {
        try {
            this.selectStr=string;
            JSONObject jsonObject=new JSONObject(string);
            tradelist= JSON.parseArray(jsonObject.optString("strRangeTrade"),SearchTypeBean.class);
            labellist=JSON.parseArray(jsonObject.optString("strRangeLabel"),SearchTypeBean.class);
            StringBuilder builder=new StringBuilder("已选择");
            List<SearchTypeBean> templist=new ArrayList<>();
            if(tradelist!=null&&tradelist.size()>0){
               templist.addAll(tradelist);
            }
            if(labellist!=null&&labellist.size()>0){
              templist.addAll(labellist);
            }
            if(templist.size()==0){
                valueTV.setText("");
                return;
            }
            if(templist.size()>2){
                builder.append(templist.get(0).getStrValue()).append(",")
                        .append(templist.get(1).getStrValue()).append("等").append(templist.size()).append("项");
            }else {
                for (SearchTypeBean b : templist) {
                    builder.append(b.getStrValue()).append(",");
                }
             builder= builder.deleteCharAt(builder.length()-1);
            }
            valueTV.setText(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
