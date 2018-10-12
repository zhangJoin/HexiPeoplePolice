package com.xiante.jingwu.qingbao.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.example.myindicator.LinearIndicator;
import com.xiante.jingwu.qingbao.Adapter.FragmentAdapter;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.Fragment.ScoreFragment;
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

public class MyScoreActivity extends BaseActivity {
    View toScore_ConstructionV, toScore_shoppingV;
    TextView scoreTotalTV, usefulScoreTV, obtionScoreTV;
    LinearIndicator indicator;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    List<String> titlelist, urllist;
    List<Fragment> fragmentList;
    LoaddingDialog loaddingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_score_layout);
        initView();
        initData();
        initListener();
        getNetData();
    }

    @Override
    public void initView() {
        toScore_ConstructionV = findViewById(R.id.toScore_ConstructionV);
        toScore_shoppingV = findViewById(R.id.toScore_shoppingV);
        scoreTotalTV = findViewById(R.id.scoreTotalTV);
        usefulScoreTV = findViewById(R.id.usefulScoreTV);
        obtionScoreTV = findViewById(R.id.obtionScoreTV);
        indicator = findViewById(R.id.score_indicator);
        viewPager = findViewById(R.id.scoreviewpager);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        titlelist = new ArrayList<>();
        urllist = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    @Override
    public void initListener() {
        findViewById(R.id.titlebarBackView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getNetData() {
        boolean isSuccess = Utils.isSuccess(MyScoreActivity.this);
        if (!isSuccess) {
            Toast.makeText(MyScoreActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(MyScoreActivity.this).dealException(str)) {
                    JSONObject resultObject = new JSONObject(str).optJSONObject("resultData");
                    updateStaticData(resultObject.optJSONObject("statistic"));
                    updataScoreList(resultObject.optJSONObject("newsType"));
                }

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
        String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        param.put("strAccount", user);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getScoreList(), param, successfulCallback, failCallback);
    }

    private void updataScoreList(JSONObject newsType) {

        String titlestr = newsType.optString("tag");
        String urlstr = newsType.optString("url");
        titlelist = JSON.parseArray(titlestr, String.class);
        urllist = JSON.parseArray(urlstr, String.class);
        for (int i = 0; i < titlelist.size(); i++) {
            Fragment fragment = new ScoreFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", urllist.get(i));
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titlelist);
        viewPager.setAdapter(fragmentAdapter);
        indicator.setViewPager(viewPager);
    }

    private void updateStaticData(JSONObject statistic) {

        if (statistic != null) {
            scoreTotalTV.setText(statistic.optString("totalScore"));
            usefulScoreTV.setText(statistic.optString("usefulScore"));
            obtionScoreTV.setText(statistic.optString("totalCash"));
        }
    }


}
