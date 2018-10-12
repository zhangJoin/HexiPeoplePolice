package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.Manager.ActivityManager;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.AESUtil;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by zhong on 2018/5/2.
 */

public class LoginActivity extends BaseActivity {

    EditText useraccoutET, userpsdET;
    View go_forgetPsdTV;
    Button commitBT;
    View psdControlView, cancelView;
    boolean showpsd = false;
    LoaddingDialog loaddingDialog;
    TextView mTvGoRegister;
    boolean isSuccess;
    public final String REGISTER="REGISTER",FORGET="FORGET";
    public String ACTION="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        initView();
        initData();
        initListener();
        autoLogin();
    }


    @Override
    public void initView() {
        ActivityManager.getInstance().finishOthers(this);
        useraccoutET = findViewById(R.id.useraccoutET);
        userpsdET = findViewById(R.id.userpadET);
        commitBT = findViewById(R.id.commitSureBT);
        psdControlView = findViewById(R.id.psdControlView);
        cancelView = findViewById(R.id.finishTV);
        loaddingDialog = new LoaddingDialog(this);
        mTvGoRegister = findViewById(R.id.tv_goRegister);
        go_forgetPsdTV = findViewById(R.id.go_forgetPsdTV);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        commitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseUrl();
            }
        });
        psdControlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showpsd) {
                    psdControlView.setBackgroundResource(R.drawable.hide_psd);
                    userpsdET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    psdControlView.setBackgroundResource(R.drawable.show_psd);
                    userpsdET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                showpsd = !showpsd;
                userpsdET.setSelection(userpsdET.getText().toString().length());
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        go_forgetPsdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACTION=FORGET;
                UrlManager urlManager=new UrlManager(LoginActivity.this);
                if(IsNullOrEmpty.isEmpty(urlManager.getData_url())){
                    updateBaseUrl();
                }else {
                    Intent intent = new Intent(LoginActivity.this, ForgetPsdActivity.class);
                    startActivity(intent);
                }
            }
        });
        setBtnBackgroudListen(useraccoutET);
        setBtnBackgroudListen(userpsdET);
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
                changeBtnCommitBackground();
            }
        });
    }

    private void changeBtnCommitBackground() {
        String userAccount = useraccoutET.getText().toString();
        String userPwd = userpsdET.getText().toString();
        if (!TextUtils.isEmpty(userAccount) && !TextUtils.isEmpty(userPwd)) {
            commitBT.setEnabled(true);
            commitBT.setBackgroundResource(R.drawable.send_power);
        } else {
            commitBT.setEnabled(false);
            commitBT.setBackgroundResource(R.drawable.send_gray1);
        }
    }

    private void login() {

        final String account = useraccoutET.getText().toString();
        if (account.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        String psd = userpsdET.getText().toString();
        if (psd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        isSuccess = Utils.isSuccess(LoginActivity.this);
        if (!isSuccess) {
            Toast.makeText(LoginActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                CodeExceptionUtil codeExceptionUtil = new CodeExceptionUtil(LoginActivity.this);
                if (codeExceptionUtil.dealException(str)) {
                    ActivityManager.getInstance().finishOthers(LoginActivity.this);
                    getSharedPreferences(Global.USER_PASSWORD, MODE_PRIVATE).edit().putString(Global.USER_PASSWORD, userpsdET.getText().toString()).commit();
                    JSONObject rootjson = new JSONObject(str);
                    JSONObject resultJson = rootjson.optJSONObject("resultData");
                    String token = resultJson.optJSONObject("person").optString("strGuid");
                    String isUpdate = resultJson.optJSONObject("person").optString("isUpdate");
                    String appUrl = resultJson.optJSONObject("person").optString("appUrl");
                    String updateMust = resultJson.optJSONObject("person").optString("updateMust");
                    getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).edit().putString(Global.USER_ACCOUNT, account).commit();
                    getSharedPreferences(Global.IS_UPDATE, MODE_PRIVATE).edit().putString(Global.IS_UPDATE, isUpdate).commit();
                    getSharedPreferences(Global.APP_URL, MODE_PRIVATE).edit().putString(Global.APP_URL, appUrl).commit();
                    getSharedPreferences(Global.UPDATE_MUSE, MODE_PRIVATE).edit().putString(Global.UPDATE_MUSE, updateMust).commit();
                    getSharedPreferences(Global.SHARE_TOKEN, MODE_PRIVATE).edit().putString(Global.SHARE_TOKEN, token).commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        HashMap<String, String> param = new HashMap<>();
        param.put("strAccount", account);
       
        CrashReport.setUserId(account);
        String psdbyte=AESUtil.encodeByMD5(psd);
        param.put("strPasswd", psdbyte);
        UrlManager urlManager = new UrlManager(this);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getLoginUrl(), param, successfulCallback, failCallback);
    }


    private void getBaseUrl() {
        isSuccess = Utils.isSuccess(LoginActivity.this);
        if (!isSuccess) {
            Toast.makeText(LoginActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory okfactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(LoginActivity.this).dealException(str)) {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject resultObject = jsonObject.optJSONObject("resultData");
                    String data_url = resultObject.optString("dataServer");
                    String file_url = resultObject.optString("fileServer");
                    getSharedPreferences(Global.MAIN_URL, MODE_PRIVATE).edit().putString(Global.MAIN_URL, data_url).commit();
                    getSharedPreferences(Global.FILE_Server_URL, MODE_PRIVATE).edit().putString(Global.FILE_Server_URL, file_url).commit();
                    login();
                }

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
        okfactory.start(NetworkFactory.METHOD_GET, urlManager.getBaseUrl(), null, successfulCallback, failCallback);
    }


    private void autoLogin() {

        String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        String psd = getSharedPreferences(Global.USER_PASSWORD, MODE_PRIVATE).getString(Global.USER_PASSWORD, "");
        userpsdET.setText(psd);
        useraccoutET.setText(user);
        if (!IsNullOrEmpty.isEmpty(psd) && !IsNullOrEmpty.isEmpty(user)) {
//            userpsdET.setText(psd);
//            useraccoutET.setText(user);
            getBaseUrl();
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        autoLogin();
    }


    private void updateBaseUrl() {
        isSuccess = Utils.isSuccess(LoginActivity.this);
        if (!isSuccess) {
            Toast.makeText(LoginActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory okfactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(LoginActivity.this).dealException(str)) {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject resultObject = jsonObject.optJSONObject("resultData");
                    String data_url = resultObject.optString("dataServer");
                    String file_url = resultObject.optString("fileServer");
                    getSharedPreferences(Global.MAIN_URL, MODE_PRIVATE).edit().putString(Global.MAIN_URL, data_url).commit();
                    getSharedPreferences(Global.FILE_Server_URL, MODE_PRIVATE).edit().putString(Global.FILE_Server_URL, file_url).commit();
                    if(ACTION.equals(REGISTER)){
                        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(intent);
                    }else  if(ACTION.equals(FORGET)){
                        startActivity(new Intent(LoginActivity.this,ForgetPsdActivity.class));
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
                loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(this);
        okfactory.start(NetworkFactory.METHOD_GET, urlManager.getBaseUrl(), null, successfulCallback, failCallback);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().finishAll();
        }
        return super.onKeyDown(keyCode, event);
    }
}
