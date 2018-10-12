package com.xiante.jingwu.qingbao.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiante.jingwu.qingbao.Activity.WebveiwActivity;
import com.xiante.jingwu.qingbao.Adapter.ModelFourListAdapter;
import com.xiante.jingwu.qingbao.Adapter.ModelOneListAdapter;
import com.xiante.jingwu.qingbao.Adapter.ModelThreeListAdapter;
import com.xiante.jingwu.qingbao.Adapter.ModelTwoListAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModeFourEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModeTwoEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ModelOneEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class NormalListViewFragment extends Fragment {
    PullToRefreshListView recyclerView;
    View rootView;
   boolean load=false;
    List<ModelOneEntity> modeOnelist;
    List<ModeTwoEntity> modeTwolist;
    List<ModeTwoEntity> modeThreelist;
    private List<ModeFourEntity> modeFourlist;
    BaseAdapter modelOneListAdapter,modeTwoListAdapter,modeThreeListAdapter,modeFourListAdapter;
    String title="";
    String  url;
    int pageindex=1,pageSize=20;
    ClickEntity clickEntity;
    String modetype="";
    LoaddingDialog loaddingDialog;
    View emptyView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       title=getArguments().getString("tag");
       url=getArguments().getString("url");
       emptyView=LayoutInflater.from(getActivity()).inflate(R.layout.emptydata_layout,null);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("hhkhkhhkj onCreateView",""+getUserVisibleHint()+"--"+title);
       if(rootView==null){
           loaddingDialog=new LoaddingDialog(getActivity());
           rootView=inflater.inflate(R.layout.normal_listview_fragment,null);
           recyclerView=rootView.findViewById(R.id.listview);
           recyclerView.setEmptyView(emptyView);
           recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
           modeOnelist=new ArrayList<>();
           modeTwolist=new ArrayList<>();
           modeThreelist=new ArrayList<>();
           modeFourlist=new ArrayList<>();
           recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   if(clickEntity!=null){
                      itemclick(position);
                   }
               }
           });
           recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
               @Override
               public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                   pageindex=1;
                   getData();
               }

               @Override
               public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                   loadMore();
               }
           });
       }
        Log.i("hhkhkhhkj onCreateView",load+"--"+getUserVisibleHint()+"---"+(rootView!=null));
        if(!load&&getUserVisibleHint()&&rootView!=null){
            load=true;
            getData();
        }
        return rootView;
    }

    private void itemclick(int position) {
       position--;
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
           case "modelFour":
               guid=modeFourlist.get(position).getStrGuid();
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
            return;
        }
        if(finalurl.contains(".html")){
            Intent intent = new Intent(getActivity(), WebveiwActivity.class);
            intent.putExtra("url",finalurl);
            intent.putExtra("click",clickEntity);
            startActivity(intent);
        }else {

            getDetailHtml(finalurl);

        }

    }
    private void getDetailHtml(String finalurl) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory=OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if(new CodeExceptionUtil(getActivity()).dealException(str)){
                    String url=new JSONObject(str).optString("resultData");
                    Intent intent = new Intent(getActivity(), WebveiwActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("click",clickEntity);
                    startActivity(intent);
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingDialog.dismissAniDialog();
            }
        };
        networkFactory.start(NetworkFactory.METHOD_GET,finalurl,null,successfulCallback,failCallback);
    }
    public void loadMore(){
          pageindex++;
          getData();
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
    }



    public void getData(){
        boolean isSuccess = Utils.isSuccess(getActivity());
        if(!isSuccess){
            Toast.makeText(getActivity(),getString(R.string.netError),Toast.LENGTH_SHORT).show();
            return;
        }
       loaddingDialog.showDialog();
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(getActivity()==null){
                    return;
                }
                     recyclerView.onRefreshComplete();
                     if(new CodeExceptionUtil(getActivity()).dealException(str)){
                         JSONObject resultObject=new JSONObject(str).optJSONObject("resultData");
                         modetype=resultObject.optString("type");
                         clickEntity= JSON.parseObject(resultObject.optString("strClick"),ClickEntity.class);
                         dealModeList(resultObject.optString("data"),modetype);
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
                if(pageindex==1){
                    modeTwolist.clear();
                    modeTwolist.addAll(temptwolist);
                    modeTwoListAdapter=new ModelTwoListAdapter(getActivity(),modeTwolist);
                    recyclerView.setAdapter(modeTwoListAdapter);
                }else {
                    modeTwolist.addAll(temptwolist);
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
                    modeThreelist.addAll(tempthreelist);
                    modeThreeListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelFour":
                List<ModeFourEntity> tempfourlist=JSON.parseArray(string,ModeFourEntity.class);
                if(tempfourlist.size()==0){
                    showNoData();
                    return;
                }
                if(pageindex==1){
                    modeFourlist.clear();
                    modeFourlist.addAll(tempfourlist);
                    modeFourListAdapter=new ModelFourListAdapter(getActivity(),modeFourlist);
                    recyclerView.setAdapter(modeFourListAdapter);
                }else {
                    modeFourlist.addAll(tempfourlist);
                    modeFourListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    private void showNoData(){
    //    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_LONG).show();
    }

}
