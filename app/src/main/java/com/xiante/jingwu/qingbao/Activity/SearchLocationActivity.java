package com.xiante.jingwu.qingbao.Activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xiante.jingwu.qingbao.Adapter.SearchAddressAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.SearchAddressBean;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.location.AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION;

/**
 * Created by zhong on 2018/5/22.
 */

public class SearchLocationActivity extends BaseActivity {
    MapView mapView;
    AMap aMap;
    ListView addressLV;
    String inputkey = "";
    String address = "";
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private GeocodeSearch geocoderSearch;
    private List<SearchAddressBean> addressBeanList;
    private SearchAddressAdapter addressAdapter;
    private SearchAddressBean selectBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location_activity);
        initView();
        mapView.onCreate(savedInstanceState);
        initData();
        initListener();
        initLocation(this);
    }

    @Override
    public void initView() {
        initTitlebar("地址选择", "确定", "");
        mapView = findViewById(R.id.mapView);
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //显示默认的定位按钮
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        addressLV = findViewById(R.id.addressList);
        View view = LayoutInflater.from(this).inflate(R.layout.header, null);
        addressLV.addHeaderView(view);
    }

    @Override
    public void initData() {
        inputkey = getIntent().getStringExtra(Global.INPUTKEY);
        addressBeanList = new ArrayList<>();
        addressAdapter = new SearchAddressAdapter(addressBeanList, this);
        addressLV.setAdapter(addressAdapter);
    }

    @Override
    public void initListener() {
        rightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UpdateViewMessage(inputkey, JSON.toJSONString(selectBean)));
                finish();
            }
        });

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                searchAddress();
            }
        });

        addressLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setAddressSelecttion(position);
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int p) {
                addressBeanList.clear();
//                List<AoiItem> aoiItemList=regeocodeResult.getRegeocodeAddress().getAois();
//                if(aoiItemList!=null){
//                    for(int i=0;i<aoiItemList.size();i++){
//                        AoiItem aoiItem=aoiItemList.get(i);
//                       SearchAddressBean bean=new SearchAddressBean(aoiItem.getAoiName(),"",aoiItem.getAdCode(),false);
//                        bean.setLatitude(aoiItem.getAoiCenterPoint().getLatitude()+"");
//                        bean.setLongtitude(aoiItem.getAoiCenterPoint().getLongitude()+"");
//                       addressBeanList.add(bean);
//                    }
//                }
                List<PoiItem> poiItemList = regeocodeResult.getRegeocodeAddress().getPois();
                if (poiItemList != null) {
                    for (int i = 0; i < poiItemList.size(); i++) {
                        PoiItem poiItem = poiItemList.get(i);
                        SearchAddressBean bean = new SearchAddressBean(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getAdCode(), false);
                        bean.setLatitude(poiItem.getLatLonPoint().getLatitude() + "");
                        bean.setLongtitude(poiItem.getLatLonPoint().getLongitude() + "");
                        addressBeanList.add(bean);
                    }
                }
                if (addressBeanList.size() > 0) {
                    addressBeanList.get(0).setSelectState(true);
                    selectBean = addressBeanList.get(0);
                }
                addressAdapter.notifyDataSetChanged();

//                List<BusinessArea> businessAreaList=regeocodeResult.getRegeocodeAddress().getBusinessAreas();
//                if(businessAreaList!=null){
//                    for(int i=0;i<businessAreaList.size();i++){
//                        BusinessArea businessArea=businessAreaList.get(i);
//                        SearchAddressBean bean=new SearchAddressBean(businessArea.getName(),businessArea.,poiItem.getAdCode(),false);
//                        addressBeanList.add(bean);
//                    }
//                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    private void searchAddress() {
        LatLng latLng = getMapCenterPoint();
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 500, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    private void initLocation(Context context) {
        mlocationClient = new AMapLocationClient(context);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                                amapLocation.getLatitude(),
                                amapLocation.getLongitude())));
                        searchAddress();

                    } else if (amapLocation.getErrorCode() == ERROR_CODE_FAILURE_LOCATION_PERMISSION) {
                        Toast.makeText(SearchLocationActivity.this, "定位权限关闭", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//设置定位间隔,单位毫秒,默认为2000ms
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    public LatLng getMapCenterPoint() {
        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(mapView.getMeasuredWidth() / 2, mapView.getMeasuredHeight() / 2));
        return pt;
    }

    public void setAddressSelecttion(int position) {
        if(position!=0){
            for (SearchAddressBean b :
                    addressBeanList) {
                b.setSelectState(false);
            }
            addressBeanList.get(position-1).setSelectState(true);
            selectBean = addressBeanList.get(position-1);
            addressAdapter.notifyDataSetChanged();
        }

    }

}
