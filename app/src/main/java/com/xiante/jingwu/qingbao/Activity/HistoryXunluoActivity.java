package com.xiante.jingwu.qingbao.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiante.jingwu.qingbao.Adapter.History_Xunluo_Adapter;
import com.xiante.jingwu.qingbao.Bean.Common.HistoryXunluoEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/5/17.
 */

public class HistoryXunluoActivity extends BaseActivity {

    private PullToRefreshListView historyLV;

    private History_Xunluo_Adapter adapter;
    private List<HistoryXunluoEntity> historyXunluoEntityList;
    private int pageIndex = 1, pageSize = 20;
    private LoaddingDialog loaddingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_xunluo);
        initView();
        initData();
        initListener();
        getData();
    }

    @Override
    public void initView() {
        initTitlebar("历史巡逻", "", "");
        loaddingDialog = new LoaddingDialog(this);
        historyLV = findViewById(R.id.xunluoLV);
    }

    @Override
    public void initData() {
        historyXunluoEntityList = new ArrayList<>();
        adapter = new History_Xunluo_Adapter(this, historyXunluoEntityList);
        historyLV.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        historyLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getData();
            }
        });

//        historyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }


    public void getData() {
        boolean isSuccess = Utils.isSuccess(HistoryXunluoActivity.this);

        if (!isSuccess) {
            Toast.makeText(HistoryXunluoActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                historyLV.onRefreshComplete();
                if (new CodeExceptionUtil(HistoryXunluoActivity.this).dealException(str)) {
                    String resultObject = new JSONObject(str).optString("resultData");
                    List<HistoryXunluoEntity> templist = JSON.parseArray(resultObject, HistoryXunluoEntity.class);
                    if (templist.size() == 0) {
                        showNodata();
                    }
                    if (pageIndex == 1) {
                        historyXunluoEntityList.clear();
                        historyXunluoEntityList.addAll(templist);
                    } else {
                        historyXunluoEntityList.addAll(templist);
                    }
                    adapter.notifyDataSetChanged();
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
                historyLV.onRefreshComplete();
                loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(this);
        String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        HashMap<String, String> param = new HashMap<>();
        param.put("intPageIndex", pageIndex + "");
        param.put("intPageSize", pageSize + "");
        param.put("strAccount", user);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getXunluo_History(), param, successfulCallback, failCallback);
    }

    private void showNodata() {
        Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
    }


}
