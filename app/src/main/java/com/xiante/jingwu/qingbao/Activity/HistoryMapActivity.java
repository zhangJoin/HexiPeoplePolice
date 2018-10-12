package com.xiante.jingwu.qingbao.Activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Common.HistoryXunluoEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/5/17.
 */

public class HistoryMapActivity extends BaseActivity {

    MapView historyMapview;
    TextView totalTimeTV, totalMileTV, speedTV;
    List<LatLng> historyList;
    AMap aMap;
    private LoaddingDialog loaddingdialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_map_activity);
        initView();
        historyMapview.onCreate(savedInstanceState);
        aMap = historyMapview.getMap();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        initTitlebar("我的巡逻", "", "");
        historyMapview = findViewById(R.id.historyMapView);
        totalTimeTV = findViewById(R.id.his_totalMileTV);
        totalMileTV = findViewById(R.id.his_totaltimeTV);
        speedTV = findViewById(R.id.his_speedTV);
    }

    @Override
    public void initData() {
        historyList = new ArrayList<>();
        loaddingdialog = new LoaddingDialog(this);
        HistoryXunluoEntity entity = (HistoryXunluoEntity) getIntent().getSerializableExtra("entity");
        totalTimeTV.setText(entity.getStrDuration());
        totalMileTV.setText(entity.getStrDistance().equals("")?entity.getStrDistance():entity.getStrDistance()+"km");
        speedTV.setText(entity.getStrSpeed().equals("")?entity.getStrSpeed():entity.getStrSpeed()+"m/s");
        if (!Utils.isSuccess(HistoryMapActivity.this)) {
            Toast.makeText(HistoryMapActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        getNetData();
//       initTextData();
//       drawHistoryLine();
    }

    private void drawHistoryLine() {


        if (historyList.size() > 0) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(historyList.get(0)));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            aMap.addMarker(new MarkerOptions().position(historyList.get(0))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start_location))));
            aMap.addPolyline(new PolylineOptions().
                    addAll(historyList).width(10).
                    color(getResources()
                            .getColor(R.color.colorPrimary)));
            aMap.addMarker(new MarkerOptions().position(historyList.get(historyList.size() - 1))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end_location))));
        }

    }

//    private void  initTextData(){
//        historyList.add(new LatLng(39.999391,116.135972));
//        historyList.add(new LatLng(39.898323,116.057694));
//        historyList.add(new LatLng(39.900430,116.265061));
//        historyList.add(new LatLng(39.955192,116.140092));
//    }


    private void getNetData() {
        loaddingdialog = new LoaddingDialog(this);
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(HistoryMapActivity.this).dealException(str)) {
                    JSONArray array = new JSONObject(str).optJSONArray("resultData");
                    for (int i = 0; i < array.length(); i++) {
                        double latitude = array.getJSONObject(i).optDouble("strLatitude");
                        double longitude = array.getJSONObject(i).optDouble("strLongitude");
                        historyList.add(new LatLng(latitude, longitude));
                    }
                    drawHistoryLine();
                }
                loaddingdialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingdialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(this);
        String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        HashMap<String, String> param = new HashMap<>();
        param.put("strAccount", user);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getXunluo_Track(), param, successfulCallback, failCallback);
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        historyMapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyMapview.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyMapview.onDestroy();
    }
}
