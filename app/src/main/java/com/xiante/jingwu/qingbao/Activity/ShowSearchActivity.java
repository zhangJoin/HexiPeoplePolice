package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class ShowSearchActivity extends BaseActivity {
    private RecyclerView rvInforOfficer;
    private IndexBar ibIndex;
    private TextView tvSelectIndex;
    private InformationOfficerAdapter mAdapter;
    private List<InforOfficeBean>dateList;
    private ImageView ivEmpty;
    private FrameLayout flSearch;
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
        setContentView(R.layout.showsearchlayout);
        rvInforOfficer=findViewById(R.id.rv_search_show);
        ibIndex=findViewById(R.id.ib_index_show);
        ivEmpty = findViewById(R.id.iv_empty);
        flSearch=findViewById(R.id.fl_search);
        tvSelectIndex=findViewById(R.id.tv_select_index_show);
        this.initTitlebar("查询结果","","");
    }

    @Override
    public void initData() {
        loaddingDialog=new LoaddingDialog(this);
         dateList = (List<InforOfficeBean>) getIntent().getSerializableExtra("dateList");
        if (dateList.size()!=0) {
            ivEmpty.setVisibility(View.GONE);
            updateView(dateList);
        }else{
            flSearch.setVisibility(View.GONE);
            ivEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void updateView(List<InforOfficeBean> dateList) {
        mAdapter = new InformationOfficerAdapter(this, dateList);
        rvInforOfficer.setLayoutManager(new LinearLayoutManager(this));
        rvInforOfficer.setAdapter(mAdapter);

        ibIndex.setmPressedShowTextView(tvSelectIndex).setNeedRealIndex(true).setmLayoutManager((LinearLayoutManager) rvInforOfficer.getLayoutManager());
        ibIndex.setmPressedBackground(Color.parseColor("#00ffffff"));
        ibIndex.setmSourceDatas(dateList);

        SuspensionDecoration suspensionDecoration = new SuspensionDecoration(this, dateList);
        suspensionDecoration.setColorTitleBg(Color.parseColor("#eaeaea"));
        suspensionDecoration.setColorTitleFont(Color.parseColor("#333333"));
        rvInforOfficer.addItemDecoration(suspensionDecoration);
        mAdapter.setItemClickListener(new InformationOfficerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                 toUserinfor(position);
            }
        });
    }

    @Override
    public void initListener() {

    }

    private void toUserinfor(int position) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if(new CodeExceptionUtil(ShowSearchActivity.this).dealException(str)){
                    UserEntity userEntity = JSON.parseObject(new JSONObject(str).optString("resultData"), UserEntity.class);
                    Intent intent=new Intent(ShowSearchActivity.this,UserInforActivity.class);
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
        param.put("strGuid",dateList.get(position).getStrGuid());
        UrlManager urlManager=new UrlManager(this);
        networkFactory.start(NetworkFactory.METHOD_GET,urlManager.getUserInformation(),param,successfulCallback,failCallback);
    }


}
