package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiante.jingwu.qingbao.Adapter.PowerMemberAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.Power;
import com.xiante.jingwu.qingbao.Bean.Common.PowerMemberEntity;
import com.xiante.jingwu.qingbao.Dialog.CodeDialog;
import com.xiante.jingwu.qingbao.Dialog.EditorDialog;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.MessageEvent.PowermemberMessage;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/5/14.
 */

public class PowerMemberActivity extends BaseActivity {

    TextView  powerGroupNumTV,addPowerMemberTV;
    TextView powerGroupNameTV;
    PullToRefreshListView pullToRefreshListView;
    LoaddingDialog loaddingDialog;
    private String strUnityGuid;
    Power power;
    List<PowerMemberEntity> memberEntityList;
    PowerMemberAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_memberlayout);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
//        getNetWork();
    }

    @Override
    public void initView() {
        initTitlebar("详情", "", "");
        powerGroupNameTV = findViewById(R.id.powerGroupNameTV);
        powerGroupNameTV.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        powerGroupNameTV.setImeOptions(EditorInfo.IME_ACTION_DONE);

        powerGroupNumTV = findViewById(R.id.powerGroupNumTV);
        addPowerMemberTV=findViewById(R.id.addPowerMemberTV);
        pullToRefreshListView = findViewById(R.id.powermenmberList);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        loaddingDialog = new LoaddingDialog(this);
        rightImageView.setBackgroundResource(R.drawable.power_groupcode);
    }

    @Override
    public void initData() {
        strUnityGuid = getIntent().getStringExtra("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetWork();
    }

    @Override
    public void initListener() {

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(power!=null){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("strUnityGuid",power.getStrGuid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CodeDialog codeDialog=new CodeDialog(PowerMemberActivity.this,power.getStrUnityName(),jsonObject.toString());
                    codeDialog.show();
                }
            }
        });
        addPowerMemberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! IsNullOrEmpty.isEmpty(strUnityGuid)){
                    Intent intent=new Intent(PowerMemberActivity.this,SearchMemberActivity.class);
                    intent.putExtra("id",strUnityGuid);
                    startActivity(intent);
                }
            }
        });
        powerGroupNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditorDialog mDialog=new EditorDialog(PowerMemberActivity.this,strUnityGuid);
                mDialog.show();
                mDialog.setOnListener(new EditorDialog.OnEditorListener() {
                    @Override
                    public void updateName(String name) {
                        power.setStrUnityName(name);
                        powerGroupNameTV.setText(name);
                    }
                });
            }
        });
    }

    private void leftPowerGroup() {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(PowerMemberActivity.this).dealException(str)) {
                    Intent intent = new Intent(PowerMemberActivity.this, MainActivity.class);
                    startActivity(intent);
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
        param.put("strUserGuid", power.getStrGuid());
        param.put("strUnityGuid", power.getStrUnitGuid());
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getLeftPowerGroup(), param, successfulCallback, failCallback);
    }

    private void getNetWork() {
        boolean isSuccess = Utils.isSuccess(PowerMemberActivity.this);
        if (!isSuccess) {
            Toast.makeText(PowerMemberActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(PowerMemberActivity.this).dealException(str)) {
                    JSONObject resultObject = new JSONObject(str).optJSONObject("resultData");
                    String powerstr = resultObject.optString("power");
                    try {
                        String memberlistStr = resultObject.optString("powerData");
                        power = JSON.parseObject(powerstr, Power.class);
                        memberEntityList = JSON.parseArray(memberlistStr, PowerMemberEntity.class);
                        updateView();
                    } catch (Exception e) {
                        Log.i("fail data", e.getMessage());
                    }
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
                Toast.makeText(PowerMemberActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loaddingDialog.dismissAniDialog();
            }
        };

        UrlManager urlManager = new UrlManager(this);
        HashMap<String, String> param = new HashMap<>();
        String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        param.put("strUnityGuid", strUnityGuid);
        param.put("strAccount", user);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getPowerMember(), param, successfulCallback, failCallback);
    }

    private void updateView() {
        powerGroupNameTV.setText(power.getStrUnityName());
        powerGroupNumTV.setText(new StringBuilder("共").append(power.getTotal()).append("名成员"));
        adapter = new PowerMemberAdapter(this, memberEntityList);
        pullToRefreshListView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMember(PowermemberMessage powermemberMessage){
        getNetWork();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       EventBus.getDefault().unregister(this);
    }
}
