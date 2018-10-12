package com.xiante.jingwu.qingbao.Activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Manager.ActivityManager;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.amap.api.location.AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION;

/**
 * Created by zhong on 2018/4/19.
 */

public class MainActivity extends ActivityGroup implements AMapLocationListener {
    @InjectView(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @InjectView(android.R.id.tabs)
    TabWidget tabs;
    @InjectView(R.id.view_tab_host)
    TabHost viewTabHost;
    private ArrayList<View> bottomArrayList;
    String[] tabtitle={"首页","我的"};
    int[] tabBackgroud={R.drawable.main_tab_shouye, R.drawable.main_tab_me};
    String[] tabActivity={"MainTab_ShouyeActivity","MainTab_MeActivity"};
    long lastTime=0;

    private final int REPORT_INTERNAL = 60*1000;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarColor(this,R.color.texthintcolor);
        setContentView(R.layout.activity_maintab);
        lastTime=System.currentTimeMillis();
        ButterKnife.inject(this);
        ActivityManager.getInstance().addActivity(this);
        initView();
        initData();
        initListener();
        initTabItem(tabtitle,tabBackgroud,viewTabHost,tabActivity);

    }



    public void initView() {

    }

    public void initData() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(REPORT_INTERNAL);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    public void initListener() {

    }


    private void initTabItem(String[] itemTitle, int[] itemIcon, TabHost tabHost, String[] toActivity) {
        bottomArrayList = new ArrayList<View>();
        // 加载TabSpec
        tabHost.setup(getLocalActivityManager());
        tabHost.setCurrentTab(0);//默认选中第一个
        for (int i = 0; i < itemTitle.length; i++) {
            RelativeLayout tab = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_tabhost, null);
            ImageView icon = (ImageView) tab.findViewById(R.id.icon);
            icon.setBackgroundResource(itemIcon[i]);
            TextView title = (TextView) tab.findViewById(R.id.title);
            title.setText(itemTitle[i]);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec("Tag" + i);
            tabSpec.setIndicator(tab);
            Intent intent = new Intent();
            intent.setClassName(this, "com.xiante.jingwu.qingbao.Activity." + toActivity[i]);
            tabSpec.setContent(intent);
            tabHost.addTab(tabSpec);
            bottomArrayList.add(tab);
        }
        //设置标签栏背景颜色
        TabWidget tw = tabHost.getTabWidget();
        tw.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            long curtime=System.currentTimeMillis();
            if(curtime-lastTime<2000){
                ActivityManager.getInstance().finishAll();
            }else {
                lastTime=System.currentTimeMillis();
                Toast.makeText(this,"再按一次退出应用",Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                reportPosition(amapLocation.getLatitude()+"",amapLocation.getLongitude()+"");
            } else if (amapLocation.getErrorCode() == ERROR_CODE_FAILURE_LOCATION_PERMISSION) {
                // Toast.makeText(this, "定位权限关闭", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void reportPosition(String latitude,String lontitude){
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {

            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        UrlManager urlManager=new UrlManager(this);
        HashMap<String,String> param=new HashMap<>();
        String user=getSharedPreferences(Global.USER_ACCOUNT,MODE_PRIVATE).getString(Global.USER_ACCOUNT,"");
        param.put("strAccount",user);
        param.put("strLatitude",latitude);
        param.put("strLongitude",lontitude);
        networkFactory.start(NetworkFactory.METHOD_GET,urlManager.getMyPosition(),param, successfulCallback,failCallback);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        viewTabHost.setCurrentTab(0);
    }
}
