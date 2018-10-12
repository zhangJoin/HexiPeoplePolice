package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.DateUtil;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 搜索信息员页面
 */
public class SearchUserActivity extends BaseActivity {
    private EditText etSearch;
    private MyGridView gvSearchType;
    private MyGridView gvSearchLabel;
    private List<SearchTypeBean> allTypelist = new ArrayList<>();
    private List<SearchTypeBean> allLableList = new ArrayList<>();
    private SearchTypeAdapter mTypeAdapter;
    private SearchLabelAdapter mLabelAdapter;
    private List<SearchTypeBean> typeList = new ArrayList<>();//用于存储已选的类型
    private List<SearchTypeBean> labelList = new ArrayList<>();//用于存储已选的标签
    private Button mCompletBtn;
    private List<InforOfficeBean>conlist;
    private LoaddingDialog loaddingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(R.layout.searchuserlayout);
        this.initTitlebar(getString(R.string.search), getString(R.string.reset), "");
        etSearch = findViewById(R.id.et_search);
        gvSearchType = findViewById(R.id.gv_search_type);
        gvSearchLabel = findViewById(R.id.gv_search_label);
        mCompletBtn = findViewById(R.id.bt_complete);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        conlist= (List<InforOfficeBean>) getIntent().getSerializableExtra("conlist");
        getMainData();
    }

    private void getMainData() {
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(SearchUserActivity.this).dealException(str)) {
                    JSONObject rootjson=new JSONObject(str);
                    JSONObject resultJson = rootjson.optJSONObject("resultData");
                    allTypelist= JSON.parseArray(resultJson.optString("strTrade"),SearchTypeBean.class);
                    allLableList= JSON.parseArray(resultJson.optString("strLabel"),SearchTypeBean.class);
                    mTypeAdapter = new SearchTypeAdapter(allTypelist, SearchUserActivity.this);
                    gvSearchType.setAdapter(mTypeAdapter);
                    mLabelAdapter = new SearchLabelAdapter(allLableList, SearchUserActivity.this);
                    gvSearchLabel.setAdapter(mLabelAdapter);
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
        String useraccout=getSharedPreferences(Global.USER_ACCOUNT,MODE_PRIVATE).getString(Global.USER_ACCOUNT,"");
        HashMap<String,String> param=new HashMap<>();
        param.put("strAccount",useraccout);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getQueryUserUrl(), param, successfulCallback, failCallback);
    }

    @Override
    public void initListener() {
        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                typeList=mTypeAdapter.getSelectList();
                labelList=mLabelAdapter.getSelectList();
                if(typeList.size()!=0||labelList.size()!=0){
                    typeList.clear();
                    labelList.clear();
                    mTypeAdapter = new SearchTypeAdapter(allTypelist, SearchUserActivity.this);
                    gvSearchType.setAdapter(mTypeAdapter);
                    mLabelAdapter = new SearchLabelAdapter(allLableList, SearchUserActivity.this);
                    gvSearchLabel.setAdapter(mLabelAdapter);
                }
            }
        });


        mCompletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<InforOfficeBean>conEditList=new ArrayList<>();
                List<InforOfficeBean>tempList=new ArrayList<>();
                String etSearchStr = etSearch.getText().toString().trim();
                for (int i=0;i<conlist.size();i++){
                    if(conlist.get(i).getStrName().contains(etSearchStr)){
                       conEditList.add(conlist.get(i));
                    }
                }
                if(conEditList.size()!=0){//比对type行业类别
                    typeList=mTypeAdapter.getSelectList();
                    if(typeList.size()!=0) {
                        String s = DateUtil.getTypeString(typeList);
                        for (int i=0;i<conEditList.size();i++){
                            if (!TextUtils.isEmpty(conEditList.get(i).getStrTradeName())){
                                String strTradeName = conEditList.get(i).getStrTradeName();
                                if(strTradeName.contains(s)){
                                    tempList.add(conEditList.get(i));
                                }
                            }
                        }
                    }else{
                        tempList=conEditList;
                    }
                    if (tempList.size()!=0) {//对比label类别
                        List<InforOfficeBean> temp=new ArrayList<>();
                        labelList=mLabelAdapter.getSelectList();
                      if(labelList.size()!=0){
                          for (int i=0;i<tempList.size();i++){
                              for (int j=0;j<labelList.size();j++){
                                  if(tempList.get(i).getStrLabelName().contains(labelList.get(j).getStrValue())){
                                      temp.add(tempList.get(i));
                                      break;
                                  }
                              }
                          }
                          tempList=temp;
                      }
                    }
                }
                if(TextUtils.isEmpty(etSearchStr)&&typeList.size()==0&&labelList.size()==0){
                    Toast.makeText(SearchUserActivity.this,"请输入或选择搜索条件",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(SearchUserActivity.this,ShowSearchActivity.class);
                intent.putExtra("dateList",(Serializable) tempList);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        typeList.clear();
        labelList.clear();
    }
}
