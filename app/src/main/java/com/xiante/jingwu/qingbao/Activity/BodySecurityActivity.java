package com.xiante.jingwu.qingbao.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Common.SecurityEntity;
import com.xiante.jingwu.qingbao.CustomView.CommonView.MapSecurityMarker;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.MessageEvent.NaviMessage;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/5/10.
 *
 */

public class BodySecurityActivity extends BaseActivity {
    MapView mapView;
    AMap aMap;
    Marker currentMarker;
    List<SecurityEntity> securityEntityList;
    LoaddingDialog loaddingDialog;
    Bitmap show_bitmap, hide_bitmap;
    View mylocationv;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker mymarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.body_security);
        mapView = findViewById(R.id.map);
        mylocationv=findViewById(R.id.mylocationv);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setInfoWindowAdapter(new MapSecurityMarker(this));
        //      aMap.getUiSettings().setMyLocationButtonEnabled(true); //显示默认的定位按钮
        //  aMap.setMyLocationEnabled(true);
//        MyLocationStyle locationStyle=new MyLocationStyle();
//        locationStyle.showMyLocation(true);
//        aMap.setMyLocationStyle(locationStyle);
        initView();
        initData();
        initListener();
        getNetData();
        drawDistrict();
    }

    @Override
    public void initView() {
        loaddingDialog = new LoaddingDialog(this);
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

            }
        });
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//设置定位间隔,单位毫秒,默认为2000ms
        mlocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void initData() {
        initTitlebar("安全在身边", "", "");
        show_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_show);
        hide_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_hide);
    }

    @Override
    public void initListener() {
        mylocationv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyMarker();
            }
        });
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mymarker==null||marker.getPosition()!=mymarker.getPosition()){
                    if (currentMarker != null) {
                        currentMarker.hideInfoWindow();
                        currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(hide_bitmap));
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(show_bitmap));
                    marker.showInfoWindow();
                    LatLng var1=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(var1));
                    currentMarker = marker;
                }

                return true;
            }
        });

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker != null&&currentMarker!=mymarker) {
                    currentMarker.hideInfoWindow();
                    currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(hide_bitmap));
                }
            }
        });

    }
    private void showMyMarker(){

        AMapLocation aMapLocation= mlocationClient.getLastKnownLocation();
        LatLng var1=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        if(mymarker!=null){
            mymarker.remove();
        }
        mymarker = aMap.addMarker(new MarkerOptions().position(var1)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.mylocationbg))));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(var1));
    }
    private void addMarkers() {

        if (securityEntityList != null) {
            for (int i = 0; i < securityEntityList.size(); i++) {
                SecurityEntity securityEntity = securityEntityList.get(i);
                LatLng latLng = new LatLng(
                        Double.parseDouble(securityEntity.getStrLatitude()),
                        Double.parseDouble(securityEntity.getStrLongitude()));
                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet("marker")
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_hide))));
                marker.setObject(securityEntity);
            }

        }
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                Double.parseDouble(securityEntityList.get(0).getStrLatitude()),
                Double.parseDouble(securityEntityList.get(0).getStrLongitude()))));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));

    }


    private void drawDistrict(){
        DistrictSearch search = new DistrictSearch(this);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("河西区");//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                ArrayList<DistrictItem> districtItems= districtResult.getDistrict();

                if(districtItems!=null&&districtItems.size()>0){
                    String[] boundary = districtItems.get(0).districtBoundary();
                    if (boundary != null && boundary.length > 0) {
                        List<LatLng> historyList=new ArrayList<>();
                        for (String b : boundary) {
                            historyList.clear();
                            String[] sublist=b.split(";");
                            for (String item :sublist) {
                                String[] split = item.split(",");
                                historyList.add(new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[0])));
                            }
                            drawHistoryLine(historyList);
                        }
                    }

                }


            }
        });//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    private void drawHistoryLine(List<LatLng> historyList) {
        if (historyList.size() > 0) {
            aMap.addPolyline(new PolylineOptions().
                    addAll(historyList).width(10).
                    color(getResources()
                            .getColor(R.color.colorPrimary)));
        }
    }
    private void getNetData() {
        boolean isSuccess = Utils.isSuccess(this);
        if (!isSuccess) {
            Toast.makeText(this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(BodySecurityActivity.this).dealException(str)) {
                    securityEntityList = JSON.parseArray(new JSONObject(str).optString("resultData"), SecurityEntity.class);
                    addMarkers();
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
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getMy_Security(), null, successfulCallback, failCallback);

    }

    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }



    private void openGPSSettings() {
        if (checkGPSIsOpen()) {

        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle("定位提示")
                    .setMessage("请开启gps定位，否则无法获取您的位置")
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                    .setPositiveButton("前去设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, 12);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealInputViewUpdate(NaviMessage naviMessage){

        if (Build.VERSION.SDK_INT >= 26&&!checkGPSIsOpen()) {
            openGPSSettings();
        }else {

            AMapLocation location=mlocationClient.getLastKnownLocation();
            Poi start = new Poi("我的位置", new com.amap.api.maps.model.LatLng(location.getLatitude(),location.getLongitude()), "");

            Poi end = new Poi(naviMessage.getSecurityEntity().getStrUnitName(), new com.amap.api.maps.model.LatLng( Double.parseDouble(naviMessage.getSecurityEntity().getStrLatitude()),Double.parseDouble( naviMessage.getSecurityEntity().getStrLongitude())), "");
            AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), new INaviInfoCallback() {
                @Override
                public void onInitNaviFailure() {

                }

                @Override
                public void onGetNavigationText(String s) {

                }

                @Override
                public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

                }

                @Override
                public void onArriveDestination(boolean b) {

                }

                @Override
                public void onStartNavi(int i) {

                }

                @Override
                public void onCalculateRouteSuccess(int[] ints) {

                }

                @Override
                public void onCalculateRouteFailure(int i) {

                }

                @Override
                public void onStopSpeaking() {

                }

                @Override
                public void onReCalculateRoute(int i) {

                }

                @Override
                public void onExitPage(int i) {

                }

                @Override
                public void onStrategyChanged(int i) {

                }

                @Override
                public View getCustomNaviBottomView() {
                    return null;
                }

                @Override
                public View getCustomNaviView() {
                    return null;
                }

                @Override
                public void onArrivedWayPoint(int i) {

                }
            });


        }


    }




    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
