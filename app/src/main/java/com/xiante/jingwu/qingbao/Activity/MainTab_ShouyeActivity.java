package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.example.myindicator.LinearIndicator;
import com.example.myindicator.TabChangeListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xiante.jingwu.qingbao.Adapter.FragmentAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Bean.Common.ReportTypeEntity;
import com.xiante.jingwu.qingbao.CustomView.CommonView.AtMostViewPager;
import com.xiante.jingwu.qingbao.CustomView.CommonView.ImageTextButton;
import com.xiante.jingwu.qingbao.Dialog.DownLoadDialog;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.Fragment.ListViewFragment;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class MainTab_ShouyeActivity extends FragmentActivity {
    public static int REQUEST_CODE_SCAN = 111;
    LinearIndicator indicator;
    AtMostViewPager viewPager;
    PullToRefreshScrollView scrollView;
    TextView veryfyNumTV, publishNumTV, xxyNumTV;
    LinearLayout reportTypeContainerLL;
    TextView currentDayTV;
    View unReadMsgView, msgiv;
    List<String> newstag;
    List<ListViewFragment> newsfragmengList;
    int currentNewsIndex = 0;
    LoaddingDialog loaddingDialog;
    ImageView mScanIV;
    boolean isSuccess;
    DownLoadDialog mDownLoadDialog;
    private String isUpdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_shouye);
        EventBus.getDefault().register(this);
        isUpdate = getSharedPreferences(Global.IS_UPDATE, MODE_PRIVATE).getString(Global.IS_UPDATE, "");
        initView();
        initData();
        initListener();
        getMainData();
    }

    public void initView() {
        indicator = findViewById(R.id.newsinditator);
        viewPager = findViewById(R.id.newspager);
        indicator.setChoseAndLoseColor(getResources().getColor(R.color.colorPrimary), Color.parseColor("#333333"),
                Color.parseColor("#00000000"), Color.parseColor("#ffffff"));
        scrollView = findViewById(R.id.shouyeScrollview);
        veryfyNumTV = findViewById(R.id.veryfyNumTV);
        publishNumTV = findViewById(R.id.publishNumTV);
        xxyNumTV = findViewById(R.id.xxyNumTV);
        reportTypeContainerLL = findViewById(R.id.reportTypeContainerLL);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        currentDayTV = findViewById(R.id.currentDayTV);
        unReadMsgView = findViewById(R.id.unReadMsgView);
        msgiv = findViewById(R.id.msgiv);
        mScanIV = findViewById(R.id.scanIV);
        loaddingDialog = new LoaddingDialog(this);
    }

    public void initData() {
        newsfragmengList = new ArrayList<>();
        initCurrentDay();

    }

    public void initListener() {
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                   scrollView.onRefreshComplete();
                   if(newsfragmengList.size()>0){
                       newsfragmengList.get(currentNewsIndex).refreshData();
                   }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                scrollView.onRefreshComplete();
                if(newsfragmengList.size()>0){
                    newsfragmengList.get(currentNewsIndex).loadMore();
                }
            }
        });

        indicator.setTabChangeListener(new TabChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentNewsIndex = position;
                viewPager.setCurrentItem(position);
                changeViewPagerHeight(((ListViewFragment) newsfragmengList.get(position)).getListViewHeight());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        msgiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_ShouyeActivity.this, MyListActivity.class);
                ClickEntity clickEntity = new ClickEntity();
                clickEntity.setStrTitle("我的消息");
                clickEntity.setStrUrl(Global.MY_MSGTYPE);
                intent.putExtra(Global.CLICK_ACTION, clickEntity);
                startActivity(intent);
            }
        });
        mScanIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(MainTab_ShouyeActivity.this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                ZxingConfig mZxingConfig = new ZxingConfig();
                                mZxingConfig.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                                mZxingConfig.setPlayBeep(false);//是否播放提示音
                                mZxingConfig.setShake(false);//是否震动
                                mZxingConfig.setShowAlbum(true);//是否显示相册
                                mZxingConfig.setShowFlashLight(true);//是否显示闪光灯
                                Intent intent = new Intent(MainTab_ShouyeActivity.this, CaptureActivity.class);
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, mZxingConfig);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
//                                Uri packageURI = Uri.parse("package:" + getPackageName());
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                startActivity(intent);

                                Toast.makeText(MainTab_ShouyeActivity.this, "没有权限无法扫描,情开启相关权限", Toast.LENGTH_LONG).show();
                            }
                        }).start();
            }
        });
    }

    private void initCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(year + "").append(" 年").append(month + "").append(" 月").append(day + "").append(" 日");
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (weekday) {
            case Calendar.SUNDAY:
                week = "星期日";
                break;
            case Calendar.MONDAY:
                week = "星期一";
                break;
            case Calendar.TUESDAY:
                week = "星期二";
                break;
            case Calendar.WEDNESDAY:
                week = "星期三";
                break;
            case Calendar.THURSDAY:
                week = "星期四";
                break;
            case Calendar.FRIDAY:
                week = "星期五";
                break;
            case Calendar.SATURDAY:
                week = "星期六";
                break;
        }
        builder.append(" ").append(week);
        currentDayTV.setText(builder.toString());
    }

    public void getMainData() {
        isSuccess = Utils.isSuccess(MainTab_ShouyeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_ShouyeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory okfactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_ShouyeActivity.this).dealException(str)) {
                    JSONObject rootObject = new JSONObject(str);
                    JSONObject resultObject = rootObject.optJSONObject("resultData");
                    JSONObject statiscObject = resultObject.optJSONObject("statistic");
                    updateStatistic(statiscObject);
                 //   updateReportTypeContainer(resultObject.optString("infoType"));
                    updateNewspart(resultObject.optJSONObject("newsType"));

                }
                loaddingDialog.dismissAniDialog();
                AndPermission.with(MainTab_ShouyeActivity.this)
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                if ("1".equals(isUpdate))
                                mDownLoadDialog = new DownLoadDialog(MainTab_ShouyeActivity.this);
                                mDownLoadDialog.setCanceledOnTouchOutside(false);
                                mDownLoadDialog.show();
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Log.e("aaa", "dfdsfdf");
                            }
                        }).start();

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
        okfactory.start(NetworkFactory.METHOD_GET, urlManager.getShouyeUrl(), null, successfulCallback, failCallback);
    }


    public void refreshMainData() {
        isSuccess = Utils.isSuccess(MainTab_ShouyeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_ShouyeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory okfactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_ShouyeActivity.this).dealException(str)) {
                    JSONObject rootObject = new JSONObject(str);
                    JSONObject resultObject = rootObject.optJSONObject("resultData");
                    JSONObject statiscObject = resultObject.optJSONObject("statistic");
                    updateStatistic(statiscObject);
                    updateReportTypeContainer(resultObject.optString("infoType"));
                }

            }

            @Override
            public void success(InputStream ism, long conentLength) {
            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        UrlManager urlManager = new UrlManager(this);
        okfactory.start(NetworkFactory.METHOD_GET, urlManager.getShouyeUrl(), null, successfulCallback, failCallback);
    }

    private void updateReportTypeContainer(String string) {

        final List<ReportTypeEntity> list = JSON.parseArray(string, ReportTypeEntity.class);
        reportTypeContainerLL.removeAllViews();
        if (list != null) {
            LinearLayout.LayoutParams buttomParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int marginLeft = DisplayUtil.dip2px(this, 10);
            buttomParam.setMargins(marginLeft, 0, 0, 0);
            if (list.size() <= 4) {
                buttomParam.setMargins(0, 0, 0, 0);
                buttomParam.width = getResources().getDisplayMetrics().widthPixels / list.size();
            } else {
                int w = (getResources().getDisplayMetrics().widthPixels - marginLeft * 5) * 2 / 9;
                buttomParam.width = w;
            }
            buttomParam.gravity = Gravity.CENTER;
            for (int i = 0; i < list.size(); i++) {
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setLayoutParams(buttomParam);
                ImageTextButton imageTextButton = new ImageTextButton(this);
                imageTextButton.setButtonText(list.get(i).getStrTypeName());
                imageTextButton.setButtomImage(list.get(i).getStrIco());
                imageTextButton.setImageNum(list.get(i).getShowNumUrl());
                imageTextButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                linearLayout.addView(imageTextButton);
                reportTypeContainerLL.addView(linearLayout);
                final int position = i;
                imageTextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(IsNullOrEmpty.isEmpty(list.get(position).getStrUrl())){
                            return;
                        }
                        String type = list.get(position).getClickType();
                        ClickEntity clickEntity = new ClickEntity();
                        clickEntity.setStrUrl(list.get(position).getStrUrl());
                        clickEntity.setStrIco(list.get(position).getStrTtileBar().getStrIco());
                        clickEntity.setStrTitle(list.get(position).getStrTypeName());
                        switch (type) {
                            case "web":
                                Intent intent = new Intent(MainTab_ShouyeActivity.this, WebveiwActivity.class);
                                intent.putExtra(Global.CLICK_ACTION, (Serializable) clickEntity);
                                startActivity(intent);
                                break;
                            case "form":
                                Intent intent1 = new Intent(MainTab_ShouyeActivity.this, InputActivity.class);
                                intent1.putExtra(Global.CLICK_ACTION, (Serializable) clickEntity);
                                startActivity(intent1);
                                break;
                            case "list":
                                Intent listIntent = new Intent(MainTab_ShouyeActivity.this, MyListActivity.class);
                                listIntent.putExtra(Global.CLICK_ACTION, (Serializable) clickEntity);
                                startActivity(listIntent);
                                break;
                        }

                    }
                });
            }
        }
    }

    private void updateNewspart(JSONObject inforTypeObject) {
        String tagstr = inforTypeObject.optString("tag");
        String urlstr = inforTypeObject.optString("url");


        newstag = JSON.parseArray(tagstr, String.class);
        List<String> urllist = JSON.parseArray(urlstr, String.class);
        if (newstag.size() == 0) {
            indicator.setVisibility(View.GONE);
        }
        for (int i = 0; i < newstag.size(); i++) {
            ListViewFragment recycleFragment = new ListViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", urllist.get(i));
            bundle.putString("tag", newstag.get(i));
            recycleFragment.setArguments(bundle);
            newsfragmengList.add(recycleFragment);
        }
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), newsfragmengList, newstag);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

    }


    private void updateStatistic(JSONObject statiscObject) {

        String reportNum = statiscObject.optString("verifyNum");
        veryfyNumTV.setText(reportNum);
        String acceptNum = statiscObject.optString("publishNum");
        publishNumTV.setText(acceptNum);
        String totalScore = statiscObject.optString("xxyNum");
        xxyNumTV.setText(totalScore);
    }


    private void getUnreadMsg() {
        isSuccess = Utils.isSuccess(MainTab_ShouyeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_ShouyeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_ShouyeActivity.this).dealException(str)) {
                    int num = new JSONObject(str).optJSONObject("resultData").optInt("num");
                    if (num > 0) {
                        unReadMsgView.setVisibility(View.VISIBLE);
                    } else {
                        unReadMsgView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {
            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        UrlManager urlManager = new UrlManager(this);
        HashMap<String, String> param = new HashMap<>();
        param.put("isRead", 0 + "");
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getUnReadMsgUrl(), param, successfulCallback, failCallback);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeViewPagerHeight(Integer viewpagerheight) {
        Log.i("listviewchangeheigh", "" + viewpagerheight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewpagerheight);
        viewPager.setLayoutParams(params);
    }

    public void stopLoadMore() {
        scrollView.onRefreshComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String scanStr = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(this, scanStr, Toast.LENGTH_SHORT).show();
                commitAddPowerPerson(scanStr);
            }
        }else if(requestCode == DownLoadDialog.INSTALL_REQUEST_CODE&&resultCode==RESULT_OK ){
          mDownLoadDialog.requestInstall();
        }
    }

    private void commitAddPowerPerson(String scanStr) {
        isSuccess = Utils.isSuccess(MainTab_ShouyeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_ShouyeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_ShouyeActivity.this).dealException(str)) {
                    Toast.makeText(MainTab_ShouyeActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                loaddingDialog.dismissAniDialog();

            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strUserGuid", "");
        param.put("strUnityGuid", "");
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getScanPowerUrl(), param, mSuccessfulCallback
                , mFailCallback);
    }


    //更新首页中间上报信息那一排按钮，有审核数量要经常更新
    private void refreshReportType() {
        isSuccess = Utils.isSuccess(MainTab_ShouyeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_ShouyeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_ShouyeActivity.this).dealException(str)) {
                    updateReportTypeContainer(new JSONObject(str).optJSONObject("resultData").optString("infoType"));
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getRefreshReportUrl(), null, mSuccessfulCallback
                , mFailCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnreadMsg();
        refreshMainData();
//        refreshReportType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return false;
    }

}
