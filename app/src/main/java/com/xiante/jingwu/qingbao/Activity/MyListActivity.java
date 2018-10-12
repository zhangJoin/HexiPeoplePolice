package com.xiante.jingwu.qingbao.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.example.myindicator.LinearIndicator;
import com.example.myindicator.TabChangeListener;
import com.xiante.jingwu.qingbao.Adapter.FragmentAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.Fragment.NormalListViewFragment;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/5/9.
 */

public class MyListActivity extends BaseActivity {
    LinearIndicator indicator;
    ViewPager viewPager;
    String url = "";
    LoaddingDialog loaddingDialog;
    ClickEntity titleBarEntity;
    int pagePosition=0;
    private ArrayList<NormalListViewFragment> newsfragmengList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_msg_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        indicator = findViewById(R.id.lineaIndicator);
        viewPager = findViewById(R.id.viewPager);
        indicator.setChoseAndLoseColor(getResources().getColor(R.color.colorPrimary), Color.parseColor("#333333"),
                Color.parseColor("#00000000"), Color.parseColor("#ffffff"));
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        titleBarEntity = (ClickEntity) getIntent().getSerializableExtra(Global.CLICK_ACTION);
        url = titleBarEntity.getStrUrl();
        initTitlebar(titleBarEntity.getStrTitle(), "", titleBarEntity.getStrIco());
    }

    @Override
    public void initListener() {
           indicator.setTabChangeListener(new TabChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

               }

               @Override
               public void onPageSelected(int position) {
                 pagePosition=position;
               }

               @Override
               public void onPageScrollStateChanged(int state) {

               }
           });
    }

    public void getNetData() {
        boolean isSuccess = Utils.isSuccess(MyListActivity.this);
        if (!isSuccess) {
            Toast.makeText(MyListActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MyListActivity.this).dealException(str)) {
                    updateListpart(new JSONObject(str).optJSONObject("resultData"));
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {
                loaddingDialog.dismissAniDialog();
            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };

        UrlManager urlManager = new UrlManager(this);
        String listurl = "";
        if (url.contains("?")) {
            listurl = new StringBuilder(urlManager.getData_url()).append(url).append("&").append(urlManager.getExtraStr()).toString();
        } else {
            listurl = new StringBuilder(urlManager.getData_url()).append(url).append("?").append(urlManager.getExtraStr()).toString();
        }

        networkFactory.start(NetworkFactory.METHOD_GET, listurl, null, successfulCallback, failCallback);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }


    private void updateListpart(JSONObject inforTypeObject) {
        String tagstr = inforTypeObject.optString("tag");
        String urlstr = inforTypeObject.optString("url");

      newsfragmengList = new ArrayList<>();
        List<String> newstag = JSON.parseArray(tagstr, String.class);
        List<String> urllist = JSON.parseArray(urlstr, String.class);
        if (newstag.size() == 0||newstag.size() == 1) {
            indicator.setVisibility(View.GONE);
        }
        for (int i = 0; i < newstag.size(); i++) {
            NormalListViewFragment recycleFragment = new NormalListViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", urllist.get(i));
            bundle.putString("tag", newstag.get(i));
            recycleFragment.setArguments(bundle);
            newsfragmengList.add(recycleFragment);
        }
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), newsfragmengList, newstag);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator.setSelectItem(pagePosition);
        viewPager.setCurrentItem(pagePosition);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
