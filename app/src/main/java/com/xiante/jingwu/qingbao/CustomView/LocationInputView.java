package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiante.jingwu.qingbao.Activity.SearchLocationActivity;
import com.xiante.jingwu.qingbao.Bean.Common.SearchAddressBean;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zhong on 2018/5/22.
 */

public class LocationInputView extends LinearLayout implements InputView {

    private TextView nameView;
    private EditText addressET;
    private View go_selectAddressV;
    private InputItemBean inputItemBean;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    SearchAddressBean selectbean;
    private boolean useNetData=false;
    public LocationInputView(Context context) {
        super(context);
        init(context);
    }

    public LocationInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(final Context context){
        View.inflate(context, R.layout.location_input_view,this);
        nameView=findViewById(R.id.inputNameTV);
        addressET=findViewById(R.id.inputValueET);
        go_selectAddressV=findViewById(R.id.go_selectLocationView);
        go_selectAddressV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SearchLocationActivity.class);
                intent.putExtra(Global.INPUTKEY,inputItemBean.getStrField());
                context.startActivity(intent);
            }
        });
        selectbean=new SearchAddressBean();
        initLocation(context);
    }

    private void initLocation(final Context context){
        mlocationClient = new AMapLocationClient(context);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(useNetData){
                    return;
                }
                if (aMapLocation.getErrorCode() == 0) {
                    selectbean.setLongtitude(aMapLocation.getLongitude()+"");
                    selectbean.setLatitude(aMapLocation.getLatitude()+"");
                    selectbean.setAdcode(aMapLocation.getAdCode());
                    if(IsNullOrEmpty.isEmpty(aMapLocation.getAdCode())){
                        selectbean.setAdcode("定位不成功");
                    }
                    addressET.setText(aMapLocation.getAddress());
                    selectbean.setAddress(aMapLocation.getAddress());
                    if(IsNullOrEmpty.isEmpty(aMapLocation.getAddress())){
                        selectbean.setAddress("定位不成功");
                        addressET.setText("定位不成功");
                    }
                }else {
                    selectbean.setAdcode("定位不成功");
                    selectbean.setAddress("定位不成功");
                    addressET.setText("定位不成功");
                }
            }
        });
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//设置定位间隔,单位毫秒,默认为2000ms
        mlocationClient.setLocationOption(mLocationOption);

        AndPermission.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        mlocationClient.startLocation();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                        Toast.makeText(context, "没有同意位置权限，不能进行定位", Toast.LENGTH_LONG).show();
                    }
                }).start();



    }

    @Override
    public UploadBean getUploadValue() {
        String value="";
        selectbean.setAddress(addressET.getText().toString());
        value=JSON.toJSONString(selectbean);
        try {
            value= URLEncoder.encode(value,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(Global.LOCATION, value);
    }

    @Override
    public boolean checkUploadValue()
    {
        if(inputItemBean.getIsMust().equals("1")){
             if(IsNullOrEmpty.isEmpty(addressET.getText().toString())){
                 Toast.makeText(getContext(),inputItemBean.getStrFieldName()+"不能为空",Toast.LENGTH_SHORT).show();
                 return false;
             }else {
                 return true;
             }
        }else {
         return true;
        }
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
      this.inputItemBean=inputItemBean;
      nameView.setText(inputItemBean.getStrFieldName()+":");
    }

    @Override
    public void updateInputView(String string) {
        useNetData=false;
        SearchAddressBean bean= JSON.parseObject(string,SearchAddressBean.class);
        this.selectbean=bean;
        addressET.setText(bean.getFirstName());
    }


    public void updateNetDataView(String string) {
        useNetData=true;
        SearchAddressBean bean= JSON.parseObject(string,SearchAddressBean.class);
        this.selectbean=bean;
        addressET.setText(bean.getAddress());

    }

    public SearchAddressBean getSelectbean() {
        selectbean.setAddress(addressET.getText().toString());
        return selectbean;
    }
}
