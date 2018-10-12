package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/5/14.
 */

public class PowerSourceActivity extends BaseActivity {

    public static final String GROUP_OWNER = "1", MANAGER = "2", NORMAL_MEMBER = "3";

    PullToRefreshListView powerLV;
    TextView publishTV;

    List<ModelOneEntity> modeOnelist;
    List<ModeTwoEntity> modeTwolist;
    List<ModeTwoEntity> modeThreelist;
    List<ModeFourEntity> modeFourlist;
    BaseAdapter modelOneListAdapter, modeTwoListAdapter, modeThreeListAdapter, modeFourListAdapter;
    private LoaddingDialog loaddingDialog;
    private String modetype = "", strUserType = "", strUnityGuid = "";
    private ClickEntity clickEntity;
    private int pageindex = 1;
    private int pageSize = 20;
    private View emptyView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peolepower_layout);
        initView();
        initData();
        initListener();
        getData();
    }

    @Override
    public void initView() {
        initTitlebar("力量源", "", "");
        emptyView= LayoutInflater.from(PowerSourceActivity.this).inflate(R.layout.emptydata_layout,null);
        rightImageView.setImageResource(R.drawable.peoplepower);
        powerLV = findViewById(R.id.peoplePowerLV);
        powerLV.setMode(PullToRefreshBase.Mode.BOTH);
        powerLV.setEmptyView(emptyView);
        publishTV = findViewById(R.id.publishMessageTV);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        modeOnelist = new ArrayList<>();
        modeTwolist = new ArrayList<>();
        modeThreelist = new ArrayList<>();
        modeFourlist = new ArrayList<>();
        strUnityGuid=getIntent().getStringExtra("strUnityGuid");
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageindex=1;
        getData();
    }

    @Override
    public void initListener() {
        powerLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindex++;
                getData();
            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PowerSourceActivity.this, PowerMemberActivity.class);
                intent.putExtra("id", strUnityGuid);
                startActivity(intent);
            }
        });

        publishTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickEntity clickEntity=new ClickEntity();
                clickEntity.setStrTitle("发布内容");
                String user=getSharedPreferences(Global.USER_ACCOUNT,MODE_PRIVATE).getString(Global.USER_ACCOUNT,"");
                String token=getSharedPreferences(Global.SHARE_TOKEN,MODE_PRIVATE).getString(Global.SHARE_TOKEN,"");
                String url= new StringBuilder(Global.POWER_PUBLISH)
                        .append("strAccount=").append(user).append("&token=")
                        .append(token).append("&strUnityGuid=").append(strUnityGuid).toString();
                clickEntity.setStrUrl(url);
                Intent intent=new Intent(PowerSourceActivity.this,InputActivity.class);
                intent.putExtra(Global.CLICK_ACTION,clickEntity);
                startActivity(intent);
            }
        });
    }


    public void getData() {
        boolean isSuccess = Utils.isSuccess(PowerSourceActivity.this);
        if (!isSuccess) {
            Toast.makeText(PowerSourceActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(PowerSourceActivity.this).dealException(str)) {
                    JSONObject resultObject = new JSONObject(str).optJSONObject("resultData");
                    modetype = resultObject.optString("type");
                    strUserType = resultObject.optString("strUserType");
                    strUnityGuid = resultObject.optString("strUnityGuid");
                    clickEntity = JSON.parseObject(resultObject.optString("strClick"), ClickEntity.class);

                    if (strUserType.equals(MANAGER)||strUserType.equals(GROUP_OWNER)) {
                        publishTV.setVisibility(View.VISIBLE);
                    } else {
                        publishTV.setVisibility(View.GONE);
                    }
                    dealModeList(resultObject.optString("data"), modetype);
                }
                loaddingDialog.dismissAniDialog();
                powerLV.onRefreshComplete();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                powerLV.onRefreshComplete();
                Toast.makeText(PowerSourceActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(this);
        HashMap<String, String> param = new HashMap<>();
        param.put("intPageIndex", pageindex + "");
        param.put("intPageSize", pageSize + "");
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getPowerSource(), param, successfulCallback, failCallback);
    }


    private void dealModeList(String string, String type) {
        switch (type) {
            case "modelOne":
                List<ModelOneEntity> templist = JSON.parseArray(string, ModelOneEntity.class);
                if (templist.size() == 0) {
                    showNoData();
                    return;
                }
                if (pageindex == 1) {
                    modeOnelist.clear();
                    modeOnelist.addAll(templist);
                    modelOneListAdapter = new ModelOneListAdapter(this, modeOnelist);
                    powerLV.setAdapter(modelOneListAdapter);
                } else {
                    modeOnelist.addAll(templist);
                    modelOneListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelTwo":
                List<ModeTwoEntity> temptwolist = JSON.parseArray(string, ModeTwoEntity.class);
                if (temptwolist.size() == 0) {
                    showNoData();
                    return;
                }
                modeTwolist.addAll(temptwolist);
                if (pageindex == 1) {
                    modeTwolist.clear();
                    modeTwoListAdapter = new ModelTwoListAdapter(this, modeTwolist);
                    powerLV.setAdapter(modeTwoListAdapter);
                } else {
                    modeTwoListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelThree":
                List<ModeTwoEntity> tempthreelist = JSON.parseArray(string, ModeTwoEntity.class);
                if (tempthreelist.size() == 0) {
                    showNoData();
                    return;
                }
                if (pageindex == 1) {
                    modeThreelist.clear();
                    modeThreelist.addAll(tempthreelist);
                    modeThreeListAdapter = new ModelThreeListAdapter(this, modeThreelist);
                    powerLV.setAdapter(modeThreeListAdapter);
                } else {
                    modeThreelist.addAll(tempthreelist);
                    modeThreeListAdapter.notifyDataSetChanged();
                }
                break;
            case "modelFour":
                List<ModeFourEntity> tempfourlist = JSON.parseArray(string, ModeFourEntity.class);
                if (tempfourlist.size() == 0) {
                    showNoData();
                    return;
                }
                if (pageindex == 1) {
                    modeFourlist.clear();
                    modeFourlist.addAll(tempfourlist);
                    modeThreeListAdapter = new ModelFourListAdapter(this, modeFourlist);
                    powerLV.setAdapter(modeThreeListAdapter);
                } else {
                    modeFourlist.addAll(tempfourlist);
                    modeFourListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }





    private void showNoData() {
        Toast.makeText(this, "没有数据了", Toast.LENGTH_LONG).show();
    }

}
