package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Adapter.SearchLabelAdapter;
import com.xiante.jingwu.qingbao.Adapter.SearchTypeAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.InforOfficeBean;
import com.xiante.jingwu.qingbao.Bean.Common.SearchTypeBean;
import com.xiante.jingwu.qingbao.CustomView.CommonView.MyGridView;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.DateUtil;
import com.xiante.jingwu.qingbao.Util.Global;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索信息员页面
 */
public class PublishTypeActivity extends BaseActivity {
    private MyGridView gvSearchType;
    private MyGridView gvSearchLabel;
    private List<SearchTypeBean> list = new ArrayList<>();
    private List<SearchTypeBean> list1 = new ArrayList<>();
    private SearchTypeAdapter mTypeAdapter;
    private SearchLabelAdapter mLabelAdapter;
    private List<String> typeList = new ArrayList<>();//用于存储已选的类型
    private List<String> labelList = new ArrayList<>();//用于存储已选的标签
    private Button mCompletBtn;
    private LoaddingDialog loaddingDialog;
    private String inputkey,url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(R.layout.publish_type_layout);
        this.initTitlebar("选择发布对象", getString(R.string.reset), "");
        gvSearchType = findViewById(R.id.gv_search_type);
        gvSearchLabel = findViewById(R.id.gv_search_label);
        mCompletBtn = findViewById(R.id.bt_complete);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        mTypeAdapter = new SearchTypeAdapter(list, PublishTypeActivity.this);
        gvSearchType.setAdapter(mTypeAdapter);
        mLabelAdapter = new SearchLabelAdapter(list1, PublishTypeActivity.this);
        gvSearchLabel.setAdapter(mLabelAdapter);
        inputkey=getIntent().getStringExtra(Global.INPUTKEY);
        url=getIntent().getStringExtra("url");
        loaddingDialog=new LoaddingDialog(this);
        getMainData();
    }

    private void getMainData() {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(PublishTypeActivity.this).dealException(str)) {
                    JSONObject rootjson=new JSONObject(str);
                    JSONObject resultJson = rootjson.optJSONObject("resultData");
                    list= JSON.parseArray(resultJson.optString("strTrade"),SearchTypeBean.class);
                    list1= JSON.parseArray(resultJson.optString("strLabel"),SearchTypeBean.class);
                    mTypeAdapter = new SearchTypeAdapter(list, PublishTypeActivity.this);
                    gvSearchType.setAdapter(mTypeAdapter);
                    mLabelAdapter = new SearchLabelAdapter(list1, PublishTypeActivity.this);
                    gvSearchLabel.setAdapter(mLabelAdapter);
                    updateSelectList();
                }
                loaddingDialog.dismissAniDialog();
            }
            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(this);
       String neturl="";
       if(url.contains("?")){
           neturl=new StringBuffer(urlManager.getData_url()).append(url).append("&").append(urlManager.getExtraStr()).toString();
       }else {
           neturl=new StringBuffer(urlManager.getData_url()).append(url).append("?").append(urlManager.getExtraStr()).toString();

       }
        networkFactory.start(NetworkFactory.METHOD_GET, neturl, null, successfulCallback, failCallback);
    }

    private void updateSelectList() {
//        list= JSON.parseArray(resultJson.optString("strTrade"),SearchTypeBean.class);
//        list1= JSON.parseArray(resultJson.optString("strLabel"),SearchTypeBean.class);
        String selectStr=getIntent().getStringExtra("select");
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(selectStr);
            List<SearchTypeBean>  tradelist= JSON.parseArray(jsonObject.optString("strRangeTrade"),SearchTypeBean.class);
             for(int i=0;i<list.size();i++){
                 for(int j=0;j<tradelist.size();j++){
                     if(list.get(i).getStrGuid().equals(tradelist.get(j).getStrGuid())){
                         mTypeAdapter.getIsSelected().put(i,true);
                         break;
                     }
                 }
             }
             mTypeAdapter.notifyDataSetChanged();
            List<SearchTypeBean>  labellist=JSON.parseArray(jsonObject.optString("strRangeLabel"),SearchTypeBean.class);
            for(int i=0;i<list1.size();i++){
                for(int j=0;j<labellist.size();j++){
                    if(list1.get(i).getStrGuid().equals(labellist.get(j).getStrGuid())){
                        mLabelAdapter.getIsSelected().put(i,true);
                        break;
                    }
                }
            }
            mLabelAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initListener() {

        mCompletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SearchTypeBean> tradelist=mTypeAdapter.getSelectList();
                if(tradelist.size()==0){
                    Toast.makeText(PublishTypeActivity.this,"信息员类型不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<SearchTypeBean> labelList=mLabelAdapter.getSelectList();
                if(labelList.size()==0){
                    Toast.makeText(PublishTypeActivity.this,"信息员标签不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
               String tradeStr=JSON.toJSONString(tradelist);
               String labelStr=JSON.toJSONString(labelList);
               JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("strRangeTrade",tradeStr);
                    jsonObject.put("strRangeLabel",labelStr);
                } catch (JSONException e) {
                }
                UpdateViewMessage updateViewMessage=new UpdateViewMessage(inputkey,jsonObject.toString());
                EventBus.getDefault().post(updateViewMessage);
                finish();
            }
        });

        rightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeAdapter.reset();
                mLabelAdapter.reset();
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
