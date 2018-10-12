package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.xiante.jingwu.qingbao.Adapter.InformationOfficerAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.InforOfficeBean;
import com.xiante.jingwu.qingbao.Bean.Common.UserEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 信息员列表页
 */
public class InformationOfficersActivity  extends BaseActivity{
    private RecyclerView rvInforOfficer;
    private IndexBar ibIndex;
    private TextView tvSelectIndex;
    private List<InforOfficeBean> conlist=new ArrayList<>();
    private InformationOfficerAdapter mAdapter;
    private LoaddingDialog loaddingDialog;
    boolean isSuccess;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(R.layout.informationlayout);
        rvInforOfficer=findViewById(R.id.rv_infor);
        ibIndex=findViewById(R.id.ib_index);
        tvSelectIndex=findViewById(R.id.tv_select_index);
        this.initTitlebar(getString(R.string.inforOfficer),"","");
        rightImageView.setImageResource(R.drawable.search_history);
        isSuccess = Utils.isSuccess(this);
    }

    @Override
    public void initData() {
        loaddingDialog = new LoaddingDialog(this);
        if (!isSuccess) {
            Toast.makeText(this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        getNetData();
    }

    private void getNetData() {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(InformationOfficersActivity.this).dealException(str)) {
                    conlist = JSON.parseArray(new JSONObject(str).optString("resultData"), InforOfficeBean.class);
                    updateView(conlist);
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
        HashMap<String, String> param = new HashMap<>();
        param.put("isAll", "-1");
        param.put("strName", "");
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getBindUserList(), param, successfulCallback, failCallback);
    }

    private void updateView(List<InforOfficeBean>list) {
        int count= rvInforOfficer.getItemDecorationCount();
        if(count>0){
            for(int i=0;i<count;i++){
                rvInforOfficer.removeItemDecorationAt(i);
            }
        }
        mAdapter = new InformationOfficerAdapter(this, list);
        mAdapter.setItemClickListener(new InformationOfficerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                toUserinfor(position);
            }
        });
        rvInforOfficer.setLayoutManager(new LinearLayoutManager(this));
        rvInforOfficer.setAdapter(mAdapter);

        ibIndex.setmPressedShowTextView(tvSelectIndex).setNeedRealIndex(true).setmLayoutManager((LinearLayoutManager) rvInforOfficer.getLayoutManager());
        ibIndex.setmPressedBackground(Color.parseColor("#999999"));
        ibIndex.setmSourceDatas(list);

        SuspensionDecoration suspensionDecoration = new SuspensionDecoration(this, list);
        suspensionDecoration.setColorTitleBg(Color.parseColor("#eaeaea"));
        suspensionDecoration.setColorTitleFont(Color.parseColor("#333333"));
        rvInforOfficer.addItemDecoration(suspensionDecoration);
    }

    @Override
    public void initListener() {
        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conlist.size()>0){
                    Intent intent=new Intent(InformationOfficersActivity.this,SearchUserActivity.class);
                    intent.putExtra("conlist",(Serializable) conlist);
                    startActivity(intent);
                }else {
                    Toast.makeText(InformationOfficersActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void toUserinfor(int position) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory=OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if(new CodeExceptionUtil(InformationOfficersActivity.this).dealException(str)){
                    UserEntity   userEntity = JSON.parseObject(new JSONObject(str).optString("resultData"), UserEntity.class);
                    Intent intent=new Intent(InformationOfficersActivity.this,UserInforActivity.class);
                    intent.putExtra("user",userEntity);
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
        HashMap<String,String> param=new HashMap<>();
        param.put("strGuid",conlist.get(position).getStrGuid());
        UrlManager urlManager=new UrlManager(this);
       networkFactory.start(NetworkFactory.METHOD_GET,urlManager.getUserInformation(),param,successfulCallback,failCallback);

    }

}
