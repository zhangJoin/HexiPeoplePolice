package com.xiante.jingwu.qingbao.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.example.myindicator.CircleIndicator;
import com.example.myindicator.TabChangeListener;
import com.xiante.jingwu.qingbao.Adapter.SplashAdapter;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by zhong on 2018/4/27.
 */

public class SplashActivity extends BaseActivity {
    int[] imageID = {R.drawable.splash1, R.drawable.splash2,R.drawable.splash3};
    int[] splashNormal = {R.drawable.splash_normal};
    ViewPager splashViewpager;
    CircleIndicator circleIndicator;
    TextView startupTV;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity_layout);
        initView();
        initData();
        initListener();
        requestLocationPermission();
    }

    @Override
    public void initView() {
        splashViewpager = findViewById(R.id.splashViewpager);
        startupTV = findViewById(R.id.startupTV);
        circleIndicator = findViewById(R.id.circleIndicator);
    }

    @Override
    public void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        String first = getSharedPreferences("first", MODE_PRIVATE).getString("first", "");
        if (first.equals("")) {
            splashViewpager.setAdapter(new SplashAdapter(imageID, this));
            getSharedPreferences("first", MODE_PRIVATE).edit().putString("first", "notfirst").commit();
        } else {
            circleIndicator.setVisibility(View.GONE);
            splashViewpager.setAdapter(new SplashAdapter(splashNormal, this));
            getBaseUrl();
        }
        circleIndicator.setChoseAndNormalColor(getResources().getColor(R.color.colorPrimary), Color.parseColor("#eaeaea"));
        circleIndicator.setViewPager(splashViewpager);
    }

    @Override
    public void initListener() {
        startupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startupTV.setEnabled(false);
                getBaseUrl();
            }
        });

        circleIndicator.setTabChangeListener(new TabChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageID.length - 1) {
                    startupTV.setVisibility(View.VISIBLE);
                    AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
                    animation.setDuration(1000);
                    animation.setRepeatCount(0);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            startupTV.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    startupTV.startAnimation(animation);
                } else {
                    startupTV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getBaseUrl() {
        boolean isSuccess = Utils.isSuccess(SplashActivity.this);
        if (!isSuccess) {
            handler.sendEmptyMessageDelayed(101, 3000);
            Toast.makeText(SplashActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory okfactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(SplashActivity.this).dealException(str)) {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject resultObject = jsonObject.optJSONObject("resultData");
                    String data_url = resultObject.optString("dataServer");
                    String file_url = resultObject.optString("fileServer");
                    getSharedPreferences(Global.MAIN_URL, MODE_PRIVATE).edit().putString(Global.MAIN_URL, data_url).commit();
                    getSharedPreferences(Global.FILE_Server_URL, MODE_PRIVATE).edit().putString(Global.FILE_Server_URL, file_url).commit();
                    handler.sendEmptyMessageDelayed(101, 3000);
                }else {
                    handler.sendEmptyMessageDelayed(101, 1000);
                    startupTV.setEnabled(true);
                }
                startupTV.setEnabled(true);
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                handler.sendEmptyMessageDelayed(101, 1000);
                startupTV.setEnabled(true);
            }
        };
        UrlManager urlManager = new UrlManager(this);
        okfactory.start(NetworkFactory.METHOD_GET, urlManager.getBaseUrl(), null, successfulCallback, failCallback);
    }

    public void requestLocationPermission() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
           //            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          //                    Manifest.permission.ACCESS_FINE_LOCATION)) {
         //                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
        //            } else {
            //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 1);
            //  }
        }
    }

}
