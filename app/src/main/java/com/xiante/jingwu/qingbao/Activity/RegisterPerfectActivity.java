package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Common.RegionBean;
import com.xiante.jingwu.qingbao.Bean.Common.UserBean;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 注册完善资料第一步
 */
public class RegisterPerfectActivity extends BaseActivity {
    private UserBean userBean;
    private List<RegionBean> dateList;
    private List<RegionBean> foreignList;
    private EditText etUserName;
    private EditText etUserAge;
    private EditText etUserAddress;
    private EditText etUserProfessional;
    private Button btNext;
    private RadioButton rbSexMan;
    private RadioButton rbSexWomen;
    private RadioGroup rg;
    private LoaddingDialog loaddingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerperfectlayout);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        etUserName = findViewById(R.id.et_user_name);
        rbSexMan = findViewById(R.id.rb_sex_man);
        rbSexWomen = findViewById(R.id.rb_sex_women);
        etUserAge = findViewById(R.id.et_user_age);
        etUserAddress = findViewById(R.id.et_user_address);
        etUserProfessional = findViewById(R.id.et_user_professional);
        btNext = findViewById(R.id.bt_next);
        rg = findViewById(R.id.rg_next);
        loaddingDialog = new LoaddingDialog(this);
        this.initTitlebar(getString(R.string.perfectTitle), null, "");
    }

    @Override
    public void initData() {
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (bundle != null) {
            String strResult = intentExtra.getStringExtra("user");
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(strResult);
                JSONObject resultData = jsonObject.optJSONObject("resultData");
                String token = resultData.optJSONObject("person").optString("strGuid");
                getSharedPreferences(Global.SHARE_TOKEN, MODE_PRIVATE).edit().putString(Global.SHARE_TOKEN, token).apply();
                JSONObject region = resultData.optJSONObject("region");
                userBean = JSON.parseObject(resultData.optString("person"), UserBean.class);
                dateList = new ArrayList<>();
                dateList = JSON.parseArray(region.optString("data"), RegionBean.class);
                foreignList = new ArrayList<>();
                foreignList = JSON.parseArray(region.optString("foreign"), RegionBean.class);
                if (!TextUtils.isEmpty(userBean.getIntAge()) && !userBean.getIntAge().equals("0")) {
                    etUserAge.setText(userBean.getIntAge());
                }
                if (!TextUtils.isEmpty(userBean.getStrRegionGuid())) {
                    if(dateList.size()!=0){
                        for (int i=0;i<dateList.size();i++){
                            if(userBean.getStrRegionGuid().equals(dateList.get(i).getStrGuid())){
                                policeAddress=dateList.get(i).getStrAddr();
                            }
                        }
                    }
                    if(foreignList.size()!=0){
                        for (int i=0;i<foreignList.size();i++){
                            if(userBean.getStrRegionGuid().equals(foreignList.get(i).getStrGuid())){
                                policeAddress=foreignList.get(i).getStrAddr();
                            }
                        }
                    }
                }
                String sexStr = userBean.getStrSex();
                if (!TextUtils.isEmpty(userBean.getStrName())) {
                    etUserName.setText(userBean.getStrName());
                }
                if (!TextUtils.isEmpty(sexStr)) {
                    if (sexStr.equals(Global.SEX_MAN)) {
                        strSex = Global.SEX_MAN;
                        rbSexMan.setChecked(true);
                        rbSexWomen.setChecked(false);
                    } else {
                        strSex = Global.SEX_WOMEN;
                        rbSexMan.setChecked(false);
                        rbSexWomen.setChecked(true);
                    }
                }
                if (!TextUtils.isEmpty(userBean.getStrAddress())) {
                    etUserAddress.setText(userBean.getStrAddress());
                }
                if (!TextUtils.isEmpty(userBean.getStrProfession())) {
                    etUserProfessional.setText(userBean.getStrProfession());
                }
                if (!TextUtils.isEmpty(userBean.getStrName()) && !TextUtils.isEmpty(userBean.getIntAge()) && !TextUtils.isEmpty(userBean.getStrAddress()) && !TextUtils.isEmpty(userBean.getStrProfession()) && !TextUtils.isEmpty(userBean.getStrSex())) {
                    btNext.setBackgroundResource(R.drawable.send_power);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    String strSex, strAge, strAddress, strProfession, strName,policeAddress;

    @Override
    public void initListener() {
        setBtnBackgroudListen(etUserName);
        setBtnBackgroudListen(etUserAge);
        setBtnBackgroudListen(etUserAddress);
        setBtnBackgroudListen(etUserProfessional);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sex_man:
                        strSex = Global.SEX_MAN;
                        break;
                    case R.id.rb_sex_women:
                        strSex = Global.SEX_WOMEN;
                        break;
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAge = etUserAge.getText().toString().trim();
                strAddress = etUserAddress.getText().toString().trim();
                strProfession = etUserProfessional.getText().toString().trim();
                strName = etUserName.getText().toString().trim();
                if (TextUtils.isEmpty(strName)) {
                    showToast(getString(R.string.inputUserName));
                    return;
                }
                if (TextUtils.isEmpty(strAge)) {
                    showToast(getString(R.string.inputUserAger));
                    return;
                }
                if (TextUtils.isEmpty(strAddress)) {
                    showToast(getString(R.string.inputUserAddress));
                    return;
                }
                if (TextUtils.isEmpty(strProfession)) {
                    showToast(getString(R.string.inputUserProfessional));
                    return;
                }
                if (TextUtils.isEmpty(strSex)) {
                    showToast(getString(R.string.inputUserSex));
                    return;
                }
                loaddingDialog.showDialog();
                goNext();
            }
        });
    }

    private void setBtnBackgroudListen(EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                strAge = etUserAge.getText().toString().trim();
                strAddress = etUserAddress.getText().toString().trim();
                strProfession = etUserProfessional.getText().toString().trim();
                strName = etUserName.getText().toString().trim();
                if (!TextUtils.isEmpty(strAge) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strProfession) && !TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strSex)) {
                    btNext.setBackgroundResource(R.drawable.send_power);
                    btNext.setEnabled(true);
                } else {
                    btNext.setEnabled(false);
                    btNext.setBackgroundResource(R.drawable.send_gray);
                }
            }
        });
    }

    private void showToast(String str) {
        btNext.setBackgroundResource(R.drawable.send_gray);
        Toast.makeText(RegisterPerfectActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private void goNext() {
        boolean isSuccess = Utils.isSuccess(RegisterPerfectActivity.this);
        if (!isSuccess) {
            Toast.makeText(RegisterPerfectActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        btNext.setEnabled(false);
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) {
                btNext.setEnabled(true);
                if (new CodeExceptionUtil(RegisterPerfectActivity.this).dealException(str)) {
                    Intent intent = new Intent(RegisterPerfectActivity.this, ChooseAddressActivity.class);
                    intent.putExtra("userBean", userBean);
                    intent.putExtra("dateList", (Serializable) dateList);
                    intent.putExtra("policeAddress", policeAddress);
                    intent.putExtra("foreignList", (Serializable) foreignList);
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
                btNext.setEnabled(true);
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strName", strName);
        param.put("strSex", strSex);
        param.put("strAge", strAge);
        param.put("strAddress", strAddress);
        param.put("strProfession", strProfession);
        UrlManager urlManager = new UrlManager(this);

        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getStepOneUrl(), param, mSuccessfulCallback
                , mFailCallback);
    }
}
