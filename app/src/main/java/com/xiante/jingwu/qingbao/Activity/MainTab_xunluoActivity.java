package com.xiante.jingwu.qingbao.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.amap.api.location.AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION;

/**
 * Created by zhong on 2018/4/28.
 */

public class MainTab_xunluoActivity extends BaseActivity implements AMapLocationListener {

    private TextView currentDayTV, walkTotalTV, walkPerMileTV;
    private MapView xunluoMapview;
    private View go_historyListIV;
    private AMap aMap;
    private TextView xunluoTimeTV, startXunluoTV;
    private LoaddingDialog loaddingDialog;
    private boolean hasXunluo = false;
    private double xunluoTotalMile = 0;
    private AMapLocation lastlocation;
    private final int REPORT_INTERNAL = 3000;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private Timer timer;
    private Handler handler;
    private int xunluoTime = 0;
    private String strAccount = "";
    private Marker lastmarker;
    String strTrackGuid = "";
    private boolean isSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_xunluo);
        initView();
        xunluoMapview.onCreate(savedInstanceState);
        aMap = xunluoMapview.getMap();
        initData();
        initListener();
        initCurrentDay();
        requestLocationPermission();
    }

    @Override
    public void initView() {
        currentDayTV = findViewById(R.id.currentDayTV);
        walkTotalTV = findViewById(R.id.walkTotalTV);
        walkPerMileTV = findViewById(R.id.walkPerMileTV);
        xunluoMapview = findViewById(R.id.xunluoMapview);
        xunluoTimeTV = findViewById(R.id.xunluoTimeTV);
        startXunluoTV = findViewById(R.id.startXunluoTV);
        go_historyListIV = findViewById(R.id.go_historyListIV);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
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
//设置定位参数
//        MyLocationStyle
//        myLocationStyle = new MyLocationStyle();
//        myLocationStyle.interval(3000);
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.location_hide)));
//        myLocationStyle.showMyLocation(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        //     aMap.setMyLocationEnabled(true);
        //  aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        strAccount = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                xunluoTime++;
                int hour = xunluoTime / 3600;
                int minute = (xunluoTime - hour * 300) / 60;
                int second = (xunluoTime - hour * 300) % 60;
                String h = String.format("%02d", hour);
                String m = String.format("%02d", minute);
                String s = String.format("%02d", second);
                xunluoTimeTV.setText(new StringBuilder(h).append(":").append(m).append(":").append(s));
            }
        };

        timer = new Timer();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void initListener() {
        startXunluoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasXunluo) {

                    stopXunluo();
                } else {
                    startXunluo();
                }
            }
        });

        go_historyListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_xunluoActivity.this, HistoryXunluoActivity.class);
                startActivity(intent);
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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            Log.i("location infor", amapLocation.getErrorCode() + "-" + amapLocation.getErrorInfo());
            if (amapLocation.getErrorCode() == 0) {

                if (lastmarker != null) {
                    lastmarker.remove();
                }
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                        amapLocation.getLatitude(),
                        amapLocation.getLongitude())));

                LatLng latLng = new LatLng(
                        amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                lastmarker = aMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_hide))));
                if (hasXunluo) {
                    reportMylocation(amapLocation);
                }
            } else if (amapLocation.getErrorCode() == ERROR_CODE_FAILURE_LOCATION_PERMISSION) {
                Toast.makeText(this, "定位权限关闭", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void startXunluo() {
        isSuccess = Utils.isSuccess(MainTab_xunluoActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_xunluoActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_xunluoActivity.this).dealException(str)) {
                    startXunluoTV.setText("结束巡逻");
                    hasXunluo = true;
                    strTrackGuid = new JSONObject(str).optJSONObject("resultData").optString("strTrackGuid");
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    }, 1000, 1000);
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
        String accout = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        param.put("strTaskGuid", "hexixunluo");
        param.put("strAccount", accout);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getXunluoStart(), param, successfulCallback, failCallback);
    }

    private void stopXunluo() {
        isSuccess = Utils.isSuccess(MainTab_xunluoActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_xunluoActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(MainTab_xunluoActivity.this).dealException(str)) {
                    Toast.makeText(MainTab_xunluoActivity.this, "巡逻结束", Toast.LENGTH_SHORT).show();
                    startXunluoTV.setText("开始巡逻");
                    timer.cancel();
                    xunluoTime = 0;
                    xunluoTimeTV.setText("00:00:00");
                    walkPerMileTV.setText("0m/s");
                    walkTotalTV.setText("0km");
                    hasXunluo = false;
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
        String accout = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        param.put("strTaskGuid", "hexixunluo");
        param.put("strAccount", accout);
        param.put("strTrackGuid", strTrackGuid);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getXunluoEnd(), param, successfulCallback, failCallback);
    }


    private void reportMylocation(AMapLocation amapLocation) {
        isSuccess=Utils.isSuccess(MainTab_xunluoActivity.this);
        if(!isSuccess){
            Toast.makeText(MainTab_xunluoActivity.this,getString(R.string.netError),Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {

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
        if (lastlocation != null) {
            xunluoTotalMile += Utils.getDistance(lastlocation.getLatitude(), lastlocation.getLongitude(), amapLocation.getLatitude(), amapLocation.getLongitude());
            lastlocation = amapLocation;
        }
        double speed = xunluoTotalMile / xunluoTime;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        walkPerMileTV.setText(decimalFormat.format(speed) + "m/s");
        walkTotalTV.setText(decimalFormat.format(xunluoTotalMile) + "km");
        UrlManager urlManager = new UrlManager(this);
        HashMap<String, String> param = new HashMap<>();
        param.put("strLatitude", amapLocation.getLatitude() + "");
        param.put("strLongitude", amapLocation.getLongitude() + "");
        param.put("strAccount", strAccount);
        param.put("strTaskGuid", "hexixunluo");
        param.put("strTrackGuid", strTrackGuid);
        param.put("strAddress", amapLocation.getAddress());
        param.put("strDuration", xunluoTime + "");
        param.put("strSpeed", decimalFormat.format(speed));
        param.put("strDistance", decimalFormat.format(xunluoTotalMile / 1000));
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getXunluo_ReportLocation(), param, successfulCallback, failCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        xunluoMapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xunluoMapview.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stopLocation();
        xunluoMapview.onDestroy();
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
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 1);
            //  }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return false;
    }
}
