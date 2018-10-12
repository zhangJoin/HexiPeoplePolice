package com.xiante.jingwu.qingbao.Activity;

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
import com.xiante.jingwu.qingbao.Adapter.ContactAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ContactEntity;
import com.xiante.jingwu.qingbao.Dialog.ContactDialog;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zhong on 2018/5/9.
 */

public class ContactActivity extends BaseActivity {
    RecyclerView contactRV;
    IndexBar contactindexbar;
    TextView indexSelectView;
    List<ContactEntity> conlist;
    ContactAdapter contactAdapter;
    ContactDialog contactDialog;
    LoaddingDialog loaddingDialog;
    boolean isSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        contactRV = findViewById(R.id.contactRV);
        contactindexbar = findViewById(R.id.contactindexbar);
        indexSelectView = findViewById(R.id.indexSelectView);
        backView = findViewById(R.id.titlebarBackView);
        isSuccess = Utils.isSuccess(this);
    }

    @Override
    public void initData() {
        initTitlebar("常用电话", "", "");
        loaddingDialog = new LoaddingDialog(this);
        if (!isSuccess) {
            Toast.makeText(ContactActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        getNetData();

    }

    @Override
    public void initListener() {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getNetData() {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(ContactActivity.this).dealException(str)) {
                    conlist = JSON.parseArray(new JSONObject(str).optString("resultData"), ContactEntity.class);
                    updateView();
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

        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getUsualContact(), null, successfulCallback, failCallback);

    }


    private void updateView() {

        contactAdapter = new ContactAdapter(this, conlist);
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        contactRV.setAdapter(contactAdapter);

        contactindexbar.setmPressedShowTextView(indexSelectView).setNeedRealIndex(true).setmLayoutManager((LinearLayoutManager) contactRV.getLayoutManager());
        contactindexbar.setmPressedBackground(Color.parseColor("#999999"));
        contactindexbar.setmSourceDatas(conlist);

        SuspensionDecoration suspensionDecoration = new SuspensionDecoration(this, conlist);
        suspensionDecoration.setColorTitleBg(Color.parseColor("#eaeaea"));
        suspensionDecoration.setColorTitleFont(Color.parseColor("#333333"));
        contactRV.addItemDecoration(suspensionDecoration);

        contactAdapter.setContactInterface(new ContactAdapter.ContactInterface() {
            @Override
            public void contact(int position, String telnumber) {
                contactDialog = new ContactDialog(ContactActivity.this, telnumber);
                contactDialog.show();
            }
        });

    }


}
