package com.xiante.jingwu.qingbao.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Activity.MainTab_ShouyeActivity;
import com.xiante.jingwu.qingbao.Activity.WebveiwActivity;
import com.xiante.jingwu.qingbao.Adapter.ModelOneListAdapter;
import com.xiante.jingwu.qingbao.Adapter.ModelThreeListAdapter;
import com.xiante.jingwu.qingbao.Adapter.ModelTwoListAdapter;
import com.xiante.jingwu.qingbao.Adapter.NoDataAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModeTwoEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.CustomView.CommonView.AtMostListView;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class ListViewFragment extends Fragment {
    AtMostListView recyclerView;
    View rootView;
   boolean load=false;
    private ArrayList<ModelOneEntity> modeOnelist;
    List<ModeTwoEntity> modeTwolist;
    List<ModeTwoEntity> modeThreelist;
    BaseAdapter modelOneListAdapter,modeTwoListAdapter,modeThreeListAdapter;
    String title="";
    String  url;
    int pageindex=1,pageSize=20;
    ClickEntity clickEntity;
    LoaddingDialog loaddingDialog;
    private String modetype;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=getArguments().getString("tag");
        url=getArguments().getString("url");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                 if(rootView==null){

                     rootView=inflater.inflate(R.layout.listview_fragment,null);
                     recyclerView=rootView.findViewById(R.id.listview);
                     recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             if(clickEntity!=null){
                                 itemclick(position);
                             }
                         }
                     });
                     recyclerView.setOnTouchListener(new View.OnTouchListener() {
                         @Override
                         public boolean onTouch(View v, MotionEvent event) {
                             EventBus.getDefault().post(new Integer(v.getMeasuredHeight()));
                             return false;
                         }
                     });

                     modeOnelist=new ArrayList<>();
                     modeTwolist=new ArrayList<>();
                     modeThreelist=new ArrayList<>();
                     recyclerView.setAdapter(modelOneListAdapter);
                     loaddingDialog=new LoaddingDialog(getActivity());

                 }
       if(!load&&getUserVisibleHint()&&rootView!=null){
           load=true;
           getData();
       }

        return rootView;
    }
    public void loadMore(){
          pageindex++;
          getData();
    }
    public void refreshData(){
        pageindex=1;
        getData();
    }

    public int getListViewHeight(){
        int height=500;
        if(recyclerView!=null){
            height=recyclerView.getMeasuredHeight();
        }
        return  height;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!load&&isVisibleToUser&&rootView!=null){
            load=true;
            getData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getData(){
        loaddingDialog.showDialog();
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(getActivity()==null){
                    return;
                }
             if(new CodeExceptionUtil(getActivity()).dealException(str)){
                 JSONObject resultObject=new JSONObject(str).optJSONObject("resultData");
                 modetype=resultObject.optString("type");
                 clickEntity= JSON.parseObject(resultObject.optString("strClick"),ClickEntity.class);
                 dealModeList(resultObject.optString("data"),modetype);
                 ((MainTab_ShouyeActivity)getActivity()).stopLoadMore();
                 if(pageindex==1){
                     EventBus.getDefault().post(new Integer(800));
                 }
             }
             loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {
                if(getActivity()==null){
                    return;
                }
                  Toast.makeText(getActivity(),"网络请求异常",Toast.LENGTH_SHORT).show();
                 loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager=new UrlManager(getActivity());
        String neturl="";
        if(url.contains("?")){
            neturl=new StringBuilder(urlManager.getData_url()).append(url).append("&").append(urlManager.getExtraStr()).toString();
        }else {
            neturl=new StringBuilder(urlManager.getData_url()).append(url).append("?").append(urlManager.getExtraStr()).toString();
        }
        HashMap<String,String> param=new HashMap<>();
        param.put("intPageIndex",pageindex+"");
        param.put("intPageSize",pageSize+"");
        networkFactory.start(NetworkFactory.METHOD_GET,neturl,param,successfulCallback,failCallback);
    }


    private void dealModeList(String string, String type) {
        recyclerView.setEnabled(true);
        switch (type){
            case "modelOne":
                List<ModelOneEntity> templist=JSON.parseArray(string,ModelOneEntity.class);
                if(templist.size()==0){
                    showNoData();
                    return;
                }
                if(pageindex==1){
                    modeOnelist.clear();
                    modeOnelist.addAll(templist);
                    modelOneListAdapter=new ModelOneListAdapter(getActivity(),modeOnelist);
                    recyclerView.setAdapter(modelOneListAdapter);
                }else {
                    if(modelOneListAdapter==null){
                       return;
                    }
                    modeOnelist.addAll(templist);
                    modelOneListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelTwo":
                List<ModeTwoEntity> temptwolist=JSON.parseArray(string,ModeTwoEntity.class);
                if(temptwolist.size()==0){
                    showNoData();
                    return;
                }
                modeTwolist.addAll(temptwolist);
                if(pageindex==1){
                    modeTwolist.clear();
                    modeTwoListAdapter=new ModelTwoListAdapter(getActivity(),modeTwolist);
                    recyclerView.setAdapter(modeTwoListAdapter);
                }else {
                    if(modeTwoListAdapter==null){
                        return;
                    }
                    modeTwoListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelThree":
                List<ModeTwoEntity> tempthreelist=JSON.parseArray(string,ModeTwoEntity.class);
                if(tempthreelist.size()==0){
                    showNoData();
                    return;
                }
                if(pageindex==1){
                    modeThreelist.clear();
                    modeThreelist.addAll(tempthreelist);
                    modeThreeListAdapter=new ModelThreeListAdapter(getActivity(),modeThreelist);
                    recyclerView.setAdapter(modeThreeListAdapter);
                }else {
                    if(modeThreeListAdapter==null){
                        return;
                    }
                    modeThreelist.addAll(tempthreelist);
                    modeThreeListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    private void showNoData(){
        if(pageindex==1){
            recyclerView.setEnabled(false);
            recyclerView.setAdapter(new NoDataAdapter(getActivity()));
        }else {

        }
    }
    private void itemclick(int position) {
        String guid="";
        switch (modetype){
            case "modelOne":
                guid=modeOnelist.get(position).getStrGuid();
                break;
            case "modelTwo":
                guid=modeTwolist.get(position).getStrGuid();
                break;
            case  "modelThree":
                guid=modeThreelist.get(position).getStrGuid();
                break;
        }
        UrlManager urlManager=new UrlManager(getActivity());
        String finalurl="";
        if(clickEntity.getStrUrl().contains("?")){
            finalurl=  new StringBuilder(urlManager.getData_url()).append(clickEntity.getStrUrl()).append("&")
                    .append(urlManager.getExtraStr()).append("&strGuid=").append(guid).toString();
        }else {
            finalurl=  new StringBuilder(urlManager.getData_url()).append(clickEntity.getStrUrl()).append("?")
                    .append(urlManager.getExtraStr()).append("&strGuid=").append(guid).toString();
        }
        if(IsNullOrEmpty.isEmpty(clickEntity.getStrUrl())){
            finalurl="";
        }
        Intent intent = new Intent(getActivity(), WebveiwActivity.class);
        intent.putExtra("url",finalurl);
        intent.putExtra(Global.CLICK_ACTION,clickEntity);
        startActivity(intent);
    }
}
