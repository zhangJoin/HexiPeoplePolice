package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Adapter.ForeginAdapter;
import com.xiante.jingwu.qingbao.Adapter.RegionAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.RegionBean;
import com.xiante.jingwu.qingbao.Bean.Common.UserBean;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * 注册完善资料——区域选择
 */
public class ChooseAddressActivity extends BaseActivity {
    private GridView gvAddress;
    private GridView gvOtherAddress;
    private Button btAddressNext;
    private LoaddingDialog loaddingDialog;
    private UserBean userBean;
    private RegionAdapter mRegionAdapter;
    private ForeginAdapter mForeginAdapter;
    private List<RegionBean> dateList;
    private List<RegionBean> foreignList;
    private String regionGuidStr, strRegionAddress, strPhone,policeAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseaddresslayout);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        gvAddress = findViewById(R.id.gv_address);
        gvOtherAddress = findViewById(R.id.gv_other_address);
        btAddressNext = findViewById(R.id.bt_address_next);
        loaddingDialog = new LoaddingDialog(this);
        this.initTitlebar(getString(R.string.addressTitle), null, "");
    }

    @Override
    public void initData() {
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (bundle != null) {
            userBean = (UserBean) intentExtra.getSerializableExtra("userBean");
            dateList = (List<RegionBean>) intentExtra.getSerializableExtra("dateList");
            foreignList = (List<RegionBean>) intentExtra.getSerializableExtra("foreignList");
            policeAddress = intentExtra.getStringExtra("policeAddress");
            mRegionAdapter = new RegionAdapter(dateList, ChooseAddressActivity.this);
            gvAddress.setAdapter(mRegionAdapter);

            mForeginAdapter = new ForeginAdapter(foreignList, ChooseAddressActivity.this);
            gvOtherAddress.setAdapter(mForeginAdapter);
            regionGuidStr = userBean.getStrRegionGuid();
            if (!TextUtils.isEmpty(regionGuidStr)) {
                btAddressNext.setBackgroundResource(R.drawable.send_power);
                for (int i = 0; i < dateList.size(); i++) {
                    if (regionGuidStr.equals(dateList.get(i).getStrAddr())) {
                        strPhone=dateList.get(i).getStrRegionPhone();
                        strRegionAddress = dateList.get(i).getStrRegionName();
                        mRegionAdapter.setSeclection(i);
                        mRegionAdapter.notifyDataSetChanged();
                    }
                }
                for (int i = 0; i < foreignList.size(); i++) {
                    if (regionGuidStr.equals(foreignList.get(i).getStrAddr())) {
                        strPhone=foreignList.get(i).getStrRegionPhone();
                        strRegionAddress = foreignList.get(i).getStrRegionName();
                        mForeginAdapter.setSeclection(i);
                        mForeginAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void initListener() {
        gvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                regionGuidStr = dateList.get(position).getStrAddr();
                strRegionAddress = dateList.get(position).getStrRegionName();
                strPhone = dateList.get(position).getStrRegionPhone();
                policeAddress=dateList.get(position).getStrAddr();
                btAddressNext.setBackgroundResource(R.drawable.send_power);
                mForeginAdapter = new ForeginAdapter(foreignList, ChooseAddressActivity.this);
                gvOtherAddress.setAdapter(mForeginAdapter);
                mRegionAdapter.setSeclection(position);
                mRegionAdapter.notifyDataSetChanged();
            }
        });
        gvOtherAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btAddressNext.setBackgroundResource(R.drawable.send_power);
                regionGuidStr = foreignList.get(position).getStrAddr();
                strRegionAddress = foreignList.get(position).getStrRegionName();
                strPhone = foreignList.get(position).getStrRegionPhone();
                policeAddress=foreignList.get(position).getStrAddr();
                mRegionAdapter = new RegionAdapter(dateList, ChooseAddressActivity.this);
                gvAddress.setAdapter(mRegionAdapter);
                mForeginAdapter.setSeclection(position);
                mForeginAdapter.notifyDataSetChanged();
            }
        });
        btAddressNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = Utils.isSuccess(ChooseAddressActivity.this);
                if(!isSuccess){
                    Toast.makeText(ChooseAddressActivity.this,getString(R.string.netError),Toast.LENGTH_SHORT).show();
                    return;
                }
                goNext();
            }
        });
    }

    private void goNext() {
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(ChooseAddressActivity.this).dealException(str)) {
                    Intent intent = new Intent(ChooseAddressActivity.this, RegisterOkActivity.class);
                    intent.putExtra("userBean", userBean);
                    intent.putExtra("strRegionAddress", strRegionAddress);
                    intent.putExtra("policeAddress", policeAddress);
                    intent.putExtra("strPhone", strPhone);
                    startActivity(intent);
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
        param.put("strRegionGuid", regionGuidStr);
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getStepTwoUrl(), param, mSuccessfulCallback
                , mFailCallback);
    }

}
